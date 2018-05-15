package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import static no.nav.tps.forvalteren.service.command.testdata.utils.TilfeldigTall.tilfeldigTall;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.AdresseNrInfo;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.HentGyldigeAdresserService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.S051.unmarshaller.TpsServiceRutineS051Unmarshaller;
import no.nav.tps.s051.AdresseData;
import no.nav.tps.s051.StatusFraTPS;
import no.nav.tps.s051.TpsAdresseData;

@Service
public class SetRandomAdresseOnPersons {
    
    private TpsServiceRutineS051Unmarshaller unmarshaller;
    private HentGyldigeAdresserService hentGyldigeAdresserService;
    
    @Autowired
    public SetRandomAdresseOnPersons(TpsServiceRutineS051Unmarshaller unmarshaller, HentGyldigeAdresserService hentGyldigeAdresserService) {
        this.unmarshaller = unmarshaller;
        this.hentGyldigeAdresserService = hentGyldigeAdresserService;
    }
    
    public void execute(List<Person> persons, AdresseNrInfo adresseNrInfo) {
        String kommuneNr = null;
        String postNr = null;
        if (adresseNrInfo != null) {
            switch (adresseNrInfo.getNummertype()) {
            case KOMMUNENR:
                kommuneNr = adresseNrInfo.getNummer();
                break;
            case POSTNR:
                postNr = adresseNrInfo.getNummer();
            }
        }
        
        TpsServiceRoutineResponse tpsServiceRoutineResponse = hentGyldigeAdresserService.hentTilfeldigAdresse(persons.size(), kommuneNr, postNr);
        final TpsAdresseData tpsAdresseData = unmarshalTpsAdresseData(tpsServiceRoutineResponse);
        throwExceptionUnlessFlereAdresserFinnes(tpsAdresseData.getTpsSvar().getSvarStatus());
      
        List<AdresseData> adresseDataList = tpsAdresseData.getTpsSvar().getAdresseDataS051().getAdrData();
        
        for (int i = 0; i < persons.size(); i++) {
            Gateadresse adresse = createGateAdresse(adresseDataList.get(i % adresseDataList.size()), persons.get(i));
            persons.get(i).setBoadresse(adresse);
        }
    }
    
    private void throwExceptionUnlessFlereAdresserFinnes(StatusFraTPS svarStatus) {
        if (!"00".equals(svarStatus.getReturStatus()) && !Arrays.asList("S051002I","S051003I").contains(svarStatus.getReturMelding())) {
            throw new TpsfFunctionalException(svarStatus.getUtfyllendeMelding());
        }
    }
    
    private TpsAdresseData unmarshalTpsAdresseData(TpsServiceRoutineResponse tpsServiceRoutineResponse) {
        try {
            TpsAdresseData tpsAdresseData = unmarshaller.unmarshal(tpsServiceRoutineResponse.getXml());
            return tpsAdresseData;
        } catch (JAXBException e) {
            throw new TpsfFunctionalException(e);
        }
    }
    
    private Gateadresse createGateAdresse(AdresseData adresseData, Person person) {
        Gateadresse adresse = new Gateadresse();
        adresse.setHusnummer(tilfeldigTall( adresseData.getHusnrfra(),adresseData.getHusnrtil()));
        adresse.setGatekode(adresseData.getGkode());
        adresse.setAdresse(adresseData.getAdrnavn());
        adresse.setPostnr(adresseData.getPnr());
        adresse.setKommunenr(adresseData.getKnr());
        
        adresse.setFlyttedato(LocalDateTime.now());
        adresse.setPerson(person);
        return adresse;
    }
    
}
