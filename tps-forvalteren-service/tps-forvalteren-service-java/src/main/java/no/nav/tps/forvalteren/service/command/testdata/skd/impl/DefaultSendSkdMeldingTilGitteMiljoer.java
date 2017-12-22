package no.nav.tps.forvalteren.service.command.testdata.skd.impl;

import java.util.Set;
import javax.jms.JMSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.exceptions.HttpForbiddenException;
import no.nav.tps.forvalteren.service.command.exceptions.HttpInternalServerErrorException;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.service.command.tps.SkdMeldingRequest;

@Service
public class DefaultSendSkdMeldingTilGitteMiljoer implements SendSkdMeldingTilGitteMiljoer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSendSkdMeldingTilGitteMiljoer.class);

    @Autowired
    private SkdMeldingRequest skdMeldingRequest;

    @Autowired
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;

    public void execute(String skdMelding, TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition, Set<String> environments) {
        Set<String> envToCheck = filterEnvironmentsOnDeployedEnvironment.execute(environments);

        for (String env : envToCheck) {
            sendSkdMelding(skdMelding, skdRequestMeldingDefinition, env);
        }
    }

    private void sendSkdMelding(String skdMelding, TpsSkdRequestMeldingDefinition skdMeldingDefinition, String environment) {
        try {
            skdMeldingRequest.execute(skdMelding, skdMeldingDefinition, environment);
        } catch (JMSException jmsException) {
            LOGGER.error(jmsException.getMessage(), jmsException);
            throw new HttpInternalServerErrorException(jmsException, "api/v1/testdata/saveTPS");
        } catch (HttpForbiddenException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new HttpForbiddenException(ex, "api/v1/testdata/saveTPS" + "skdInnvandring");
        }
    }
}
