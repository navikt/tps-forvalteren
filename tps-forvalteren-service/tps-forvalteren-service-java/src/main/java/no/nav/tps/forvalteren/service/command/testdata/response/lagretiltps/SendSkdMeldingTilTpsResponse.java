package no.nav.tps.forvalteren.service.command.testdata.response.lagretiltps;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Medlemsvariabelen status er (value) lagringsstatus for skdmeldingen per (key) miljø.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendSkdMeldingTilTpsResponse {

    private String personId;
    private String skdmeldingstype;
    private Map<String, String> status; //Map<Environment, TPS respons statusmelding >

    public String getStatus(String environment) {
        return status.get(environment);
    }

}
