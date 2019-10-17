package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.BARN;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.FAR;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.FOEDSEL;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.MOR;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.PARTNER;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.RelasjonType;

@RunWith(MockitoJUnitRunner.class)
public class PersonerBestillingServiceTest {

    @InjectMocks
    PersonerBestillingService service;

    @Test
    public void setRelasjonerPaaPersoner_MannOgMannNullBarn() {

        Person mann1 = Person.builder().kjonn("M").build();
        Person mann2 = Person.builder().kjonn("M").build();

        service.setRelasjonerPaaPersoner(asList(mann1), asList(mann2), emptyList());

        assertThat(mann1.getRelasjoner().size(), is(1));
        assertThat(mann2.getRelasjoner().size(), is(1));

        assertThat(mann1.getRelasjoner().get(0).getPersonRelasjonMed(), is(mann2));
        assertThat(mann2.getRelasjoner().get(0).getPersonRelasjonMed(), is(mann1));

        assertThat(mann1.getRelasjoner().get(0).getRelasjonTypeNavn(), is(PARTNER.getName()));
        assertThat(mann2.getRelasjoner().get(0).getRelasjonTypeNavn(), is(PARTNER.getName()));
    }

    @Test
    public void setRelasjonerPaaPersoner_MannOgBarnUtenKvinneGirIngenFoedsel() {

        Person mann = Person.builder().kjonn("M").build();
        Person barn = Person.builder().kjonn("K").build();

        service.setRelasjonerPaaPersoner(asList(mann), new ArrayList<>(), asList(barn));

        assertThat(mann.getRelasjoner().size(), is(1));
        assertThat(barn.getRelasjoner().size(), is(1));

        assertRelasjon(mann, barn, BARN);
        assertRelasjon(barn, mann, FAR);
    }

    @Test
    public void setRelasjonerPaaPersoner_KvinneOgBarnUtenMannGirFodsel() {

        Person kvinne = Person.builder().kjonn("K").build();
        Person barn = Person.builder().kjonn("K").build();

        service.setRelasjonerPaaPersoner(asList(kvinne), new ArrayList<>(), asList(barn));

        assertThat(kvinne.getRelasjoner().size(), is(1));
        assertThat(barn.getRelasjoner().size(), is(1));

        assertRelasjon(kvinne, barn, FOEDSEL);
        assertRelasjon(barn, kvinne, MOR);
    }

    @Test
    public void setRelasjonerPaaPersoner_MannKvinneOgBarnGirFoedselRelasjonPaaFarOgMorOgMorFarRelasjonPaaBarn() {

        Person mann = Person.builder().kjonn("M").build();
        Person kvinne = Person.builder().kjonn("K").build();
        Person barn = Person.builder().kjonn("K").build();

        service.setRelasjonerPaaPersoner(asList(mann), asList(kvinne), asList(barn));

        assertThat(mann.getRelasjoner().size(), is(2));
        assertThat(kvinne.getRelasjoner().size(), is(2));
        assertThat(barn.getRelasjoner().size(), is(2));

        assertRelasjon(mann, barn, FOEDSEL);
        assertRelasjon(mann, kvinne, PARTNER);

        assertRelasjon(kvinne, barn, FOEDSEL);
        assertRelasjon(kvinne, mann, PARTNER);

        assertRelasjon(barn, kvinne, MOR);
        assertRelasjon(barn, mann, FAR);
    }

    @Test
    public void setRelasjonerPaaPersoner_MannOgKvinneOg2Barn() {

        Person mann = Person.builder().kjonn("M").build();
        Person kvinne = Person.builder().kjonn("K").build();
        Person barn1 = Person.builder().kjonn("K").build();
        Person barn2 = Person.builder().kjonn("M").build();

        service.setRelasjonerPaaPersoner(asList(mann), asList(kvinne), asList(barn1, barn2));

        assertThat(mann.getRelasjoner().size(), is(3));
        assertThat(kvinne.getRelasjoner().size(), is(3));
        assertThat(barn1.getRelasjoner().size(), is(2));
        assertThat(barn2.getRelasjoner().size(), is(2));

        assertRelasjon(mann, barn1, FOEDSEL);
        assertRelasjon(mann, barn2, FOEDSEL);
        assertRelasjon(mann, kvinne, PARTNER);

        assertRelasjon(kvinne, barn1, FOEDSEL);
        assertRelasjon(kvinne, barn2, FOEDSEL);
        assertRelasjon(kvinne, mann, PARTNER);

        assertRelasjon(barn1, kvinne, MOR);
        assertRelasjon(barn1, mann, FAR);

        assertRelasjon(barn2, kvinne, MOR);
        assertRelasjon(barn2, mann, FAR);
    }

