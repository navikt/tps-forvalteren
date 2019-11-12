package no.nav.tps.forvalteren.domain.rs.dolly;

import static java.util.Objects.isNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.rs.AdresseNrInfo;
import no.nav.tps.forvalteren.domain.rs.RsSimplePersonRequest;
import no.nav.tps.forvalteren.domain.rs.RsSimpleRelasjoner;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsPersonBestillingKriteriumRequest extends RsSimplePersonRequest {

    private Set<String> environments;

    private List<String> opprettFraIdenter;

    @Min(1)
    @Max(99)
    private Integer antall;

    private RsSimpleRelasjoner relasjoner;

    private String sivilstand;

    private LocalDateTime sivilstandRegdato;

    private LocalDateTime regdato;

    @Size(min = 4, max = 4)
    private String typeSikkerhetsTiltak;

    private LocalDateTime sikkerhetsTiltakDatoFom;

    private LocalDateTime sikkerhetsTiltakDatoTom;

    @Size(min = 1, max = 50)
    private String beskrSikkerhetsTiltak;

    private AdresseNrInfo adresseNrInfo;

    public List<String> getOpprettFraIdenter() {
        if (isNull(opprettFraIdenter)) {
            opprettFraIdenter = new ArrayList();
        }
        return opprettFraIdenter;
    }

    public RsSimpleRelasjoner getRelasjoner() {
        if (isNull(relasjoner)) {
            relasjoner = new RsSimpleRelasjoner();
        }
        return relasjoner;
    }
}