import config.RepositoryTestConfig;
import no.nav.tps.vedlikehold.domain.repository.jpa.repoes.TPSKTestPersonRepository;
import no.nav.tps.vedlikehold.domain.repository.jpa.repoes.TestGruppeRepository;
import no.nav.tps.vedlikehold.domain.service.jpa.TPSKTestPerson;
import no.nav.tps.vedlikehold.domain.service.jpa.TestGruppe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import repoes.TPSKTestPersonTestRepository;
import repoes.TestGruppTestRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Peter Fløgstad on 16.02.2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootApplication(scanBasePackageClasses = RepositoryTestConfig.class)
@Transactional
public class TestGruppe_TestPersonIntergrationTest {

    private static final String name1 = "TestPeter";
    private static final String name2 = "TestRetep";
    private static final String slektsnavn1= "TestSlekt";
    private static final String slektsnavn2= "TestSlekt2";
    private static final String personnummer1 = "12345";
    private static final String personnummer2 = "54321";
    private static final String fodselsnummer = "070192";
    private static final String fodselsnummer2 = "070199";

    TPSKTestPerson person1,person2;

    @Autowired
    private TPSKTestPersonTestRepository tpskTestPersonTestRepository;

    @Autowired
    private TPSKTestPersonRepository tpskTestPersonRepository;

    @Autowired
    private TestGruppTestRepository testGruppTestRepository;

    @Autowired
    private TestGruppeRepository testGruppeRepository;

    //TODO Opprette testpersoner paa en "finere" maate
    @Before
    public void setup(){
        person1 = new TPSKTestPerson();
        System.out.println("TEs:" + person1.getFornavn());
        System.out.println("TEs:" + person1.getTestgrupper());
        person1.setFornavn(name1);
        person1.setSlektsnavn(slektsnavn1);
        person1.setPersonnummer(personnummer1);
        person1.setFodselsnummer(fodselsnummer);

        person2 = new TPSKTestPerson();
        person2.setFornavn(name2);
        person2.setSlektsnavn(slektsnavn2);
        person2.setPersonnummer(personnummer2);
        person2.setFodselsnummer(fodselsnummer2);
    }

    @Test
    public void lagredeGrupperMedNyePersonerBlirOgsaaReflektertITestPersonTabell() throws Exception{
        TestGruppe gruppe1 = new TestGruppe();
        gruppe1.setOwnerID("F148888");
        gruppe1.addTestPerson(person1);
        testGruppTestRepository.save(gruppe1);

        List<TestGruppe> alleGrupper = testGruppeRepository.findAll();
        assertTrue(alleGrupper.get(0).getTestpersoner().contains(person1));

        gruppe1 = testGruppeRepository.findByOwnerID("F148888").get(0);
        gruppe1.addTestPerson(person2);
        testGruppTestRepository.save(gruppe1);

        alleGrupper = testGruppeRepository.findAll();
        assertTrue(containsByFodselsnummer(alleGrupper.get(0).getTestpersoner(), fodselsnummer));
        assertTrue(containsByFodselsnummer(alleGrupper.get(0).getTestpersoner(), fodselsnummer2));

        List<TPSKTestPerson> allePersoner = tpskTestPersonRepository.findAll();
        assertTrue(containsByFodselsnummer(allePersoner, fodselsnummer));
        assertTrue(containsByFodselsnummer(allePersoner, fodselsnummer2));

        //TODO Feiler for faar ikke tak i Testgrupper pA denne maaten.
        for(TPSKTestPerson person : allePersoner){
            assertFalse(person.getTestgrupper().isEmpty());
            for(TestGruppe gruppe : person.getTestgrupper()){
                assertTrue(gruppe.getOwnerID().equals("F148888"));
            }
        }

        assertThat(alleGrupper, hasSize(1));
        assertThat(alleGrupper.get(0).getTestpersoner(), hasSize(2));
        assertThat(allePersoner, hasSize(2));
    }

