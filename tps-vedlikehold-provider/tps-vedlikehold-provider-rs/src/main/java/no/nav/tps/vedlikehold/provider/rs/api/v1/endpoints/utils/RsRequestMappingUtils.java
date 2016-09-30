package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils;

import java.util.Map;

import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.requests.TpsRequestEndringsmelding;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.GetTpsServiceRutinerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Kenneth Gunnerud (Visma Consulting AS).
 */
@Component
public class RsRequestMappingUtils {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GetTpsServiceRutinerService getTpsServiceRutinerService;

    //@Autowired
    //private GetTpsEndringsmeldingerService getTpsEndringsmeldingerService;

    public <T> T convert(Map<String, Object> params, Class<T> type) {
        return objectMapper.convertValue(params, type);
    }

    public TpsRequestServiceRoutine convertToTpsRequestServiceRoutine(String serviceRutineNavn, JsonNode node) {
        Class<? extends TpsRequestServiceRoutine> requestClass = getTpsServiceRutinerService.execute()
                .stream()
                .filter(request -> request.getName().equalsIgnoreCase(serviceRutineNavn))
                .findFirst().get()
                .getJavaClass();

        return objectMapper.convertValue(node, requestClass);
    }

    public TpsRequestEndringsmelding convertToTpsRequestEndingsmelding(String endringsmeldingNavn, JsonNode node){
        //Class<? extends TpsRequestEndringsmelding> requestClass = getTpsEndringsmeldingerService.
        /*Class<? extends TpsRequestEndringsmelding> requestClass = getTpsServiceRutinerService.execute()
                .stream()
                .filter(request -> request.getName().equalsIgnoreCase(endringsmeldingNavn))
                .findFirst().get()
                .getJavaClass();*/

        //getTpsServiceRutinerServicereturn objectMapper.convertValue(node, requestClass);
        return null;
        /* Det som skjer er at den går igjen alle TpsService/meldinger du har også leter den etter
        * den du har kalt ved navn "para som du sender inn". Navnet setter du i klassen til
        * kommandoen, blir satt i "EndreSprak" klassen for eksempel gjennom builderen dens.
        * Så da finner den den klassen også returnerer den den. Altså finner riktig Javaklasse
        * basert på navnet på Klassen, som man sender inn som para. */
    }
}
