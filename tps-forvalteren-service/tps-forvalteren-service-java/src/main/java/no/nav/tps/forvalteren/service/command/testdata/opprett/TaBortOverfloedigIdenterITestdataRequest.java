package no.nav.tps.forvalteren.service.command.testdata.opprett;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaBortOverfloedigIdenterITestdataRequest {

    public void execute(TestdataRequest request) {
        if (request.getIdenterTilgjengligIMiljoe().size() > request.getKriterium().getAntall()) {
            List<String> identer = new ArrayList<>(request.getIdenterTilgjengligIMiljoe());
            request.getIdenterTilgjengligIMiljoe().clear();
            for(int i = 0; i<request.getKriterium().getAntall(); i++){
                request.getIdenterTilgjengligIMiljoe().add(identer.get(i));
            }
        }
    }
}
