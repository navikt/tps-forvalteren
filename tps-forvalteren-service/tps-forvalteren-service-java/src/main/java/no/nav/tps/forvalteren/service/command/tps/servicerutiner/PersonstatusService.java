package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S004HentPersonopplysninger.PERSON_OPPLYSNINGER_SERVICE_ROUTINE;
import static no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.ServiceroutineEnum.AKSJONSKODE;
import static no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.ServiceroutineEnum.ENVIRONMENT;
import static no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.ServiceroutineEnum.FNR;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.xjc.ctg.domain.s004.PersondataFraTpsS004;
import no.nav.tps.xjc.ctg.domain.s004.TpsPersonData;

@Service
public class PersonstatusService {

    private static final String FEILET_HENTING_PERSONSTATUS = "Feilet henting av personstatus (s004) for ident %s i miljoe %s. Feilmelding: %s";

    @Autowired
    private TpsServiceRoutineService tpsServiceRoutineService;

    public PersondataFraTpsS004 hentPersonstatus(String ident, String env) {

        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put(FNR.getName(), ident);
        requestParams.put(ENVIRONMENT.getName(), env);
        requestParams.put(AKSJONSKODE.getName(), "F0");

        TpsServiceRoutineResponse response = tpsServiceRoutineService.execute(PERSON_OPPLYSNINGER_SERVICE_ROUTINE, requestParams, true);
        try {
            JAXBContext context = JAXBContext.newInstance(TpsPersonData.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            TpsPersonData personData = ((TpsPersonData) unmarshaller.unmarshal(new StringReader(response.getXml())));

            if (personData.getTpsSvar() != null && !"00".equals(personData.getTpsSvar().getSvarStatus().getReturStatus())) {
                throw new TpsfTechnicalException(String.format(FEILET_HENTING_PERSONSTATUS, ident, env, personData.getTpsSvar().getSvarStatus().getReturMelding() +
                        personData.getTpsSvar().getSvarStatus().getUtfyllendeMelding()));
            }
            return personData.getTpsSvar() != null ? personData.getTpsSvar().getPersonDataS004() : null;

        } catch (JAXBException e) {
            throw new TpsfTechnicalException(String.format(FEILET_HENTING_PERSONSTATUS, ident, env, e.getMessage()), e);
        }
    }
}
