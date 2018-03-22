package no.nav.tps.forvalteren.service.command.testdata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import no.nav.tps.forvalteren.domain.rs.RsTpsStatusPaaIdenterResponse;
import no.nav.tps.forvalteren.domain.rs.TpsStatusPaaIdent;
import no.nav.tps.forvalteren.domain.service.user.User;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jarl Øystein Samseth, Visma Consulting
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class StatusPaaIdenterITpsTest {
	private final Set<String> ALLE_MILJOER = new HashSet<>(Arrays.asList("u6", "t1", "t2", "t3", "t4", "t5", "t6", "t7", "t8", "t9", "t10", "t11"));
	private final List<String> IDENTER = Arrays.asList("123", "456");
	private final List<TpsStatusPaaIdent> EXPECTED_TPS_STATUS = Arrays.asList(
			new TpsStatusPaaIdent(IDENTER.get(0), Arrays.asList("t1")),
			new TpsStatusPaaIdent(IDENTER.get(1), Arrays.asList("t1", "t2")));
	
	@Mock
	private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;
	@Mock
	private UserContextHolder userContextHolder;
	
	@InjectMocks
	private StatusPaaIdenterITps statusPaaIdenterITps;
	
	@Before
	public void setup() {
		when(filterEnvironmentsOnDeployedEnvironment.execute(any())).thenReturn(ALLE_MILJOER);
		when(userContextHolder.getUser()).thenReturn(new User("bruker", "brukernavn"));
		//TODO wiremock tps-kø. returner kun
	}
	
	@Test
	public void shouldHentStatusPaaIdenterIAlleMiljoer() {
		
		RsTpsStatusPaaIdenterResponse actualStatus = statusPaaIdenterITps.hentStatusPaaIdenterIAlleMiljoer(IDENTER);
		assertEquals(IDENTER.size(), actualStatus.getStatusPaaIdenter().size());
		EXPECTED_TPS_STATUS.forEach(expectedStatusPaaIdent -> {
			assertTrue("Assert ident", actualStatus.getStatusPaaIdenter().stream()
					.anyMatch(actualtpsStatusPaaIdent -> expectedStatusPaaIdent.getIdent()
							.equals(actualtpsStatusPaaIdent.getIdent())));
			assertTrue("Assert env", actualStatus.getStatusPaaIdenter().stream()
					.anyMatch(actualtpsStatusPaaIdent -> expectedStatusPaaIdent.getEnv()
							.equals(actualtpsStatusPaaIdent.getEnv())));
		});
	}
}
