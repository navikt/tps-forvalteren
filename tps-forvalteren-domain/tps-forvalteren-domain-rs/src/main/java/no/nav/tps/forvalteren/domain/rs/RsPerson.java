package no.nav.tps.forvalteren.domain.rs;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsPerson {

    private Long personId;

    @NotBlank
    @Size(min = 11, max = 11)
    private String ident;

    @NotBlank
    @Size(min = 3, max = 3)
    private String identtype;

    @NotBlank
    @Size(min = 3, max = 3)
    private String kjonn;

    @NotBlank
    @Size(min = 1, max = 50)
    private String fornavn;

    @Size(min = 1, max = 50)
    private String mellomnavn;

    @NotBlank
    @Size(min = 1, max = 15)
    private String etternavn;

    private String statsborgerskap;

    private LocalDateTime statsborgerskapRegdato;

    @Size(min = 1, max = 1)
    private String spesreg;

    private LocalDateTime spesregDato;

    private LocalDateTime doedsdato;

    private String sivilstand;

    @Size(min = 0, max = 3)
    private String innvandretFraLand;

    private LocalDateTime innvandretFraLandFlyttedato;

    private LocalDateTime innvandretFraLandRegdato;

    @Size(min = 0, max = 3)
    private String utvandretTilLand;

    private LocalDateTime utvandretTilLandFlyttedato;

    private LocalDateTime utvandretTilLandRegdato;

    private RsAdresse boadresse;

    private List<RsPostadresse> postadresse;

    @NotNull
    private LocalDateTime regdato;

    private RsSimpleGruppe gruppe;

    private List<RsSimpleRelasjon> relasjoner;

    private LocalDateTime egenAnsattDatoFom;

    private LocalDateTime egenAnsattDatoTom;

    @Size(min = 4, max = 4)
    private String typeSikkerhetsTiltak;

    private LocalDateTime sikkerhetsTiltakDatoFom;

    private LocalDateTime sikkerhetsTiltakDatoTom;

    @Size(min = 1, max = 50)
    private String beskrSikkerhetsTiltak;

    private LocalDateTime foedselsdato;

    private Integer alder;

    private LocalDateTime opprettetDato;

    private String opprettetAv;

    private String sprakKode;

    private LocalDateTime datoSprak;

    private String tknr;

    private String gtType;

    private String gtVerdi;

    private String gtRegel;
}
