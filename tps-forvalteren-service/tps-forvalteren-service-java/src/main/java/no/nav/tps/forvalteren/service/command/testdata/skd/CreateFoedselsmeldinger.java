package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.ArrayList;
import java.util.List;

import no.nav.tps.forvalteren.domain.jpa.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateFoedselsmeldinger {

    @Autowired
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    private static final String NAVN_FOEDSELSMELDING = "Foedselsmelding";

    public List<SkdMeldingTrans1> execute(List<Person> personer, boolean addHeader) {

        List<SkdMeldingTrans1> skdMeldinger = new ArrayList<>();
        if (!personer.isEmpty()) {
            skdMeldinger.addAll(skdMessageCreatorTrans1.execute(NAVN_FOEDSELSMELDING, personer, addHeader));
        }
        return skdMeldinger;
    }

}
