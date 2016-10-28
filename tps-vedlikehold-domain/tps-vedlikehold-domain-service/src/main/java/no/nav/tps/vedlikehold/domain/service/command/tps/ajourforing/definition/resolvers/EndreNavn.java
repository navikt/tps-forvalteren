package no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.definition.resolvers;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameterType;
import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.definition.TpsEndringsmelding;
import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.requests.TpsRequestEndringsmelding;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsEndreNavnRequestEndringsmelding;

import static no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.definition.TpsEndringsmeldingBuilder.aTpsEndringsmelding;

/**
 * Created by f148888 on 29.09.2016.
 */

public class EndreNavn implements EndringsmeldingResolver{

    @Override
    public TpsEndringsmelding resolve() {
        return aTpsEndringsmelding()
                .name("EndreNavn")
                .internalName("Endre: Navn")
                .javaClass(TpsEndreNavnRequestEndringsmelding.class)

                .parameter()
                .name("offentligIdent")
                .required()
                .type(TpsParameterType.STRING)

                .and()
                .parameter()
                .name("fornavn")
                .required()
                .type(TpsParameterType.STRING)

                .and()
                .parameter()
                .name("mellomnavn")
                .required()
                .type(TpsParameterType.STRING)

                .and()
                .parameter()
                .name("etternavn")
                .required()
                .type(TpsParameterType.STRING)

                .and()
                .parameter()
                .name("kortnavn")
                .required()
                .type(TpsParameterType.STRING)

                .and()
                .parameter()
                .name("tidligerenavn")
                .required()
                .type(TpsParameterType.STRING)

                .and()
                .parameter()
                .name("datoNyttNavn")
                .required()
                .type(TpsParameterType.DATE)

                .and()
                .parameter()
                .name("kilde")
                .required()
                .type(TpsParameterType.STRING)
                .values("FS22")

                .and()
                .build();
    }
}
