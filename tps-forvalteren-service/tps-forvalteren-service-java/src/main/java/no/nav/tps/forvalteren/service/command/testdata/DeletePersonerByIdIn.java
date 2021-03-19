package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.service.command.testdata.utils.TestdataConstants.ORACLE_MAX_IN_SET_ELEMENTS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.collect.Lists;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;

@Service
public class DeletePersonerByIdIn {

    private PersonRepository personRepository;
    private RelasjonRepository relasjonRepository;
    private DoedsmeldingRepository doedsmeldingRepository;
    private SessionFactory sessionFactory;

    public DeletePersonerByIdIn(EntityManagerFactory factory, PersonRepository personRepository,
            RelasjonRepository relasjonRepository, DoedsmeldingRepository doedsmeldingRepository) {

        this.sessionFactory = factory.unwrap(SessionFactory.class);
        this.personRepository = personRepository;
        this.relasjonRepository = relasjonRepository;
        this.doedsmeldingRepository = doedsmeldingRepository;
    }

    public void execute(List<Long> ids) {

        List<List<Long>> partitionsIds = Lists.partition(ids, ORACLE_MAX_IN_SET_ELEMENTS);
        for (List<Long> partition : partitionsIds) {
            doedsmeldingRepository.deleteByPersonIdIn(partition);
            relasjonRepository.deleteByPersonRelasjonMedIdIn(new HashSet<>(partition));
            personRepository.deleteByIdIn(partition);
        }
    }

    @Transactional
    public void delete(Set<Long> personIds) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        personIds.forEach(id ->
                session.delete(session.get(Person.class, id)));

        session.getTransaction().commit();
        session.close();
    }
}
