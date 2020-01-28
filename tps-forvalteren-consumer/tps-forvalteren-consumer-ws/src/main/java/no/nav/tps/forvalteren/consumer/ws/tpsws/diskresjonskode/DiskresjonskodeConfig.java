package no.nav.tps.forvalteren.consumer.ws.tpsws.diskresjonskode;

import javax.xml.namespace.QName;
import org.apache.cxf.frontend.ClientProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import no.nav.tjeneste.pip.diskresjonskode.binding.DiskresjonskodePortType;
import no.nav.tps.forvalteren.consumer.ws.kodeverk.config.ConsumerConfigUtil;
import no.nav.tps.forvalteren.consumer.ws.sts.TpsfStsClient;

@Configuration
public class DiskresjonskodeConfig {

    private static final Logger logger = LoggerFactory.getLogger(DiskresjonskodeConfig.class);

    @Value("${virksomhet.diskresjonskode.v1.endpointurl}")
    private String diskresjonskodeAddress;

    private static final String DISKRESJON_WSDL_URL = "wsdl/no/nav/tjeneste/pip/Diskresjonskode/Diskresjonskode.wsdl";
    private static final QName DISKRESJON_QNAME = new QName("http://nav.no/tjeneste/pip/diskresjonskode/", "DiskresjonskodePortTypePort");

    @Bean
    DiskresjonskodePortType diskresjonskodePortType() {
        if (logger.isInfoEnabled()) {
            logger.info("Tjeneste etablert med endepunkt: {}", diskresjonskodeAddress);
        }
        return ConsumerConfigUtil.createWsProxy(DiskresjonskodePortType.class, DISKRESJON_WSDL_URL, DISKRESJON_QNAME, DISKRESJON_QNAME, diskresjonskodeAddress);
    }

    @Bean
    TpsfStsClient cxfStsClientDiskresjonskode(DiskresjonskodePortType diskresjonskode) {
        return new TpsfStsClient(ClientProxy.getClient(diskresjonskode));
    }

    @Bean
    DiskresjonskodeConsumer diskresjonskodeConsumer() {
        return new DefaultDiskresjonskodeConsumer();
    }
}
