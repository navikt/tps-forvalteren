import config.RepositoryTestConfig;
import no.nav.tps.vedlikehold.domain.repository.jpa.repoes.TPSKTestPersonRepository;
import no.nav.tps.vedlikehold.domain.service.jpa.TPSKTestPerson;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Peter Fløgstad on 14.02.2017.
 */

//TODO Trengs en egen Test Rep? Skal kunne skrive osv osv i prod også
    //TODO Gjøre ferdig denne en gang.. Finner ikke Klassen at the momement

@Transactional
public class TPSKTestPersonTestRepositoryTest {


    private static final String name1 = "TestPeter";
    private static final String name2 = "TestRetep";

    @Autowired
    private TPSKTestPersonTestRepository tpskTestPersonTestRepository;

    @Autowired
    private TPSKTestPersonRepository tpskTestPersonRepository;

    @Test
    public void findAllPersons(){

        TPSKTestPerson person1 = new TPSKTestPerson();
        person1.setFornavn(name1);
        TPSKTestPerson person2 = new TPSKTestPerson();
        person2.setFornavn(name2);


        tpskTestPersonTestRepository.save(person1);
        tpskTestPersonTestRepository.save(person2);

        List<TPSKTestPerson> liste = tpskTestPersonRepository.findAll();

        assertEquals(2, liste.size());

        for(TPSKTestPerson person : liste){
            System.out.println(person.getFornavn());
        }
    }


}