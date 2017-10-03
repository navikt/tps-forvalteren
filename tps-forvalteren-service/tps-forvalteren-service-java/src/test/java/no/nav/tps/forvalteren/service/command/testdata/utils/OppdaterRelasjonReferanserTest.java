package no.nav.tps.forvalteren.service.command.testdata.utils;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;

@RunWith(MockitoJUnitRunner.class)
public class OppdaterRelasjonReferanserTest {

    @InjectMocks
    private OppdaterRelasjonReferanser oppdaterRelasjonReferanser;

    private Person personFraGUI = aMalePerson().build();
    private Person personFraDB = aMalePerson().build();

    @Test
    public void identiskeRelasjonerSomSkalOppdatereDenFraGUI() {
        Relasjon gyldigFarRelasjon = new Relasjon(1L, personFraGUI, personFraDB, "FAR");
        Relasjon gyldigBarnRelasjon = new Relasjon(2L, personFraDB, personFraGUI, "FAR");

        personFraGUI.setRelasjoner(Arrays.asList(gyldigFarRelasjon));
        personFraDB.setRelasjoner(Arrays.asList(gyldigBarnRelasjon));

        oppdaterRelasjonReferanser.execute(personFraGUI, personFraDB);

        assertThat(gyldigFarRelasjon.getId(), is(gyldigBarnRelasjon.getId()));
    }

    @Test
    public void forskjelligeRelasjoner() {
        Relasjon gyldigFarRelasjon = new Relasjon(1L, personFraGUI, personFraDB, "FAR");
        Relasjon gyldigBarnRelasjon = new Relasjon(2L, personFraDB, personFraGUI, "BARN");

        personFraGUI.setRelasjoner(Arrays.asList(gyldigFarRelasjon));
        personFraDB.setRelasjoner(Arrays.asList(gyldigBarnRelasjon));

        oppdaterRelasjonReferanser.execute(personFraGUI, personFraDB);

        assertThat(gyldigFarRelasjon.getId(), is(1L));
        assertThat(gyldigBarnRelasjon.getId(), is(2L));

    }
}