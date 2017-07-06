package no.nav.tps.forvalteren.domain.rs;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("Matrikkeladresse")
public class RsMatrikkeladresse extends RsAdresse {

    @Size(min = 1, max = 25)
    private String mellomnavn;

    @Size(min = 1, max = 5)
    private String gardsnr;

    @Size(min = 1, max = 5)
    private String bruksnr;

    @Size(min = 1, max = 4)
    private String festenr;

    @Size(min = 1, max = 3)
    private String undernr;

}
