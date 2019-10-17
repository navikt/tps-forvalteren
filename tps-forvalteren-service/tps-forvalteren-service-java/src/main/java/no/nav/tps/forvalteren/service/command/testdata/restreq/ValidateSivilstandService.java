package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.service.Sivilstand.ENKE_ELLER_ENKEMANN;
import static no.nav.tps.forvalteren.domain.service.Sivilstand.GIFT;
import static no.nav.tps.forvalteren.domain.service.Sivilstand.GJENLEVENDE_PARTNER;
import static no.nav.tps.forvalteren.domain.service.Sivilstand.REGISTRERT_PARTNER;
import static no.nav.tps.forvalteren.domain.service.Sivilstand.SEPARERT;
import static no.nav.tps.forvalteren.domain.service.Sivilstand.SEPARERT_PARTNER;
import static no.nav.tps.forvalteren.domain.service.Sivilstand.SKILT;
import static no.nav.tps.forvalteren.domain.service.Sivilstand.SKILT_PARTNER;
import static no.nav.tps.forvalteren.domain.service.Sivilstand.UGIFT;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.rs.RsPartnerRequest;
import no.nav.tps.forvalteren.domain.rs.RsSivilstand;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.domain.service.Sivilstand;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;

@Service
public class ValidateSivilstandService {

    private static final LocalDateTime PARTNERSKAP_SKD = LocalDateTime.of(2009, 1, 1, 0, 0);

    @Autowired
    private MessageProvider messageProvider;

    public void validateStatus(RsPersonBestillingKriteriumRequest request) {

        if (nonNull(request.getRelasjoner().getPartner()) && !request.getRelasjoner().getPartnere().isEmpty()) {
            throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.sivilstand.partnere"));
        }

        if (!request.getRelasjoner().getPartnere().isEmpty()) {
            validateInnhold(request);
            validateDatoer(request.getRelasjoner().getPartnere());
            validatePartnerskapDato(request.getRelasjoner().getPartnere());

            AtomicBoolean harVaertGift = new AtomicBoolean(false);
            request.getRelasjoner().getPartnere().forEach(partnerRequest ->
                    partnerRequest.getSivilstander().forEach(sivilstand -> {
                        if (isGift(sivilstand.getSivilstand())) {
                            harVaertGift.set(true);
                        }
                    }));

            validateHistoricIntegrity(request.getRelasjoner().getPartnere(), harVaertGift.get());
        }
    }

