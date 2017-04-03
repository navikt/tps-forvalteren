package no.nav.tps.vedlikehold.service.command.tps.transformation.response;

import no.nav.tps.vedlikehold.domain.service.user.User;
import no.nav.tps.vedlikehold.domain.service.tps.Response;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.response.RemoveUnauthorizedPeopleFromResponseTransform;
import no.nav.tps.vedlikehold.service.command.authorisation.TpsAuthorisationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RemoveUnauthorizedPeopleFromResponseTransformStrategyTest {


    @Mock
    private TpsAuthorisationService tpsAuthorisationServiceMock;


    @InjectMocks
    private RemoveUnauthorizedPeopleFromResponseTransformStrategy strategy;



    @Test
    public void isSupportedReturnsTrueForRemoveUnauthorizedPeopleFromResponseTransform() {
        RemoveUnauthorizedPeopleFromResponseTransform transform = new RemoveUnauthorizedPeopleFromResponseTransform();

        boolean isSupported = strategy.isSupported(transform);

        assertThat(isSupported, is(true));
    }


    @Test
    public void executeRemovesUnauthorisedPeopleFromRawXml() {

        String fnr1 = "01018012345";
        String fnr2 = "02028012345";

        String person1 = "<enPersonRes><fnr>"+fnr1+"</fnr></enPersonRes>";
        String person2 = "<enPersonRes><fnr>"+fnr2+"</fnr></enPersonRes>";

        RemoveUnauthorizedPeopleFromResponseTransform transform = new RemoveUnauthorizedPeopleFromResponseTransform();
        transform.setTotalHitsXmlElement("totalHits");
        transform.setHitsInBufferXmlElement("hits");

        Response response = new Response();
        response.setContext(createContext());
        response.setServiceRoutine(new TpsServiceRoutineDefinition());
        response.setRawXml(
                "<data>"
                + "<totalHits>2</totalHits>"
                + "<hits>2</hits>"
                + person1
                + person2
                + "</data>"
        );

        when(tpsAuthorisationServiceMock.isAuthorisedToSeePerson(any(), eq(fnr1), any())).thenReturn(false);
        when(tpsAuthorisationServiceMock.isAuthorisedToSeePerson(any(), eq(fnr2), any())).thenReturn(true);

        strategy.execute(response, transform);

        String expectedResponseXml =
                "<data>"
                + "<totalHits>1</totalHits>"
                + "<hits>1</hits>"
                + person2
                + "</data>";


        assertThat(response.getRawXml(), is(equalTo(expectedResponseXml)));
    }

    private TpsRequestContext createContext() {
        TpsRequestContext context = new TpsRequestContext();
        context.setUser(new User("name", "username", null));
        return context;
    }

}