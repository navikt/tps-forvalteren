package no.nav.tps.forvalteren.service.command.testdata.opprett;

import lombok.Getter;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsPersonMal;

import java.util.Set;

@Setter
@Getter
public class TestdataRequest {

    RsPersonMal personMal;
            ;

    Set<String> identerGenerertForKriterie;

    Set<String> identerTilgjengligIMiljoe;

    public TestdataRequest(RsPersonMal personMal){
       personMal = personMal ;
    }

}
