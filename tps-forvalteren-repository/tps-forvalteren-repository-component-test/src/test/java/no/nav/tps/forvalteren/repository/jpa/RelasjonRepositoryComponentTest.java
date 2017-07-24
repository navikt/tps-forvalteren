package no.nav.tps.forvalteren.repository.jpa;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.jpa.RelasjonType;
import no.nav.tps.forvalteren.repository.jpa.config.RepositoryTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;
import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aFemalePerson;
import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static no.nav.tps.forvalteren.domain.test.provider.GruppeProvider.aGruppe;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RepositoryTestConfig.class)
@Transactional
public class RelasjonRepositoryComponentTest {

    private Person personOla = aMalePerson().build();
    private Person personKari = aFemalePerson().build();
    private Gruppe enGruppe = aGruppe().build();

    private final static String REL_TYPE_GIFT = "gift";

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private GruppeRepository gruppeRepository;

    @Autowired
    private RelasjonTypeRepository relasjonTypeRepository;

    @Autowired
    private RelasjonRepository relasjonRepository;

    @Test
    @Rollback
    public void saveRelasjonLagrerRelasjon() {
        enGruppe.setPersoner(Arrays.asList(personKari,personOla));

        gruppeRepository.save(enGruppe);

        personRepository.save(Arrays.asList(personKari,personOla));

        RelasjonType type = new RelasjonType();
        type.setName(REL_TYPE_GIFT);

        relasjonTypeRepository.save(type);

        Relasjon rel = new Relasjon();
        rel.setPerson(personKari);
        rel.setPersonRelasjonMed(personOla);
        rel.setRelasjonType(type);

        personKari.setRelasjoner(Arrays.asList(rel));

        relasjonRepository.save(rel);

        List<Relasjon> relasjon = relasjonRepository.findAll();
        List<Person> personer = personRepository.findAllByOrderByIdAsc();

        assertThat(relasjon, hasSize(1));

        assertSame(relasjon.get(0).getPerson().getFornavn(),personKari.getFornavn() );
        assertSame(relasjon.get(0).getPersonRelasjonMed().getFornavn(),personOla.getFornavn() );

        assertSame(relasjon.get(0).getRelasjonType().getName(),REL_TYPE_GIFT );

        assertSame(personer.get(0).getRelasjoner().get(0).getPersonRelasjonMed().getFornavn(), personOla.getFornavn());
    }


}