package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.dolly;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;

@RunWith(MockitoJUnitRunner.class)
public class ListExtractorKommaSeperatedTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private ListExtractorKommaSeperated extractor;

    @Test
    public void langStringBlirTilidenter() throws Exception{
        String testString = "12345678912,98765432198";

        List<String> identer = extractor.extractIdenter(testString);

        assertThat(identer.size(), is(2));
        assertThat(identer.get(0), is("12345678912"));
        assertThat(identer.get(1), is("98765432198"));
    }

    @Test
    public void langStringBlirTilidenterHvisKunEnIdent() throws Exception{
        String testString = "08099400111";

        List<String> identer = extractor.extractIdenter(testString);

        assertThat(identer.size(), is(1));
        assertThat(identer.get(0), is("08099400111"));
    }

    @Test
    public void kastExceptionHvisFeilIInputstringSomIkkeErident() throws Exception{
        String testString = "12345678912,987654321";

        expectedException.expect(TpsfFunctionalException.class);

        extractor.extractIdenter(testString);
    }

    @Test
    public void kastExceptionHvisFeilIInputstringSomInneholderBokstaver() throws Exception{
        String testString = "12345678912,98765432abc";

        expectedException.expect(TpsfFunctionalException.class);

        extractor.extractIdenter(testString);
    }

}