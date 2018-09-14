package no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.unmarshaller;

import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.springframework.stereotype.Component;

import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.xjc.ctg.domain.s004.TpsPersonData;

@Component
public class TpsServiceRutineS004Unmarshaller {

    Unmarshaller unmarshaller;

    public TpsServiceRutineS004Unmarshaller() {
        try {
            JAXBContext context = JAXBContext.newInstance(TpsPersonData.class);
            this.unmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
            throw new TpsfTechnicalException("Feil oppstod under konstruksjon av unmarshaller for TpsPersonData", e);
        }
    }
    
    public TpsPersonData unmarshal(String serviceRutineResponseXml) throws JAXBException {
        return ((TpsPersonData) unmarshaller.unmarshal(new StringReader(serviceRutineResponseXml)));
    }
}