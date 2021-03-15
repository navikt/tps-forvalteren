package no.nav.tps.forvalteren.domain.rs.dolly;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.nav.tps.forvalteren.domain.rs.RsForeldreRequest;
import no.nav.tps.forvalteren.domain.rs.RsSivilstandRequest;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsPersonBestillingRelasjonRequest {

    public enum BorHos {MEG, OSS, DEG}

    private RsRelasjoner relasjoner;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RsRelasjoner {

        private List<RsPartnerRelasjonRequest> partnere;
        private List<RsBarnRelasjonRequest> barn;
        private List<RsForeldreRelasjonRequest> foreldre;

        public List<RsPartnerRelasjonRequest> getPartnere() {
            if (isNull(partnere)) {
                partnere = new ArrayList();
            }
            return partnere;
        }

        public List<RsBarnRelasjonRequest> getBarn() {
            if (isNull(barn)) {
                barn = new ArrayList();
            }
            return barn;
        }

        public List<RsForeldreRelasjonRequest> getForeldre() {
            if (isNull(foreldre)) {
                foreldre = new ArrayList<>();
            }
            return foreldre;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RsPartnerRelasjonRequest {

        private String ident;
        private List<RsSivilstandRequest> sivilstander;
        private Boolean harFellesAdresse;

        public List<RsSivilstandRequest> getSivilstander() {
            if (isNull(sivilstander)) {
                sivilstander = new ArrayList();
            }
            return sivilstander;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RsBarnRelasjonRequest {

        private String ident;
        private String partnerIdent;
        private BorHos borHos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RsForeldreRelasjonRequest {

        private String ident;
        private RsForeldreRequest.ForeldreType foreldreType;
        private Boolean harFellesAdresse;
    }
}