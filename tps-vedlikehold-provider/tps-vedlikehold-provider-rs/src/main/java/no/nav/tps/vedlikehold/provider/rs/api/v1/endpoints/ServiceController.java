package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import static org.springframework.util.ObjectUtils.caseInsensitiveValueOf;
import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response.ServiceRoutineResponse;
import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils.RsRequestMappingUtils;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpBadRequestException;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpInternalServerErrorException;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpUnauthorisedException;
import no.nav.tps.vedlikehold.provider.rs.security.logging.Sporingslogger;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.authorisation.TpsAuthorisationService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.TpsServiceRutineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Tobias Hansen, Visma Consulting AS
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@RestController
@RequestMapping(value = "api/v1")
public class ServiceController {

    private static final String TPS_SERVICE_ROUTINE_PARAM_NAME = "serviceRutinenavn";

    @Autowired
    private UserContextHolder userContextHolder;

    @Autowired
    private TpsServiceRutineService tpsServiceRutineService;

    @Autowired
    private TpsAuthorisationService tpsAuthorisationService;

    @Autowired
    private RsRequestMappingUtils mappingUtils;

    @Autowired
    private MessageProvider messageProvider;

    @LogExceptions
    @RequestMapping(value = "/service/{"+ TPS_SERVICE_ROUTINE_PARAM_NAME +"}", method = RequestMethod.GET)
    public ServiceRoutineResponse getService(@RequestParam(required = false) Map<String, Object> tpsRequestParameters, @PathVariable String serviceRutinenavn) {
        tpsRequestParameters.put(TPS_SERVICE_ROUTINE_PARAM_NAME, serviceRutinenavn);

        JsonNode jsonNode = mappingUtils.convert(tpsRequestParameters, JsonNode.class);
        return getService(jsonNode);
    }

    @LogExceptions
    @RequestMapping(value = "/service", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ServiceRoutineResponse getService(@RequestBody JsonNode body) {
        validateRequest(body);

        String environment = body.get("environment").asText();
        String tpsServiceRutinenavn = body.get(TPS_SERVICE_ROUTINE_PARAM_NAME).asText();
        String fnr = null;
        if(body.has("fnr")){
            fnr = body.get("fnr").asText();
        }

        System.out.println(tpsServiceRutinenavn);
        validateAuthorized(fnr, environment, tpsServiceRutinenavn);

        Sporingslogger.log(environment, tpsServiceRutinenavn, fnr);

        TpsRequestServiceRoutine tpsRequest = mappingUtils.convertToTpsRequestServiceRoutine(tpsServiceRutinenavn, body);
        ServiceRoutineResponse tpsResponse = sendTpsRequest(tpsRequest);
        //TODO Create a better logic for which "code" to service routine to choose.
        if(!body.has("fnr")){
            removePersonsNotAuthorizedToSeeFromTpsRepsons(tpsResponse);
        }
        return tpsResponse;
    }

    private void selectExecution(String serviceRoutine){
        switch (serviceRoutine){
            case ("FS03-FDNUMMER-PERSDATA-O"):
                break;
            case ("FS03-FDNUMMER-KONTINFO-O"):
                break;
            case ("FS03-NAADRSOK-PERSDATA-O"):
                //getAuthorizedUsers.
                break;
            default:
                break;
        }
    }

    //TODO Move to own authorization class.
    private void removePersonsNotAuthorizedToSeeFromTpsRepsons(ServiceRoutineResponse tpsResponse){
        String serviceRutineName = tpsResponse.getServiceRoutineName();
        String environment = tpsResponse.getEnvironment();
        String allPersonsInXmlRegex = "<enPersonRes>.+</enPersonRes>";
        String xmlWithoutPersons = Pattern.compile(allPersonsInXmlRegex, Pattern.DOTALL)
                .matcher(tpsResponse.getXml()).replaceFirst("-->erstatt_senere<--");
        String personRegex = "<enPersonRes>.*?<fnr>(\\d{11})</fnr>.+?</enPersonRes>";
        Matcher matcher = Pattern.compile(personRegex, Pattern.DOTALL).matcher(tpsResponse.getXml());
        String personsReplacement = "";
        while(matcher.find()){
            String fnr = matcher.group(1);
            try{
                validateAuthorized(fnr, environment, serviceRutineName);
                personsReplacement = personsReplacement + matcher.group();
            } catch (HttpUnauthorisedException unauthorisedException){
                //Do not add xml to final xml. Do anything.?
            }
        }
        String xmlWithoutUnauthorizedToViewPersons = xmlWithoutPersons.replace("-->erstatt_senere<--", personsReplacement);
        tpsResponse.setXml(xmlWithoutUnauthorizedToViewPersons);
    }

    // Not used for now, but will maybe be used later for prod when who you can view is restricted.
    private void removePersonsWithDiscretionCodesFromTpsResponse(ServiceRoutineResponse tpsResponse){
        String personRegex = "(<enPersonRes>.+?<spesregType>\\w+?</spesregType>.+?</enPersonRes>)";
        Pattern pattern = Pattern.compile(personRegex, Pattern.DOTALL);
        String xmlWithoutDiscreteUsers = pattern.matcher(tpsResponse.getXml()).replaceAll("");
        tpsResponse.setXml(xmlWithoutDiscreteUsers);
    }

    private void validateRequest(JsonNode body) {
        if (!body.has("environment") || !body.has(TPS_SERVICE_ROUTINE_PARAM_NAME)) {
            throw new HttpBadRequestException(messageProvider.get("rest.service.request.exception.MissingRequiredParams"), "api/v1/service");
        }
    }

    private ServiceRoutineResponse sendTpsRequest(TpsRequestServiceRoutine request) {
        try {
            return tpsServiceRutineService.execute(request);
        } catch (Exception exception) {
            throw new HttpInternalServerErrorException(exception, "api/v1/service");
        }
    }

    // Dette er bare for rettighet til å lese person vel..
    // Hva skjer her når fnr er empty? Bare authoriserer den ikke? hmm jaja bra da. For nå...
    //TODO Add authorized for Search on Name.
    private void validateAuthorized(String fnr, String environment, String serviceRutinenavn) {
        if (!isEmpty(fnr)) {
            User user = userContextHolder.getUser();
            boolean authorized = tpsAuthorisationService.userIsAuthorisedToReadPersonInEnvironment(user, fnr, environment);
            if (!authorized) {
                throw new HttpUnauthorisedException(messageProvider.get("rest.service.request.exception.Unauthorized"), "api/v1/service/" + serviceRutinenavn);
            }
        }
    }
}
