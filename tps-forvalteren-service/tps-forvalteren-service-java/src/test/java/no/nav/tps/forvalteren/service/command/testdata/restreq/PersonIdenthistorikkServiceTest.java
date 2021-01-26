package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsAliasRequest;
import no.nav.tps.forvalteren.domain.rs.RsAliasResponse;
import no.nav.tps.forvalteren.domain.rs.RsIdenthistorikkKriterium;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersonerOgSjekkMiljoeService;
import no.nav.tps.forvalteren.service.command.testdata.skd.LagreTilTpsService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentKjoennFraIdentService;

@RunWith(MockitoJUnitRunner.class)
public class PersonIdenthistorikkServiceTest {

    private static final String IDENT = "11111111111";
    private static final String IDENT_2 = "22222222222";
    private static final LocalDateTime FODSELSDAG = LocalDateTime.of(1970,1,1,0,0);
    private static final String ETTERNAVN = "Hansen";
    private static final String FORNAVN = "Hans";
    private static final String MELLOMNAVN = "Hansene";

    @Mock
    private PersonRepository personRepository;

    @Mock
    private HentDatoFraIdentService hentDatoFraIdentService;

    @Mock
    private HentKjoennFraIdentService hentKjoennFraIdentService;

    @Mock
    private OpprettPersonerOgSjekkMiljoeService opprettPersonerOgSjekkMiljoeService;

    @Mock
    private IdenthistorikkService identhistorikkService;

    @Mock
    private LagreTilTpsService lagreTilTpsService;

    @Mock
    private MapperFacade mapperFacade;

    @InjectMocks
    private PersonIdenthistorikkService personIdenthistorikkService;

    @Test
    public void prepareAliases_OK() {

        when(personRepository.findByIdent(IDENT)).thenReturn(Person.builder().ident(IDENT).build());
        when(opprettPersonerOgSjekkMiljoeService.createNyeIdenter(any(RsPersonKriteriumRequest.class))).thenReturn(singletonList(Person.builder().ident(IDENT_2).build()));
        when(hentDatoFraIdentService.extract(anyString())).thenReturn(FODSELSDAG);
        when(mapperFacade.map(any(Person.class), eq(RsAliasResponse.Personnavn.class))).thenReturn(
                RsAliasResponse.Personnavn.builder().etternavn(ETTERNAVN).mellomnavn(MELLOMNAVN).fornavn(FORNAVN).build());

        RsAliasRequest request = RsAliasRequest.builder()
                .aliaser(singletonList(RsAliasRequest.AliasSpesification.builder()
                        .identtype(RsAliasRequest.IdentType.FNR)
                        .nyIdent(true)
                        .build()))
                .ident(IDENT)
                .build();

        RsAliasResponse response = personIdenthistorikkService.prepareAliases(request);
        assertThat(response.getHovedperson().getIdent(), is(equalTo(IDENT)));
        assertThat(response.getHovedperson().getNavn().getEtternavn(), is(equalTo(ETTERNAVN)));
        assertThat(response.getHovedperson().getNavn().getFornavn(), is(equalTo(FORNAVN)));
        assertThat(response.getHovedperson().getFodselsdato(), is(equalTo(FODSELSDAG)));
        assertThat(response.getAliaser().get(0).getIdent(), is(equalTo(IDENT_2)));
        assertThat(response.getAliaser().get(0).getNavn().getEtternavn(), is(equalTo(ETTERNAVN)));
        assertThat(response.getAliaser().get(0).getNavn().getMellomnavn(), is(equalTo(MELLOMNAVN)));
        assertThat(response.getAliaser().get(0).getNavn().getFornavn(), is(equalTo(FORNAVN)));
        assertThat(response.getAliaser().get(0).getFodselsdato(), is(equalTo(FODSELSDAG)));
    }

    @Test
    public void prepareIdenthistorikk_OK() {

        when(hentDatoFraIdentService.extract(anyString())).thenReturn(FODSELSDAG);

        personIdenthistorikkService.prepareIdenthistorikk(Person.builder().ident(IDENT).build(),
                singletonList(RsIdenthistorikkKriterium.builder()
                        .identtype("FNR")
                        .build()), false);

        verify(opprettPersonerOgSjekkMiljoeService).createNyeIdenter(any(RsPersonKriteriumRequest.class));
        verify(personRepository).saveAll(anyList());
        verify(identhistorikkService).save(anyString(), anyList(), anyList());
    }
}