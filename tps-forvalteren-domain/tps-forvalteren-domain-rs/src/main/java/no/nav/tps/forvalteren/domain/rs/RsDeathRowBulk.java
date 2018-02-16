package no.nav.tps.forvalteren.domain.rs;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
public class RsDeathRowBulk {

    private Long id;

    @NotBlank
    @Size(min = 11, max = 11)
    private List<String> identer;

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

    @NotNull
    private LocalDateTime doedsdato;

    private LocalDateTime tidspunkt;

    private String bruker;
}
