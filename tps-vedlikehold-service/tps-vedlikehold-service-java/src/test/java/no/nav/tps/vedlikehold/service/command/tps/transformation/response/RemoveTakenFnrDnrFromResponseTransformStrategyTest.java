package no.nav.tps.vedlikehold.service.command.tps.transformation.response;

import no.nav.tps.vedlikehold.domain.service.User.User;
import no.nav.tps.vedlikehold.domain.service.tps.Response;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.response.RemoveTakenFnrFromResponseTransform;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Peter Fløgstad on 18.01.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class RemoveTakenFnrDnrFromResponseTransformStrategyTest {

    @InjectMocks
    private RemoveTakenFnrDnrFromResponseTransformStrategy removeFnrStrategy;


    @Test
    public void isSupported() throws Exception {
        RemoveTakenFnrFromResponseTransform transform = new RemoveTakenFnrFromResponseTransform();
        boolean isSupported = removeFnrStrategy.isSupported(transform);
        assertThat(isSupported, is(true));
    }

    @Test
    public void execute() throws Exception {
        String fnr1 = "07019233152";
        String fnr2 ="07018833152";

        String personTaken1 = "<EFnr><fnr>"+fnr1+"</fnr></EFnr>";
        String personTaken2 = "<EFnr><fnr>"+fnr2+"</fnr></EFnr>";
        String personNotTaken1 = "<EFnr><returMelding>S201005F</returMelding></EFnr>";
        String personNotTaken2 = "<EFnr><returMelding>S201005F</returMelding></EFnr>";

        RemoveTakenFnrFromResponseTransform transform = new RemoveTakenFnrFromResponseTransform();
        transform.setAntallFnrRequestTag("antallFM201");

        Response response = new Response();
        response.setContext(createContext());
        response.setServiceRoutine(new TpsServiceRoutineDefinition());
        response.setRawXml(
                        "<data>"+
                        "<antallFM201>4</antallFM201>"
                        + personTaken1
                                + personNotTaken1
                                + personNotTaken2
                        + personTaken2
                        + "</data>"
        );

        removeFnrStrategy.execute(response, transform);

        String expectedResponseXml =
                "<data>"+
                        "<antallFM201>2</antallFM201>"
                        + personNotTaken1
                        + personNotTaken2
                        + "</data>";

        assertThat(response.getRawXml(), is(equalTo(expectedResponseXml)));
    }

    private TpsRequestContext createContext() {
        TpsRequestContext context = new TpsRequestContext();
        context.setUser(new User("name", "username", null));
        return context;
    }
}