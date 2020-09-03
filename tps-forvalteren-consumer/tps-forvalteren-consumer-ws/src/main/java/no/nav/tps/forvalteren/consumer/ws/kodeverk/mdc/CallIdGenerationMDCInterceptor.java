package no.nav.tps.forvalteren.consumer.ws.kodeverk.mdc;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.slf4j.MDC;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CallIdGenerationMDCInterceptor implements SOAPHandler<SOAPMessageContext> {

    private static final String MDC_CALL_ID = "callId";
    private static final QName CALLID_QNAME = new QName("uri:no.nav.applikasjonsrammeverk", "callId");
    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        MDC.put(MDC_CALL_ID, generateCallId());
        boolean shouldContinueProcessing = handleThisMessage(context);

        MDC.remove(MDC_CALL_ID);
        return shouldContinueProcessing;
    }

    @Override
    public boolean handleFault(SOAPMessageContext soapMessageContext) {
        return true;
    }

    @Override
    public void close(MessageContext messageContext) {

    }

    @Override
    public Set<QName> getHeaders() {
        return new HashSet<QName>() {
            {
                this.add(CALLID_QNAME);
            }
        };
    }

    private boolean handleThisMessage(SOAPMessageContext context) {
        Boolean outbound = (Boolean)context.get("javax.xml.ws.handler.message.outbound");
        if (outbound) {
            String callId = MDC.get(MDC_CALL_ID);
            if (isBlank(callId)) {
                throw new RuntimeException("CallId skal være tilgjengelig i MDC på dette tidspunkt. Om du er en webapp, må du legge til et MDCFilter i web.xml (oppskrift på dette: http://confluence.adeo.no/display/Modernisering/MDCFilter). Om du er noe annet må du generere callId selv og legge på MDC. Hjelpemetoder finnes i no.nav.modig.common.MDCOperations.");
            }

            log.debug("Add the callId to the SOAP message: " + callId);

            try {
                SOAPEnvelope envelope = context.getMessage().getSOAPPart().getEnvelope();
                SOAPHeader header = envelope.getHeader();
                SOAPElement callIdElement = header.addChildElement(CALLID_QNAME);
                callIdElement.setValue(callId);
            } catch (SOAPException var7) {
                log.error(var7.getMessage());
                throw new ProtocolException(var7);
            }
        }

        return true;
    }

    public static String generateCallId() {

        return new StringBuilder()
                .append("CallId_")
                .append(System.currentTimeMillis())
                .append('_')
                .append(secureRandom.nextInt(2147483647))
                .toString();
    }
}