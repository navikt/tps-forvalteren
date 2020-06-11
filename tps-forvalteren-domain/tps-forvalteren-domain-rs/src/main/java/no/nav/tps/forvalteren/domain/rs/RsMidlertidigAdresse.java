package no.nav.tps.forvalteren.domain.rs;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class RsMidlertidigAdresse {

    private Long id;
    private LocalDateTime gyldigTom;
    private MidlertidigAdressetype adressetype;
    private String tilleggsadresse;
    private String postnr;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MidlertidigGateAdresse extends RsMidlertidigAdresse {

        private String gatenavn;
        private String gatekode;
        private String husnr;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MidlertidigStedAdresse extends RsMidlertidigAdresse {

        private String eiendomsnavn;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MidlertidigPboxAdresse extends RsMidlertidigAdresse {

        private String postboksnr;
        private String postboksAnlegg;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MidlertidigUtadAdresse extends RsMidlertidigAdresse {

        private String postLinje1;
        private String postLinje2;
        private String postLinje3;
        private String postLand;
    }
}