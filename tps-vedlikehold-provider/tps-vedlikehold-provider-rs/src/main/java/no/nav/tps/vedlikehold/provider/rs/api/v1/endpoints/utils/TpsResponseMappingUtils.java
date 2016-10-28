package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.domain.service.command.authorisation.Person;
import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.response.EndringsmeldingResponse;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response.ServiceRoutineResponse;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpUnauthorisedException;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.authorisation.TpsAuthorisationService;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Peter Fløgstad on 17.10.2016.
 */

@Component
public class TpsResponseMappingUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpsResponseMappingUtils.class);

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

    public EndringsmeldingResponse xmlResponseToEndringsmeldingResponse(String responseXml) throws IOException{
        try{
            //TODO Failer her... På Xml to JSON.
            JSONObject jObject = XML.toJSONObject(responseXml);
            Object responseData = objectMapper.readValue(jObject.toString(), Map.class);          //TODO Map to custom object
            return new EndringsmeldingResponse(responseXml, responseData);
        } catch (IOException exception){
            LOGGER.error("Failed to convert TPS during XML marshalling with exception: {}", exception.toString());
            throw exception;
        } catch (Exception e){
            String err = e.getMessage();
            e.printStackTrace();
            throw e;
        }
    }

    public void remapTpsResponse(ServiceRoutineResponse tpsResponse) throws IOException{
        try{
            JSONObject jObject = XML.toJSONObject(tpsResponse.getXml());
            Object responseData = objectMapper.readValue(jObject.toString(), Map.class);          //TODO Map to custom object
            tpsResponse.setData(responseData);
        } catch (IOException exception){
            LOGGER.error("Failed to convert TPS during XML marshalling with exception: {}", exception.toString());
            throw exception;
        }
    }

    public void removeUnauthorizedDataFromTpsResponse(ServiceRoutineResponse tpsResponse){
        User user = userContextHolder.getUser();
        if(!tpsAuthorisationService.isAuthorizedToReadAtLeastOnePerson(user, tpsResponse.getPersons(), tpsResponse.getEnvironment())){
            throw new HttpUnauthorisedException(messageProvider.get("rest.service.request.exception.Unauthorized"), "api/v1/service/" + tpsResponse.getServiceRoutineName());
        }
        ArrayList<Person> authorizedPersons = tpsAuthorisationService.getAuthorizedPersons(user, tpsResponse.getPersons(), tpsResponse.getEnvironment());
        StringBuilder authorizedPersonsXml = new StringBuilder();
        for(Person person : authorizedPersons){
            authorizedPersonsXml.append(person.getXml());
        }
        String xmlWithoutUnauthorizedData = removeUnauthorizedPersonsFromXML(authorizedPersonsXml.toString(), tpsResponse);
        xmlWithoutUnauthorizedData = setAuthorizedResultCountInXml(xmlWithoutUnauthorizedData, tpsResponse.getPersons().size(), authorizedPersons.size());
        tpsResponse.setXml(xmlWithoutUnauthorizedData);
    }

    private String removeUnauthorizedPersonsFromXML(String filteredXml, ServiceRoutineResponse tpsResponse){
        String everyPersonInXmlRegex = "<enPersonRes>.+</enPersonRes>";
        return  Pattern.compile(everyPersonInXmlRegex, Pattern.DOTALL)
                .matcher(tpsResponse.getXml()).replaceFirst(filteredXml);
    }

    private String setAuthorizedResultCountInXml(String xmlWithoutUnauthorizedData, int countTotalMatches, int countAuthorizedMatches){
        return xmlWithoutUnauthorizedData.replace("<antallTotalt>"+ countTotalMatches +"</antallTotalt>",
                "<antallTotalt>"+ countAuthorizedMatches +"</antallTotalt>");

    }
}
