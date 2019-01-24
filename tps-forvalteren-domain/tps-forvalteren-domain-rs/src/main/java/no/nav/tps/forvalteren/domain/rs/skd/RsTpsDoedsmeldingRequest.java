package no.nav.tps.forvalteren.domain.rs.skd;

import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.rs.skd.DoedsmeldingHandlingType.D;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDateTime;
import java.util.List;
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
public class RsTpsDoedsmeldingRequest {

    @NotBlank
    @Size(min = 11, max = 11)
    private String ident;

    @Size(min = 11, max = 11)
    private DoedsmeldingHandlingType handling;

    private LocalDateTime doedsdato;

    @NotBlank
    @Size(min = 2, max = 2)
    private List<String> miljoer;

    public boolean validatesOk() {
        return (nonNull(doedsdato) || handling != D) && isValidMiljoe();
    }

    private boolean isValidMiljoe() {
        return isNotBlank(ident) && nonNull(miljoer) && !miljoer.isEmpty();
    }
}