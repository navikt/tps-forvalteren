package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataRequest;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TaBortOverfloedigIdenterITestdataRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultTaBortOverfloedigIdenterITestdataRequest implements TaBortOverfloedigIdenterITestdataRequest {

    public void execute(TestdataRequest request, int antallIdenter) {
        if (request.getIdenterTilgjengligIMiljoe().size() > antallIdenter) {
            List<String> identer = new ArrayList<>(request.getIdenterTilgjengligIMiljoe());
            request.getIdenterTilgjengligIMiljoe().clear();
            for(int i =0; i<antallIdenter; i++){
                request.getIdenterTilgjengligIMiljoe().add(identer.get(i));
            }
        }
    }
}
