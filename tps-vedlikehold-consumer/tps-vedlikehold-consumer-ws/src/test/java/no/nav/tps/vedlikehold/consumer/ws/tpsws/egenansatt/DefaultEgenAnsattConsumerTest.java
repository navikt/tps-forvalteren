package no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt;

import no.nav.tjeneste.pip.diskresjonskode.DiskresjonskodePortType;
import no.nav.tjeneste.pip.pipegenansatt.v1.PipEgenAnsattPortType;
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

import static no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DefaultDiskresjonskodeConsumer.THE_DATABASE_DOES_NOT_ANSWER_ERROR;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultEgenAnsattConsumerTest {

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
}