package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.service.RelasjonType;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.FoedselsmeldingSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.InnvandringUpdateSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentKjoennFraIdentService;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.HusbokstavEncoder;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.SetAdresseService;

@RunWith(SpringJUnit4ClassRunner.class)
@Import({HentKjoennFraIdentService.class, SetAdresseService.class, HusbokstavEncoder.class, HentDatoFraIdentService.class})
public class FoedselsmeldingSkdParameterStrategyTest {

    @Autowired
    private HentKjoennFraIdentService hentKjoennFraIdentService;

    @Autowired
    private SetAdresseService setAdresseService;

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    private FoedselsmeldingSkdParameterStrategy foedselsmeldingSkdParameterStrategy;

    private Person barn;

    private Relasjon morsrelasjon;
    private Relasjon farsrelasjon;

    private Gateadresse gateadresse;
    private SkdMeldingTrans1 result;

    @Before
    public void setup() {
        foedselsmeldingSkdParameterStrategy = new FoedselsmeldingSkdParameterStrategy();
        ReflectionTestUtils.setField(foedselsmeldingSkdParameterStrategy, "setAdresseService", setAdresseService);
        ReflectionTestUtils.setField(foedselsmeldingSkdParameterStrategy, "hentKjoennFraIdentService", hentKjoennFraIdentService);
        ReflectionTestUtils.setField(setAdresseService, "hentDatoFraIdentService", hentDatoFraIdentService);

        barn = new Person();

        barn.setId(0001L);
        barn.setFornavn("Anne");
        barn.setEtternavn("Knutsdottir");
        barn.setIdent("01011846678");
        barn.setRegdato(LocalDateTime.now());

        Person mor = new Person();
        mor.setId(0101L);
        mor.setIdent("12128024680");
        mor.setEtternavn("Hansen");
        morsrelasjon = Relasjon.builder()
                .person(barn)
                .personRelasjonMed(mor)
                .relasjonTypeNavn(RelasjonType.MOR.name())
                .build();

        Person far = new Person();
        far.setId(0102L);
        far.setIdent("21027013579");
        farsrelasjon = Relasjon.builder()
                .person(barn)
                .personRelasjonMed(far)
                .relasjonTypeNavn(RelasjonType.FAR.name())
                .build();

        gateadresse = Gateadresse.builder()
                .adresse("Storgata")
                .husnummer("2")
                .gatekode("12345")
                .build();

        gateadresse.setKommunenr("0341");
        gateadresse.setPostnr("1234");
        barn.setBoadresse(gateadresse);
    }

    @Test
    public void isSupportedTest() {
        SkdParametersCreator correctOject = mock(FoedselsmeldingSkdParametere.class);
        SkdParametersCreator incorrectObject = mock(InnvandringUpdateSkdParametere.class);

        assertThat(foedselsmeldingSkdParameterStrategy.isSupported(correctOject), is(true));
        assertThat(foedselsmeldingSkdParameterStrategy.isSupported(incorrectObject), is(false));
    }

    @Test
    public void createCorrectFoedselmeldingParamsFromPerson() {
        barn.getRelasjoner().addAll(Arrays.asList(morsrelasjon, farsrelasjon));

        result = foedselsmeldingSkdParameterStrategy.execute(barn);

        assertThat(result.getFodselsdato(), is("010118"));
        assertThat(result.getPersonnummer(), is("46678"));
        assertThat(result.getKjoenn(), is("K"));
        assertThat(result.getFarsFodselsdato(), is("210270"));
        assertThat(result.getFarsPersonnummer(), is("13579"));
        assertThat(result.getSlektsnavn(), is("Knutsdottir"));
        assertThat(result.getForeldreansvar(), is("D"));
    }

    @Test
    public void createFoedselsmeldingParamsWithOnlyOneParent() {
        barn.getRelasjoner().addAll(Arrays.asList(morsrelasjon));

        result = foedselsmeldingSkdParameterStrategy.execute(barn);

        assertThat(result.getFarsFodselsdato(), is(nullValue()));
        assertThat(result.getFarsPersonnummer(), is(nullValue()));
        assertThat(result.getForeldreansvar(), is("M"));
    }

    @Test
    public void createFoedselsmeldingParamsWithGateadresse() {
        barn.getRelasjoner().addAll(Arrays.asList(morsrelasjon, farsrelasjon));
        barn.setBoadresse(gateadresse);

        result = foedselsmeldingSkdParameterStrategy.execute(barn);

        assertThat(result.getAdressenavn(), is("Storgata"));
        assertThat(result.getHusBruk(), is("2"));
        assertThat(result.getKommunenummer(), is("0341"));
        assertThat(result.getGateGaard(), is("12345"));
        assertThat(result.getAdressetype(), is("O"));
        assertThat(result.getPostnummer(), is("1234"));
    }
}
