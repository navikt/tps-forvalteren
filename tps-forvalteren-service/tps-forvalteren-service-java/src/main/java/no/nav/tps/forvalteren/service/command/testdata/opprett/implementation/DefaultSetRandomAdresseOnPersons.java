package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import static no.nav.tps.forvalteren.service.command.testdata.utils.TilfeldigTall.tilfeldigTall;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import javax.xml.bind.JAXBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetRandomAdresseOnPersons;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.HentGyldigeAdresserService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.S051.AdresseData;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.S051.TpsAdresseData;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.S051.unmarshaller.TpsServiceRutineS051Unmarshaller;

@Service
public class DefaultSetRandomAdresseOnPersons implements SetRandomAdresseOnPersons {
    
    private TpsServiceRutineS051Unmarshaller unmarshaller;
    private HentGyldigeAdresserService hentGyldigeAdresserService;
    
    @Autowired
    public DefaultSetRandomAdresseOnPersons(TpsServiceRutineS051Unmarshaller unmarshaller, HentGyldigeAdresserService hentGyldigeAdresserService) {
        this.unmarshaller = unmarshaller;
        this.hentGyldigeAdresserService = hentGyldigeAdresserService;
    }
    
    @Override
    public void execute(List<Person> persons) {
        TpsServiceRoutineResponse tpsServiceRoutineResponse= hentGyldigeAdresserService.hentTilfeldigAdresse(persons.size(), null, null);
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
            throw new RuntimeException(e);
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
