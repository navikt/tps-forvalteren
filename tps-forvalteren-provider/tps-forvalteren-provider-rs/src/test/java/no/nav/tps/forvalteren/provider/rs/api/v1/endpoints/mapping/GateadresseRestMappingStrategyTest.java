package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.rs.RsGateadresse;
import no.nav.tps.forvalteren.provider.rs.util.MapperTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static no.nav.tps.forvalteren.domain.test.provider.GateadresseProvider.standardGateadresse;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

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

        Gateadresse JpaGateadresse = mapper.map(rsGateadresse, Gateadresse.class);

        assertThat(JpaGateadresse.getAdresse(), is(rsGateadresse.getGateadresse()));
        assertThat(JpaGateadresse.getGatekode(), is(rsGateadresse.getGatekode()));
        assertThat(JpaGateadresse.getHusnummer(), is(rsGateadresse.getHusnummer()));
        assertThat(JpaGateadresse.getId(), is(rsGateadresse.getAdresseId()));
        assertThat(JpaGateadresse.getPostnr(), is(rsGateadresse.getPostnr()));
        assertThat(JpaGateadresse.getKommunenr(), is(rsGateadresse.getKommunenr()));
        assertThat(JpaGateadresse.getPerson().getId(), is(rsGateadresse.getPersonId()));
        assertThat(JpaGateadresse.getFlyttedato(), is(rsGateadresse.getFlyttedato()));
    }



}