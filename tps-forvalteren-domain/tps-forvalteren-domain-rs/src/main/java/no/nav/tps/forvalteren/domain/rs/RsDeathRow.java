package no.nav.tps.forvalteren.domain.rs;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsDeathRow {

    @NotNull
    private Long id;

    @NotBlank
    @Size(min = 11, max = 11)
    private String ident;

    @NotBlank
    @Size(min = 1, max = 50)
    private String handling;

    @NotBlank
    @Size(min = 1, max = 3)
    private String miljoe;

    @Size(min = 1, max = 50)
    private String status;

    @Size(min = 1, max = 50)
    private String tilstand;

    private LocalDateTime doedsdato;

    @NotNull
    private LocalDateTime tidspunkt;
    
    private String bruker;
    
}
