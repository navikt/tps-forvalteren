package no.nav.tps.forvalteren.consumer.ws.tpsws.egenansatt;

import no.nav.modig.core.context.ModigSecurityConstants;
import no.nav.modig.jaxws.handlers.MDCOutHandler;
import no.nav.tjeneste.pip.pipegenansatt.v1.PipEgenAnsattPortType;
import no.nav.tps.forvalteren.consumer.ws.tpsws.cxf.TimeoutFeature;

import javax.security.auth.callback.CallbackHandler;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.util.HashMap;
import java.util.Map;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.interceptor.StaxOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.message.Message;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EgenAnsattConfig {

    @Value("${validering.virksomhet.egenansattv1.url}")
    private String egenAnsattAddress;

    @Value("${TPS_FORVALTEREN_USERNAME_TOKERN_USERNAME}")
    private String srvtpsBrukernavn;

    @Value("${TPS_FORVALTEREN_USERNAME_TOKERN_PASSWORD}")
    private String srvtpsPassowrd;

    private static final String PIP_EGENANSATT_WSDL_URL     = "wsdl/no/nav/tjeneste/pip/pipEgenAnsatt/v1/PipEgenAnsatt.wsdl";
    private static final QName PIP_EGENANSATT_SERVICE_NAME  = new QName("http://nav.no/tjeneste/pip/pipEgenAnsatt/v1/", "PipEgenAnsatt_v1");
    private static final QName PIP_EGENANSATT_ENDPOINT_NAME = new QName("http://nav.no/tjeneste/pip/pipEgenAnsatt/v1/", "PipEgenAnsatt_v1");

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

            System.setProperty(ModigSecurityConstants.SYSTEMUSER_USERNAME, srvtpsBrukernavn);
            System.setProperty(ModigSecurityConstants.SYSTEMUSER_PASSWORD, srvtpsPassowrd);

            properties.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
            properties.put(WSHandlerConstants.USER, System.getProperty(ModigSecurityConstants.SYSTEMUSER_USERNAME));
            properties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
            properties.put(WSHandlerConstants.PW_CALLBACK_REF, (CallbackHandler) callbacks -> {
                String password = System.getProperty(ModigSecurityConstants.SYSTEMUSER_PASSWORD);

                WSPasswordCallback passwordCallback = (WSPasswordCallback) callbacks[0];
                passwordCallback.setPassword(password);
            });

            return new WSS4JOutInterceptor(properties);
        }
}
