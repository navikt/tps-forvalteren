package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.domain.rs.RsPostadresse;
import no.nav.tps.forvalteren.provider.rs.util.MapperTestUtils;

public class PostadresseMappingStrategyTest {

    private static final String POST_ADR_LINJE_1 = "Postadresselinje 1";
    private static final String POST_ADR_LINJE_2 = "Postadresselinje 2";
    private static final String POST_ADR_LINJE_3 = "Postadresselinje 3";

    private MapperFacade mapperFacade;

    @Before
    public void setup() {
        mapperFacade = MapperTestUtils.createMapperFacadeForMappingStrategy(new PostadresseMappingStrategy());
    }

    @Test
    public void mapPostadresseAlleAdresselinjer() {

        Postadresse postadresse = mapperFacade.map(RsPostadresse.builder()
                .postLinje1(POST_ADR_LINJE_1)
                .postLinje2(POST_ADR_LINJE_2)
                .postLinje3(POST_ADR_LINJE_3)
                .build(), Postadresse.class);

        assertThat(postadresse.getPostLinje1(), is(equalTo(POST_ADR_LINJE_1)));
        assertThat(postadresse.getPostLinje2(), is(equalTo(POST_ADR_LINJE_2)));
        assertThat(postadresse.getPostLinje3(), is(equalTo(POST_ADR_LINJE_3)));
    }

    @Test
    public void mapPostadresse_Adresselinje1isBlank() {

        Postadresse postadresse = mapperFacade.map(RsPostadresse.builder()
                .postLinje2(POST_ADR_LINJE_2)
                .postLinje3(POST_ADR_LINJE_3)
                .build(), Postadresse.class);

        assertThat(postadresse.getPostLinje1(), is(equalTo(POST_ADR_LINJE_2)));
        assertThat(postadresse.getPostLinje2(), is(equalTo(POST_ADR_LINJE_3)));
        assertThat(postadresse.getPostLinje3(), is(nullValue()));
    }

    @Test
    public void mapPostadresse_Adresselinje2isBlank() {

        Postadresse postadresse = mapperFacade.map(RsPostadresse.builder()
                .postLinje1(POST_ADR_LINJE_1)
                .postLinje3(POST_ADR_LINJE_3)
                .build(), Postadresse.class);

        assertThat(postadresse.getPostLinje1(), is(equalTo(POST_ADR_LINJE_1)));
        assertThat(postadresse.getPostLinje2(), is(equalTo(POST_ADR_LINJE_3)));
        assertThat(postadresse.getPostLinje3(), is(nullValue()));
    }

    @Test
    public void mapPostadresse_Adresselinje1And2isBlank() {

        Postadresse postadresse = mapperFacade.map(RsPostadresse.builder()
                .postLinje3(POST_ADR_LINJE_3)
                .build(), Postadresse.class);

        assertThat(postadresse.getPostLinje1(), is(equalTo(POST_ADR_LINJE_3)));
        assertThat(postadresse.getPostLinje2(), is(nullValue()));
        assertThat(postadresse.getPostLinje3(), is(nullValue()));
    }
}