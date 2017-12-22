package no.nav.tps.forvalteren.domain.rs.skd;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsNewSkdEndringsmelding {

    @NonNull
    @Size(min = 2, max = 2)
    private String meldingstype;

    @NonNull
    @Size(min = 1, max = 200)
    private String navn;

}
