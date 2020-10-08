package no.nav.tps.forvalteren.domain.rs.dolly;

import java.time.LocalDateTime;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsVergemaalRequest {

    @Size(min = 4, max = 4)
    private String embete;

    @Size(min = 3, max = 3)
    private String sakstype;

    private LocalDateTime vedtaksdato;

    @Size(min = 3, max = 3)
    private String identTypeVerge;

    @Size(min = 3, max = 3)
    private String mandattype;
}
