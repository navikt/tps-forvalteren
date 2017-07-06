package no.nav.tps.forvalteren.service.command.testdata.skd.impl;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.exceptions.HttpForbiddenException;
import no.nav.tps.forvalteren.service.command.exceptions.HttpInternalServerErrorException;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.service.command.tps.SkdMeldingRequest;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.GetTpsSkdmeldingService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.util.Set;

@Service
public class DefaultSendSkdMeldingTilGitteMiljoer implements SendSkdMeldingTilGitteMiljoer {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DefaultSendSkdMeldingTilGitteMiljoer.class);

    @Autowired
    private SkdMeldingRequest skdMeldingRequest;

    @Autowired
    private GetTpsSkdmeldingService getTpsSkdmeldingService;

    @Autowired
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;

    public void execute(String skdMelding, Set<String> environments){
        //TODO naa er det bare innvandring. Set parameter strategy her?
        //TODO Lag parameter strategy. Og execute strategy.
        TpsSkdRequestMeldingDefinition skdMeldingDefinition = getTpsSkdmeldingService.execute().get(0);  // For now only 1 SkdMeldingDefinition

        Set<String> envToCheck = filterEnvironmentsOnDeployedEnvironment.execute(environments);

        for(String env : envToCheck){
            sendSkdMelding(skdMelding, skdMeldingDefinition, env);
        }
    }

    private void sendSkdMelding(String skdMelding, TpsSkdRequestMeldingDefinition skdMeldingDefinition, String environment){
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