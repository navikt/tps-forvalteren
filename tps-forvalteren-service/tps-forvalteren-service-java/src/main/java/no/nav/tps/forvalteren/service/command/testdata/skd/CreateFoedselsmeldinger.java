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

    public List<String> execute(List<Person> personer, boolean addHeader) {

        List<String> skdMeldinger = new ArrayList<>();
        if (!personer.isEmpty()) {
            skdMeldinger.addAll(skdMessageCreatorTrans1.execute(NAVN_FOEDSELSMELDING, personer, addHeader));
        }
        return skdMeldinger;
    }

}
