package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.service.command.testdata.utils.ExtractDataFromTpsServiceRoutineResponse.trekkUtIdenterFraResponse;
import static no.nav.tps.forvalteren.service.command.testdata.utils.TpsRequestParameterCreator.opprettParametereForM201TpsRequest;

import no.nav.tps.forvalteren.domain.rs.RsTpsStatusPaaIdenterResponse;
import no.nav.tps.forvalteren.domain.rs.TpsStatusPaaIdent;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsRequestMappingUtils;
import no.nav.tps.forvalteren.service.command.tpsconfig.GetEnvironments;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Denne tjenesten returnerer hvilke miljøer som de ulike fødselsnumrene eksisterer i.
 *
 * @author Jarl Øystein Samseth, Visma Consulting
 */
@Service
public class StatusPaaIdenterITps {
	
	@Autowired
	private GetEnvironments getEnvironments;
	
	TpsRequestContext context = new TpsRequestContext();
	@Autowired
	private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;
	@Autowired
	private UserContextHolder userContextHolder;
	@Autowired
	private TpsRequestSender tpsRequestSender;
	
	@Autowired
	private RsTpsRequestMappingUtils mappingUtils;
	
	
	public RsTpsStatusPaaIdenterResponse hentStatusPaaIdenterIAlleMiljoer(List<String> identer) {
		context.setUser(userContextHolder.getUser());
		List<TpsStatusPaaIdent> tpsStatusPaaIdentList = new ArrayList<>();
		Map<String, Object> tpsRequestParameters = opprettParametereForM201TpsRequest(identer, "A0");
		for (String ident : identer) {
			TpsStatusPaaIdent statusPaaIdent = new TpsStatusPaaIdent();
			statusPaaIdent.setIdent(ident);
			tpsStatusPaaIdentList.add(statusPaaIdent);
		}
		RsTpsStatusPaaIdenterResponse tpsStatusPaaIdenterResponse = new RsTpsStatusPaaIdenterResponse(tpsStatusPaaIdentList);
		settMiljoerDerIdenteneEksisterer(tpsStatusPaaIdenterResponse, tpsRequestParameters);
		tpsStatusPaaIdenterResponse.getStatusPaaIdenter()
				.forEach(tpsStatusPaaIdent -> Collections.sort(tpsStatusPaaIdent.getEnv()));
		return tpsStatusPaaIdenterResponse;
	}
	
	private void settMiljoerDerIdenteneEksisterer(RsTpsStatusPaaIdenterResponse tpsStatusPaaIdenterResponse, Map<String, Object> tpsRequestParameters) {
		Set<String> environmentsToCheck = filterEnvironmentsOnDeployedEnvironment.execute(getEnvironments.getEnvironmentsFromFasit("tpsws"));
		
		for (String env : environmentsToCheck) {
			List<String> identerIMiljoet = finnIdenteneIMiljoet(env, tpsRequestParameters);
			tpsStatusPaaIdenterResponse.addEnvToTheseIdents(env, identerIMiljoet);
		}
	}
	
	private List<String> finnIdenteneIMiljoet(String env, Map<String, Object> tpsRequestParameters) {
		context.setEnvironment(env);
		TpsServiceRoutineRequest tpsServiceRoutineRequest = mappingUtils.convertToTpsServiceRoutineRequest(String.valueOf(tpsRequestParameters
				.get("serviceRutinenavn")), tpsRequestParameters);
		TpsServiceRoutineResponse tpsResponse = tpsRequestSender.sendTpsRequest(tpsServiceRoutineRequest, context);
		List<String> identeneSomEksistererIMiljoet = new ArrayList<>(trekkUtIdenterFraResponse(tpsResponse));
		return identeneSomEksistererIMiljoet;
	}
	
}


