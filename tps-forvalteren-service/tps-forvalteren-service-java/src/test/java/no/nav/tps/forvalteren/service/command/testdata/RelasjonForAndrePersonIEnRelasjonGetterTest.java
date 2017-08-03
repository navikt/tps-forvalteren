package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.service.RelasjonType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class RelasjonForAndrePersonIEnRelasjonGetterTest {

    Relasjon testRelasjon = new Relasjon();
    Person testPersonA = new Person();
    Person testPersonB = new Person();

    private String testFornavnA = "fornavn";
    private String testFornavnB = "fornavnB";

    @InjectMocks
    private RelasjonForAndrePersonIEnRelasjonGetter relasjonForAndrePersonIEnRelasjonGetter;

    @Before
    public void setup() {
        testPersonA.setFornavn(testFornavnA);
        testPersonB.setFornavn(testFornavnB);
    }

    @Test
    public void lagreGiftemaalRelasjonLagrer2RelasjonerOgSetterOgsaaRelasjonPaaAndrePersonenIHovedrelasjon() {
        testRelasjon.setPerson(testPersonA);
        testRelasjon.setPersonRelasjonMed(testPersonB);
        testRelasjon.setRelasjonTypeNavn(RelasjonType.EKTEFELLE.getRelasjonTypeNavn());

        Relasjon relasjonForPersonB = relasjonForAndrePersonIEnRelasjonGetter.execute(testRelasjon);

        assertEquals(relasjonForPersonB.getRelasjonTypeNavn(), RelasjonType.EKTEFELLE.getRelasjonTypeNavn());
        assertEquals(relasjonForPersonB.getPersonRelasjonMed().getFornavn(), testFornavnA);
        assertEquals(relasjonForPersonB.getPerson().getFornavn(), testFornavnB);
    }

    @Test
    public void hvisPersonARelasjonErFarSaaErPersonBIRelasjonSoenn(){
        testRelasjon.setPerson(testPersonA);
        testRelasjon.setPersonRelasjonMed(testPersonB);
        testRelasjon.setRelasjonTypeNavn(RelasjonType.FAR.getRelasjonTypeNavn());

        Relasjon relasjonForPersonB = relasjonForAndrePersonIEnRelasjonGetter.execute(testRelasjon);

        assertEquals(relasjonForPersonB.getRelasjonTypeNavn(), RelasjonType.BARN.getRelasjonTypeNavn());
        assertEquals(relasjonForPersonB.getPersonRelasjonMed().getFornavn(), testFornavnA);
        assertEquals(relasjonForPersonB.getPerson().getFornavn(), testFornavnB);
    }

    @Test
    public void hvisPersonARelasjonErMorSaaErPersonBIRelasjonSoenn(){
        testRelasjon.setPerson(testPersonA);
        testRelasjon.setPersonRelasjonMed(testPersonB);
        testRelasjon.setRelasjonTypeNavn(RelasjonType.MOR.getRelasjonTypeNavn());

        Relasjon relasjonForPersonB = relasjonForAndrePersonIEnRelasjonGetter.execute(testRelasjon);

        assertEquals(relasjonForPersonB.getRelasjonTypeNavn(), RelasjonType.BARN.getRelasjonTypeNavn());
        assertEquals(relasjonForPersonB.getPersonRelasjonMed().getFornavn(), testFornavnA);
        assertEquals(relasjonForPersonB.getPerson().getFornavn(), testFornavnB);
    }

    @Test
    public void hvisPersonARelasjonBarnOgPersonBErMannSaaBlirDetRelasjonFar(){
        testPersonB.setKjonn('M');
        testRelasjon.setPerson(testPersonA);
        testRelasjon.setPersonRelasjonMed(testPersonB);
        testRelasjon.setRelasjonTypeNavn(RelasjonType.BARN.getRelasjonTypeNavn());

        Relasjon relasjonForPersonB = relasjonForAndrePersonIEnRelasjonGetter.execute(testRelasjon);

        assertEquals(relasjonForPersonB.getRelasjonTypeNavn(), RelasjonType.FAR.getRelasjonTypeNavn());
        assertEquals(relasjonForPersonB.getPersonRelasjonMed().getFornavn(), testFornavnA);
        assertEquals(relasjonForPersonB.getPerson().getFornavn(), testFornavnB);
    }

    @Test
    public void hvisPersonARelasjonBarnOgPersonBErKvinneSaaBlirDetRelasjonMor(){
        testPersonB.setKjonn('K');
        testRelasjon.setPerson(testPersonA);
        testRelasjon.setPersonRelasjonMed(testPersonB);
        testRelasjon.setRelasjonTypeNavn(RelasjonType.BARN.getRelasjonTypeNavn());

        Relasjon relasjonForPersonB = relasjonForAndrePersonIEnRelasjonGetter.execute(testRelasjon);

        assertEquals(relasjonForPersonB.getRelasjonTypeNavn(), RelasjonType.MOR.getRelasjonTypeNavn());
        assertEquals(relasjonForPersonB.getPersonRelasjonMed().getFornavn(), testFornavnA);
        assertEquals(relasjonForPersonB.getPerson().getFornavn(), testFornavnB);
    }

}