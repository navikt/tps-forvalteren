package no.nav.tps.forvalteren.domain.rs.skd;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDateTime;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
public class RsTpsFoedselsmeldingRequest {

    @NotBlank
    @Size(min = 11, max = 11)
    private String identMor;

    @Size(min = 11, max = 11)
    private String identFar;

    @NotBlank
    @Size(min = 3, max = 3)
    private IdentType identtype;

    @NotBlank
    private LocalDateTime foedselsdato;

    @Size(min = 1, max = 1)
    private KjoennType kjonn;

    @Size(min = 3, max = 5)
    private AddressOrigin adresseFra;

    @NotBlank
    @Size(min = 2, max = 2)
    private Set<String> miljoer;

    public boolean validatesOk() {
        return identtype != null && foedselsdato != null && isNotBlank(identMor) && isValidateMiljoer();
    }

    private boolean isValidateMiljoer() {
        return miljoer != null && !miljoer.isEmpty();
    }
}