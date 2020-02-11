package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static java.util.Collections.singletonList;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anySet;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.service.IdentpoolService;
import no.nav.tps.forvalteren.service.command.testdata.FiltrerPaaIdenterTilgjengeligIMiljo;

@RunWith(MockitoJUnitRunner.class)
public class OpprettPersonerOgSjekkMiljoeServiceTest {

    private static final String IDENT_1 = "11111111111";
    private static final String IDENT_2 = "22222222222";

    @Mock
    private FindIdenterNotUsedInDB findIdenterNotUsedInDB;

    @Mock
    private FiltrerPaaIdenterTilgjengeligIMiljo filtrerPaaIdenterTilgjengeligIMiljo;

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

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setOpprettFraIdenter(newArrayList(IDENT_1, IDENT_2));
        when(opprettPersonerFraIdenter.execute(any(Collection.class))).thenReturn(singletonList(new Person()));

        opprettPersonerOgSjekkMiljoeService.createEksisterendeIdenter(request);

        verify(findIdenterNotUsedInDB).filtrer(anySet());
        verify(filtrerPaaIdenterTilgjengeligIMiljo).filtrer(anySet(), anySet());
        verify(opprettPersonerFraIdenter).execute(anySet());
        verify(setNameOnPersonsService).execute(any(Person.class), eq(null));
    }

    @Test
    public void createNyeIdenter() {

        when(opprettPersonerFraIdenter.execute(anyList())).thenReturn(singletonList(new Person()));

        opprettPersonerOgSjekkMiljoeService.createNyeIdenter(RsPersonKriteriumRequest.builder().build());

        verify(opprettPersonerFraIdenter).execute(anyList());
        verify(setNameOnPersonsService).execute(any(Person.class), eq(null));
    }
}