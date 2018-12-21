package no.nav.tps.forvalteren.service.command.testdata.skd;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S610HentGT.PERSON_KERNINFO_SERVICE_ROUTINE;
import static org.assertj.core.util.Sets.newHashSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.testdata.TpsServiceroutineFnrRequest;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsServiceRoutineService;

@RunWith(MockitoJUnitRunner.class)
public class TknrOgGtFraMiljoServiceTest {

    private static final String TKNR = "1234";
    private static final String LANDKODE = "0123";
    private static final String KOMMUNENR = "0234";
    private static final String BYDEL = "312142";
    private static final String GT_REGEL = "A";

    @Mock
    private PersonRepository personRepository;

    @Mock
    private TpsServiceRoutineService tpsServiceRoutineService;

    @Mock
    private TpsServiceroutineFnrRequest tpsFnrRequest;

    @InjectMocks
    private TknrOgGtFraMiljoService tknrOgGtFraMiljoService;

    @Mock
    private TpsServiceRoutineResponse response;

    @Before
    public void setup() {
        when(tpsFnrRequest.buildRequest(any(Person.class), anyString())).thenReturn(new HashMap());
        when(tpsServiceRoutineService.execute(eq(PERSON_KERNINFO_SERVICE_ROUTINE),
                any(Map.class),
                eq(true)))
                .thenReturn(response);
    }

    @Test
    public void hentTknrOgSettPaPersonOk() {
        when(response.getResponse()).thenReturn(buildResponseObject(null, null, null, TKNR));

        Person resultat = tknrOgGtFraMiljoService.hentTknrOgGtPaPerson(singletonList(Person.builder().build()), newHashSet(singleton("u2"))).get(0);

        assertThat(resultat.getTknr(), is(equalTo(TKNR)));
    }

    @Test
    public void hentGtLandOgSettPaPersonOk() {
        when(response.getResponse()).thenReturn(buildResponseObject(LANDKODE, null, null, null));

        Person resultat = tknrOgGtFraMiljoService.hentTknrOgGtPaPerson(singletonList(Person.builder().build()), newHashSet(singleton("u2"))).get(0);

        assertThat(resultat.getGtType(), is(equalTo("LAND")));
        assertThat(resultat.getGtVerdi(), is(equalTo(LANDKODE)));
    }

    @Test
    public void hentGtKommuneOgSettPaPersonOk() {
        when(response.getResponse()).thenReturn(buildResponseObject(null, KOMMUNENR, null, null));

        Person resultat = tknrOgGtFraMiljoService.hentTknrOgGtPaPerson(singletonList(Person.builder().build()), newHashSet(singleton("u2"))).get(0);

        assertThat(resultat.getGtType(), is(equalTo("KNR")));
        assertThat(resultat.getGtVerdi(), is(equalTo(KOMMUNENR)));
    }

    @Test
    public void hentGtBydelOgSettPaPersonOk() {
        when(response.getResponse()).thenReturn(buildResponseObject(null, null, BYDEL, null));

        Person resultat = tknrOgGtFraMiljoService.hentTknrOgGtPaPerson(singletonList(Person.builder().build()), newHashSet(singleton("u2"))).get(0);

        assertThat(resultat.getGtType(), is(equalTo("BYDEL")));
        assertThat(resultat.getGtVerdi(), is(equalTo(BYDEL)));
    }

    @Test
    public void hentGtRegelOgSettPaPersonOk() {
        when(response.getResponse()).thenReturn(buildResponseObject(null, null, BYDEL, null));

        Person resultat = tknrOgGtFraMiljoService.hentTknrOgGtPaPerson(singletonList(Person.builder().build()), newHashSet(singleton("u2"))).get(0);

        assertThat(resultat.getGtRegel(), is(equalTo(GT_REGEL)));
    }

    private Map buildResponseObject(String landkode, String kommuneNr, String bydel, String tknr) {

        Map bruker = new HashMap();
        bruker.put("regelForGeografiskTilknytning", GT_REGEL);
        Map geografiskTilknytning = new HashMap();
        geografiskTilknytning.put("landKode", landkode);
        geografiskTilknytning.put("kommunenr", kommuneNr);
        geografiskTilknytning.put("bydel", bydel);
        bruker.put("geografiskTilknytning", geografiskTilknytning);
        Map fullBostedsAdresse = new HashMap();
        fullBostedsAdresse.put("tknr", tknr);
        Map bostedsAdresse = new HashMap();
        bostedsAdresse.put("fullBostedsAdresse", fullBostedsAdresse);
        Map data = new HashMap();
        data.put("bostedsAdresse", bostedsAdresse);
        data.put("bruker", bruker);
        Map response = new HashMap();
        response.put("data1", data);
        return response;
    }
}