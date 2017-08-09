package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.domain.rs.RsMatrikkeladresse;
import no.nav.tps.forvalteren.provider.rs.util.MapperTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static no.nav.tps.forvalteren.domain.test.provider.MatrikkeladresseProvider.standardMatrikkeladresse;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(MockitoJUnitRunner.class)
public class MatrikkeladresseRestMappingStrategyTest {

    private Matrikkeladresse matrikkeladresse = standardMatrikkeladresse().build();

    private MapperFacade mapper;

    @InjectMocks
    private MatrikkeladresseRestMappingStrategy strategy;

    @Before
    public void setUpHappyPath() {
        mapper = MapperTestUtils.createMapperFacadeForMappingStrategy(strategy);
    }

    @Test
    public void mapsFromMatrikkeladresseToRsMatrikkeladresse() {
        RsMatrikkeladresse rsMatrikkeladresse = mapper.map(matrikkeladresse, RsMatrikkeladresse.class);

        assertThat(rsMatrikkeladresse, is(notNullValue()));
        assertThat(rsMatrikkeladresse.getBruksnr(), is(matrikkeladresse.getBruksnr()));
        assertThat(rsMatrikkeladresse.getFestenr(), is(matrikkeladresse.getFestenr()));
        assertThat(rsMatrikkeladresse.getGardsnr(), is(matrikkeladresse.getGardsnr()));
        assertThat(rsMatrikkeladresse.getMellomnavn(), is(matrikkeladresse.getMellomnavn()));
        assertThat(rsMatrikkeladresse.getUndernr(), is(matrikkeladresse.getUndernr()));
        assertThat(rsMatrikkeladresse.getAdresseId(), is(matrikkeladresse.getId()));
        assertThat(rsMatrikkeladresse.getFlyttedato(), is(matrikkeladresse.getFlyttedato()));
        assertThat(rsMatrikkeladresse.getKommunenr(), is(matrikkeladresse.getKommunenr()));
        assertThat(rsMatrikkeladresse.getPersonId(), is(matrikkeladresse.getPerson().getId()));
        assertThat(rsMatrikkeladresse.getPostnr(), is(matrikkeladresse.getPostnr()));
    }

    @Test
    public void mapsFromRsMatrikkeladresseToMatrikkeladresse() {
        RsMatrikkeladresse rsMatrikkeladresse = new RsMatrikkeladresse();
        rsMatrikkeladresse.setBruksnr(matrikkeladresse.getBruksnr());
        rsMatrikkeladresse.setFestenr(matrikkeladresse.getFestenr());
        rsMatrikkeladresse.setGardsnr(matrikkeladresse.getGardsnr());
        rsMatrikkeladresse.setMellomnavn(matrikkeladresse.getMellomnavn());
        rsMatrikkeladresse.setUndernr(matrikkeladresse.getUndernr());
        rsMatrikkeladresse.setAdresseId(matrikkeladresse.getId());
        rsMatrikkeladresse.setFlyttedato(matrikkeladresse.getFlyttedato());
        rsMatrikkeladresse.setKommunenr(matrikkeladresse.getKommunenr());
        rsMatrikkeladresse.setPersonId(matrikkeladresse.getPerson().getId());
        rsMatrikkeladresse.setPostnr(matrikkeladresse.getPostnr());

        Matrikkeladresse matrikkeladresse = mapper.map(rsMatrikkeladresse, Matrikkeladresse.class);

        assertThat(matrikkeladresse, is(notNullValue()));
        assertThat(matrikkeladresse.getBruksnr(), is(rsMatrikkeladresse.getBruksnr()));
        assertThat(matrikkeladresse.getFestenr(), is(rsMatrikkeladresse.getFestenr()));
        assertThat(matrikkeladresse.getGardsnr(), is(rsMatrikkeladresse.getGardsnr()));
        assertThat(matrikkeladresse.getMellomnavn(), is(rsMatrikkeladresse.getMellomnavn()));
        assertThat(matrikkeladresse.getUndernr(), is(rsMatrikkeladresse.getUndernr()));
        assertThat(matrikkeladresse.getId(), is(rsMatrikkeladresse.getAdresseId()));
        assertThat(matrikkeladresse.getFlyttedato(), is(rsMatrikkeladresse.getFlyttedato()));
        assertThat(matrikkeladresse.getKommunenr(), is(rsMatrikkeladresse.getKommunenr()));
        assertThat(matrikkeladresse.getPerson().getId(), is(rsMatrikkeladresse.getPersonId()));
        assertThat(matrikkeladresse.getPostnr(), is(rsMatrikkeladresse.getPostnr()));
    }

}