package no.nav.tps.forvalteren.domain.rs.dolly;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.rs.RsSivilstandRequest;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsPersonBestillingRelasjonRequest {

    public enum BarnType {MITT, FELLES, DITT}
    public enum BorHos {MEG, OSS, DEG}

    private RsRelasjoner relasjoner;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RsRelasjoner {

        private List<RsPartnerRelasjonRequest> partner;
        private List<RsBarnRelasjonRequest> barn;

        public List<RsPartnerRelasjonRequest> getPartner() {
            if (isNull(partner)) {
                partner = new ArrayList();
            }
            return partner;
        }

        public List<RsBarnRelasjonRequest> getBarn() {
            if (isNull(barn)) {
                barn = new ArrayList();
            }
            return barn;
        }
    }

    @Getter
    @Setter
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

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RsBarnRelasjonRequest {

        private String ident;
        private BarnType barnType;
        private String partnerIdent;
        private BorHos borHos;
        private Boolean erAdoptert;
    }
}