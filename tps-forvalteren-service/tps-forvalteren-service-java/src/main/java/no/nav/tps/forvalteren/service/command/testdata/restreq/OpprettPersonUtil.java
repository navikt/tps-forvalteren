package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.DefaultBestillingDatoer.getForeldreProcessedFoedtEtter;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.DefaultBestillingDatoer.getForeldreProcessedFoedtFoer;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.DefaultBestillingDatoer.getProcessedFoedtEtter;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.DefaultBestillingDatoer.getProcessedFoedtFoer;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;

import java.util.List;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;
import no.nav.tps.forvalteren.domain.rs.RsBarnRequest;
import no.nav.tps.forvalteren.domain.rs.RsForeldreRequest;
import no.nav.tps.forvalteren.domain.rs.RsForeldreRequest.ForeldreType;
import no.nav.tps.forvalteren.domain.rs.RsPartnerRequest;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.skd.KjoennType;

@UtilityClass
public class OpprettPersonUtil {
    public static RsPersonKriteriumRequest extractMainPerson(RsPersonBestillingKriteriumRequest request) {

        return RsPersonKriteriumRequest.builder()
                .personKriterierListe(singletonList(RsPersonKriterier.builder()
                        .antall(1)
                        .identtype(nullcheckSetDefaultValue(request.getIdenttype(), "FNR"))
                        .kjonn(nullcheckSetDefaultValue(request.getKjonn(), KjoennType.U))
                        .foedtEtter(getProcessedFoedtEtter(request.getAlder(), request.getFoedtEtter(), request.getFoedtFoer(), false))
                        .foedtFoer(getProcessedFoedtFoer(request.getAlder(), request.getFoedtEtter(), request.getFoedtFoer(), false))
                        .harMellomnavn(request.getHarMellomnavn())
                        .navSyntetiskIdent(request.getNavSyntetiskIdent())
                        .build()))
                .build();
    }

    public static RsPersonKriteriumRequest extractPartner(List<RsPartnerRequest> partnerRequests,
            Boolean hovedpersonHarMellomnavn,
            Boolean navSyntetiskIdent) {

        return RsPersonKriteriumRequest.builder()
                .personKriterierListe(partnerRequests.stream()
                        .map(partnerReq -> RsPersonKriterier.builder()
                                .antall(1)
                                .identtype(nullcheckSetDefaultValue(partnerReq.getIdenttype(), "FNR"))
                                .kjonn(nullcheckSetDefaultValue(partnerReq.getKjonn(), KjoennType.U))
                                .foedtEtter(getProcessedFoedtEtter(partnerReq.getAlder(), partnerReq.getFoedtEtter(), partnerReq.getFoedtFoer(), false))
                                .foedtFoer(getProcessedFoedtFoer(partnerReq.getAlder(), partnerReq.getFoedtEtter(), partnerReq.getFoedtFoer(), false))
                                .harMellomnavn(nullcheckSetDefaultValue(partnerReq.getHarMellomnavn(), hovedpersonHarMellomnavn))
                                .navSyntetiskIdent(navSyntetiskIdent)
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public static RsPersonKriteriumRequest extractBarn(List<RsBarnRequest> barnRequests,
            Boolean hovedpersonHarMellomnavn,
            Boolean navSyntetiskIdent) {

        return RsPersonKriteriumRequest.builder()
                .personKriterierListe(barnRequests.stream()
                        .map(barnReq -> RsPersonKriterier.builder()
                                .antall(1)
                                .identtype(nullcheckSetDefaultValue(barnReq.getIdenttype(), "FNR"))
                                .kjonn(nullcheckSetDefaultValue(barnReq.getKjonn(), KjoennType.U))
                                .foedtEtter(getProcessedFoedtEtter(barnReq.getAlder(), barnReq.getFoedtEtter(), barnReq.getFoedtFoer(), true))
                                .foedtFoer(getProcessedFoedtFoer(barnReq.getAlder(), barnReq.getFoedtEtter(), barnReq.getFoedtFoer(), true))
                                .harMellomnavn(nullcheckSetDefaultValue(barnReq.getHarMellomnavn(), hovedpersonHarMellomnavn))
                                .navSyntetiskIdent(navSyntetiskIdent)
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public static RsPersonKriteriumRequest extractForeldre(List<RsForeldreRequest> foreldreRequest,
            Boolean hovedpersonHarMellomnavn,
            Boolean navSyntetiskIdent) {

        return RsPersonKriteriumRequest.builder()
                .personKriterierListe(foreldreRequest.stream()
                        .map(foreldre -> RsPersonKriterier.builder()
                                .antall(1)
                                .identtype(nullcheckSetDefaultValue(foreldre.getIdenttype(), "FNR"))
                                .kjonn(nullcheckSetDefaultValue(foreldre.getKjonn(), getForeldreKjoenn(foreldre)))
                                .foedtFoer(getForeldreProcessedFoedtFoer(foreldre.getAlder(), foreldre.getFoedtEtter(), foreldre.getFoedtFoer()))
                                .foedtEtter(getForeldreProcessedFoedtEtter(foreldre.getAlder(), foreldre.getFoedtEtter(), foreldre.getFoedtFoer()))
                                .harMellomnavn(nullcheckSetDefaultValue(foreldre.getHarMellomnavn(), hovedpersonHarMellomnavn))
                                .navSyntetiskIdent(navSyntetiskIdent)
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private static KjoennType getForeldreKjoenn(RsForeldreRequest request) {

        if (nonNull(request.getForeldreType())) {
            return ForeldreType.MOR == request.getForeldreType() ? KjoennType.K : KjoennType.M;

        } else {
            return KjoennType.U;
        }
    }
}
