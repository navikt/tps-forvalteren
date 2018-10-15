package no.nav.tps.forvalteren.service.command.testdata.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class HentAlderFraIdentTest {

    private static final String FNR = "120375012345";
    private static final LocalDateTime TIME_NOW = LocalDateTime.of(2018, 10, 9, 0,0);
    private static final LocalDateTime DATO_FOEDT = LocalDateTime.of(1975, 3, 12, 0,0);
    private static final LocalDateTime DATO_DOED= LocalDateTime.of(2015, 10, 10, 0,0);

    private Clock clock;

    @Mock
    private HentDatoFraIdentService hentDatoFraIdentService;

    @InjectMocks
    private HentAlderFraIdent hentAlderFraIdent;

    @Before
    public void setup() {
        clock = Clock.fixed(TIME_NOW.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());

        ReflectionTestUtils.setField(hentAlderFraIdent, "clock", clock);

        when(hentDatoFraIdentService.extract(FNR)).thenReturn(DATO_FOEDT);
    }

    @Test
    public void alderPaaPersonSomLever() throws Exception {

        assertThat(hentAlderFraIdent.extract(FNR, null), is(equalTo(43)));
    }

    @Test
    public void alderPaaPersonSomErDoed() throws Exception {

        assertThat(hentAlderFraIdent.extract(FNR, DATO_DOED), is(equalTo(40)));
    }
}