package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import java.time.LocalDateTime;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertStringToDate;
import no.nav.tps.xjc.ctg.domain.s018.BoAdresseType;
import no.nav.tps.xjc.ctg.domain.s018.S018PersonType;

@Service
public class AdresseService {

    @Autowired
    private PersonhistorikkService personhistorikkService;

    public Adresse hentBoadresseForDato(String ident, LocalDateTime bodato, String miljoe) {

        S018PersonType s018PersonType = personhistorikkService.hentPersonhistorikk(ident, bodato, miljoe);
        if (s018PersonType.getBostedsAdresse().isEmpty()) {
            return null;
        }

        Adresse adresse = null;
        for (BoAdresseType boadresse : s018PersonType.getBostedsAdresse()) {

            if (isAddressDate(boadresse, bodato)) {

                if ("OFFA".equals(boadresse.getAdresseType())) {
                    adresse = Gateadresse.builder()
                            .adresse(boadresse.getOffAdresse().getGateNavn())
                            .husnummer(boadresse.getOffAdresse().getBokstav())
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

                break;
            }
        }
        return adresse;
    }

    private boolean isAddressDate(BoAdresseType boadresse, LocalDateTime bodato) {

        return bodato.compareTo(ConvertStringToDate.yyyysMMsdd(boadresse.getDatoFom())) >= 0 &&
                (StringUtils.isBlank(boadresse.getDatoTom()) ||
                        bodato.compareTo(ConvertStringToDate.yyyysMMsdd(boadresse.getDatoTom())) <= 0);
    }
}
