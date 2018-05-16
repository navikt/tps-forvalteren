package no.nav.tps.forvalteren.service.command.testdata.skd;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.exceptions.HttpForbiddenException;
import no.nav.tps.forvalteren.service.command.exceptions.HttpInternalServerErrorException;
import no.nav.tps.forvalteren.service.command.tps.SkdMeldingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class SendSkdMeldingTilGitteMiljoer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SendSkdMeldingTilGitteMiljoer.class);
	
	@Autowired
	private SkdMeldingRequest skdMeldingRequest;
	
	@Autowired
	private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;
	
	public Map<String, String> execute(String skdMelding, TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition, Set<String> environments) {
		Set<String> envToCheck = filterEnvironmentsOnDeployedEnvironment.execute(environments);
		Map<String, String> responseSkdMeldinger = new HashMap<>();
		for (String env : envToCheck) {
			String responsMelding = sendSkdMelding(skdMelding, skdRequestMeldingDefinition, env);
			responseSkdMeldinger.put(env, responsMelding);
		}
		return responseSkdMeldinger;
	}
	
	private String sendSkdMelding(String skdMelding, TpsSkdRequestMeldingDefinition skdMeldingDefinition, String environment) {
		try {
			return skdMeldingRequest.execute(skdMelding, skdMeldingDefinition, environment);
		} catch (JMSException jmsException) {
			LOGGER.error(jmsException.getMessage(), jmsException);
			throw new HttpInternalServerErrorException(jmsException, "api/v1/testdata/saveTPS");
		} catch (HttpForbiddenException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new HttpForbiddenException(ex, "api/v1/testdata/saveTPS" + "skdInnvandring");
		}
	}
}
