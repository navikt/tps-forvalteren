package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.anyLong;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FoedselsmeldingSkdParameterStrategyTest {

    @Mock
    private RelasjonRepository relasjonRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private AdresseRepository adresseRepository;

    @InjectMocks
    private FoedselsmeldingSkdParameterStrategy foedselsmeldingSkdParameterStrategy;

    private Person barn;
    private Person mor;
    private Person far;

    private Relasjon relasjoner;
    private Gateadresse gateadresse;
    private Adresse adresse;

    private HashMap<String, String> skdParameters;

    private List<Relasjon> resultRelasjonRepository;

    @Before
    public void setup() {
        barn = new Person();

        barn.setId(0001L);
        barn.setFornavn("JON");
        barn.setIdent("01011845678");
        barn.setRegdato(LocalDateTime.now());

        mor = new Person();
        mor.setId(0101L);
        mor.setIdent("12128024680");
        mor.setEtternavn("HANSEN");

        far = new Person();
        far.setId(0102L);
        far.setIdent("21027013579");

        gateadresse = new Gateadresse();
        gateadresse.setAdresse("Storgata");
        gateadresse.setHusnummer("2");
        gateadresse.setGatekode("12345");
        gateadresse.setKommunenr("0341");

        resultRelasjonRepository = new ArrayList<>();
        resultRelasjonRepository.add(new Relasjon(0002L, new Person(), mor, "MOR"));
        resultRelasjonRepository.add(new Relasjon(0003L, new Person(), far, "FAR"));

        skdParameters = new HashMap<>();
        createResultSkdParameter(skdParameters);
    }

    @Test
    public void createCorrectFoedselmeldingParamsFromPerson() {

        Map<String, String> result = new HashMap<String, String>();
        when(relasjonRepository.findByPersonId(anyLong())).thenReturn(resultRelasjonRepository);
        when(personRepository.findById(0101L)).thenReturn(mor);
        when(personRepository.findById(0102L)).thenReturn(far);
        result = foedselsmeldingSkdParameterStrategy.execute(barn);

        assertThat(result.get("foreldreansvar"), is("D"));
    }

    private void createResultSkdParameter(HashMap<String, String> skdParameters) {

    }
}
