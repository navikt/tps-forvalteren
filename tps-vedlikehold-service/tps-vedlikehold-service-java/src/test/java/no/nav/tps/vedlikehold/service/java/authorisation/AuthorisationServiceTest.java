package no.nav.tps.vedlikehold.service.java.authorisation;

import no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConsumer;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import no.nav.tps.vedlikehold.domain.service.User;
import no.nav.tps.vedlikehold.service.java.authorisation.AuthorisationService;
import no.nav.tps.vedlikehold.service.java.service.rutine.ServiceRutineService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 *  @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class AuthorisationServiceTest {

    private static final String FNR = "12345678910";

    @Mock
    private User userMock;

    @Mock
    private DiskresjonskodeConsumer diskresjonskodeConsumerMock;

    @Mock
    private EgenAnsattConsumer egenAnsattConsumerMock;

    @InjectMocks
    private AuthorisationService authorisationService;

    @Before
    public void setUp() {

    }

    public void userIsAuthorisedReturnsFalseIfAnExceptionIsThrown()  {
        assert false;
    }

}
