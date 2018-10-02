package no.nav.tps.forvalteren.service.command.dodsmeldinger;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.skd.RsTpsDoedsmeldingRequest;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.response.lagreTilTps.SendSkdMeldingTilTpsResponse;
import no.nav.tps.xjc.ctg.domain.s004.PersondataFraTpsS004;

@Service
public class SendTpsDoedsmeldingService extends SendDodsmeldingTilTpsService {

    public SendSkdMeldingTilTpsResponse sendDoedsmelding(RsTpsDoedsmeldingRequest request) {
        validate(request);

        Person person = new Person();
        person.setIdent(request.getIdent());
        person.setRegdato(LocalDateTime.now());

        PersondataFraTpsS004 persondataFraTps = hentPersonstatus(request.getIdent(), request.getMiljoe());

        Map<String, String> sentStatus = new HashMap<>();
        sentStatus.putAll(sendAnnulering(person, persondataFraTps.getDatoDo(), request.getHandling().name(), request.getMiljoe()));
        sentStatus.putAll(sendDoedsmelding(person, persondataFraTps.getDatoDo(), request.getDoedsdato(), request.getHandling().name(), request.getMiljoe()));

        return prepareStatus(sentStatus, person.getIdent());
    }

    private void validate(RsTpsDoedsmeldingRequest request) {
        if (!request.validatesOk()) {
            throw new TpsfFunctionalException("Påkrevet parameter mangler.");
        }
    }

    private SendSkdMeldingTilTpsResponse prepareStatus(Map<String, String> sentStatus, String ident) {
        sentStatus.replaceAll((env, status) -> status.matches("^00.*") ? "OK" : status);
        return SendSkdMeldingTilTpsResponse.builder()
                .personId(ident)
                .skdmeldingstype("Doedsmelding")
                .status(sentStatus)
                .build();
    }
}