package no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.requests;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsRequest;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by f148888 on 29.09.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TpsRequestAjourforing extends TpsRequest {
    private String xmlEndringsMeldingTag;
}
