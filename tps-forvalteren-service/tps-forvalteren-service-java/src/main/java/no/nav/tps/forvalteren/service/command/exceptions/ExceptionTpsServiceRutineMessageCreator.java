package no.nav.tps.forvalteren.service.command.exceptions;

import no.nav.tps.forvalteren.domain.service.TpsStatusKoder;
import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;

public class ExceptionTpsServiceRutineMessageCreator {

    public static String execute(ResponseStatus status) {
        StringBuilder sb = new StringBuilder();
        sb.append("{Kode=").append(status.getKode()).append(" - ").append(TpsStatusKoder.getNameByKode(status.getKode())).append("}      ");
        sb.append("{Melding=").append(status.getMelding()).append("}      ");
        sb.append("{UtfyllendeMelding=").append(status.getUtfyllendeMelding()).append("}");

        return sb.toString();
    }
}
