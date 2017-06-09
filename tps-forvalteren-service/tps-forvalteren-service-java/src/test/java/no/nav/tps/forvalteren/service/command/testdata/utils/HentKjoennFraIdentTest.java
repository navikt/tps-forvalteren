package no.nav.tps.forvalteren.service.command.testdata.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class HentKjoennFraIdentTest {

    private final static  String oddetallIdent = "00000000100";
    private final static  String partallIdent = "00000000200";

    @InjectMocks
    private HentKjoennFraIdent hentKjoennFraIdent;

    @Test
    public void hvisIdentHarOddetallPaaNummer9individNummer3SaaMann() throws Exception {
        char kjoenn = hentKjoennFraIdent.execute(oddetallIdent);
        assertThat(kjoenn, is(equalTo('M')));
    }

    @Test
    public void hvisIdentHarpartallPaaNummer9individNummer3SaaKvinne() throws Exception {
        char kjoenn = hentKjoennFraIdent.execute(partallIdent);
        assertThat(kjoenn, is(equalTo('K')));
    }
}