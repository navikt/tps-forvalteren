package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import static no.nav.tps.forvalteren.domain.test.provider.DeathRowProvider.aDeathRow;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.domain.rs.RsDeathRow;
import no.nav.tps.forvalteren.provider.rs.util.MapperTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class DeathRowMappingStrategyTest {

    private static final DeathRow DEATH_ROW = aDeathRow().id(1337L).build();
    private MapperFacade mapper;

    @InjectMocks
    private DeathRowMappingStrategy deathRowMappingStrategy;

    @Before
    public void setup() {
        mapper = MapperTestUtils.createMapperFacadeForMappingStrategy(deathRowMappingStrategy);
    }

    @Test
    public void MapsFromJpaDeathRowToRsDeathRow() {
        RsDeathRow rsDeathRow = mapper.map(DEATH_ROW, RsDeathRow.class);

        assertThat(rsDeathRow.getId(), is(DEATH_ROW.getId()));
        assertThat(rsDeathRow.getIdent(), is(DEATH_ROW.getIdent()));
        assertThat(rsDeathRow.getHandling(), is(DEATH_ROW.getHandling()));
        assertThat(rsDeathRow.getMiljoe(), is(DEATH_ROW.getMiljoe()));
        assertThat(rsDeathRow.getStatus(), is(DEATH_ROW.getStatus()));
        assertThat(rsDeathRow.getTilstand(), is(DEATH_ROW.getTilstand()));
        assertThat(rsDeathRow.getDoedsdato(), is(DEATH_ROW.getDoedsdato()));
        assertThat(rsDeathRow.getTidspunkt(), is(DEATH_ROW.getEndretDato()));
        assertThat(rsDeathRow.getBruker(), is(DEATH_ROW.getEndretAv()));
    }

    @Test
    public void MapsFromRsDeathRowToJpaDeathRow() {
        RsDeathRow rsDeathRow = mapper.map(DEATH_ROW, RsDeathRow.class);
        DeathRow jpaDeathRow = mapper.map(rsDeathRow, DeathRow.class);

        assertThat(jpaDeathRow.getId(), is(rsDeathRow.getId()));
        assertThat(jpaDeathRow.getIdent(), is(rsDeathRow.getIdent()));
        assertThat(jpaDeathRow.getHandling(), is(rsDeathRow.getHandling()));
        assertThat(jpaDeathRow.getMiljoe(), is(rsDeathRow.getMiljoe()));
        assertThat(jpaDeathRow.getStatus(), is(rsDeathRow.getStatus()));
        assertThat(jpaDeathRow.getTilstand(), is(rsDeathRow.getTilstand()));
        assertThat(jpaDeathRow.getDoedsdato(), is(rsDeathRow.getDoedsdato()));
        assertThat(jpaDeathRow.getEndretDato(), is(rsDeathRow.getTidspunkt()));
        assertThat(jpaDeathRow.getEndretAv(), is(rsDeathRow.getBruker()));
    }

}