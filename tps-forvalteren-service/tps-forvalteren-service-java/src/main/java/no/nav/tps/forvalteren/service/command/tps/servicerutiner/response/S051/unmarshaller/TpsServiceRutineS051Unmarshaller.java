package no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.S051.unmarshaller;

import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.springframework.stereotype.Component;

import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.s051.TpsAdresseData;

@Component
public class TpsServiceRutineS051Unmarshaller {
    Unmarshaller unmarshaller;
    public TpsServiceRutineS051Unmarshaller() {
        try {
            JAXBContext context = JAXBContext.newInstance(TpsAdresseData.class);
            this.unmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
            throw new TpsfTechnicalException("Feil oppstod under konstruksjon av unmarshaller for TpsAdresseData",e);
        }
    }
    
    public TpsAdresseData unmarshal(String serviceRutineResponseXml) throws JAXBException {
        return ((TpsAdresseData) unmarshaller.unmarshal(new StringReader(serviceRutineResponseXml)));
    }
}
