package no.nav.tps.forvalteren.domain.rs.dolly;

import java.util.Set;

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
public class ImporterPersonRequest {

    private String ident;
    private Set<String> miljoe;
}
