package no.nav.tps.forvalteren.domain.rs.skd;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsSkdEndringsmelding {

    private Long id;

    @NotBlank
    @Size(min = 1500, max = 1500)
    private String endringsmelding;


}
