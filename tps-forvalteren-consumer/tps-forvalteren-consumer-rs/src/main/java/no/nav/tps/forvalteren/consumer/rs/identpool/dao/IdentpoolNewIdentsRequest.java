package no.nav.tps.forvalteren.consumer.rs.identpool.dao;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.rs.skd.IdentType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdentpoolNewIdentsRequest {

    private Long antall;
    private LocalDate foedtEtter;
    private LocalDate foedtFoer;
    private IdentType identtype;
    private IdentpoolKjoenn kjoenn;
    private String rekvirertAv;
    private Boolean syntetisk;
}
