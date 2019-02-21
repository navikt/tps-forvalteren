package no.nav.tps.forvalteren.service.command.testdata;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.service.command.testdata.opprett.FindIdenterNotUsedInDB;
import no.nav.tps.forvalteren.service.command.testdata.response.IdentMedStatus;

@RunWith(MockitoJUnitRunner.class)
public class SjekkIdenterServiceTest {

    @Mock
    private FindIdenterNotUsedInDB findIdenterNotUsedInDB;

    @Mock
    private SjekkOmGyldigeIdenter sjekkOmGyldigeIdenter;

    @Mock
    private FiltrerPaaIdenterTilgjengeligIMiljo filtrerPaaIdenterTilgjengeligeIMiljo;

    @InjectMocks
    private SjekkIdenterService sjekkIdenterService;

    private List<String> identer;

    private static final String IKKE_GYLDIG = "IG";
    private static final String IKKE_LEDIG = "IL";
    private static final String LEDIG_OG_GYLDIG = "LOG";

    @Before
    public void setup() {
        identer = new ArrayList<>();
    }

    @Test
    public void callsAllServices() {
        sjekkIdenterService.finnGyldigeOgLedigeIdenter(identer);

        verify(sjekkOmGyldigeIdenter).execute(anySet());
        verify(findIdenterNotUsedInDB).filtrer(anySet());
        verify(filtrerPaaIdenterTilgjengeligeIMiljo).filtrer(anySet(), anySet());
    }

    @Test
    public void callWithEmptyIdentList() {
        Set<IdentMedStatus> result = sjekkIdenterService.finnGyldigeOgLedigeIdenter(identer);

        verify(sjekkOmGyldigeIdenter).execute(anySet());
        verify(findIdenterNotUsedInDB).filtrer(anySet());
        verify(filtrerPaaIdenterTilgjengeligeIMiljo).filtrer(anySet(),anySet());

        assertThat(result, hasSize(0));
    }

    @Test
    public void callWithInvalidIdenter() {
        identer.add("28125468404");
        identer.add("");
        identer.add("123456789123456789");

        when(sjekkOmGyldigeIdenter.execute(anySet())).thenReturn(new HashSet());
        when(findIdenterNotUsedInDB.filtrer(anySet())).thenReturn(new HashSet());
        when(filtrerPaaIdenterTilgjengeligeIMiljo.filtrer(anySet(), anySet())).thenReturn(new HashSet());

        Set<IdentMedStatus> result = sjekkIdenterService.finnGyldigeOgLedigeIdenter(identer);

        assertThat(result, hasSize(3));
        for (IdentMedStatus ident : result) {
            assertThat(ident.getStatus(), is(IKKE_GYLDIG));
        }
    }

    @Test
    public void callWithLedigOgGyldigIdenter() {
        identer.add("08087700047");
        identer.add("01021070501");

        Set<String> serviceResponse = new HashSet<>();
        serviceResponse.add("08087700047");
        serviceResponse.add("01021070501");

        when(sjekkOmGyldigeIdenter.execute(anySet())).thenReturn(serviceResponse);
        when(findIdenterNotUsedInDB.filtrer(anySet())).thenReturn(serviceResponse);
        when(filtrerPaaIdenterTilgjengeligeIMiljo.filtrer(anySet(),anySet())).thenReturn(serviceResponse);

        Set<IdentMedStatus> result = sjekkIdenterService.finnGyldigeOgLedigeIdenter(identer);

        assertThat(result, hasSize(2));
        for (IdentMedStatus ident : result) {
            assertThat(ident.getStatus(), is(LEDIG_OG_GYLDIG));
        }
    }

    @Test
    public void callWithIkkeLedigIdenter() {
        identer.add("08087700047");
        identer.add("01021070501");

        Set<String> serviceResponse = new HashSet<>();
        serviceResponse.add("08087700047");
        serviceResponse.add("01021070501");

        when(sjekkOmGyldigeIdenter.execute(anySet())).thenReturn(serviceResponse);
        when(findIdenterNotUsedInDB.filtrer(anySet())).thenReturn(new HashSet());
        when(filtrerPaaIdenterTilgjengeligeIMiljo.filtrer(anySet(),anySet())).thenReturn(new HashSet());

        Set<IdentMedStatus> result = sjekkIdenterService.finnGyldigeOgLedigeIdenter(identer);

        assertThat(result, hasSize(2));
        for (IdentMedStatus ident : result) {
            assertThat(ident.getStatus(), is(IKKE_LEDIG));
        }
    }

    @Test
    public void callWithIkkeLedigAndIkkeGyldigAndLedigOgGyldig() {
        final String IDENT_LEDIG_OG_GYLDIG = "08087700047";
        final String IDENT_IKKE_LEDIG = "01021070501";
        final String IDENT_IKKE_GYLDIG = "16049434774";

        identer.add(IDENT_LEDIG_OG_GYLDIG);
        identer.add(IDENT_IKKE_LEDIG);
        identer.add(IDENT_IKKE_GYLDIG);

        Set<String> serviceResponseGyldig = new HashSet<>();
        serviceResponseGyldig.add(IDENT_LEDIG_OG_GYLDIG);
        serviceResponseGyldig.add(IDENT_IKKE_LEDIG);

        Set<String> serviceResponseDB = new HashSet<>();
        serviceResponseDB.add(IDENT_IKKE_LEDIG);
        serviceResponseDB.add(IDENT_LEDIG_OG_GYLDIG);


        Set<String> serviceResponseMiljo = new HashSet<>();
        serviceResponseMiljo.add(IDENT_LEDIG_OG_GYLDIG);

        when(sjekkOmGyldigeIdenter.execute(anySet())).thenReturn(serviceResponseGyldig);
        when(findIdenterNotUsedInDB.filtrer(anySet())).thenReturn(serviceResponseDB);
        when(filtrerPaaIdenterTilgjengeligeIMiljo.filtrer(anySet(), anySet())).thenReturn(serviceResponseMiljo);

        Set<IdentMedStatus> result = sjekkIdenterService.finnGyldigeOgLedigeIdenter(identer);

        boolean hasIkkeGyldig = false;
        boolean hasIkkeLedig = false;
        boolean hasLedigOgGyldig = false;

        for (IdentMedStatus identMedStatus : result) {
            if (identMedStatus.getStatus().equalsIgnoreCase(LEDIG_OG_GYLDIG)
                    && identMedStatus.getIdent().equalsIgnoreCase(IDENT_LEDIG_OG_GYLDIG)) {
                hasLedigOgGyldig = true;
            } else if (identMedStatus.getStatus().equalsIgnoreCase(IKKE_LEDIG)
                    && identMedStatus.getIdent().equalsIgnoreCase(IDENT_IKKE_LEDIG)) {
                hasIkkeLedig = true;
            } else if (identMedStatus.getStatus().equalsIgnoreCase(IKKE_GYLDIG)
                    && identMedStatus.getIdent().equalsIgnoreCase(IDENT_IKKE_GYLDIG)) {
                hasIkkeGyldig = true;
            }
        }
        assertThat(hasIkkeGyldig, is(true));
        assertThat(hasIkkeLedig, is(true));
        assertThat(hasLedigOgGyldig, is(true));
    }

}