    @Test
    public void lagredePersonerMedNyeGrupperBlirIKKEReflektertITestGruppeTabell() throws Exception{
        TestGruppe gruppe1 = new TestGruppe();
        gruppe1.setOwnerID("F148888");

        List<TestGruppe> alleGrupper = testGruppeRepository.findAll();
        assertThat(alleGrupper, hasSize(0));

        person1.addToGroup(gruppe1);
        tpskTestPersonTestRepository.save(person1);
        alleGrupper = testGruppeRepository.findAll();
        assertThat(alleGrupper, hasSize(0));

        testGruppTestRepository.save(gruppe1);
        alleGrupper = testGruppeRepository.findAll();
        assertThat(alleGrupper, hasSize(1));
        assertThat(alleGrupper.get(0).getTestpersoner(), hasSize(0));

        gruppe1 = testGruppeRepository.findByOwnerID("F148888").get(0);
        person2.addToGroup(gruppe1);
        tpskTestPersonTestRepository.save(person2);
        alleGrupper = testGruppeRepository.findAll();
        assertThat(alleGrupper, hasSize(1));
        assertThat(alleGrupper.get(0).getTestpersoner(), hasSize(0));

        gruppe1 = testGruppeRepository.findByOwnerID("F148888").get(0);
        gruppe1.addTestPerson(person1);
        gruppe1.addTestPerson(person2);
        testGruppTestRepository.save(gruppe1);
        alleGrupper = testGruppeRepository.findAll();
        assertThat(alleGrupper, hasSize(1));
        assertThat(alleGrupper.get(0).getTestpersoner(), hasSize(2));
    }

    @Test
    public void changesInATestPersonWhichIsAlreadyInATestGroupIsReflectedInGroupTableWhenPersonIsSaved() throws Exception{
        TestGruppe gruppe1 = new TestGruppe();
        gruppe1.setOwnerID("F148888");
        gruppe1.addTestPerson(person1);
        testGruppTestRepository.save(gruppe1);

        person1 = tpskTestPersonRepository.findByIdentNummer(fodselsnummer,personnummer1);
        List<TestGruppe> alleGrupper = testGruppeRepository.findAll();
        assertTrue(containsByFodselsnummer(alleGrupper.get(0).getTestpersoner(), fodselsnummer));
        assertFalse(containsByFodselsnummer(alleGrupper.get(0).getTestpersoner(), fodselsnummer2));

        person1 = tpskTestPersonRepository.findByIdentNummer(fodselsnummer,personnummer1);
        person1.setFodselsnummer(fodselsnummer2);
        tpskTestPersonTestRepository.save(person1);
        alleGrupper = testGruppeRepository.findAll();
        assertTrue(containsByFodselsnummer(alleGrupper.get(0).getTestpersoner(), fodselsnummer2));
        assertFalse(containsByFodselsnummer(alleGrupper.get(0).getTestpersoner(), fodselsnummer));
    }

    @Test
    public void fjerningAvPersonFraGruppeReflekteresIPersonTabell() throws Exception{
        TestGruppe gruppe1 = new TestGruppe();
        gruppe1.setOwnerID("F148888");
        gruppe1.addTestPerson(person1);
        //person1.addToGroup(gruppe1);      // Hvis man adder utkommenterte, saa virker det.
        gruppe1 = testGruppTestRepository.save(gruppe1);

        person1 = tpskTestPersonRepository.findAll().get(0);
        System.out.println("Pers navn: " + person2.getFornavn());
        System.out.println("Testgrupper: " + person2.getTestgrupper());
        assertFalse(person1.getTestgrupper().isEmpty());

        gruppe1.removeTestPerson(person2);
        //person2.removeFromGroup(gruppe1);

        testGruppTestRepository.save(gruppe1);
        person1 = tpskTestPersonRepository.findByIdentNummer(fodselsnummer,personnummer1);
        assertTrue(person2.getTestgrupper().isEmpty());
    }

    private boolean containsByFodselsnummer(Collection<TPSKTestPerson> personSet, String fodsel){
       for(TPSKTestPerson person : personSet) {
           if(person.getFodselsnummer().equals(fodsel))return true;
       }
       return false;
    }
}