    private void validateInnhold(RsPersonBestillingKriteriumRequest request) {

        for (int i = 0; i < request.getRelasjoner().getPartnere().size(); i++) {
            for (int j = 0; j < request.getRelasjoner().getPartnere().get(i).getSivilstander().size(); j++) {
                if (!Sivilstand.exists(request.getRelasjoner().getPartnere().get(i).getSivilstander().get(j).getSivilstand())) {
                    throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.sivilstand.ugyldig-kode",
                            request.getRelasjoner().getPartnere().get(i).getSivilstander().get(j).getSivilstand()));
                }
                if (i > 0) {
                    if (isNull(request.getRelasjoner().getPartnere().get(i).getSivilstander().get(j).getSivilstandRegdato())) {
                        throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.sivilstand.regdato.required.multiple"));
                    }
                }
                if (j > 0) {
                    if (isNull(request.getRelasjoner().getPartnere().get(i).getSivilstander().get(j).getSivilstandRegdato())) {
                        throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.sivilstand.regdato.multiple"));
                    }
                }
            }
        }
    }

    private void validateDatoer(List<RsPartnerRequest> partnereRequest) {

        LocalDateTime dateComperator = LocalDateTime.now();

        for (int i = 0; i < partnereRequest.size(); i++) {
            for (int j = 0; j < partnereRequest.get(i).getSivilstander().size(); j++) {

                if (partnereRequest.get(i).getSivilstander().get(j).getSivilstandRegdato().isAfter(dateComperator.minusDays(2))) {
                    throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.sivilstand.datoer"));
                } else {
                    dateComperator = partnereRequest.get(i).getSivilstander().get(j).getSivilstandRegdato();
                }
            }
        }
    }

    private void validatePartnerskapDato(List<RsPartnerRequest> partnereRequest) {

        partnereRequest.forEach(partnerRequest ->
                partnerRequest.getSivilstander().forEach(sivilstand -> {
                    if (nonNull(sivilstand.getSivilstandRegdato()) && sivilstand.getSivilstandRegdato().isAfter(PARTNERSKAP_SKD)) {
                        if (isRegistertOrSeparertPartner(sivilstand) || isGjenlevendeOrSkiltPartner(sivilstand)) {
                            throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.sivilstand.partner"));
                        }
                    }
                }));
    }

    private boolean isGjenlevendeOrSkiltPartner(RsSivilstand sivilstand) {
        return GJENLEVENDE_PARTNER.getKodeverkskode().equals(sivilstand.getSivilstand()) ||
                SKILT_PARTNER.getKodeverkskode().equals(sivilstand.getSivilstand());
    }

    private boolean isRegistertOrSeparertPartner(RsSivilstand sivilstand) {
        return REGISTRERT_PARTNER.getKodeverkskode().equals(sivilstand.getSivilstand()) ||
                SEPARERT_PARTNER.getKodeverkskode().equals(sivilstand.getSivilstand());
    }

    private void validateHistoricIntegrity(List<RsPartnerRequest> partnereRequest, boolean harVaertGift) {

        String currentSivilstand = UGIFT.getKodeverkskode();

        for (int i = partnereRequest.size() - 1; i >= 0; i--) {
            for (int j = partnereRequest.get(i).getSivilstander().size() - 1; j >= 0; j--) {

                if (isUgift(partnereRequest.get(i).getSivilstander().get(j).getSivilstand()) && !isUgift(currentSivilstand)) {
                    throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.sivilstand.ugift"));
                }
                if (isSeparert(partnereRequest.get(i).getSivilstander().get(j).getSivilstand()) && isSkilt(currentSivilstand)) {
                    throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.sivilstand.separert"));
                }
                if (partnereRequest.get(i).getSivilstander().get(j).getSivilstand().equals(currentSivilstand)) {
                    throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.sivilstand.samme"));
                }
                currentSivilstand = partnereRequest.get(i).getSivilstander().get(j).getSivilstand();

                validateUgiftAndSivilstandHistorikk(partnereRequest.get(i).getSivilstander().get(j), harVaertGift);
            }
        }
    }

    private void validateUgiftAndSivilstandHistorikk(RsSivilstand sivilstand, boolean harVaertGift) {

        validateUgiftAndSivilstandHistorikk(harVaertGift, sivilstand, SEPARERT, SEPARERT_PARTNER, "bestilling.input.validation.sivilstand.separert.ugift");
        validateUgiftAndSivilstandHistorikk(harVaertGift, sivilstand, SKILT, SKILT_PARTNER, "bestilling.input.validation.sivilstand.skilt.ugift");
        validateUgiftAndSivilstandHistorikk(harVaertGift, sivilstand, ENKE_ELLER_ENKEMANN, GJENLEVENDE_PARTNER, "bestilling.input.validation.sivilstand.enke.ugift");
    }

    private void validateUgiftAndSivilstandHistorikk(boolean harVaertGift, RsSivilstand sivilstand, Sivilstand gift, Sivilstand partner, String error) {
        if (!harVaertGift && gift.getKodeverkskode().equals(sivilstand.getSivilstand()) ||
                partner.getKodeverkskode().equals(sivilstand.getSivilstand())) {
            throw new TpsfFunctionalException(messageProvider.get(error));
        }
    }

    private static boolean isSeparert(String sivilstand) {
        return SEPARERT.getKodeverkskode().equals(sivilstand) ||
                SEPARERT_PARTNER.getKodeverkskode().equals(sivilstand);
    }

    private static boolean isSkilt(String sivilstand) {
        return SKILT.getKodeverkskode().equals(sivilstand) ||
                SKILT_PARTNER.getKodeverkskode().equals(sivilstand);
    }

    private static boolean isUgift(String sivilstand) {
        return UGIFT.getKodeverkskode().equals(sivilstand);
    }

    private static boolean isGift(String sivilstand) {
        return GIFT.getKodeverkskode().equals(sivilstand) ||
                REGISTRERT_PARTNER.getKodeverkskode().equals(sivilstand);
    }
}