package no.nav.tps.forvalteren.service.command.tps.transformation.request;

import no.nav.tps.forvalteren.service.command.tps.transformation.TransformStrategy;
import no.nav.tps.forvalteren.domain.service.tps.Request;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.Transformer;

public interface RequestTransformStrategy extends TransformStrategy {
    void execute(Request request, Transformer transformer);
}
