package no.nav.tps.forvalteren.domain.rs;

import java.util.List;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RsDeathRowCheckIdent {

    @NotBlank
    @Size(min = 11, max = 11)
    private List<String> identer;

    @NotBlank
    @Size(min = 1, max = 3)
    private String miljoe;
}
