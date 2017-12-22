package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingLoggRepository;

@RunWith(MockitoJUnitRunner.class)
public class GetLoggForGruppeTest {

    @Mock
    private SkdEndringsmeldingLoggRepository skdEndringsmeldingLoggRepository;

    @InjectMocks
    private GetLoggForGruppe getLoggForGruppe;

    private static final Long GRUPPE_ID = 1337L;

    @Test
    public void verifyServiceCall() {
        getLoggForGruppe.execute(GRUPPE_ID);

        verify(skdEndringsmeldingLoggRepository).findAllByMeldingsgruppeId(GRUPPE_ID);
    }

}