package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import java.time.LocalDateTime;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.service.command.foedselsmelding.AdresserResponse;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertStringToDate;
import no.nav.tps.xjc.ctg.domain.s018.BoAdresseType;
import no.nav.tps.xjc.ctg.domain.s018.PostAdresseType;
import no.nav.tps.xjc.ctg.domain.s018.S018PersonType;

@Service
public class PersonAdresseService {

    @Autowired
    private PersonhistorikkService personhistorikkService;

    public AdresserResponse hentAdresserForDato(String ident, LocalDateTime bodato, String miljoe) {

        S018PersonType s018PersonType = personhistorikkService.hentPersonhistorikk(ident, bodato, miljoe);
        if (s018PersonType.getBostedsAdresse().isEmpty()) {
            return null;
        }

        return hentAdresserForDato(s018PersonType, bodato);
    }

    public AdresserResponse hentAdresserForDato(S018PersonType personType, LocalDateTime bodato) {

        return AdresserResponse.builder()
                .boadresse(getBoAdresse(personType.getBostedsAdresse(), bodato))
                .postadresse(getPostAdresse(personType.getPostAdresse(), bodato))
                .build();
    }

    private Adresse getBoAdresse(List<BoAdresseType> boadresser, LocalDateTime bodato) {

        Adresse adresse = null;
        for (BoAdresseType boadresse : boadresser) {

            if (isAddressDate(boadresse, bodato)) {

                if ("OFFA".equals(boadresse.getAdresseType())) {
                    adresse = Gateadresse.builder()
                            .adresse(boadresse.getOffAdresse().getGateNavn())
                            .husnummer(boadresse.getOffAdresse().getHusnr() +
                                    (StringUtils.isNotBlank(boadresse.getOffAdresse().getBokstav()) ?
                                            boadresse.getOffAdresse().getBokstav() : ""))
                            .gatekode(boadresse.getOffAdresse().getGatekode())
                            .build();

                } else { //MATR
                    adresse = Matrikkeladresse.builder()
                            .mellomnavn(boadresse.getMatrAdresse().getMellomAdresse())
                            .bruksnr(boadresse.getMatrAdresse().getBruksnr())
                            .gardsnr(boadresse.getMatrAdresse().getGardsnr())
                            .festenr(boadresse.getMatrAdresse().getFestenr())
                            .undernr(boadresse.getMatrAdresse().getUndernr())
                            .build();
                }
                adresse.setKommunenr(boadresse.getKommunenr());
                adresse.setPostnr(boadresse.getPostnr());
                adresse.setFlyttedato(ConvertStringToDate.yyyysMMsdd(boadresse.getDatoFom()));
                adresse.setBolignr(boadresse.getBolignr());
                break;
            }
        }

        return adresse;
    }

    private Postadresse getPostAdresse(List<PostAdresseType> postadresser, LocalDateTime bodato) {

        for (PostAdresseType postadresse : postadresser) {

            if (isAddressDate(postadresse, bodato)) {

                return Postadresse.builder()
                        .postLinje1(postadresse.getAdresse1())
                        .postLinje2(postadresse.getAdresse2())
                        .postLinje3(postadresse.getAdresse3())
                        .postLand(postadresse.getLandKode())
                        .build();
            }
        }
        return null;
    }

    private boolean isAddressDate(BoAdresseType boadresse, LocalDateTime bodato) {

        return bodato.compareTo(ConvertStringToDate.yyyysMMsdd(boadresse.getDatoFom())) >= 0 &&
                (StringUtils.isBlank(boadresse.getDatoTom()) ||
                        bodato.compareTo(ConvertStringToDate.yyyysMMsdd(boadresse.getDatoTom())) <= 0);
    }

    private boolean isAddressDate(PostAdresseType postadresse, LocalDateTime bodato) {

        return bodato.compareTo(ConvertStringToDate.yyyysMMsdd(postadresse.getDatoFom())) >= 0 &&
                (StringUtils.isBlank(postadresse.getDatoTom()) ||
                        bodato.compareTo(ConvertStringToDate.yyyysMMsdd(postadresse.getDatoTom())) <= 0);
    }
}
