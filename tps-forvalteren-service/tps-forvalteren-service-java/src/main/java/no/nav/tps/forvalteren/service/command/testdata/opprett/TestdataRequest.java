package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;

@Setter
@Getter
public class TestdataRequest {

    RsPersonKriterier kriterium;

    Set<String> identerGenerertForKriteria;

    Set<String> identerTilgjengligIMiljoe;

    public TestdataRequest(RsPersonKriterier personKriterier) {
        kriterium = personKriterier;
    }

    public Set<String> getIdenterGenerertForKriteria() {
        if (identerGenerertForKriteria == null) {
            identerGenerertForKriteria = new HashSet<>();
        }
        return identerGenerertForKriteria;
    }

    public Set<String> getIdenterTilgjengligIMiljoe() {
        if (identerTilgjengligIMiljoe == null) {
            identerTilgjengligIMiljoe = new HashSet<>();
        }
        return identerTilgjengligIMiljoe;
    }
}
