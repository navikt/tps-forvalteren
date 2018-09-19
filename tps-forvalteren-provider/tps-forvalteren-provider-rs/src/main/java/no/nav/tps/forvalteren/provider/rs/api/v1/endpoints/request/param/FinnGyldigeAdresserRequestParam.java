package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.request.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.JaEllerNei;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.Sorteringskategorier;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.Typesok;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinnGyldigeAdresserRequestParam {
    
    private String adresseNavnsok;
    @ApiModelProperty(value = "Tjenesten kan returnere fonetiske likheter(F), eksakte likheter(E) eller tilfeldige likheter(T) med søket.")
    private Typesok typesok;
    private String husNrsok;
    private String kommuneNrsok;
    private String postNrsok;
    private Integer maxRetur;
    private JaEllerNei alleSkrivevarianter;
    @ApiModelProperty(value = "Ønsker du at postnummer og poststed returneres? J = Ja, N = Nei. Svarer du Nei, får du betydelig raskere responstid.")
    private JaEllerNei visPostnr;
    @ApiModelProperty(value = "I responsen sorteres gyldige adresser:\n"
            + " K - Ønskes sortert på kommunenr og deretter adressenavn,\n"
            + " P - Ønskes sortert på postnr og deretter adressenavn,\n"
            + " N - Ønskes sortert kun på Adressenavn,\n"
            + " Blank (ingenting er oppgitt) = N.")
    private Sorteringskategorier sortering;
}
