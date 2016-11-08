package no.nav.tps.vedlikehold.service.command.tps.transformation.response;

import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.domain.service.command.tps.Response;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.response.RemoveUnauthorizedPeopleFromResponseTransform;
import no.nav.tps.vedlikehold.service.command.authorisation.TpsAuthorisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RemoveUnauthorizedPeopleFromResponseTransformStrategy implements ResponseTransformStrategy {

    @Autowired
    private TpsAuthorisationService tpsAuthorisationService;

    @Autowired
    private MessageProvider messageProvider;

    @Override
    public void execute(Response response) {

    }
/*
    public void removeUnauthorizedDataFromTpsResponse(ServiceRoutineResponse tpsResponse, User user){
        if(!tpsAuthorisationService.isAuthorizedToReadAtLeastOnePerson(user, tpsResponse.getPersons(), tpsResponse.getEnvironment())){
//            throw new HttpUnauthorisedException(messageProvider.get("rest.service.request.exception.Unauthorized"), "api/v1/service/" + tpsResponse.getServiceRoutineName());
        }
        List<Person> authorizedPersons = tpsAuthorisationService.getAuthorizedPersons(user, tpsResponse.getPersons(), tpsResponse.getEnvironment());
        StringBuilder authorizedPersonsXml = new StringBuilder();
        for(Person person : authorizedPersons){
            authorizedPersonsXml.append(person.getXml());
        }
        String xmlWithoutUnauthorizedData = removeUnauthorizedPeopleFromXML(authorizedPersonsXml.toString(), tpsResponse);
        xmlWithoutUnauthorizedData = setAuthorizedResultCountInXml(xmlWithoutUnauthorizedData, tpsResponse.getPersons().size(), authorizedPersons.size());
        tpsResponse.setXml(xmlWithoutUnauthorizedData);
    }

    private String removeUnauthorizedPeopleFromXML(String filteredXml, ServiceRoutineResponse tpsResponse){
        String everyPersonInXmlRegex = "<enPersonRes>.+</enPersonRes>";
        return  Pattern.compile(everyPersonInXmlRegex, Pattern.DOTALL)
                .matcher(tpsResponse.getXml()).replaceFirst(filteredXml);
    }

*/
    @Override
    public boolean isSupported(Object o) {
        return o instanceof RemoveUnauthorizedPeopleFromResponseTransform;
    }
}
