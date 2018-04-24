package no.nav.tps.forvalteren.domain.rs;

import java.util.List;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsPersonMalRequest {

    @NotEmpty
    @Size(min = 1)
    private List<RsPersonMal> inputPersonMalRequest;

}
