package no.nav.tps.forvalteren.consumer.ws.kodeverk.config;

import javax.xml.namespace.QName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import no.nav.tjeneste.virksomhet.kodeverk.v2.KodeverkPortType;
import no.nav.tps.forvalteren.consumer.ws.kodeverk.KodeverkConsumer;

@Configuration
@ComponentScan(basePackageClasses = KodeverkConsumer.class)
public class KodeverkConsumerConfig {

    private static final QName SERVICE = new QName("http://nav.no/tjeneste/virksomhet/kodeverk/v2/", "Kodeverk_v2");
    private static final String WSDL_URL = "wsdl/no/nav/tjeneste/virksomhet/kodeverk/v2/Kodeverk.wsdl";

    @Value("${virksomhet.kodeverk.v2.endpointurl}")
    private String kodeverkEndpointUrl;

    @Bean
    KodeverkPortType kodeverkWs() {
        return ConsumerConfigUtil.createWsProxy(KodeverkPortType.class, WSDL_URL, SERVICE, SERVICE, kodeverkEndpointUrl);
    }
}
