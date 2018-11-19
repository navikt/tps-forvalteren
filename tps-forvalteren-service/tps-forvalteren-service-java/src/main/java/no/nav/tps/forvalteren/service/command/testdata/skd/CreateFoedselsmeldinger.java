package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;

@Service
public class CreateFoedselsmeldinger {

    @Autowired
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    private static final String NAVN_FOEDSELSMELDING = "Foedselsmelding";

    public List<SkdMeldingTrans1> executeFromPersons(List<Person> personerSomSkalHaFoedselsmelding, boolean addHeader) {

        return skdMessageCreatorTrans1.execute(NAVN_FOEDSELSMELDING, personerSomSkalHaFoedselsmelding, addHeader);
    }
}