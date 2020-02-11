package no.nav.tps.forvalteren.consumer.ws.kodeverk.config;

import java.net.URL;
import javax.xml.namespace.QName;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;

import no.nav.tps.forvalteren.consumer.ws.kodeverk.mdc.CallIdGenerationMDCInterceptor;

public class ConsumerConfigUtil {

    private ConsumerConfigUtil() {
    }

    private static final long DEFAULT_TIMEOUT = 20000;

    public static <T> T createWsProxy(Class<T> serviceClass, String wsdlUrl, QName serviceName, QName portName, String endpointAddress) {
        return createWsProxy(serviceClass, wsdlUrl, serviceName, portName, endpointAddress, DEFAULT_TIMEOUT);
    }

    public static <T> T createWsProxy(Class<T> serviceClass, String wsdlUrl, QName serviceName, QName portName, String endpointAddress, long timeout) {
        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setWsdlURL(getUrlFromClasspathResource(wsdlUrl));
        factoryBean.setServiceName(serviceName);
        factoryBean.setEndpointName(portName);
        factoryBean.setServiceClass(serviceClass);
        factoryBean.setAddress(endpointAddress);
        factoryBean.getFeatures().add(new WSAddressingFeature());
        factoryBean.getFeatures().add(new TimeoutFeature(timeout));
        factoryBean.getHandlers().add(new CallIdGenerationMDCInterceptor());

        return factoryBean.create(serviceClass);
    }

    private static String getUrlFromClasspathResource(String classpathResource) {
        URL url = ConsumerConfigUtil.class.getClassLoader().getResource(classpathResource);
        if (url != null) {
            return url.toString();
        }
        throw new IllegalStateException("Failed to find resource: " + classpathResource);
    }
}
