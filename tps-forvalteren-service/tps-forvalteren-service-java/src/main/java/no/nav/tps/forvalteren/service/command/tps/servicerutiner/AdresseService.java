package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import java.time.LocalDateTime;
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

    public Adresse hentAdresseForDato(String ident, LocalDateTime aksjonsdato, String env) {

        S018PersonType s018PersonType = personhistorikkService.hentPersonhistorikk(ident, aksjonsdato, env);
        if (s018PersonType.getBostedsAdresse().isEmpty()) {
            return null;
        }

        BoAdresseType boAdresse = s018PersonType.getBostedsAdresse().get(0);
        Adresse adresse;

        if ("OFFA".equals(boAdresse.getAdresseType())) {
            adresse = Gateadresse.builder()
                    .adresse(boAdresse.getOffAdresse().getGateNavn())
                    .husnummer(boAdresse.getOffAdresse().getBokstav())
                    .gatekode(boAdresse.getOffAdresse().getGatekode())
                    .build();

        } else { //MATR
            adresse = Matrikkeladresse.builder()
                    .mellomnavn(boAdresse.getMatrAdresse().getMellomAdresse())
                    .bruksnr(boAdresse.getMatrAdresse().getBruksnr())
                    .gardsnr(boAdresse.getMatrAdresse().getGardsnr())
                    .festenr(boAdresse.getMatrAdresse().getFestenr())
                    .undernr(boAdresse.getMatrAdresse().getUndernr())
                    .build();
        }
        adresse.setKommunenr(boAdresse.getKommunenr());
        adresse.setPostnr(boAdresse.getPostnr());
        adresse.setFlyttedato(ConvertStringToDate.yyyysMMsdd(boAdresse.getDatoFom()));

        return adresse;
    }
}
