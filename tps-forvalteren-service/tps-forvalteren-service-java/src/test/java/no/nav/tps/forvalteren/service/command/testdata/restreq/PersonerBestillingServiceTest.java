package no.nav.tps.forvalteren.service.command.testdata.restreq;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.RelasjonType;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PersonerBestillingServiceTest {

    @InjectMocks
    PersonerBestillingService service;

    @Test
    public void createTpsfPersonFromRestRequest() {
    }

    @Test
    public void convertRequestTilPersoner() {

    }

    @Test
    public void setRelasjonerPaaPersoner_MannOgMannNullBarn() {
        Person mann1 = new Person();
        Person mann2 = new Person();
        mann1.setKjonn('M');
        mann2.setKjonn('M');

        service.setRelasjonerPaaPersoner(Arrays.asList(mann1), Arrays.asList(mann2), null);

        assertThat(mann1.getRelasjoner().size(), is(1));
        assertThat(mann2.getRelasjoner().size(), is(1));

        assertThat(mann1.getRelasjoner().get(0).getPersonRelasjonMed(), is(mann2));
        assertThat(mann2.getRelasjoner().get(0).getPersonRelasjonMed(), is(mann1));

        assertThat(mann1.getRelasjoner().get(0).getRelasjonTypeNavn(), is(RelasjonType.EKTEFELLE.getRelasjonTypeNavn()));
        assertThat(mann2.getRelasjoner().get(0).getRelasjonTypeNavn(), is(RelasjonType.EKTEFELLE.getRelasjonTypeNavn()));
    }

    @Test
    public void setRelasjonerPaaPersoner_MannOgBarnUtenKvinneGirIngenFoedsel() {
        Person mann = new Person();
        Person barn = new Person();
        mann.setKjonn('M');
        barn.setKjonn('K');

        service.setRelasjonerPaaPersoner(Arrays.asList(mann), new ArrayList<>(), Arrays.asList(barn));

        assertThat(mann.getRelasjoner().size(), is(1));
        assertThat(barn.getRelasjoner().size(), is(1));

        assertThat(mann.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(barn))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.BARN.getRelasjonTypeNavn())))
        ));

        assertThat(barn.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(mann))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.FAR.getRelasjonTypeNavn())))
        ));
    }

    @Test
    public void setRelasjonerPaaPersoner_KvinneOgBarnUtenMannGirFodsel() {
        Person kvinne = new Person();
        Person barn = new Person();
        kvinne.setKjonn('K');
        barn.setKjonn('K');

        service.setRelasjonerPaaPersoner(Arrays.asList(kvinne), new ArrayList<>(), Arrays.asList(barn));

        assertThat(kvinne.getRelasjoner().size(), is(1));
        assertThat(barn.getRelasjoner().size(), is(1));

        assertThat(kvinne.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(barn))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.FOEDSEL.getRelasjonTypeNavn())))
        ));

        assertThat(barn.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(kvinne))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.MOR.getRelasjonTypeNavn())))
        ));
    }

    @Test
    public void setRelasjonerPaaPersoner_MannKvinneOgBarnGirFoedselRelasjonPaaFarOgMorOgMorFarRelasjonPaaBarn() {
        Person mann = new Person();
        Person kvinne = new Person();
        Person barn = new Person();
        mann.setKjonn('M');
        kvinne.setKjonn('K');
        barn.setKjonn('K');

        service.setRelasjonerPaaPersoner(Arrays.asList(mann), Arrays.asList(kvinne), Arrays.asList(barn));

        assertThat(mann.getRelasjoner().size(), is(2));
        assertThat(kvinne.getRelasjoner().size(), is(2));
        assertThat(barn.getRelasjoner().size(), is(2));

        assertThat(mann.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(barn))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.FOEDSEL.getRelasjonTypeNavn())))
        ));

        assertThat(mann.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(kvinne))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.EKTEFELLE.getRelasjonTypeNavn())))
        ));

        assertThat(kvinne.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(barn))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.FOEDSEL.getRelasjonTypeNavn())))
        ));

        assertThat(kvinne.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(mann))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.EKTEFELLE.getRelasjonTypeNavn())))
        ));

        assertThat(barn.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(kvinne))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.MOR.getRelasjonTypeNavn())))
        ));

        assertThat(barn.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(mann))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.FAR.getRelasjonTypeNavn())))
        ));
    }

    @Test
    public void setRelasjonerPaaPersoner_MannOgKvinneOg2Barn() {
        Person mann = new Person();
        Person kvinne = new Person();
        Person barn1 = new Person();
        Person barn2 = new Person();
        mann.setKjonn('M');
        kvinne.setKjonn('K');
        barn1.setKjonn('K');
        barn2.setKjonn('M');

        service.setRelasjonerPaaPersoner(Arrays.asList(mann), Arrays.asList(kvinne), Arrays.asList(barn1, barn2));

        assertThat(mann.getRelasjoner().size(), is(3));
        assertThat(kvinne.getRelasjoner().size(), is(3));
        assertThat(barn1.getRelasjoner().size(), is(2));
        assertThat(barn2.getRelasjoner().size(), is(2));

        assertThat(mann.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(barn1))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.FOEDSEL.getRelasjonTypeNavn())))
        ));

        assertThat(mann.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(barn2))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.FOEDSEL.getRelasjonTypeNavn())))
        ));

        assertThat(mann.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(kvinne))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.EKTEFELLE.getRelasjonTypeNavn())))
        ));

        assertThat(kvinne.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(barn1))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.FOEDSEL.getRelasjonTypeNavn())))
        ));

        assertThat(kvinne.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(barn2))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.FOEDSEL.getRelasjonTypeNavn())))
        ));

        assertThat(kvinne.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(mann))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.EKTEFELLE.getRelasjonTypeNavn())))
        ));

        assertThat(barn1.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(kvinne))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.MOR.getRelasjonTypeNavn())))
        ));

        assertThat(barn1.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(mann))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.FAR.getRelasjonTypeNavn())))
        ));

        assertThat(barn2.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(kvinne))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.MOR.getRelasjonTypeNavn())))
        ));

        assertThat(barn2.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(mann))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.FAR.getRelasjonTypeNavn())))
        ));
    }

    @Test
    public void setRelasjonerPaaPersoner_MannMannOgBarnGirFarRelasjonPaaBeggeFedreMenIngenFodselMenBarnRelasjon() {
        Person mann1 = new Person();
        Person mann2 = new Person();
        Person barn = new Person();
        mann1.setKjonn('M');
        mann2.setKjonn('M');
        barn.setKjonn('K');

        service.setRelasjonerPaaPersoner(Arrays.asList(mann1), Arrays.asList(mann2), Arrays.asList(barn));

        assertThat(mann1.getRelasjoner().size(), is(2));
        assertThat(mann2.getRelasjoner().size(), is(2));

        assertThat(mann1.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(barn))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.BARN.getRelasjonTypeNavn())))
        ));

        assertThat(mann1.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(mann2))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.EKTEFELLE.getRelasjonTypeNavn())))
        ));

        assertThat(mann2.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(barn))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.BARN.getRelasjonTypeNavn())))
        ));

        assertThat(mann2.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(mann1))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.EKTEFELLE.getRelasjonTypeNavn())))
        ));

        assertThat(barn.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(mann1))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.FAR.getRelasjonTypeNavn())))
        ));

        assertThat(barn.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(mann2))).and(
                hasProperty("relasjonTypeNavn", equalTo(RelasjonType.FAR.getRelasjonTypeNavn())))
        ));
    }

}