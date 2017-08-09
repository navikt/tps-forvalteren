package no.nav.tps.forvalteren.service.command.testdata.skd.utils;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class DefaultPersonToSkdParametersForInnvandringMapperTest {


    @InjectMocks
    private DefaultPersonToSkdParametersForInnvandringMapper personToSkdParametersForInnvandringMapper;


    @Test
    public void parametereBlirLagetMedSammeFeltverdierSomLiggerSomAttributterPaaPerson() {
        Person person = new Person();
        person.setIdent("12345678911");
        person.setFornavn("ola");
        person.setEtternavn("nordmann");
        person.setStatsborgerskap("000");
        LocalDateTime now = LocalDateTime.now();
        person.setRegdato(now);

        Map<String, String> param = personToSkdParametersForInnvandringMapper.create(person);

        assertThat(param.get(SkdConstants.FODSELSDATO), is(equalTo("123456")));
        assertThat(param.get(SkdConstants.PERSONNUMMER), is(equalTo("78911")));
        assertThat(param.get(SkdConstants.FORNAVN), is(equalTo("ola")));
        assertThat(param.get(SkdConstants.SLEKTSNAVN), is(equalTo("nordmann")));
        assertThat(param.get(SkdConstants.STATSBORGERSKAP), is(equalTo("000")));
    }

}