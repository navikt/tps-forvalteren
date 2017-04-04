package no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.resolvers;

import no.nav.tps.vedlikehold.domain.service.tps.TpsParameterType;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.endring.TpsEndreTiadEndringsmeldingRequest;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.DiskresjonskodeAuthorisation;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.EgenAnsattAuthorisation;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.response.ResponseDataTransformer;

import static no.nav.tps.vedlikehold.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS;
import static no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.request.EndringsmeldingRequestTransform.endringsmeldingXmlWrappingAppender;
import static no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.response.ResponseStatusTransformer.extractStatusFromXmlElement;


@SuppressWarnings("squid:S138")
public class EndreTiad implements ServiceRoutineResolver{

    @Override
    public TpsServiceRoutineDefinition resolve() {
        return TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine()
                .name("EndreTiad")
                .internalName("Endre: TIAD")
                .javaClass(TpsEndreTiadEndringsmeldingRequest.class)
                .config()
                    .requestQueue(REQUEST_QUEUE_ENDRINGSMELDING_ALIAS)
                .and()

                .transformer()
                    .preSend(endringsmeldingXmlWrappingAppender())
                    .postSend(ResponseDataTransformer.extractDataFromXmlElement("nyAdresseNavNorge"))
                    .postSend(extractStatusFromXmlElement("svarStatus"))
                .and()

                .parameter()
                    .name("offentligIdent")
                    .required()
                    .type(TpsParameterType.STRING)
                .and()

                .parameter()
                    .name("datoTom")
                    .optional()
                    .type(TpsParameterType.DATE)
                .and()

                .parameter()
                    .name("typeAdresseNavNorge")
                    .required()
                    .type(TpsParameterType.STRING)
                .and()

                .parameter()
                    .name("typeTilleggslinje")
                    .optional()
                    .type(TpsParameterType.STRING)
                .and()

                .parameter()
                    .name("tilleggslinje")
                    .optional()
                    .type(TpsParameterType.STRING)
                .and()

                .parameter()
                    .name("kommunenrTiad")
                    .optional()
                    .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("gatekode")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("gatenavn")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("husnr")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("postnr")
                .required()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("husbokstav")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("bolignr")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("eiendomsnavn")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("postboksnr")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("postboksAnlegg")
                .optional()
                .type(TpsParameterType.STRING)
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
