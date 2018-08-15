package no.nav.tps.forvalteren.service.command.testdata.skd.impl;

import javax.jms.JMSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.exceptions.HttpForbiddenException;
import no.nav.tps.forvalteren.service.command.exceptions.HttpInternalServerErrorException;
import no.nav.tps.forvalteren.service.command.tps.SkdMeldingMQConsumer;

@Service
public class SendEnSkdMelding {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SendEnSkdMelding.class);
    
    @Autowired
    private SkdMeldingMQConsumer skdMeldingMQConsumer;
    
    public String sendSkdMelding(String skdMelding, TpsSkdRequestMeldingDefinition skdMeldingDefinition, String environment) {
        try {
            return skdMeldingMQConsumer.sendMessage(skdMelding, skdMeldingDefinition, environment);
        } catch (JMSException jmsException) {
            LOGGER.error(jmsException.getMessage(), jmsException);
            throw new HttpInternalServerErrorException(jmsException, "api/v1/testdata/saveTPS");
        } catch (HttpForbiddenException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new HttpForbiddenException(ex, "api/v1/testdata/saveTPS" + "skdInnvandring");
        }
    }
}
