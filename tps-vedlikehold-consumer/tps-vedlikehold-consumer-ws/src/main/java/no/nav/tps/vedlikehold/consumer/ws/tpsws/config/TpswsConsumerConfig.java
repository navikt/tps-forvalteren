package no.nav.tps.vedlikehold.consumer.ws.tpsws.config;

import no.nav.modig.core.context.ModigSecurityConstants;
import no.nav.modig.jaxws.handlers.MDCOutHandler;
import no.nav.modig.security.ws.SystemSAMLOutInterceptor;
import no.nav.tjeneste.pip.diskresjonskode.DiskresjonskodePortType;
import no.nav.tjeneste.pip.pipegenansatt.v1.PipEgenAnsattPortType;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.PackageMarker;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.cxf.TimeoutFeature;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.interceptor.StaxOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.message.Message;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for the TPSWS Consumer module
 *
 * @author Tobias Hansen (Visma Consulting AS).
 */
@Configuration
@ComponentScan(basePackageClasses = {
        PackageMarker.class
})
public class TpswsConsumerConfig {
    private final String DISKRESJONSKODE_WSDL_URL = "wsdl/Diskresjonskode.wsdl";

    private final QName DISKRESJON_QNAME = new QName("http://nav.no/tjeneste/pip/diskresjonskode/", "Diskresjonskode");

    private static final String PIP_EGENANSATT_WSDL_URL = "wsdl/no/nav/tjeneste/pip/pipEgenAnsatt/v1/PipEgenAnsatt.wsdl";
    private static final QName PIP_EGENANSATT_SERVICE_NAME = new QName("http://nav.no/tjeneste/pip/pipEgenAnsatt/v1/", "PipEgenAnsatt_v1");
    private static final QName PIP_EGENANSATT_ENDPOINT_NAME = new QName("http://nav.no/tjeneste/pip/pipEgenAnsatt/v1/", "PipEgenAnsatt_v1");

    @Value("${validering.virksomhet.diskresjonskodev1.url}")
    private String diskresjonskodeAddress;

    @Value("${validering.virksomhet.egenansattv1.url}")
    private String egenAnsattAddress;

    @Bean
    public DiskresjonskodePortType diskresjonskodePortType() {
        JaxWsProxyFactoryBean factoryBean = createJaxWsProxyFactoryBean();

        factoryBean.setWsdlURL(DISKRESJONSKODE_WSDL_URL);
        factoryBean.setServiceName(DISKRESJON_QNAME);
        factoryBean.setEndpointName(DISKRESJON_QNAME);
        factoryBean.setAddress(diskresjonskodeAddress);

        SystemSAMLOutInterceptor samlOutInterceptor = new SystemSAMLOutInterceptor();
        factoryBean.getOutInterceptors().add(samlOutInterceptor);

        return factoryBean.create(DiskresjonskodePortType.class);
    }

    @Bean
    public PipEgenAnsattPortType pipEgenAnsattPortType() {
        JaxWsProxyFactoryBean factoryBean = createJaxWsProxyFactoryBean();

        factoryBean.setWsdlURL(PIP_EGENANSATT_WSDL_URL);
        factoryBean.setServiceName(PIP_EGENANSATT_SERVICE_NAME);
        factoryBean.setEndpointName(PIP_EGENANSATT_ENDPOINT_NAME);
        factoryBean.setAddress(egenAnsattAddress);

        factoryBean.getOutInterceptors().add(createSystemUsernameTokenOutInterceptor());

        PipEgenAnsattPortType port = factoryBean.create(PipEgenAnsattPortType.class);
        ((BindingProvider) port).getRequestContext().put(Message.SCHEMA_VALIDATION_ENABLED, "true");
        // Tatt fra inntektskomponentens consumer, usikker på funksjon:
        ((BindingProvider) port).getRequestContext().put(StaxOutInterceptor.FORCE_START_DOCUMENT, "true");

        return port;
    }

    private JaxWsProxyFactoryBean createJaxWsProxyFactoryBean() {
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();

        jaxWsProxyFactoryBean.getFeatures().add(new WSAddressingFeature());
        jaxWsProxyFactoryBean.getFeatures().add(new LoggingFeature());
        jaxWsProxyFactoryBean.getFeatures().add(new TimeoutFeature(20000));
        jaxWsProxyFactoryBean.getHandlers().add(new MDCOutHandler());

        return jaxWsProxyFactoryBean;
    }

    private WSS4JOutInterceptor createSystemUsernameTokenOutInterceptor() {
        Map<String, Object> properties = new HashMap<String, Object>();

        properties.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
        properties.put(WSHandlerConstants.USER, System.getProperty(ModigSecurityConstants.SYSTEMUSER_USERNAME));
        properties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        properties.put(WSHandlerConstants.PW_CALLBACK_REF, new CallbackHandler() {
            @Override
            public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                String password = System.getProperty(ModigSecurityConstants.SYSTEMUSER_PASSWORD);

                WSPasswordCallback passwordCallback = (WSPasswordCallback) callbacks[0];
                passwordCallback.setPassword(password);
            }
        });

        WSS4JOutInterceptor outInterceptor = new WSS4JOutInterceptor(properties);
        return outInterceptor;
    }
}
