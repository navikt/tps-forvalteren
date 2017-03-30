import config.RepositoryTestConfig;
import no.nav.tps.vedlikehold.domain.repository.jpa.repoes.TPSKTestPersonRepository;
import no.nav.tps.vedlikehold.domain.service.jpa.TPSKTestPerson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import repoes.TPSKTestPersonTestRepository;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Peter Fløgstad on 14.02.2017.
 */

//TODO Trengs en egen Test Rep? Skal kunne skrive osv osv i prod også
    //TODO Gjøre ferdig denne en gang.. Finner ikke Klassen at the momement


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootApplication(scanBasePackageClasses = RepositoryTestConfig.class)
@Transactional
public class TPSKTestPersonTestRepositoryTest {

    //TODO Lag en litt bebdre måte å opprette bruker/Objekter. Sjekk ut NORG2 hvordan de gjør det.
    private static final String name1 = "TestPeter";
    private static final String name2 = "TestRetep";
    private static final String slektsnavn1= "TestSlekt";
    private static final String slektsnavn2= "TestSlekt2";
    private static final String personnummer1 = "12345";
    private static final String personnummer2 = "54321";
    private static final String fodselsnummer = "070192";
    private static final String fodselsnummer2 = "070199";

    @Autowired
    private TPSKTestPersonTestRepository tpskTestPersonTestRepository;

    @Autowired
    private TPSKTestPersonRepository tpskTestPersonRepository;

    @Test
    public void findAllPersons(){

        TPSKTestPerson person1 = new TPSKTestPerson();
        person1.setFornavn(name1);
        person1.setSlektsnavn(slektsnavn1);
        person1.setPersonnummer(personnummer1);
        person1.setFodselsnummer(fodselsnummer);

        TPSKTestPerson person2 = new TPSKTestPerson();
        person2.setFornavn(name2);
        person2.setSlektsnavn(slektsnavn2);
        person2.setPersonnummer(personnummer2);
        person2.setFodselsnummer(fodselsnummer2);

        tpskTestPersonTestRepository.save(person1);
        tpskTestPersonTestRepository.save(person2);

        List<TPSKTestPerson> liste = tpskTestPersonRepository.findAll();

        assertEquals(2, liste.size());

        for(TPSKTestPerson person : liste){
            System.out.println(person.getFornavn());
        }
    }

    @Test
    public void findByIdent(){
        TPSKTestPerson person1 = new TPSKTestPerson();
        person1.setFornavn(name1);
        person1.setSlektsnavn(slektsnavn1);
        person1.setPersonnummer(personnummer1);
        person1.setFodselsnummer(fodselsnummer);

        tpskTestPersonTestRepository.save(person1);

        TPSKTestPerson pers = tpskTestPersonRepository.findByIdentNummer(fodselsnummer, personnummer1);

        assertNotNull(pers);
        assertTrue(name1.equals(pers.getFornavn()));
    }
}