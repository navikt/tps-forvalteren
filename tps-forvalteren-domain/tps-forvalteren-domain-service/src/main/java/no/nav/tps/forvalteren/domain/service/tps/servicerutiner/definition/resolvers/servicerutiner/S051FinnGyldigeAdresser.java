package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner;

import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;

import no.nav.tps.forvalteren.domain.service.tps.TpsParameterType;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.DiskresjonskodeServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.EgenAnsattServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ReadServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsFinnGyldigeAdresserRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.request.ServiceRoutineAdresseDataRequestTransform;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseDataTransformer;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseStatusTransformer;

public class S051FinnGyldigeAdresser implements ServiceRoutineResolver {
    public static final String FINN_GYLDIGE_ADRESSER_SERVICERUTINE_NAVN = "FS03-ADRSNAVN-ADRSDATA-O";
    
    @Override public TpsServiceRoutineDefinitionRequest resolve() {
        return TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine()
                .name(FINN_GYLDIGE_ADRESSER_SERVICERUTINE_NAVN)
                .internalName("Finn gyldige adresser")
                .javaClass(TpsFinnGyldigeAdresserRequest.class)
                .config()
                .requestQueue(REQUEST_QUEUE_SERVICE_RUTINE_ALIAS)
                .and()
                
                .parameter()
                .name("aksjonsKode")
                .required()
                .type(TpsParameterType.STRING)
                .value("A")
                .and()
                .parameter()
                .name("aksjonsKode2")
                .required()
                .type(TpsParameterType.STRING)
                .value("0")
                .and()
                
                    .parameter()
                .name("adresseNavnsok")
                .required()
                .type(TpsParameterType.STRING)
                .and()
                    .parameter()
                .name("typesok")
                .required()
                .type(TpsParameterType.STRING)
                .values("F","E","T")
                .and()
                    .parameter()
                .name("husNrsok")
                .optional()
                .type(TpsParameterType.STRING)
                .and()
                    .parameter()
                .name("kommuneNrsok")
                .optional()
                .type(TpsParameterType.STRING)
                .and()
                    .parameter()
                .name("postNrsok")
                .optional()
                .type(TpsParameterType.STRING)
                .and()
                    .parameter()
                .name("maxRetur")
                .optional()
                .type(TpsParameterType.INT)
                .and()
                    .parameter()
                .name("alltidRetur")
                .required()
                .type(TpsParameterType.STRING)
                .values("J","N")
                .and()
                    .parameter()
                .name("alleSkrivevarianter")
                .required()
                .type(TpsParameterType.STRING)
                .values("J","N")
                .and()
                    .parameter()
                .name("visPostnr")
                .optional()
                .type(TpsParameterType.STRING)
                .values("J","N")
                .and()
                    .parameter()
                .name("sortering")
                .optional()
                .type(TpsParameterType.STRING)
                .values("K","P","N","")
                .and()
                
                .transformer()
                .preSend(ServiceRoutineAdresseDataRequestTransform.serviceRoutineXmlWrappingAppender())
                .postSend(ResponseDataTransformer.extractDataFromXmlElement("adresseDataS051"))
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
