package no.nav.tps.forvalteren.service.command.testdata.utils;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;

@RunWith(MockitoJUnitRunner.class)
public class HentUtdaterteRelasjonIderTest {

    @InjectMocks
    private HentUtdaterteRelasjonIder hentUtdaterteRelasjonIder;

    private Person personFraGUI = aMalePerson().build();
    private Person personFraDB = aMalePerson().build();

    @Test
    public void identiskeRelasjoner() {
        Relasjon gyldigFarRelasjon = new Relasjon(1L, personFraGUI, personFraDB, "FAR");
        Relasjon gyldigBarnRelasjon = new Relasjon(2L, personFraDB, personFraGUI, "FAR");

        personFraGUI.setRelasjoner(Arrays.asList(gyldigFarRelasjon));
        personFraDB.setRelasjoner(Arrays.asList(gyldigBarnRelasjon));

        Set<Long> result = hentUtdaterteRelasjonIder.execute(personFraGUI, personFraDB);

        assertThat(result, hasSize(0));
    }

    @Test
    public void forskjelligeRelasjoner() {
        Relasjon gyldigFarRelasjon = new Relasjon(1L, personFraGUI, personFraDB, "FAR");
        Relasjon gyldigBarnRelasjon = new Relasjon(2L, personFraDB, personFraGUI, "BARN");

        personFraGUI.setRelasjoner(Arrays.asList(gyldigFarRelasjon));
        personFraDB.setRelasjoner(Arrays.asList(gyldigBarnRelasjon));

        Set<Long> result = hentUtdaterteRelasjonIder.execute(personFraGUI, personFraDB);

        assertThat(result, hasSize(1));
        assertThat(result, contains(gyldigBarnRelasjon.getId()));
    }

}