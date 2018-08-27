package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.service.command.testdata.utils.ResourceHandling.resourceUrlToString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.google.common.io.Resources;
import no.nav.tps.forvalteren.domain.rs.RsTpsStatusPaaIdenterResponse;
import no.nav.tps.forvalteren.domain.rs.TpsStatusPaaIdent;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.domain.service.user.User;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsRequestMappingUtils;
import no.nav.tps.forvalteren.service.command.tpsconfig.GetEnvironments;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * @author Jarl Øystein Samseth, Visma Consulting
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class StatusPaaIdenterITpsTest {
	private final String T1 = "t1";
	private final String T2 = "t2";
	private final List<String> ALLE_MILJOER_LIST = Arrays.asList(T1, T2);
	private final Set<String> ALLE_MILJOER = new HashSet<>(ALLE_MILJOER_LIST);
	private final List<String> IDENTER = Arrays.asList("1111111111", "22222222222");
	private final List<TpsStatusPaaIdent> EXPECTED_TPS_STATUS = Arrays.asList(
			new TpsStatusPaaIdent(IDENTER.get(0), ALLE_MILJOER_LIST.subList(0, 1)),
			new TpsStatusPaaIdent(IDENTER.get(1), ALLE_MILJOER_LIST));
	private User user = new User("bruker", "brukernavn");
	
	private String MQ_xmlResponse_envT1 = resourceUrlToString(Resources.getResource("service/tpsstatuspaaidenter/MQ_Response_envT1.xml"));
	private String MQ_xmlResponse_envT2 = resourceUrlToString(Resources.getResource("service/tpsstatuspaaidenter/MQ_Response_envT2.xml"));
	
	@Mock
	private GetEnvironments getEnvironments;
	@Mock
	private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;
	@Mock
	private UserContextHolder userContextHolder;
	@Mock
	private RsTpsRequestMappingUtils mappingUtils;
	@Mock
	private TpsRequestSender tpsRequestSender;
	
	@InjectMocks
	private StatusPaaIdenterITps statusPaaIdenterITps;
	
	@Before
	public void setup() {
		mockTestedServiceDependencies();
		mockTpsRequestSender();
	}
	
	private void mockTpsRequestSender() {
		
		when(tpsRequestSender.sendTpsRequest(any(), any())).thenAnswer(invocation -> createResponseFromTPS(invocation.getArgumentAt(1, TpsRequestContext.class)));
	}
	
	private TpsServiceRoutineResponse createResponseFromTPS(TpsRequestContext context) {
		switch (context.getEnvironment()) {
			case T1:
				return createResponseFromTPSInEnvT1();
			case T2:
				return createResponseFromTPSInEnvT2();
		}
		return createResponseFromTPSInEnvT1();
	}
	
	private TpsServiceRoutineResponse createResponseFromTPSInEnvT2() {
		TpsServiceRoutineResponse response2 = new TpsServiceRoutineResponse();
		response2.setXml(MQ_xmlResponse_envT2);
		LinkedHashMap linkedHashMap_Response1 = new LinkedHashMap<>();
		linkedHashMap_Response1.put("antallTotalt", 1);
		LinkedHashMap data1 = new LinkedHashMap();
		data1.put("fnr", IDENTER.get(0));
		linkedHashMap_Response1.put("data1", data1);
		response2.setResponse(linkedHashMap_Response1);
		return response2;
	}
	
	private TpsServiceRoutineResponse createResponseFromTPSInEnvT1() {
		TpsServiceRoutineResponse response1 = new TpsServiceRoutineResponse();
		response1.setXml(MQ_xmlResponse_envT1);
		LinkedHashMap linkedHashMap_Response1 = new LinkedHashMap<>();
		linkedHashMap_Response1.put("antallTotalt", 2);
		LinkedHashMap data1 = new LinkedHashMap();
		LinkedHashMap data2 = new LinkedHashMap();
		data1.put("fnr", IDENTER.get(0));
		data2.put("fnr", IDENTER.get(1));
		linkedHashMap_Response1.put("data1", data1);
		linkedHashMap_Response1.put("data2", data2);
		response1.setResponse(linkedHashMap_Response1);
		return response1;
	}
	
	private void mockTestedServiceDependencies() {
		when(getEnvironments.getEnvironmentsFromFasit(any())).thenReturn(ALLE_MILJOER);
		when(filterEnvironmentsOnDeployedEnvironment.execute(any())).thenReturn(ALLE_MILJOER);
		when(userContextHolder.getUser()).thenReturn(user);
		
		TpsServiceRoutineRequest tpsServiceRoutineRequest = new TpsServiceRoutineRequest();
		tpsServiceRoutineRequest.setServiceRutinenavn("FS03-FDLISTER-DISKNAVN-M-TESTDATA");
		when(mappingUtils.convertToTpsServiceRoutineRequest(any(), any())).thenReturn(tpsServiceRoutineRequest);
		
	}
	
	/**
	 * En enhetstest av service-laget.
	 * Testbetingelser:
	 * - HVIS status på en liste med identer blir etterspurt av tjenesten, SÅ skal tjenesten returnere en liste for hver ident,
	 * over de miljøene der identen (fødselsnummeret) er å finne. Dette skal gjøres ved å sende en spørring til TPS.
	 */
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