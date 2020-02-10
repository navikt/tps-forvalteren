package no.nav.tps.forvalteren.service.command.testdata.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HentKjoennFraIdentServiceServiceTest {

    private final static  String ODDETALL_IDENT = "00000000100";
    private final static  String PARTALL_IDENT = "00000000200";

    @InjectMocks
    private HentKjoennFraIdentService hentKjoennFraIdentService;

    @Test
    public void hvisIdentHarOddetallPaaNummer9individNummer3SaaMann() throws Exception {
        String kjoenn = hentKjoennFraIdentService.execute(ODDETALL_IDENT);
        assertThat(kjoenn, is(equalTo("M")));
    }

    @Test
    public void hvisIdentHarpartallPaaNummer9individNummer3SaaKvinne() throws Exception {
        String kjoenn = hentKjoennFraIdentService.execute(PARTALL_IDENT);
        assertThat(kjoenn, is(equalTo("K")));
    }
}