package no.nav.tps.forvalteren.service.command.testdata.skd;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.FoedselsmeldingAarsakskode01.FOEDSEL_MLD_NAVN;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;

@Service
public class CreateFoedselsmeldinger {

    @Autowired
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    public List<SkdMeldingTrans1> executeFromPersons(List<Person> personerSomSkalHaFoedselsmelding, boolean addHeader) {

        return skdMessageCreatorTrans1.execute(FOEDSEL_MLD_NAVN, personerSomSkalHaFoedselsmelding, addHeader);
    }
}