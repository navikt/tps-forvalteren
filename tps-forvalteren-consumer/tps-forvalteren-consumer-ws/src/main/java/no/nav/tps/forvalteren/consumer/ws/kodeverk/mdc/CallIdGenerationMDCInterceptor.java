package no.nav.tps.forvalteren.consumer.ws.kodeverk.mdc;

import no.nav.modig.common.MDCOperations;
import no.nav.modig.jaxws.handlers.MDCOutHandler;

import javax.xml.ws.handler.soap.SOAPMessageContext;

import static no.nav.modig.common.MDCOperations.MDC_CALL_ID;
import static no.nav.modig.common.MDCOperations.generateCallId;
import static no.nav.modig.common.MDCOperations.putToMDC;

public class CallIdGenerationMDCInterceptor extends MDCOutHandler {

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        putToMDC(MDC_CALL_ID, generateCallId());
        boolean shouldContinueProcessing = super.handleMessage(context);
        MDCOperations.remove(MDC_CALL_ID);
        return shouldContinueProcessing;
    }
}