package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TpsFinnGyldigeAdresserResponse {

    private String xml;
    private Response response;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private DataContainer data1;
        private ResponseStatus status;
        private Integer antallTotalt;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataContainer {

        private Integer antallForekomster;
        private List<Adressedata> adrData;

        public List<Adressedata> getAdrData() {
            if (isNull(adrData)) {
                adrData = new ArrayList();
            }
            return adrData;
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Adressedata {

         private String psted;
         private String knavn;
         private String adrnavn;
         private String gkode;
         private String pnr;
         private String geotilk;
         private String husnrtil;
         private String bydel;
         private String knr;
         private String husnrfra;
         private String matrikkelId;
    }
}
