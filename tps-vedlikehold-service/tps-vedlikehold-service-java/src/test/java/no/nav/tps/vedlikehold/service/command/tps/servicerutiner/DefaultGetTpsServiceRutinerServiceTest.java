package no.nav.tps.vedlikehold.service.command.tps.servicerutiner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.xml.XmlMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.TpsServiceRutine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultGetTpsServiceRutinerServiceTest {

    private static final TpsServiceRutine[] SERVICE_RUTINES = new TpsServiceRutine[]{new TpsServiceRutine()};

    @Mock
    private XmlMapper xmlMapperMock;

    @InjectMocks
    private DefaultGetTpsServiceRutinerService getTpsServiceRutinerService;

    @Before
    public void setUp() throws Exception {
        when( xmlMapperMock.readValue(any(InputStream.class), any(Class.class)) ).thenReturn(SERVICE_RUTINES);
    }

    @Test
    public void getTpsServiceRutinerServiceReturnsACollectionOfTpsServiceRutines() throws IOException {
        Collection<TpsServiceRutine> services = getTpsServiceRutinerService.exectue();

        verify(xmlMapperMock).readValue(any(InputStream.class), any(Class.class));

        assertThat(services, containsInAnyOrder(SERVICE_RUTINES));
    }

    @Test
    public void getTpsServiceRutinerServiceFailsGracefullyForJaonParseExceptions() throws IOException {
        when( xmlMapperMock.readValue(any(InputStream.class), any(Class.class)) ).thenThrow(JsonParseException.class);

        Collection<TpsServiceRutine> services = getTpsServiceRutinerService.exectue();

        assertThat(services, hasSize(0));
    }

    @Test
    public void getTpsServiceRutinerServiceFailsGracefullyForJaonMappingExceptions() throws IOException {
        when( xmlMapperMock.readValue(any(InputStream.class), any(Class.class)) ).thenThrow(JsonMappingException.class);

        Collection<TpsServiceRutine> services = getTpsServiceRutinerService.exectue();

        assertThat(services, hasSize(0));
    }

    @Test
    public void getTpsServiceRutinerServiceFailsGracefullyForIOExceptions() throws IOException {
        when( xmlMapperMock.readValue(any(InputStream.class), any(Class.class)) ).thenThrow(IOException.class);

        Collection<TpsServiceRutine> services = getTpsServiceRutinerService.exectue();

        assertThat(services, hasSize(0));
    }

    @Test(expected = Exception.class)
    public void getTpsServiceRutinerServiceFailsForOtherExceptions() throws IOException {
        when( xmlMapperMock.readValue(any(InputStream.class), any(Class.class)) ).thenThrow(Exception.class);

        getTpsServiceRutinerService.exectue();
    }
}