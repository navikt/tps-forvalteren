package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.service.command.testdata.utils.TilfeldigTall.tilfeldigTall;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.AdresseNrInfo;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.HentGyldigeAdresserService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.unmarshaller.TpsServiceRutineS051Unmarshaller;
import no.nav.tps.xjc.ctg.domain.s051.AdresseData;
import no.nav.tps.xjc.ctg.domain.s051.StatusFraTPS;
import no.nav.tps.xjc.ctg.domain.s051.TpsAdresseData;

@Slf4j
@Service
public class RandomAdresseService {

    private TpsServiceRutineS051Unmarshaller unmarshaller;
    private HentGyldigeAdresserService hentGyldigeAdresserService;

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    @Autowired
    private DummyAdresseService dummyAdresseService;

    @Autowired
    public RandomAdresseService(TpsServiceRutineS051Unmarshaller unmarshaller, HentGyldigeAdresserService hentGyldigeAdresserService) {
        this.unmarshaller = unmarshaller;
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
            TpsServiceRoutineResponse tpsServiceRoutineResponse = hentGyldigeAdresserService.hentTilfeldigAdresse(total, kommuneNr, postNr);
            TpsAdresseData tpsAdresseData = unmarshalTpsAdresseData(tpsServiceRoutineResponse);
            throwExceptionUnlessFlereAdresserFinnes(tpsAdresseData.getTpsSvar().getSvarStatus());

            List<AdresseData> adresseDataList = tpsAdresseData.getTpsSvar().getAdresseDataS051().getAdrData();

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

    private void throwExceptionUnlessFlereAdresserFinnes(StatusFraTPS svarStatus) {
        if (!"00".equals(svarStatus.getReturStatus()) && !newArrayList("S051002I", "S051003I").contains(svarStatus.getReturMelding())) {
            throw new TpsfFunctionalException(svarStatus.getUtfyllendeMelding());
        }
    }

    private TpsAdresseData unmarshalTpsAdresseData(TpsServiceRoutineResponse tpsServiceRoutineResponse) {
        try {
            return unmarshaller.unmarshal(tpsServiceRoutineResponse.getXml());
        } catch (JAXBException e) {
            throw new TpsfFunctionalException(e.getMessage(), e);
        }
    }

    private Gateadresse createGateAdresse(AdresseData adresseData) {
        Gateadresse adresse = new Gateadresse();
        adresse.setHusnummer(tilfeldigTall(adresseData.getHusnrfra(), adresseData.getHusnrtil()));
        adresse.setGatekode(adresseData.getGkode());
        adresse.setAdresse(adresseData.getAdrnavn());
        adresse.setPostnr(adresseData.getPnr());
        adresse.setKommunenr(adresseData.getKnr());
        return adresse;
    }
}
