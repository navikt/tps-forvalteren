package no.nav.tps.vedlikehold.service.services;

import org.hibernate.validator.constraints.Mod10Check;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
    @RunWith(MockitoJUnitRunner.class)
public class GetTpsServiceRutinerServiceTest {

    private static final String SERVICES = "SERVICES";

    @Mock
    GetTpsServiceRutinerService getTpsServiceRutinerService;

    @Before
    public void setUp() {
        when(getTpsServiceRutinerService.getTpsServiceRutiner()).thenReturn(SERVICES);
    }

    @Test
    public void getTpsServiceRutinerReturnsServices() {
        String services = getTpsServiceRutinerService.getTpsServiceRutiner();

        assertThat(services, is(equalTo(SERVICES)));
    }
}