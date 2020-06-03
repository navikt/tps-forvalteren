package no.nav.tps.forvalteren.domain.rs;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsMidlertidigAdresse {

    public enum Adressetype {PBOX, GATE, STED, UTAD}

    private LocalDateTime gyldigTom;

    private Adressetype adressetype;

    private AdresseNrInfo gateadresseNrInfo;

    private NorskAdresse norskAdresse;

    private RsPostadresse utenlandskAdresse;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NorskAdresse {

        private RsRequestAdresse.TilleggAdressetype tilleggsadresse;

        private String postnr;

        private String gatenavn;

        private String gatekode;

        private String husnr;

        private String eiendomsnavn;

        private String postboksnr;

        private String postboksAnlegg;
    }
}