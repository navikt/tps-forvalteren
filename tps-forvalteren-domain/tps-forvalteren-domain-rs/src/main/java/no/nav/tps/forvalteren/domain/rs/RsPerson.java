package no.nav.tps.forvalteren.domain.rs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsPerson {

    private Long id;

    @NotBlank
    @Size(min = 11, max = 11)
    private String ident;

    @NotBlank
    @Size(min = 3, max = 3)
    private String identtype;

    @NotBlank
    @Size(min = 3, max = 3)
    private Character kjonn;

    @NotBlank
    @Size(min = 1, max = 50)
    private String fornavn;

    @Size(min = 1, max = 50)
    private String mellomnavn;

    @NotBlank
    @Size(min = 1, max = 15)
    private String etternavn;

    private String statsborgerskap;

    private String spesreg;

    private LocalDateTime spesregDato;

    private List<RsGateadresse> gateadresse;

    private List<RsPostadresse> postadresse;

    private LocalDateTime regdato;

    private RsSimpleGruppe gruppe;

}
