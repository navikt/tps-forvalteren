package no.nav.tps.forvalteren.domain.rs;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "adressetype")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RsMidlertidigAdresse.MidlertidigGateAdresse.class, name = "GATE"),
        @JsonSubTypes.Type(value = RsMidlertidigAdresse.MidlertidigStedAdresse.class, name = "STED"),
        @JsonSubTypes.Type(value = RsMidlertidigAdresse.MidlertidigPboxAdresse.class, name = "PBOX"),
        @JsonSubTypes.Type(value = RsMidlertidigAdresse.MidlertidigUtadAdresse.class, name = "UTAD")
})
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
        private String matrikkelId;
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