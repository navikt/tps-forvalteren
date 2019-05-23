package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static java.util.Arrays.asList;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newHashSet;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anySet;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.service.IdentpoolService;
import no.nav.tps.forvalteren.service.command.testdata.FiltrerPaaIdenterTilgjengeligIMiljo;

@RunWith(MockitoJUnitRunner.class)
public class OpprettPersonerOgSjekkMiljoeServiceTest {

    private static final String IDENT_1 = "11111111111";
    private static final String IDENT_2 = "22222222222";
    private static final String ENV_1 = "u5";
    private static final String ENV_2 = "u6";

    @Mock
    private FindIdenterNotUsedInDB findIdenterNotUsedInDB;

    @Mock
    private FiltrerPaaIdenterTilgjengeligIMiljo filtrerPaaIdenterTilgjengeligIMiljo;

    @Mock
    private EkstraherIdenterFraTestdataRequests ekstraherIdenterFraTestdataRequests;

    @Mock
    private TestdataIdenterFetcher testdataIdenterFetcher;

    @Mock
    private PersonNameService setNameOnPersonsService;

    @Mock
    private OpprettPersonerService opprettPersonerFraIdenter;

    @Mock
    private IdentpoolService identpoolService;

    @InjectMocks
    private OpprettPersonerOgSjekkMiljoeService opprettPersonerOgSjekkMiljoeService;

    @Test
    public void createEksisterendeIdenter() {

        opprettPersonerOgSjekkMiljoeService.createEksisterendeIdenter(newArrayList(IDENT_1, IDENT_2));

        verify(findIdenterNotUsedInDB).filtrer(anySet());
        verify(filtrerPaaIdenterTilgjengeligIMiljo).filtrer(anyList(), anySet());
        verify(opprettPersonerFraIdenter).execute(anySet());
        verify(setNameOnPersonsService).execute(anyList());
    }

    @Test
    public void createNyeIdenter() {

        opprettPersonerOgSjekkMiljoeService.createNyeIdenter(RsPersonKriteriumRequest.builder().build(), newHashSet(asList(ENV_1, ENV_2)));

        verify(testdataIdenterFetcher).getTestdataRequestsInnholdeneTilgjengeligeIdenterFlereMiljoer(any(RsPersonKriteriumRequest.class), anySet());
        verify(ekstraherIdenterFraTestdataRequests).execute(anyList());
        verify(opprettPersonerFraIdenter).execute(anyList());
        verify(setNameOnPersonsService).execute(anyList());
    }
}