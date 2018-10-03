package no.nav.tps.forvalteren.service.command.exceptions;

import no.nav.tps.forvalteren.domain.service.TpsStatusKoder;
import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;

public final class ExceptionTpsServiceRutineMessageCreator {

    private ExceptionTpsServiceRutineMessageCreator() {
    }

    public static String execute(ResponseStatus status) {
        return new StringBuilder()
                .append("{Kode=")
                .append(status.getKode())
                .append(" - ")
                .append(TpsStatusKoder.getNameByKode(status.getKode()))
                .append("}")
                .append("{Melding=")
                .append(status.getMelding())
                .append("}")
                .append("{UtfyllendeMelding=")
                .append(status.getUtfyllendeMelding())
                .append("}")
                .toString();
    }
}
