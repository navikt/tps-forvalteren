package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.service.command.testdata.utils.TilfeldigTall.tilfeldigTall;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.AdresseNrInfo;
import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsFinnGyldigeAdresserResponse;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.HentGyldigeAdresserService;

@Slf4j
@Service
public class RandomAdresseService {

    private HentGyldigeAdresserService hentGyldigeAdresserService;

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    @Autowired
    private DummyAdresseService dummyAdresseService;

    @Autowired
    public RandomAdresseService(HentGyldigeAdresserService hentGyldigeAdresserService) {
        this.hentGyldigeAdresserService = hentGyldigeAdresserService;
    }

    public List<Person> execute(List<Person> persons, AdresseNrInfo adresseNrInfo) {

        List<Adresse> adresser = hentRandomAdresse(persons.size(), adresseNrInfo);

        for (int i = 0; i < persons.size(); i++) {
            adresser.get(i).setFlyttedato(hentDatoFraIdentService.extract(persons.get(i).getIdent()));
            adresser.get(i).setPerson(persons.get(i));
            persons.get(i).setBoadresse(adresser.get(i));
        }

        return persons;
    }

    public List<Adresse> hentRandomAdresse(int total, AdresseNrInfo adresseNrInfo) {
        String kommuneNr = null;
        String postNr = null;

        if (nonNull(adresseNrInfo)) {
            switch (adresseNrInfo.getNummertype()) {
            case POSTNR:
                postNr = adresseNrInfo.getNummer();
                break;
            case KOMMUNENR:
            default:
                kommuneNr = adresseNrInfo.getNummer();
            }
        }

        try {
            TpsFinnGyldigeAdresserResponse addrResponse = hentGyldigeAdresserService.hentTilfeldigAdresse(total, kommuneNr, postNr);
            throwExceptionUnlessFlereAdresserFinnes(addrResponse.getResponse().getStatus());

            List<TpsFinnGyldigeAdresserResponse.Adressedata> adresseDataList = addrResponse.getResponse().getData1().getAdrData();

            List<Adresse> adresser = new ArrayList(adresseDataList.size());
            for (int i = 0; i < total; i++) {
                adresser.add(createGateAdresse(adresseDataList.get(i % adresseDataList.size())));
            }
            return adresser;

        } catch (RuntimeException e) {
            log.error("Adresseoppslag med kommunenr {} alt postnr {} feilet: {}", kommuneNr, postNr, e.getMessage());
            return singletonList(dummyAdresseService.createDummyBoAdresse(null));
        }
    }

    public List<Person> execute(List<Person> persons) {

        return execute(persons, null);
    }

    private void throwExceptionUnlessFlereAdresserFinnes(ResponseStatus svarStatus) {
        if (!"00".equals(svarStatus.getKode()) && !newArrayList("S051002I", "S051003I").contains(svarStatus.getMelding())) {
            throw new TpsfFunctionalException(svarStatus.getUtfyllendeMelding());
        }
    }

    private Gateadresse createGateAdresse(TpsFinnGyldigeAdresserResponse.Adressedata adresseData) {
        Gateadresse adresse = new Gateadresse();
        adresse.setHusnummer(tilfeldigTall(adresseData.getHusnrfra(), adresseData.getHusnrtil()));
        adresse.setGatekode(adresseData.getGkode());
        adresse.setAdresse(adresseData.getAdrnavn());
        adresse.setPostnr(adresseData.getPnr());
        adresse.setKommunenr(adresseData.getKnr());
        return adresse;
    }
}
