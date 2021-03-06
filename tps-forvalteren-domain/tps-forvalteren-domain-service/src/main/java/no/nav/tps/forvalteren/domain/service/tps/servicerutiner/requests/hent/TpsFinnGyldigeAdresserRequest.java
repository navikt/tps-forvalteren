package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineHentRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.JaEllerNei;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.Sorteringskategorier;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.Typesok;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JacksonXmlRootElement(localName = "tpsServiceRutine")
public class TpsFinnGyldigeAdresserRequest extends TpsServiceRoutineHentRequest {
    private String adresseNavnsok;
    private Typesok typesok;
    private String husNrsok;
    private String kommuneNrsok;
    private String postNrsok;
    private Integer maxRetur;
    private JaEllerNei alltidRetur;
    private JaEllerNei alleSkrivevarianter;
    private JaEllerNei visPostnr;
    private Sorteringskategorier sortering;
}
