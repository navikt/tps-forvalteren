package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.service.command.testdata.opprett.FindIdenterNotUsedInDB;
import no.nav.tps.forvalteren.service.command.testdata.response.IdentMedStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SjekkIdenterTest {

    @Mock
    private FindIdenterNotUsedInDB findIdenterNotUsedInDBMock;

    @Mock
    private sjekkOmGyldigeIdenter sjekkOmGyldigeIdenterMock;

    @Mock
    private FilterPaaIdenterTilgjengeligeIMiljo filterPaaIdenterTilgjengeligeIMiljoMock;

    @InjectMocks
    private SjekkIdenter sjekkIdenter;

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
        sjekkIdenter.finnGyldigeOgLedigeIdenter(identer);

        verify(sjekkOmGyldigeIdenterMock).execute(anySet());
        verify(findIdenterNotUsedInDBMock).filtrer(anySet());
        verify(filterPaaIdenterTilgjengeligeIMiljoMock).filtrer(anySet());
    }

    @Test
    public void callWithEmptyIdentList() {
        Set<IdentMedStatus> result = sjekkIdenter.finnGyldigeOgLedigeIdenter(identer);

        verify(sjekkOmGyldigeIdenterMock).execute(anySet());
        verify(findIdenterNotUsedInDBMock).filtrer(anySet());
        verify(filterPaaIdenterTilgjengeligeIMiljoMock).filtrer(anySet());

        assertThat(result, hasSize(0));
    }

    @Test
    public void callWithInvalidIdenter() {
        identer.add("28125468404");
        identer.add("");
        identer.add("123456789123456789");

        when(sjekkOmGyldigeIdenterMock.execute(anySet())).thenReturn(new HashSet());
        when(findIdenterNotUsedInDBMock.filtrer(anySet())).thenReturn(new HashSet());
        when(filterPaaIdenterTilgjengeligeIMiljoMock.filtrer(anySet())).thenReturn(new HashSet());

        Set<IdentMedStatus> result = sjekkIdenter.finnGyldigeOgLedigeIdenter(identer);

        assertThat(result, hasSize(3));
        for(IdentMedStatus ident : result) {
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

        when(sjekkOmGyldigeIdenterMock.execute(anySet())).thenReturn(serviceResponse);
        when(findIdenterNotUsedInDBMock.filtrer(anySet())).thenReturn(serviceResponse);
        when(filterPaaIdenterTilgjengeligeIMiljoMock.filtrer(anySet())).thenReturn(serviceResponse);

        Set<IdentMedStatus> result = sjekkIdenter.finnGyldigeOgLedigeIdenter(identer);

        assertThat(result, hasSize(2));
        for(IdentMedStatus ident : result) {
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

        when(sjekkOmGyldigeIdenterMock.execute(anySet())).thenReturn(serviceResponse);
        when(findIdenterNotUsedInDBMock.filtrer(anySet())).thenReturn(new HashSet());
        when(filterPaaIdenterTilgjengeligeIMiljoMock.filtrer(anySet())).thenReturn(new HashSet());

        Set<IdentMedStatus> result = sjekkIdenter.finnGyldigeOgLedigeIdenter(identer);

        assertThat(result, hasSize(2));
        for(IdentMedStatus ident : result) {
            assertThat(ident.getStatus(), is(IKKE_LEDIG));
        }
    }



}