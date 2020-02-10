package no.nav.tps.forvalteren.service;

import static java.util.Arrays.asList;
import static org.assertj.core.util.Sets.newHashSet;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import no.nav.tps.forvalteren.consumer.rs.identpool.IdentpoolConsumer;

@RunWith(MockitoJUnitRunner.class)
public class IdentpoolServiceTest {

    private static final String IDENT_1 = "1";
    private static final String IDENT_2 = "2";
    private static final String IDENT_3 = "3";
    private static final String IDENT_4 = "4";
    private static final String IDENT_5 = "5";

    @Mock
    private IdentpoolConsumer identpoolConsumer;

    @InjectMocks
    private IdentpoolService identpoolService;

    @Before
    public void setup() {
        when(identpoolConsumer.getWhitelistedIdent()).thenReturn(ResponseEntity.ok(new String[] { IDENT_1, IDENT_2 }));
    }

    @Test
    public void getWhitedlistedIdents_OK() {

        Set<String> result = identpoolService.getWhitedlistedIdents();

        verify(identpoolConsumer).getWhitelistedIdent();
        assertThat(result, hasItems(IDENT_1, IDENT_2));
    }

    @Test
    public void getWhitedlistedIdents_ThrowsException() {

        when(identpoolConsumer.getWhitelistedIdent()).thenThrow(new HttpClientErrorException(INTERNAL_SERVER_ERROR));
        Set<String> result = identpoolService.getWhitedlistedIdents();

        verify(identpoolConsumer).getWhitelistedIdent();
        assertThat(result, is(empty()));
    }


    @Test
    public void whitelistAjustmentOfIdents_OK() {

        Set requestedIdents = newHashSet(asList(IDENT_1, IDENT_2, IDENT_3, IDENT_4, IDENT_5));
        Set availableInDB = newHashSet(asList(IDENT_4, IDENT_3, IDENT_2));
        Set availableInEnvironment = newHashSet(asList(IDENT_3));

        Set<String> ajustedAvailableInEnvironment = identpoolService.whitelistAjustmentOfIdents(requestedIdents, availableInDB, availableInEnvironment);

        verify(identpoolConsumer).getWhitelistedIdent();
        assertThat(ajustedAvailableInEnvironment, hasItems(IDENT_2, IDENT_3));
        assertThat(ajustedAvailableInEnvironment, not(hasItems(IDENT_1, IDENT_4, IDENT_5)));
    }
}