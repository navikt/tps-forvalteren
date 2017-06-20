package no.nav.tps.forvalteren.service.command.testdata.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class IdentListeStatusResponse {

    private Set<IdentMedStatus> identer;

}
