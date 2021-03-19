package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.service.command.testdata.utils.TestdataConstants.ORACLE_MAX_IN_SET_ELEMENTS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;

@Service
public class DeleteRelasjonerByIdIn {

    private RelasjonRepository relasjonRepository;
    private SessionFactory sessionFactory;

    public DeleteRelasjonerByIdIn(EntityManagerFactory factory, RelasjonRepository relasjonRepository) {
        this.sessionFactory = factory.unwrap(SessionFactory.class);
        this.relasjonRepository = relasjonRepository;
    }

    public void execute(List<Long> personIds) {

        List<List<Long>> partitionsIds = Lists.partition(personIds, ORACLE_MAX_IN_SET_ELEMENTS);
        for (List<Long> partition : partitionsIds) {
            relasjonRepository.deleteByPersonRelasjonMedIdIn(new HashSet<>(partition));
        }
    }

    public void delete(Set<Long> relasjonIds) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        relasjonIds.forEach(id ->
                session.delete(session.get(Relasjon.class, id)));

        session.getTransaction().commit();
        session.close();
    }
}
