package no.nav.tps.forvalteren.domain.service.appinfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationInfo {

    private String environment;
    private String applicationVersion;
    private String hostName;

}
