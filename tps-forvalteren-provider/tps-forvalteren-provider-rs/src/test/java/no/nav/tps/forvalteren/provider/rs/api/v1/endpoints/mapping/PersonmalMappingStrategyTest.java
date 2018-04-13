package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import ma.glasnost.orika.MapperFacade;
import static no.nav.tps.forvalteren.domain.test.provider.PersonmalProvider.personmalA;
import no.nav.tps.forvalteren.domain.jpa.Personmal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PersonmalMappingStrategyTest {

    private static final Personmal PERSONMAL_A = personmalA().build();

    private MapperFacade mapperFacade;

    @InjectMocks
    private PersonmalMappingStrategy personmalMappingStrategy;

    @Before
    public void setup() {

    }

    @Test
    public void MapsFromJpaPersonmalToRsPersonmal() {

    }

    @Test
    public void MapsFromRsPersonmalToJpaPersonmal() {

    }
}
