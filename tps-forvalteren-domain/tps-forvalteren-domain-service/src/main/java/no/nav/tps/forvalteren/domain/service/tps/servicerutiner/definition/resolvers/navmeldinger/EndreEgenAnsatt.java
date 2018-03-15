package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger;

import no.nav.tps.forvalteren.domain.service.tps.TpsParameterType;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.DiskresjonskodeServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.EgenAnsattServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ReadServiceRutineAuthorisation;
import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.ServiceRoutineResolver;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreEgenansattRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.request.EndringsmeldingRequestTransform;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseDataTransformer;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseStatusTransformer;

public class EndreEgenAnsatt implements ServiceRoutineResolver {

    @Override
    public TpsServiceRoutineDefinitionRequest resolve() {
        return TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine()
                .name("endre_egen_ansatt")
                .internalName("Endre Egen Ansatt")
                .javaClass(TpsEndreEgenansattRequest.class)
                .config()
                .requestQueue(REQUEST_QUEUE_ENDRINGSMELDING_ALIAS)
                .and()

                .parameter()
                .name("offentligIdent")
                .required()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("fom")
                .required()
                .type(TpsParameterType.DATE)
                .and()

                .parameter()
                .name("tom")
                .optional()
                .type(TpsParameterType.DATE)
                .and()

                .transformer()
                .preSend(EndringsmeldingRequestTransform.endringsmeldingXmlWrappingAppender())
                //.postSend(ResponseDataTransformer.extractDataFromXmlElement(""))
                .postSend(ResponseStatusTransformer.extractStatusFromXmlElement("svarStatus"))
                .and()

                .securityBuilder()
                .addRequiredSearchAuthorisationStrategy(DiskresjonskodeServiceRutineAuthorisation.diskresjonskodeAuthorisation())
                .addRequiredSearchAuthorisationStrategy(EgenAnsattServiceRutineAuthorisation.egenAnsattAuthorisation())
                .addRequiredSearchAuthorisationStrategy(ReadServiceRutineAuthorisation.readAuthorisation())
                .addSecurity()

                .build();
    }
}
