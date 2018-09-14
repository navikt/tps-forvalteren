package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S004HentPersonopplysninger.PERSON_OPPLYSNINGER_SERVICE_ROUTINE;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S018PersonHistorikk.PERSON_HISTORY_SERVICE_ROUTINE;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.unmarshaller.TpsServiceRutineS004Unmarshaller;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.unmarshaller.TpsServiceRutineS018Unmarshaller;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertStringToDate;
import no.nav.tps.xjc.ctg.domain.s018.BoAdresseType;
import no.nav.tps.xjc.ctg.domain.s018.TpsPersonData;

@Service
public class AdresseService {

    private static final String PERSON_IKKE_DOED = "Personen med ident %s er ikke død i miljoe %s.";
    private static final String FEILET_HENTING_DOEDSDATO = "Feilet henting av doedsdato for ident %s i miljoe %s. Feilmelding: %s";
    private static final String FEILET_HENTING_BOADRESSE = "Feilet henting av boadresse for ident %s i miljoe %s. Feilmelding: %s";

    @Autowired
    private TpsServiceRoutineService tpsServiceRoutineService;

    @Autowired
    private TpsServiceRutineS004Unmarshaller tpsServiceRutineS004Unmarshaller;

    @Autowired
    private TpsServiceRutineS018Unmarshaller tpsServiceRutineS018Unmarshaller;

    public Adresse hentAdresseFoerDoed(String ident, String env) {

        LocalDateTime doedsDato = hentDoedsdato(ident, env);
        if (doedsDato == null) {
            throw new TpsfTechnicalException(String.format(PERSON_IKKE_DOED, ident, env));
        }

        BoAdresseType boAdresse = hentPersondata(ident, doedsDato.minusDays(1), env);
        if (boAdresse == null) {
            return null;
        }

        Adresse adresse = null;

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

    private BoAdresseType hentPersondata(String ident, LocalDateTime date, String env) {

        Map requestParams = prepRequest(ident, env);
        requestParams.put("aksjonsKode", "A0");
        requestParams.put("aksjonsDato", ConvertDateToString.yyyysMMsdd(date));

        TpsServiceRoutineResponse response = tpsServiceRoutineService.execute(PERSON_HISTORY_SERVICE_ROUTINE, requestParams, true);
        try {
            TpsPersonData personData = tpsServiceRutineS018Unmarshaller.unmarshal(response.getXml());
            if (personData.getTpsSvar() != null && !"00".equals(personData.getTpsSvar().getSvarStatus().getReturStatus())) {
                throw new TpsfTechnicalException(String.format(FEILET_HENTING_BOADRESSE, ident, env, personData.getTpsSvar().getSvarStatus().getReturMelding() +
                        personData.getTpsSvar().getSvarStatus().getUtfyllendeMelding()));
            }
            return personData.getTpsSvar() != null && !personData.getTpsSvar().getPersonDataS018().getBostedsAdresse().isEmpty() ?
                    personData.getTpsSvar().getPersonDataS018().getBostedsAdresse().get(0) :
                    null;
        } catch (JAXBException e) {
            throw new TpsfTechnicalException(String.format(FEILET_HENTING_BOADRESSE, ident, env, e.getMessage()), e);
        }
    }

    private LocalDateTime hentDoedsdato(String ident, String env) {

        Map requestParams = prepRequest(ident, env);
        requestParams.put("aksjonsKode", "F0");

        TpsServiceRoutineResponse response = tpsServiceRoutineService.execute(PERSON_OPPLYSNINGER_SERVICE_ROUTINE, requestParams, true);
        try {
            no.nav.tps.xjc.ctg.domain.s004.TpsPersonData personData = tpsServiceRutineS004Unmarshaller.unmarshal(response.getXml());
            if (personData.getTpsSvar() != null && !"00".equals(personData.getTpsSvar().getSvarStatus().getReturStatus())) {
                throw new TpsfTechnicalException(String.format(FEILET_HENTING_DOEDSDATO, ident, env, personData.getTpsSvar().getSvarStatus().getReturMelding() +
                        personData.getTpsSvar().getSvarStatus().getUtfyllendeMelding()));
            }
            return personData.getTpsSvar() != null && StringUtils.isNotBlank(personData.getTpsSvar().getPersonDataS004().getDatoDo()) ?
                    ConvertStringToDate.yyyysMMsdd(personData.getTpsSvar().getPersonDataS004().getDatoDo()) :
                    null;
        } catch (JAXBException e) {
            throw new TpsfTechnicalException(String.format(FEILET_HENTING_DOEDSDATO, ident, env, e.getMessage()), e);
        }
    }

    private Map prepRequest(String ident, String env) {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("fnr", ident);
        requestParams.put("environment", env);
        return requestParams;
    }
}
