package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.service.command.testdata.opprett.TaBortOverfloedigIdenterITestdataRequest;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataRequest;

@Service
public class DefaultTaBortOverfloedigIdenterITestdataRequest implements TaBortOverfloedigIdenterITestdataRequest {

    public void execute(TestdataRequest request) {
        if (request.getIdenterTilgjengligIMiljoe().size() > request.getInputPerson().getAntallIdenter()) {
            List<String> identer = new ArrayList<>(request.getIdenterTilgjengligIMiljoe());
            request.getIdenterTilgjengligIMiljoe().clear();
            for (int i = 0; i < request.getInputPerson().getAntallIdenter(); i++) {
                request.getIdenterTilgjengligIMiljoe().add(identer.get(i));
            }
        }
    }
}
