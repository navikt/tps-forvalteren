package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import no.nav.tps.forvalteren.common.logging.LogExceptions;
import no.nav.tps.forvalteren.domain.service.appinfo.ApplicationInfo;

@RestController
@RequestMapping(value = "api/v1")
public class AppInfoController {

    @Value("${tpsf.environment.name}")
    private String environmentName;

    @Value("${application.version}")
    private String appVersion;

    @LogExceptions
    @RequestMapping(value = "/appinfo", method = RequestMethod.GET)
    public ApplicationInfo getInfo() {
        ApplicationInfo applicationInfo = new ApplicationInfo();
        applicationInfo.setApplicationVersion(appVersion);
        applicationInfo.setEnvironment(environmentName);

        return applicationInfo;
    }
}
