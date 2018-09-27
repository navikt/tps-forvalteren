package no.nav.tps.forvalteren.domain.rs.skd;

import java.time.LocalDateTime;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RsTpsFoedselsmelding {

    @NotBlank
    @Size(min = 11, max = 11)
    private String identMor;

    @Size(min = 11, max = 11)
    private String identFar;

    @Size(min = 3, max = 3)
    private String identtype;

    @NotBlank
    private LocalDateTime foedselsdato;

    @Size(min = 1, max = 1)
    private KjonnType kjonn;

    @Size(min = 3, max = 5)
    private AddressOrigin adresseFra;

    @NotBlank
    @Size(min = 2, max = 2)
    private String miljoe;
}
