package no.nav.tps.vedlikehold.provider.rs.api.v1.selftest.models;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ApplicationPropertyTest {

    @Test
    public void constructorInitializesNameAndValue() {
        ApplicationProperty property = new ApplicationProperty("Property name", "Property value");

        assertThat(property.getName(), is(equalTo("Property name")));
        assertThat(property.getValue(), is(equalTo("Property value")));
    }
}
