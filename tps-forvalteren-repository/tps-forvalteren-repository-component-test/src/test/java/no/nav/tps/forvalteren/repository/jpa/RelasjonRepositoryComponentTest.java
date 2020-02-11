package no.nav.tps.forvalteren.repository.jpa;

import static no.nav.tps.forvalteren.domain.test.provider.GruppeProvider.aGruppe;
import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aFemalePerson;
import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.service.RelasjonType;
import no.nav.tps.forvalteren.repository.jpa.config.RepositoryTestConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RepositoryTestConfig.class)
@Transactional
public class RelasjonRepositoryComponentTest {

    private Person personOla = aMalePerson().build();
    private Person personKari = aFemalePerson().build();
    private Gruppe enGruppe = aGruppe().build();

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private GruppeRepository gruppeRepository;

    @Autowired
    private RelasjonRepository relasjonRepository;

    @Autowired
    private RelasjonTestRepository relasjonTestRepository;

    @Test
    @Rollback
    public void saveRelasjonLagrerRelasjon() {
        enGruppe.setPersoner(Arrays.asList(personKari, personOla));

        gruppeRepository.save(enGruppe);

        personRepository.saveAll(Arrays.asList(personKari, personOla));

        Relasjon rel = new Relasjon();
        rel.setPerson(personKari);
        rel.setPersonRelasjonMed(personOla);
        rel.setRelasjonTypeNavn(RelasjonType.EKTEFELLE.getName());

        personKari.setRelasjoner(Arrays.asList(rel));

        relasjonRepository.save(rel);

        List<Relasjon> relasjon = relasjonTestRepository.findAll();
        List<Person> personer = personRepository.findAllByOrderByIdAsc();

        assertThat(relasjon, hasSize(1));

        assertSame(relasjon.get(0).getPerson().getFornavn(), personKari.getFornavn());
        assertSame(relasjon.get(0).getPersonRelasjonMed().getFornavn(), personOla.getFornavn());

        assertSame(relasjon.get(0).getRelasjonTypeNavn(), RelasjonType.EKTEFELLE.getName());

        assertSame(personer.get(0).getRelasjoner().get(0).getPersonRelasjonMed().getFornavn(), personOla.getFornavn());
    }

    @Test
    @Rollback
    public void findByIdFinnerTidligereLagretRelasjoner() {
        enGruppe.setPersoner(Arrays.asList(personKari, personOla));

        gruppeRepository.save(enGruppe);

        personRepository.saveAll(Arrays.asList(personKari, personOla));

        Relasjon rel = new Relasjon();
        rel.setPerson(personKari);
        rel.setPersonRelasjonMed(personOla);
        rel.setRelasjonTypeNavn(RelasjonType.EKTEFELLE.getName());

        personKari.setRelasjoner(Arrays.asList(rel));

        relasjonRepository.save(rel);

        Relasjon relasjonFetched = relasjonRepository.findById(rel.getId()).get();

        assertNotNull(relasjonFetched);

        Relasjon relasjonFetchedFail = relasjonRepository.findById(123456);

        assertNull(relasjonFetchedFail);
    }

    @Test
    @Rollback
    public void saveRelasjonPaaBeggerPersoner() {
        enGruppe.setPersoner(Arrays.asList(personKari,personOla));

        gruppeRepository.save(enGruppe);

        personRepository.saveAll(Arrays.asList(personKari,personOla));

        Person kari = personRepository.findByIdentIn(Arrays.asList(personKari.getIdent())).get(0);
        Person ola = personRepository.findByIdentIn(Arrays.asList(personOla.getIdent())).get(0);

        saveGiftemaalCopyOfGiftemaalService(kari, ola);

        kari = personRepository.findByIdentIn(Arrays.asList(personKari.getIdent())).get(0);
        ola = personRepository.findByIdentIn(Arrays.asList(personOla.getIdent())).get(0);

        assertSame(kari.getRelasjoner().get(0).getPersonRelasjonMed().getFornavn(), ola.getFornavn());
        assertSame(kari.getRelasjoner().get(0).getRelasjonTypeNavn(), RelasjonType.EKTEFELLE.getName());

        assertSame(ola.getRelasjoner().get(0).getPersonRelasjonMed().getFornavn(), kari.getFornavn());
        assertSame(ola.getRelasjoner().get(0).getRelasjonTypeNavn(), RelasjonType.EKTEFELLE.getName());
    }

    private void saveGiftemaalCopyOfGiftemaalService(Person person1, Person person2){
        Relasjon relasjon1 =  new Relasjon();
        relasjon1.setPerson(person1);
        relasjon1.setPersonRelasjonMed(person2);

        Relasjon relasjon2 =  new Relasjon();
        relasjon2.setPerson(person2);
        relasjon2.setPersonRelasjonMed(person1);

        RelasjonType relasjonType = RelasjonType.EKTEFELLE;

        relasjon1.setRelasjonTypeNavn(relasjonType.getName());
        relasjon2.setRelasjonTypeNavn(relasjonType.getName());

        // Gjor dette fordi H2 ikke lager tom liste(Sender NULL) naar den ikke finner data av en eller annen grunn.
        if(person1.getRelasjoner() == null){
            person1.setRelasjoner(new ArrayList<>());
        }
        if(person2.getRelasjoner() == null){
            person2.setRelasjoner(new ArrayList<>());
        }

        for(Relasjon relasjon : person1.getRelasjoner()){
            if(relasjon.getRelasjonTypeNavn().equals(relasjonType.getName()) &&
                    relasjon.getPersonRelasjonMed().getIdent().equalsIgnoreCase(person2.getIdent())){
                return;
            }
        }

        person1.getRelasjoner().add(relasjon1);
        person2.getRelasjoner().add(relasjon2);

        relasjonRepository.save(relasjon1);
        relasjonRepository.save(relasjon2);
    }

}