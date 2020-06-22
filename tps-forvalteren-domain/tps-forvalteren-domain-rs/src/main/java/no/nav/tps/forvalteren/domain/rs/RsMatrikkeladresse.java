package no.nav.tps.forvalteren.domain.rs;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("Matrikkeladresse")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
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

    @Override
    @JsonIgnore
    public boolean isValidAdresse() {
        return isNotBlank(getBruksnr());
    }
}