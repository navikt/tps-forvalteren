package no.nav.tps.forvalteren.consumer.ws.sts;

import static org.apache.cxf.message.Message.ENDPOINT_ADDRESS;

import java.util.HashMap;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.xml.namespace.QName;
import org.apache.cxf.BusException;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.EndpointException;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.apache.cxf.ws.policy.EndpointPolicy;
import org.apache.cxf.ws.policy.PolicyBuilder;
import org.apache.cxf.ws.policy.PolicyEngine;
import org.apache.cxf.ws.policy.attachment.reference.ReferenceResolver;
import org.apache.cxf.ws.policy.attachment.reference.RemoteReferenceResolver;
import org.apache.cxf.ws.security.tokenstore.SecurityToken;
import org.apache.cxf.ws.security.tokenstore.TokenStore;
import org.apache.cxf.ws.security.tokenstore.TokenStoreFactory;
import org.apache.cxf.ws.security.trust.STSClient;
import org.apache.neethi.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class TpsfStsClient extends STSClient {

    private static final Logger logger = LoggerFactory.getLogger(TpsfStsClient.class);

    private TokenStore stsTokenStore;
    private Client clientTpsf;

    @Value("${srvtps.forvalteren.username}")
    private String srvtpsBrukernavn;

    @Value("${srvtps.forvalteren.password}")
    private String srvtpsPassord;

    @Value("${securitytokenservice.url}")
    private String securityTokenUrl;

    public TpsfStsClient(Client client) {
        super(client.getBus());
        this.clientTpsf = client;
    }

    @PostConstruct
    public void configure() throws EndpointException, BusException {
        configureStsForSystemUser();
        configureStsPolicy();
        disableCNCheckIfConfigured();
        HashMap<String, Object> stsProperties = new HashMap<>();
        stsProperties.put("security.username", srvtpsBrukernavn);
        stsProperties.put("security.password", srvtpsPassord);
        getClient().getRequestContext().put(ENDPOINT_ADDRESS, securityTokenUrl);
        setProperties(stsProperties);

        if (logger.isInfoEnabled()) {
            logger.info("Tjeneste etablert med endepunkt: {}", securityTokenUrl);
        }
    }

    @Override
    public SecurityToken requestSecurityToken(String appliesTo, String action, String requestType, String binaryExchange) throws Exception {
        String key = "systemSAML";
        createTokenStoreIfNotExist();
        SecurityToken token = stsTokenStore.getToken(key);

        if (token == null) {
            token = super.requestSecurityToken(appliesTo, action, requestType, binaryExchange);
            stsTokenStore.add(key, token);
        }

        return token;
    }

    @Override
    protected boolean useSecondaryParameters() {
        return false;
    }

    private void createTokenStoreIfNotExist() {
        if (stsTokenStore == null) {
            stsTokenStore = TokenStoreFactory.newInstance().newTokenStore("org.apache.cxf.ws.security.tokenstore.TokenStore", this.message);
        }
    }

    private void configureStsForSystemUser() {
        (new WSAddressingFeature()).initialize(clientTpsf, clientTpsf.getBus());
        clientTpsf.getRequestContext().put("ws-security.sts.client", this);
        setServiceQName(new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512/wsdl", "SecurityTokenServiceProvider"));
        setEndpointQName(new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512/wsdl", "SecurityTokenServiceSOAP"));
        setWsdlLocation("classpath:wsdl/ws-trust-1.4-service.wsdl");
        setEnableAppliesTo(false);
        setAllowRenewing(false);
    }

    private void configureStsPolicy() {
        PolicyBuilder policyBuilder = clientTpsf.getBus().getExtension(PolicyBuilder.class);
        ReferenceResolver resolver = new RemoteReferenceResolver("", policyBuilder);
        Policy stsPolicy = resolver.resolveReference("classpath:policies/stspolicy.xml");
        PolicyEngine policyEngine = clientTpsf.getBus().getExtension(PolicyEngine.class);
        EndpointPolicy endpointPolicy = policyEngine.getClientEndpointPolicy(clientTpsf.getEndpoint().getEndpointInfo(), clientTpsf.getConduit(), null);
        policyEngine.setClientEndpointPolicy(clientTpsf.getEndpoint().getEndpointInfo(), endpointPolicy.updatePolicy(stsPolicy, null));
    }

    private void disableCNCheckIfConfigured() {
        HTTPConduit httpConduit = (HTTPConduit) clientTpsf.getConduit();
        TLSClientParameters tlsClientParameters = Optional.ofNullable(httpConduit.getTlsClientParameters()).orElseGet(TLSClientParameters::new);
        if (Boolean.valueOf(System.getProperty("disable.ssl.cn.check", "false"))) {
            tlsClientParameters.setDisableCNCheck(true);
        }
        httpConduit.setTlsClientParameters(tlsClientParameters);
    }
}
