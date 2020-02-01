package no.nav.tps.forvalteren.service.command.testdata.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HentIdenttypeFraIdentServiceTest {

    private static final String IDENT_FNR = "12031212345";
    private static final String IDENT_DNR = "52031212345";
    private static final String IDENT_BOST = "12231212345";

    @InjectMocks
    private HentIdenttypeFraIdentService hentIdenttypeFraIdentService;

    @Test
    public void hentIdentTypeFnr() {
        String result = hentIdenttypeFraIdentService.execute(IDENT_FNR);

        assertThat(result, is(equalTo("FNR")));
    }

    @Test
    public void hentIdentTypeDnr() {
        String result = hentIdenttypeFraIdentService.execute(IDENT_DNR);

        assertThat(result, is(equalTo("DNR")));
    }

    @Test
    public void hentIdentTypeBost() {
        String result = hentIdenttypeFraIdentService.execute(IDENT_BOST);

        assertThat(result, is(equalTo("BOST")));
    }
}