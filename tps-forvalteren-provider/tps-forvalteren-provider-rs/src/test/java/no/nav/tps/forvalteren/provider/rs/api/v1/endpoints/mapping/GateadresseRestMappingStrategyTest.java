package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import static no.nav.tps.forvalteren.domain.test.provider.GateadresseProvider.standardGateadresse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.rs.RsGateadresse;
import no.nav.tps.forvalteren.provider.rs.util.MapperTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class GateadresseRestMappingStrategyTest {

    private Gateadresse gateadresse = standardGateadresse().build();

    private MapperFacade mapper;

    @InjectMocks
    private GateadresseRestMappingStrategy strategy;

    @Before
    public void setUpHappyPath() {
        mapper = MapperTestUtils.createMapperFacadeForMappingStrategy(strategy);
    }

    @Test
    public void MapsFromJpaGateadresseToRsGateadresse() {
        RsGateadresse rsGateadresse = mapper.map(gateadresse, RsGateadresse.class);

        assertThat(rsGateadresse.getGateadresse(), is(gateadresse.getAdresse()));
        assertThat(rsGateadresse.getGatekode(), is(gateadresse.getGatekode()));
        assertThat(rsGateadresse.getHusnummer(), is(gateadresse.getHusnummer()));
        assertThat(rsGateadresse.getAdresseId(), is(gateadresse.getId()));
        assertThat(rsGateadresse.getPostnr(), is(gateadresse.getPostnr()));
        assertThat(rsGateadresse.getKommunenr(), is(gateadresse.getKommunenr()));
        assertThat(rsGateadresse.getPersonId(), is(gateadresse.getPerson().getId()));
        assertThat(rsGateadresse.getFlyttedato(), is(gateadresse.getFlyttedato()));
    }

    @Test
    public void MapsFromRsGateadresseToJpaGateadresse() {
        RsGateadresse rsGateadresse = new RsGateadresse();
        rsGateadresse.setGateadresse(gateadresse.getAdresse());
        rsGateadresse.setGatekode(gateadresse.getGatekode());
        rsGateadresse.setHusnummer(gateadresse.getHusnummer());
        rsGateadresse.setAdresseId(gateadresse.getId());
        rsGateadresse.setPostnr(gateadresse.getPostnr());
        rsGateadresse.setKommunenr(gateadresse.getKommunenr());
        rsGateadresse.setPersonId(gateadresse.getPerson().getId());
        rsGateadresse.setFlyttedato(gateadresse.getFlyttedato());

        Gateadresse jpaGateadresse = mapper.map(rsGateadresse, Gateadresse.class);

        assertThat(jpaGateadresse.getAdresse(), is(rsGateadresse.getGateadresse()));
        assertThat(jpaGateadresse.getGatekode(), is(rsGateadresse.getGatekode()));
        assertThat(jpaGateadresse.getHusnummer(), is(rsGateadresse.getHusnummer()));
        assertThat(jpaGateadresse.getId(), is(rsGateadresse.getAdresseId()));
        assertThat(jpaGateadresse.getPostnr(), is(rsGateadresse.getPostnr()));
        assertThat(jpaGateadresse.getKommunenr(), is(rsGateadresse.getKommunenr()));
        assertThat(jpaGateadresse.getPerson().getId(), is(rsGateadresse.getPersonId()));
        assertThat(jpaGateadresse.getFlyttedato(), is(rsGateadresse.getFlyttedato()));
    }



}