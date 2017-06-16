package no.nav.tps.forvalteren.service.command.testdata.opprett;

import lombok.Getter;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;

import java.util.Set;

@Setter
@Getter
public class TestdataRequest {

    RsPersonKriterier kriterie;

    Set<String> identerGenerertForKriterie;

    Set<String> identerTilgjengligIMiljoe;

    public TestdataRequest(RsPersonKriterier personKriterier){
       kriterie = personKriterier ;
    }

}
