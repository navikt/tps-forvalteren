package no.nav.tps.forvalteren.service.command.testdata.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ContainsXmlElementsTest {

    @InjectMocks
    private ContainsXmlElements containsXmlElements;

    @Test
    public void verifyContainsXmlElements() {
        assertThat(containsXmlElements.execute("<xml></xml>"), is(true));
        assertThat(containsXmlElements.execute("justarandomtest</string>"), is(true));
        assertThat(containsXmlElements.execute("01015021862017033006505113220170323300000>   "), is(true));
        assertThat(containsXmlElements.execute("461 79 UPPHÄRAD SVERIGE                       10600000001062017032320"), is(false));
        assertThat(containsXmlElements.execute(""), is(false));
    }
}
