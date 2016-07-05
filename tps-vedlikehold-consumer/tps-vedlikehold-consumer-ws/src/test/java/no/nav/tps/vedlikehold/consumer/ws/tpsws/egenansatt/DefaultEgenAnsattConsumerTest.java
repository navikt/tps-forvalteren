package no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt;

import no.nav.tjeneste.pip.diskresjonskode.DiskresjonskodePortType;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeRequest;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;
import no.nav.tjeneste.pip.pipegenansatt.v1.PipEgenAnsattPortType;
import no.nav.tjeneste.pip.pipegenansatt.v1.meldinger.ErEgenAnsattEllerIFamilieMedEgenAnsattRequest;
import no.nav.tjeneste.pip.pipegenansatt.v1.meldinger.ErEgenAnsattEllerIFamilieMedEgenAnsattResponse;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DefaultDiskresjonskodeConsumer;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.exceptions.FNrEmptyException;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.exceptions.PersonNotFoundException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.inject.Inject;

import static no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DefaultDiskresjonskodeConsumer.DISKRESJONSKODE_NOT_FOUND_ERROR;
import static no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DefaultDiskresjonskodeConsumer.THE_DATABASE_DOES_NOT_ANSWER_ERROR;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultEgenAnsattConsumerTest {

    public static final String EGENANSATT_NOT_FOUND_ERROR = "Ingen forekomster funnet";


    @InjectMocks
    private DefaultEgenAnsattConsumer consumer;

    @Mock
    private PipEgenAnsattPortType egenAnsattPortType;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void isEgenAnsattThrowsFNrEmptyExceptionCalledWithEmptyString(){
        expectedException.expect(FNrEmptyException.class);

        consumer.isEgenAnsatt("");
    }

    @Test
    public void isEgenAnsattThrowsFNrEmptyExceptionCalledWithNull(){
        expectedException.expect(FNrEmptyException.class);

        consumer.isEgenAnsatt(null);
    }

    @Test
    public void pingReturnsTrueWhenErEgenAnsattRespondsNormally() throws Exception {
        when(egenAnsattPortType.erEgenAnsattEllerIFamilieMedEgenAnsatt((ErEgenAnsattEllerIFamilieMedEgenAnsattRequest)
                any(ErEgenAnsattEllerIFamilieMedEgenAnsattRequest.class)))
                .thenReturn(new ErEgenAnsattEllerIFamilieMedEgenAnsattResponse());

        boolean result = consumer.ping();

        assertThat(result, is(equalTo(true)));
    }

}