package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import no.nav.tps.forvalteren.domain.service.appinfo.ApplicationInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class AppInfoControllerTest {

    @InjectMocks
    private AppInfoController appInfoController;

    @Test
    public void happyPath(){
        ReflectionTestUtils.setField(appInfoController, "environmentName", "testEnv");
        ReflectionTestUtils.setField(appInfoController, "hostname", "testHost");
        ReflectionTestUtils.setField(appInfoController, "appVersion", "testAppV");

        ApplicationInfo applicationInfo = appInfoController.getInfo();

        assertEquals(applicationInfo.getEnvironment(), "testEnv");
        assertEquals(applicationInfo.getHostName(), "testHost");
        assertEquals(applicationInfo.getApplicationVersion(), "testAppV");
    }

}