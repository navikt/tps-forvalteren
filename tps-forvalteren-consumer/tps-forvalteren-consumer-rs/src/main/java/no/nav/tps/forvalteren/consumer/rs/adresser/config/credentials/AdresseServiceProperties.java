package no.nav.tps.forvalteren.consumer.rs.adresser.config.credentials;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.nav.tps.forvalteren.consumer.rs.config.credentials.Scopeable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdresseServiceProperties implements Scopeable {
    private String url;
    private String cluster;
    private String name;
    private String namespace;

    @Override
    public String toScope() {
        return "api://" + cluster + "." +  namespace + "." + name + "/.default";
    }
}