package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import static no.nav.tps.forvalteren.domain.test.provider.PersonmalProvider.personmalA;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Personmal;
import no.nav.tps.forvalteren.domain.rs.RsPersonMal;
import no.nav.tps.forvalteren.provider.rs.util.MapperTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class PersonmalMappingStrategyTest {

    private static final Personmal PERSONMAL_A = personmalA().build();

    private MapperFacade mapperFacade;

    @InjectMocks
    private PersonmalMappingStrategy personmalMappingStrategy;

    @Before
    public void setup() {
        mapperFacade = MapperTestUtils.createMapperFacadeForMappingStrategy(personmalMappingStrategy);
    }

    @Test
    public void MapsFromJpaPersonmalToRsPersonmal() {
        RsPersonMal rsPersonMal = mapperFacade.map(PERSONMAL_A, RsPersonMal.class);

        assertThat(rsPersonMal.getKjonn(), is(PERSONMAL_A.getKjonn().toCharArray()[0]));
        assertThat(rsPersonMal.getMinAntallBarn(), is(String.valueOf(PERSONMAL_A.getMinAntallBarn())));
        assertThat(rsPersonMal.getMaxAntallBarn(), is(String.valueOf(PERSONMAL_A.getMinAntallBarn())));
        assertThat(rsPersonMal.getAdresse(), is(PERSONMAL_A.getAdresse()));
        assertThat(rsPersonMal.getGatePostnr(), is(PERSONMAL_A.getGatePostnr()));
        assertThat(rsPersonMal.getFodtEtter(), is(PERSONMAL_A.getFodtEtter()));
        assertThat(rsPersonMal.getFodtFor(), is(PERSONMAL_A.getFodtFor()));
    }

    @Test
    public void MapsFromRsPersonmalToJpaPersonmal() {
        RsPersonMal rsPersonMal = mapperFacade.map(PERSONMAL_A, RsPersonMal.class);
        Personmal personmal = mapperFacade.map(rsPersonMal, Personmal.class);

        assertThat(personmal.getKjonn(), is(rsPersonMal.getKjonn().toString()));
        assertThat(personmal.getMinAntallBarn(), is(Integer.parseInt(rsPersonMal.getMinAntallBarn())));
        assertThat(personmal.getMaxAntallBarn(), is(Integer.parseInt(rsPersonMal.getMaxAntallBarn())));
        assertThat(personmal.getAdresse(), is(rsPersonMal.getAdresse()));

    }
}
