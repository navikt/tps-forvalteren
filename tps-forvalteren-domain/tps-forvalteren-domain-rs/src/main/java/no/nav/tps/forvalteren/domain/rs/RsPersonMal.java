package no.nav.tps.forvalteren.domain.rs;

import javax.validation.constraints.Size;

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
public class RsPersonMal {

    @Size(min=1, max = 8)
    private String fodtetter;

    @Size(min=1, max = 8)
    private String fodtfor;

    @Size(min=1, max = 8)
    private String kjonn;

    @Size(min=1, max = 8)
    private String statsborgerskap;

    @Size(min=1, max = 8)
    private String spesreg;

    @Size(min=1, max = 8)
    private String spesregdato;

    @Size(max = 8)
    private String doedsdato;

    @Size(min=1, max = 1)
    private String sivilstand;

    @Size(max = 3)
    private String innvandretfraland;

    @Size(max = 2)
    private String minantallbarn;

    @Size(max = 2)
    private String maxantallbarn;

    @Size(max = 50)
    private String gateadresse;

    @Size(max = 4)
    private String gatehusnr;

    @Size(max = 4)
    private String gatepostnr;

    @Size(max = 4)
    private String gatekommunenr;

    @Size(max = 8)
    private String gateflyttedatofra;

    @Size(max = 8)
    private String gateflyttedatotil;

    @Size(max = 30)
    private String postlinje1;

    @Size(max = 30)
    private String postlinje2;

    @Size(max = 30)
    private String postlinje3;

    @Size(max = 3)
    private String postland;

    @Size(max = 5)
    private String postgardnr;

    @Size(max = 4)
    private String postbruksnr;

    @Size(max = 4)
    private String postfestenr;

    @Size(max = 3)
    private String postundernr;

    @Size(max = 4)
    private String postpostnr;

    @Size(max = 4)
    private String postkommunenr;

    @Size(max = 8)
    private String postflyttedatofra;

    @Size(max = 8)
    private String postflyttedatotil;




}
