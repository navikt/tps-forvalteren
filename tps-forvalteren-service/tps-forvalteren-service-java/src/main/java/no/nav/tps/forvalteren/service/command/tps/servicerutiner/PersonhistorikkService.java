package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S018PersonHistorikk.PERSON_HISTORY_SERVICE_ROUTINE;
import static no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.ServiceroutineEnum.AKSJONSDATO;
import static no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.ServiceroutineEnum.AKSJONSKODE;
import static no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.ServiceroutineEnum.ENVIRONMENT;
import static no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.ServiceroutineEnum.FNR;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import no.nav.tps.xjc.ctg.domain.s018.S018PersonType;
import no.nav.tps.xjc.ctg.domain.s018.TpsPersonData;

@Service
public class PersonhistorikkService {

    private static final String FEILET_HENTING_PERSONHISTORIKK = "Feilet henting av personhistroikk (s018) for ident %s i miljoe %s. Feilmelding: %s";

    @Autowired
    private TpsServiceRoutineService tpsServiceRoutineService;

    public S018PersonType hentPersonhistorikk(String ident, LocalDateTime date, String env) {

        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put(FNR.getName(), ident);
        requestParams.put(ENVIRONMENT.getName(), env);
        requestParams.put(AKSJONSKODE.getName(), "A0");
        requestParams.put(AKSJONSDATO.getName(), ConvertDateToString.yyyysMMsdd(date));

        TpsServiceRoutineResponse response = tpsServiceRoutineService.execute(PERSON_HISTORY_SERVICE_ROUTINE, requestParams, true);
        try {
            JAXBContext context = JAXBContext.newInstance(TpsPersonData.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            TpsPersonData personData = (TpsPersonData) unmarshaller.unmarshal(new StringReader(response.getXml()));

            if (nonNull(personData.getTpsSvar()) && !"00".equals(personData.getTpsSvar().getSvarStatus().getReturStatus())) {
                throw new TpsfTechnicalException(String.format(FEILET_HENTING_PERSONHISTORIKK, ident, env, personData.getTpsSvar().getSvarStatus().getReturMelding() +
                        personData.getTpsSvar().getSvarStatus().getUtfyllendeMelding()));
            }
            return nonNull(personData.getTpsSvar()) ? personData.getTpsSvar().getPersonDataS018() : null;

        } catch (JAXBException e) {
            throw new TpsfTechnicalException(String.format(FEILET_HENTING_PERSONHISTORIKK, ident, env, e.getMessage()), e);
        }
    }
}
