package no.nav.tps.forvalteren.domain.rs;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsVergemaal {

    @NotNull
    private Long id;

    @NotBlank
    @Size(min = 11, max = 11)
    private String ident;

    @NotBlank
    private String saksid;

    @Size(min = 4, max = 4)
    private String embete;

    @Size(min = 3, max = 3)
    private String sakstype;

    private LocalDateTime vedtaksdato;

    @NotBlank
    private String internVergeId;

    @Size(min = 11, max = 11)
    private String vergeFnr;

    @Size(min = 3, max = 3)
    private String vergetype;

    @Size(min = 3, max = 3)
    private String mandattype;

    @Size(max = 100)
    private String mandattekst;
}
