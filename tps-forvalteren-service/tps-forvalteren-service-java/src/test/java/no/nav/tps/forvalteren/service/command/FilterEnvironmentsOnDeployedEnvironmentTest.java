package no.nav.tps.forvalteren.service.command;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class FilterEnvironmentsOnDeployedEnvironmentTest {

    private static final String ENVIRONMENT_PROPERTY_VALUE = "deployedEnvironment";

    private Set<String> envIn = new HashSet<>();

    @InjectMocks
    FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;

    @Test
    public void hvisErIUMiljoeSaaReturnerU() {

        ReflectionTestUtils.setField(filterEnvironmentsOnDeployedEnvironment, ENVIRONMENT_PROPERTY_VALUE, "u");

        envIn.add("u1");
        envIn.add("u2");
        envIn.add("t1");
        envIn.add("q1");

        Set<String> ret = filterEnvironmentsOnDeployedEnvironment.execute(envIn);

        assertTrue(ret.contains("q1"));
        assertTrue(ret.contains("t1"));
        assertTrue(ret.contains("u1"));
        assertTrue(ret.contains("u2"));
    }
}