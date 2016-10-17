package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response.ServiceRoutineResponse;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpUnauthorisedException;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.authorisation.TpsAuthorisationService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.DefaultTpsServiceRutineService;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Peter Fløgstad on 17.10.2016.
 */

@Component
public class TpsResponseMappingUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTpsServiceRutineService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserContextHolder userContextHolder;

    @Autowired
    private TpsAuthorisationService tpsAuthorisationService;

    @Autowired
    private MessageProvider messageProvider;

    public ServiceRoutineResponse xmlResponseToServiceRoutineResponse(String responseXml) throws IOException{
        try{
            JSONObject jObject = XML.toJSONObject(responseXml);
            Object responseData = objectMapper.readValue(jObject.toString(), Map.class);          //TODO Map to custom object
            return new ServiceRoutineResponse(responseXml, responseData);
        }catch (IOException exception){
            LOGGER.error("Failed to convert TPS during XML marshalling with exception: {}", exception.toString());
            throw exception;
        }
    }

    public void remapTpSResponse(ServiceRoutineResponse tpsResponse) throws IOException{
        try{
            JSONObject jObject = XML.toJSONObject(tpsResponse.getXml());
            Object responseData = objectMapper.readValue(jObject.toString(), Map.class);          //TODO Map to custom object
            tpsResponse.setData(responseData);
        } catch (IOException exception){
            LOGGER.error("Failed to convert TPS during XML marshalling with exception: {}", exception.toString());
            throw exception;
        }
    }

    //TODO Split it up. Refactor. Kanskje flytte...
    public void removeUnauthorizedDataFromTpsResponse(ServiceRoutineResponse tpsResponse){
        String extractPersonDataRegex = "<enPersonRes>.*?<fnr>(\\d{11})</fnr>.+?</enPersonRes>";
        Matcher matcher = Pattern.compile(extractPersonDataRegex, Pattern.DOTALL).matcher(tpsResponse.getXml());
        StringBuilder filteredXMLsb = new StringBuilder();
        int countAuthorizedMatches = 0;
        int countTotalMatches = 0;
        User user = userContextHolder.getUser();
        while(matcher.find()){
            String fnr = matcher.group(1);
            if(tpsAuthorisationService.userIsAuthorisedToReadPersonInEnvironment(user, fnr, tpsResponse.getEnvironment())){
                filteredXMLsb.append(matcher.group());
                countAuthorizedMatches++;
            }
            countTotalMatches++;
        }
        if(countAuthorizedMatches == 0 && countTotalMatches > 0) {
            throw new HttpUnauthorisedException(messageProvider.get(HttpUnauthorisedException.messageKey), "api/v1/service/" + tpsResponse.getServiceRoutineName());
        }
        String everyPersonInXmlRegex = "<enPersonRes>.+</enPersonRes>";
        String xmlWithoutUnauthorizedData = Pattern.compile(everyPersonInXmlRegex, Pattern.DOTALL)
                                            .matcher(tpsResponse.getXml()).replaceFirst(filteredXMLsb.toString());
        xmlWithoutUnauthorizedData = xmlWithoutUnauthorizedData.replace("<antallTotalt>"+ countTotalMatches +"</antallTotalt>",
                                    "<antallTotalt>"+ countAuthorizedMatches +"</antallTotalt>");
        tpsResponse.setXml(xmlWithoutUnauthorizedData);
    }
}
