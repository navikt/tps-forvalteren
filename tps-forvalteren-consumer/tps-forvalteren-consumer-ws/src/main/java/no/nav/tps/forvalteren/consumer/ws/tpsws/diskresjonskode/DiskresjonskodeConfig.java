package no.nav.tps.forvalteren.consumer.ws.tpsws.diskresjonskode;

import no.nav.tjeneste.pip.diskresjonskode.DiskresjonskodePortType;
import no.nav.tps.forvalteren.consumer.ws.kodeverk.config.ConsumerConfigUtil;
import no.nav.tps.forvalteren.consumer.ws.sts.TpsfStsClient;

import javax.xml.namespace.QName;
import org.apache.cxf.frontend.ClientProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiskresjonskodeConfig {

    @Value("${validering.virksomhet.diskresjonskodev1.url}")
    private String diskresjonskodeAddress;

    private static final String DISKRESJONSKODE_WSDL_URL = "wsdl/Diskresjonskode.wsdl";
    private static final QName DISKRESJON_QNAME = new QName("http://nav.no/tjeneste/pip/diskresjonskode/", "Diskresjonskode");

    @Bean
    DiskresjonskodePortType diskresjonskodePortType() {
        return ConsumerConfigUtil.createWsProxy(DiskresjonskodePortType.class, DISKRESJONSKODE_WSDL_URL, DISKRESJON_QNAME, DISKRESJON_QNAME, diskresjonskodeAddress);
    }

    @Bean
    TpsfStsClient cxfStsClientDiskresjonskode(DiskresjonskodePortType diskresjonskodePortType) {
        return new TpsfStsClient(ClientProxy.getClient(diskresjonskodePortType));
    }

}
