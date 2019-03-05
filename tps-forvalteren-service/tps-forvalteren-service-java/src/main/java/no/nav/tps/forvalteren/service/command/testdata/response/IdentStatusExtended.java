package no.nav.tps.forvalteren.service.command.testdata.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdentStatusExtended {

    private String ident;
    private String status;
    private boolean available;
}
