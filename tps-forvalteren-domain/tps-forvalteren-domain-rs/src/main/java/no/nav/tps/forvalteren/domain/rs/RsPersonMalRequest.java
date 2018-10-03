package no.nav.tps.forvalteren.domain.rs;

import java.util.List;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsPersonMalRequest {

    @NotEmpty
    @Size(min = 1)
    private List<Long> personmalIdListe;

}