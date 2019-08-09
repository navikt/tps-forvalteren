package no.nav.tps.forvalteren.service.command.testdata.skd;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.NavneEndringsmeldingAarsakskode06.NAVN_ENDRING_MLD;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;

@Service
public class CreateNavnEndringsmeldinger {

    @Autowired
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    public List<SkdMeldingTrans1> executeFromPersons(List<Person> personerSomSkalHaNavnEndringsmelding, boolean addHeader) {

        return skdMessageCreatorTrans1.execute(NAVN_ENDRING_MLD, personerSomSkalHaNavnEndringsmelding, addHeader);
    }
}