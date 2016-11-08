package no.nav.tps.vedlikehold.service.command.tps.transformation.request;

import no.nav.tps.vedlikehold.domain.service.command.tps.Request;
import no.nav.tps.vedlikehold.service.command.tps.transformation.TransformStrategy;

public interface RequestTransformStrategy extends TransformStrategy {
    void execute(Request request);
}
