package no.nav.tps.forvalteren.domain.rs.dolly;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.rs.RsPerson;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsPersonMiljoe {

    private String environment;
    private RsPerson person;
}