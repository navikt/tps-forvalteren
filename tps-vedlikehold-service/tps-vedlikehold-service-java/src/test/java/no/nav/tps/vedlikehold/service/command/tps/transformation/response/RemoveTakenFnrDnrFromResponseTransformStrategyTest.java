package no.nav.tps.vedlikehold.service.command.tps.transformation.response;

import no.nav.tps.vedlikehold.domain.service.command.User.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.Response;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.response.RemoveTakenFnrFromResponseTransform;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

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
        String personNotTaken1 = "<EFnr><utfyllendeMelding>PERSON IKKE FUNNET</utfyllendeMelding></EFnr>";
        String personNotTaken2 = "<EFnr><utfyllendeMelding>PERSON IKKE FUNNET</utfyllendeMelding></EFnr>";

        RemoveTakenFnrFromResponseTransform transform = new RemoveTakenFnrFromResponseTransform();
        transform.setAntallFnrRequestTag("antallFM211");

        Response response = new Response();
        response.setContext(createContext());
        response.setServiceRoutine(new TpsServiceRoutineDefinition());
        response.setRawXml(
                        "<data>"+
                        "<antallFM211>4</antallFM211>"
                        + personTaken1
                                + personNotTaken1
                                + personNotTaken2
                        + personTaken2
                        + "</data>"
        );

        removeFnrStrategy.execute(response, transform);

        String expectedResponseXml =
                "<data>"+
                        "<antallFM211>2</antallFM211>"
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