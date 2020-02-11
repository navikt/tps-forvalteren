package no.nav.tps.forvalteren.consumer.ws.tpsws.egenansatt;

import javax.xml.namespace.QName;
import org.apache.cxf.frontend.ClientProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import no.nav.tjeneste.pip.egenansatt.v1.binding.EgenAnsattV1;
import no.nav.tps.forvalteren.consumer.ws.kodeverk.config.ConsumerConfigUtil;
import no.nav.tps.forvalteren.consumer.ws.sts.TpsfStsClient;

@Configuration
public class EgenAnsattConfig {

    private static final Logger logger = LoggerFactory.getLogger(EgenAnsattConfig.class);

    @Value("${virksomhet.egenansatt.v1.endpointurl}")
    private String egenAnsattAddress;

    private static final String PIP_EGENANSATT_WSDL_URL = "wsdl/no/nav/tjeneste/pip/EgenAnsatt/v1/egenAnsatt.wsdl";
    private static final QName PIP_EGENANSATT_SERVICE_NAME = new QName("http://nav.no/tjeneste/pip/egenAnsatt/v1/", "EgenAnsatt_v1Port");

    @Bean
    public EgenAnsattV1 egenAnsatt() {
        if (logger.isInfoEnabled()) {
            logger.info("Tjeneste etablert med endepunkt: {}", egenAnsattAddress);
        }
        return ConsumerConfigUtil.createWsProxy(EgenAnsattV1.class, PIP_EGENANSATT_WSDL_URL, PIP_EGENANSATT_SERVICE_NAME, PIP_EGENANSATT_SERVICE_NAME, egenAnsattAddress);
    }

    @Bean
    public TpsfStsClient cxfStsClientEgenAnsatt(EgenAnsattV1 egenAnsatt) {
        return new TpsfStsClient(ClientProxy.getClient(egenAnsatt));
    }

    @Bean
    public EgenAnsattConsumer egenAnsattConsumer() {
        return new DefaultEgenAnsattConsumer();
    }
}