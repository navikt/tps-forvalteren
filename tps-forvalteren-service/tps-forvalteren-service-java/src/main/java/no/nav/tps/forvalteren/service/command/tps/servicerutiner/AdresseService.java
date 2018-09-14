package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertStringToDate;
import no.nav.tps.xjc.ctg.domain.s004.PersondataFraTpsS004;
import no.nav.tps.xjc.ctg.domain.s018.BoAdresseType;
import no.nav.tps.xjc.ctg.domain.s018.S018PersonType;

@Service
public class AdresseService {

    private static final String PERSON_IKKE_DOED = "Personen med ident %s er ikke død i miljoe %s.";

    @Autowired
    private PersonstatusService personstatusService;

    @Autowired
    private PersonhistorikkService personhistorikkService;

    public Adresse hentAdresseFoerDoed(String ident, String env) {

        PersondataFraTpsS004 persondataFraTpsS004 = personstatusService.hentPersonstatus(ident, env);
        if (StringUtils.isBlank(persondataFraTpsS004.getDatoDo())) {
            throw new TpsfTechnicalException(String.format(PERSON_IKKE_DOED, ident, env));
        }

        S018PersonType s018PersonType = personhistorikkService.hentPersonhistorikk(ident,
                ConvertStringToDate.yyyysMMsdd(persondataFraTpsS004.getDatoDo()).minusDays(1), env);
        if (!s018PersonType.getBostedsAdresse().isEmpty()) {
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
