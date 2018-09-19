package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import no.nav.tps.forvalteren.domain.service.appinfo.ApplicationInfo;

@RunWith(MockitoJUnitRunner.class)
public class AppInfoControllerTest {

    @InjectMocks
    private AppInfoController appInfoController;

    @Test
    public void happyPath(){
        ReflectionTestUtils.setField(appInfoController, "environmentName", "testEnv");
        ReflectionTestUtils.setField(appInfoController, "appVersion", "testAppV");

        ApplicationInfo applicationInfo = appInfoController.getInfo();

        assertThat(applicationInfo.getEnvironment(), is("testEnv"));
        assertThat(applicationInfo.getApplicationVersion(), is("testAppV"));
    }
}