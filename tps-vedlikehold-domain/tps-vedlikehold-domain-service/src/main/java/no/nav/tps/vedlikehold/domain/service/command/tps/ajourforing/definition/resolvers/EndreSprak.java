package no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.definition.resolvers;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameterType;
import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.definition.TpsEndringsmelding;
import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.requests.TpsRequestEndringsmelding;

import static no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.definition.TpsEndringsmeldingBuilder.aTpsEndringsmelding;

/**
 * Created by f148888 on 29.09.2016.
 */

public class EndreSprak implements EndringsmeldingResolver{

    @Override
    public TpsEndringsmelding resolve() {
        return aTpsEndringsmelding()
                .name("EndreSprak")
                .internalName("Endre: Sprak")
                .javaClass(TpsRequestEndringsmelding.class)
                .parameter()
                .name("offentligIdent")
                .required()
                .type(TpsParameterType.STRING)
                .and()
                .parameter()
                .name("sprakKode")
                .required()
                .type(TpsParameterType.STRING)
                .values("NN")
                .and()
                .parameter()
                .name("datoSprak")
                .optional()
                .type(TpsParameterType.DATE)
                .and()
                .build();
    }
}