    @Test
    public void setRelasjonerPaaPersoner_MannMannOgBarnGirFarRelasjonPaaBeggeFedreMenIngenFodselMenBarnRelasjon() {
        Person mann1 = new Person();
        Person mann2 = new Person();
        Person barn = new Person();
        mann1.setKjonn("M");
        mann2.setKjonn("M");
        barn.setKjonn("K");

        service.setRelasjonerPaaPersoner(asList(mann1), asList(mann2), asList(barn));

        assertThat(mann1.getRelasjoner().size(), is(2));
        assertThat(mann2.getRelasjoner().size(), is(2));

        assertRelasjon(mann1, barn, BARN);
        assertRelasjon(mann1, mann2, PARTNER);

        assertRelasjon(mann2, barn, BARN);
        assertRelasjon(mann2, mann1, PARTNER);

        assertRelasjon(barn, mann1, FAR);
        assertRelasjon(barn, mann2, FAR);
    }

    @Test
    public void setRelasjonerPaaPersoner_PersonerMedFlerePartnereOgMedBarn() {

        Person mann = Person.builder().kjonn("M").build();
        Person kvinne = Person.builder().kjonn("K").build();

        Person partner1 = Person.builder().kjonn("K").build();
        Person partner2 = Person.builder().kjonn("K").build();
        Person partner3 = Person.builder().kjonn("M").build();
        Person partner4 = Person.builder().kjonn("M").build();

        Person barn1 = Person.builder().kjonn("K").build();
        Person barn2 = Person.builder().kjonn("M").build();
        Person barn3 = Person.builder().kjonn("K").build();
        Person barn4 = Person.builder().kjonn("M").build();
        Person barn5 = Person.builder().kjonn("K").build();
        Person barn6 = Person.builder().kjonn("M").build();

        service.setRelasjonerPaaPersoner(
                asList(mann, kvinne),
                asList(partner1, partner2, partner3, partner4),
                asList(barn1, barn2, barn3, barn4, barn5, barn6));

        assertThat(mann.getRelasjoner().size(), is(5));
        assertThat(kvinne.getRelasjoner().size(), is(5));

        assertRelasjon(mann, partner1, PARTNER);
        assertRelasjon(mann, partner2, PARTNER);
        assertRelasjon(mann, barn1, FOEDSEL);
        assertRelasjon(mann, barn2, FOEDSEL);
        assertRelasjon(mann, barn3, FOEDSEL);

        assertRelasjon(kvinne, partner3, PARTNER);
        assertRelasjon(kvinne, partner4, PARTNER);
        assertRelasjon(kvinne, barn4, FOEDSEL);
        assertRelasjon(kvinne, barn5, FOEDSEL);
        assertRelasjon(kvinne, barn6, FOEDSEL);

        assertRelasjon(partner1, mann, PARTNER);
        assertRelasjon(partner1, barn1, FOEDSEL);
        assertRelasjon(partner1, barn3, FOEDSEL);

        assertRelasjon(partner2, mann, PARTNER);
        assertRelasjon(partner2, barn2, FOEDSEL);

        assertRelasjon(partner3, kvinne, PARTNER);
        assertRelasjon(partner3, barn4, FOEDSEL);
        assertRelasjon(partner3, barn6, FOEDSEL);

        assertRelasjon(partner4, kvinne, PARTNER);
        assertRelasjon(partner4, barn5, FOEDSEL);

        assertRelasjon(barn1, mann, FAR);
        assertRelasjon(barn1, partner1, MOR);

        assertRelasjon(barn2, mann, FAR);
        assertRelasjon(barn2, partner2, MOR);

        assertRelasjon(barn3, mann, FAR);
        assertRelasjon(barn3, partner1, MOR);

        assertRelasjon(barn4, kvinne, MOR);
        assertRelasjon(barn4, partner3, FAR);

        assertRelasjon(barn5, kvinne, MOR);
        assertRelasjon(barn5, partner4, FAR);

        assertRelasjon(barn6, kvinne, MOR);
        assertRelasjon(barn6, partner3, FAR);
    }

    private static void assertRelasjon(Person relasjon1, Person relasjon2, RelasjonType type) {

        assertThat(relasjon1.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(relasjon2))).and(
                hasProperty("relasjonTypeNavn", equalTo(type.getName())))
        ));
    }
}