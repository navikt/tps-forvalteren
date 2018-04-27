package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import static no.nav.tps.forvalteren.service.command.testdata.utils.TilfeldigTall.tilfeldigTall;

import java.time.LocalDateTime;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.AdresseNrInfo;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.exceptions.TpsTimeoutException;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.HentGyldigeAdresserService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.S051.AdresseData;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.S051.TpsAdresseData;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.S051.unmarshaller.TpsServiceRutineS051Unmarshaller;

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
            case kommuneNr:
                kommuneNr = adresseNrInfo.getNummer();
                break;
            case postNr:
                postNr = adresseNrInfo.getNummer();
            }
        }
        
        TpsServiceRoutineResponse tpsServiceRoutineResponse= hentGyldigeAdresserService.hentTilfeldigAdresse(persons.size(), kommuneNr, postNr);
        List<AdresseData> adresseDataList= unmarshalTpsAdresseData(tpsServiceRoutineResponse).getTpsSvar().getAdresseDataS051().getAdrData();
        
        for (int i = 0; i < persons.size(); i++) {
            Gateadresse adresse = createGateAdresse(adresseDataList.get(i), persons.get(i));
            persons.get(i).setBoadresse(adresse);
        }
    }
    
    private TpsAdresseData unmarshalTpsAdresseData(TpsServiceRoutineResponse tpsServiceRoutineResponse) {
        try {
            TpsAdresseData tpsAdresseData= unmarshaller.unmarshal(tpsServiceRoutineResponse.getXml());
            return tpsAdresseData;
        } catch (JAXBException e) {
            throw new TpsfFunctionalException(e);
        }
    }
    
    private Gateadresse createGateAdresse(AdresseData adresseData, Person person) {
        Gateadresse adresse = new Gateadresse();
        adresse.setHusnummer(tilfeldigTall(adresseData.getHusnrtil(),adresseData.getHusnrfra()));
        adresse.setGatekode(adresseData.getGkode());
        adresse.setAdresse(adresseData.getAdrnavn());
        adresse.setPostnr(adresseData.getPnr());
        adresse.setKommunenr(adresseData.getKnr());
    
        adresse.setFlyttedato(LocalDateTime.now());
        adresse.setPerson(person);
        return adresse;
    }
    
    
}
