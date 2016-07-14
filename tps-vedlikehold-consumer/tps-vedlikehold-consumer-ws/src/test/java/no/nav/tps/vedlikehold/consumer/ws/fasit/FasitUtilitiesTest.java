package no.nav.tps.vedlikehold.consumer.ws.fasit;

import no.nav.aura.envconfig.client.DomainDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;


/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

public class FasitUtilitiesTest {

    @Test
    public void returnsCorrectDomainForTEnvironment() {
        assertThat(FasitUtilities.domainFor("t2"), is(DomainDO.TestLocal));
    }

    @Test
    public void returnsCorrectDomainForPEnvironment() {
        assertThat(FasitUtilities.domainFor("p2"), is(DomainDO.Adeo));
    }

    @Test
    public void returnsCorrectDomainForQEnvironment() {
        assertThat(FasitUtilities.domainFor("q2"), is(DomainDO.PreProd));
    }

    @Test
    public void returnsCorrectDomainForREnvironment() {
        assertThat(FasitUtilities.domainFor("r2"), is(DomainDO.Devillo));
    }

    @Test
    public void returnsCorrectDomainForUEnvironment() {
        assertThat(FasitUtilities.domainFor("u2"), is(DomainDO.Devillo));
    }

    @Test
    public void returnsCorrectDomainForSEnvironment() {
        assertThat(FasitUtilities.domainFor("s2"), is(DomainDO.Devillo));
    }

}
