package no.nav.tps.forvalteren.service.command.testdata;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@RunWith(MockitoJUnitRunner.class)
public class sjekkOmGyldigeIdenterTest {

    @InjectMocks
    private sjekkOmGyldigeIdenter sjekkOmGyldigeIdenter;

    @Test
    public void sjekkOmGyldigeFnrErGyldige() {
        Set<String> identer = new HashSet<>();
        identer.add("24030441210");
        identer.add("26065825936");
        identer.add("27034234111");

        Set<String> result = sjekkOmGyldigeIdenter.execute(identer);

        assertThat(result, hasSize(3));
    }

    @Test
    public void sjekkOmGyldigeDnrErGyldige() {
        Set<String> identer = new HashSet<>();
        identer.add("67127901624");
        identer.add("65056503425");
        identer.add("48085709316");

        Set<String> result = sjekkOmGyldigeIdenter.execute(identer);

        assertThat(result, hasSize(3));
    }

    @Test
    public void sjekkOmGyldigeBnrErGyldige() {
        Set<String> identer = new HashSet<>();
        identer.add("01210123977");
        identer.add("01210120668");
        identer.add("01210126690");

        Set<String> result = sjekkOmGyldigeIdenter.execute(identer);

        assertThat(result, hasSize(3));
    }

    @Test
    public void sjekkIdenterMedFeilKontrollsiffer() {
        Set<String> identer = new HashSet<>();
        identer.add("01210123999");
        identer.add("67127901630");
        identer.add("26065825902");

        Set<String> result = sjekkOmGyldigeIdenter.execute(identer);

        assertThat(result, hasSize(0));
    }

    @Test
    public void sjekkIdenterSomErForLange() {
        Set<String> identer = new HashSet<>();
        identer.add("012106123999");
        identer.add("671237901630");
        identer.add("260645825902");

        Set<String> result = sjekkOmGyldigeIdenter.execute(identer);

        assertThat(result, hasSize(0));
    }

    @Test
    public void sjekkIdenterMedUgyldigDato() {
        Set<String> identer = new HashSet<>();
        identer.add("32130441261");
        identer.add("67137901667");
        identer.add("33210123915");

        Set<String> result = sjekkOmGyldigeIdenter.execute(identer);

        assertThat(result, hasSize(0));
    }

    @Test
    public void sjekkIdent() {
        Set<String> identer = new HashSet<>();
        identer.add("01021070501");

        Set<String> result = sjekkOmGyldigeIdenter.execute(identer);

        assertThat(result, hasSize(1));
    }

}