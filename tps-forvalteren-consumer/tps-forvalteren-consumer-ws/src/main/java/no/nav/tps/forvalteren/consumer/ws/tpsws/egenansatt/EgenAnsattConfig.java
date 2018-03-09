package no.nav.tps.forvalteren.consumer.ws.tpsws.egenansatt;

import no.nav.tjeneste.pip.egenansatt.v1.binding.EgenAnsattV1;
import no.nav.tps.forvalteren.consumer.ws.kodeverk.config.ConsumerConfigUtil;
import no.nav.tps.forvalteren.consumer.ws.sts.TpsfStsClient;

import javax.xml.namespace.QName;
import org.apache.cxf.frontend.ClientProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EgenAnsattConfig {

    @Value("${validering.virksomhet.egenansattv1.url}")
    private String egenAnsattAddress;

    private static final String PIP_EGENANSATT_WSDL_URL     = "wsdl/no/nav/tjeneste/pip/EgenAnsatt/v1/egenAnsatt.wsdl";
    private static final QName PIP_EGENANSATT_SERVICE_NAME  = new QName("http://nav.no/tjeneste/pip/egenAnsatt/v1/", "EgenAnsatt_v1Port");

    @Bean
    EgenAnsattV1 egenAnsatt() {
        return ConsumerConfigUtil.createWsProxy(EgenAnsattV1.class, PIP_EGENANSATT_WSDL_URL, PIP_EGENANSATT_SERVICE_NAME, PIP_EGENANSATT_SERVICE_NAME, egenAnsattAddress);
    }

    @Bean
    TpsfStsClient cxfStsClientEgenAnsatt(EgenAnsattV1 egenAnsatt) {
        return new TpsfStsClient(ClientProxy.getClient(egenAnsatt));
    }
}
