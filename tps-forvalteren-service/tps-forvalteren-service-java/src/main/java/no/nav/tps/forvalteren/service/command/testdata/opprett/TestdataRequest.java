package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsPersonMal;

@Setter
@Getter
public class TestdataRequest {

    RsPersonKriterier kriterium;
    RsPersonMal inputPerson;

    Set<String> identerGenerertForKriterie;

    Set<String> identerTilgjengligIMiljoe;

    public TestdataRequest(RsPersonMal personMal){
       inputPerson = personMal ;
    }

}
