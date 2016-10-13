package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

import org.json.JSONObject;
import org.json.XML;
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
        return sendRequest(body);
    }

    private ServiceRoutineResponse sendRequest(JsonNode body){
        String environment = body.get("environment").asText();
        String tpsServiceRutinenavn = body.get(TPS_SERVICE_ROUTINE_PARAM_NAME).asText();
        String fnr = null;
        if(body.has("fnr")) fnr = body.get("fnr").asText();
        if(fnr != null) {
            return sendTpsRequestAndAuthorizeBeforeSendingRequest(tpsServiceRutinenavn,fnr,environment,body);
        } else{
            return sendTpsRequestAndAuthorizeAfterResponseIsReceived(tpsServiceRutinenavn,environment,body);
        }
    }

    private ServiceRoutineResponse sendTpsRequestAndAuthorizeBeforeSendingRequest(String tpsServiceRutinenavn, String fnr, String environment, JsonNode body){
        User user = userContextHolder.getUser();
        if (!tpsAuthorisationService.userIsAuthorisedToReadPersonInEnvironment(user, fnr, environment)) {
            throw new HttpUnauthorisedException(messageProvider.get("rest.service.request.exception.Unauthorized"), "api/v1/service/" + tpsServiceRutinenavn);
        }
        Sporingslogger.log(environment, tpsServiceRutinenavn, fnr);
        TpsRequestServiceRoutine tpsRequest = mappingUtils.convertToTpsRequestServiceRoutine(tpsServiceRutinenavn, body);
        return sendTpsRequest(tpsRequest);
    }

    private ServiceRoutineResponse sendTpsRequestAndAuthorizeAfterResponseIsReceived(String tpsServiceRutinenavn, String environment, JsonNode body){
        Sporingslogger.log(environment, tpsServiceRutinenavn, null);
        TpsRequestServiceRoutine tpsRequest = mappingUtils.convertToTpsRequestServiceRoutine(tpsServiceRutinenavn, body);
        ServiceRoutineResponse tpsResponse = sendTpsRequest(tpsRequest);
        removeUnauthorizedDataFromTpsResponse(tpsResponse);
        remapResponseData(tpsResponse);
        return tpsResponse;
    }

    //TODO Move to mapper class or something..
    private void remapResponseData(ServiceRoutineResponse tpsResponse){
        JSONObject jObject = XML.toJSONObject(tpsResponse.getXml());
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        Object responseData = null;
        try {
            responseData = mapper.readValue(jObject.toString(), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        tpsResponse.setData(responseData);
    }

    //TODO Move to own authorization class. Split it up.
    private void removeUnauthorizedDataFromTpsResponse(ServiceRoutineResponse tpsResponse){
        String extractPersonDataRegex = "<enPersonRes>.*?<fnr>(\\d{11})</fnr>.+?</enPersonRes>";
        Matcher matcher = Pattern.compile(extractPersonDataRegex, Pattern.DOTALL).matcher(tpsResponse.getXml());
        String filteredXML = "";
        int antallAuthoriserteTreff = 0,  totaltAntallTreff = 0;
        User user = userContextHolder.getUser();
        while(matcher.find()){
            String fnr = matcher.group(1);
            if(tpsAuthorisationService.userIsAuthorisedToReadPersonInEnvironment(user, fnr, tpsResponse.getEnvironment())){
                filteredXML = filteredXML + matcher.group();
                antallAuthoriserteTreff++;
            }
            totaltAntallTreff++;
        }
        if(antallAuthoriserteTreff == 0 && totaltAntallTreff > 0) {
            throw new HttpUnauthorisedException(messageProvider.get("rest.service.request.exception.Unauthorized"), "api/v1/service/" + tpsResponse.getServiceRoutineName());
        }
        String everyPersonInXmlRegex = "<enPersonRes>.+</enPersonRes>";
        String xmlWithoutUnauthorizedData = Pattern.compile(everyPersonInXmlRegex, Pattern.DOTALL)
                                            .matcher(tpsResponse.getXml()).replaceFirst(filteredXML);
        xmlWithoutUnauthorizedData = xmlWithoutUnauthorizedData.replace("<antallTotalt>"+totaltAntallTreff+"</antallTotalt>",
                                            "<antallTotalt>"+antallAuthoriserteTreff+"</antallTotalt>");
        tpsResponse.setXml(xmlWithoutUnauthorizedData);
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

    private void isUserAuthorized(String fnr, String environment, String serviceRutinenavn) {
        if (!isEmpty(fnr)) {
            User user = userContextHolder.getUser();
            boolean authorized = tpsAuthorisationService.userIsAuthorisedToReadPersonInEnvironment(user, fnr, environment);
            if (!authorized) {
                throw new HttpUnauthorisedException(messageProvider.get("rest.service.request.exception.Unauthorized"), "api/v1/service/" + serviceRutinenavn);
            }
        }
    }
}
