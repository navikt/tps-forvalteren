package no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MeldingOmDubletterSkdParametere implements SkdParametersCreator {

    public static SkdParametersCreator meldingOmDubletterParameterCreator() {
        return new MeldingOmDubletterSkdParametere();
    }
}
