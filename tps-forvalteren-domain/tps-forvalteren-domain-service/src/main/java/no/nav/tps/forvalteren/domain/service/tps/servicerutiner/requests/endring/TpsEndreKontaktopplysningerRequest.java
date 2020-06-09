package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineEndringRequest;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JacksonXmlRootElement(localName = "endreKontaktopplysninger")
public class TpsEndreKontaktopplysningerRequest extends TpsServiceRoutineEndringRequest {

    private Spraak endringAvSprak;
    private Kontonummer endringAvKontonr;
    private NavAdresse endringAvNAVadresse;
    private Telefon endringAvTelefon;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class NavAdresse {

        private TiadAdresse nyAdresseNavNorge;
		private UtadAdresse nyAdresseNavUtland;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class TiadAdresse {

			private String datoTom; //yyyy-mm-dd
			private String typeAdresseNavNorge;
			private String typeTilleggsLinje;
			private String tilleggsLinje;
			private String kommunenr;
			private String gatekode;
			private String gatenavn;
			private String husnr;
			private String husbokstav;
			private String eiendomsnavn;
			private String bolignr;
			private String postboksnr;
			private String postboksAnlegg;
			private String postnr;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class UtadAdresse {

			private String datoTom; //yyyy-mm-dd
			private String adresse1;
			private String adresse2;
			private String adresse3;
			private String kodeLand;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Telefon {

        private List<NyTelefon> nyTelefon;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class NyTelefon {

        private String typeTelefon;
        private String telefonLandkode;
        private String telefonNr;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Kontonummer {

        private NorskKontonummer endreKontonrNorsk;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class NorskKontonummer {

        private String giroNrNorsk;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Spraak {

        private NyttSprak endreSprak;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class NyttSprak {
        private String sprakKode;
        private String datoSprak; //yyyy-mm-dd
    }
}
