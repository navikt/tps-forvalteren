package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import no.nav.tps.forvalteren.domain.service.appinfo.ApplicationInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1")
public class AppInfoController {

    @Value("${environment.name}")
    private String environmentName;

    @Value("${node.hostname}")
    private String hostname;

    @Value("${application.version}")
    private String appVersion;

    @RequestMapping(value = "/appinfo", method = RequestMethod.GET)
    public ApplicationInfo getInfo() {
        ApplicationInfo applicationInfo = new ApplicationInfo();
        applicationInfo.setApplicationVersion(appVersion);
        applicationInfo.setHostName(hostname);
        applicationInfo.setEnvironment(environmentName);

        return applicationInfo;
    }
}
