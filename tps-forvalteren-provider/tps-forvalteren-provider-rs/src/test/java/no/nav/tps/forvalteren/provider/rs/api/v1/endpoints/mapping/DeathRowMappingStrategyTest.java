package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import static no.nav.tps.forvalteren.domain.test.provider.DeathRowProvider.aDeathRow;
import static no.nav.tps.forvalteren.domain.test.provider.rs.RsDeathRowBulkProvider.aDeathRowBulk;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.domain.rs.RsDeathRow;
import no.nav.tps.forvalteren.domain.rs.RsDeathRowBulk;
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

    @Test
    public void MapsFromRsDeathRowBulkToMultipleDeathRows() {
        RsDeathRowBulk deathRowBulk = aDeathRowBulk().build();
        List<DeathRow> deathRowList = mapper.map(deathRowBulk, List.class);

        assertThat(deathRowList, hasSize(2));
        assertThat(deathRowList.get(0).getIdent(), is("1111"));
        assertThat(deathRowList.get(1).getIdent(), is("2222"));

        deathRowList.forEach(deathRow -> {
            assertThat(deathRow.getId(), is(nullValue()));
            assertThat(deathRow.getDoedsdato(), is(LocalDate.now().minusDays(1)));
            assertThat(deathRow.getHandling(), is("handling"));
            assertThat(deathRow.getMiljoe(), is("miljoe"));
            assertThat(deathRow.getStatus(), is("status"));
            assertThat(deathRow.getTilstand(), is("tilstand"));
        });
    }

    @Test
    public void MapsFromEmptyRsDeathRowBulkToEmptyList() {
        List<String> emptyIdentList = new ArrayList<>();
        RsDeathRowBulk deathRowBulk = aDeathRowBulk().identer(emptyIdentList).build();
        List<DeathRow> deathRowList = mapper.map(deathRowBulk, List.class);

        assertThat(deathRowList, hasSize(0));
    }

    @Test
    public void MapsFromRsDeathRowBulkWithNullListToEmptyList() {
        RsDeathRowBulk deathRowBulk = aDeathRowBulk().identer(null).build();
        List<DeathRow> deathRowList = mapper.map(deathRowBulk, List.class);

        assertThat(deathRowList, hasSize(0));
    }
}