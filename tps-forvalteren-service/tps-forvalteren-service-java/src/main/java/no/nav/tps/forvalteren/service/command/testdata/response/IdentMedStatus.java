package no.nav.tps.forvalteren.service.command.testdata.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(exclude = "status")
public class IdentMedStatus {

    private String ident;
    private String status;
}
