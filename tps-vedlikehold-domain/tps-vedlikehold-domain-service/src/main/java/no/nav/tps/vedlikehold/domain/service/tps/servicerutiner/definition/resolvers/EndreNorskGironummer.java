package no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.resolvers;

import no.nav.tps.vedlikehold.domain.service.tps.TpsParameterType;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.endring.TpsEndreNorskGironummerEndringsmeldingRequest;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.DiskresjonskodeAuthorisation;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.EgenAnsattAuthorisation;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.response.ResponseDataTransformer;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.response.ResponseStatusTransformer;

import static no.nav.tps.vedlikehold.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS;
import static no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.request.EndringsmeldingRequestTransform.endringsmeldingXmlWrappingAppender;

/**
 * Created by F148888 on 16.11.2016.
 */
public class EndreNorskGironummer implements ServiceRoutineResolver {

    @Override
    public TpsServiceRoutineDefinition resolve() {
        return TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine()
                    .name("EndreNorskGironummer")
                    .internalName("Endre: Norsk Gironummer")
                    .javaClass(TpsEndreNorskGironummerEndringsmeldingRequest.class)
                .config()
                    .requestQueue(REQUEST_QUEUE_ENDRINGSMELDING_ALIAS)
                .and()

                .transformer()
                    .preSend(endringsmeldingXmlWrappingAppender())
                .postSend(ResponseDataTransformer.extractDataFromXmlElement("endreGironrNorsk"))
                .postSend(ResponseStatusTransformer.extractStatusFromXmlElement("svarStatus"))

                .and()


                .parameter()
                .name("offentligIdent")
                .required()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("giroNrNorsk")
                .required()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("datogiroNrNorsk")
                .required()
                .type(TpsParameterType.DATE)
                .and()

                .parameter()
                .name("kilde")
                .required()
                .type(TpsParameterType.STRING)
                .values("FS22")

                .and()

                .securityBuilder()
                .addRequiredSearchAuthorisationStrategy(DiskresjonskodeAuthorisation.diskresjonskodeAuthorisation())
                .addRequiredSearchAuthorisationStrategy(EgenAnsattAuthorisation.egenAnsattAuthorisation())
                .addSecurity()

                .build();
    }
}
