package no.nav.tps.forvalteren.domain.rs;

import static java.util.Objects.nonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsBarnRequest extends RsSimplePersonRequest {

    public enum BarnType {MITT, FELLES, DITT}

    public enum BorHos {MEG, OSS, DEG, BEGGE}

    private BarnType barnType;
    private Integer partnerNr; // 1, 2, 3 Kan være tom hvis felles eller mine, identifiser partner hvis dine
    private BorHos borHos;
    private Boolean erAdoptert;

    @JsonIgnore
    public boolean isAdresseMedHovedPerson() {
        return nonNull(getBorHos()) && (
                getBorHos() == BorHos.MEG ||
                        getBorHos() == BorHos.OSS ||
                        getBorHos() == BorHos.BEGGE
        );
    }

    @JsonIgnore
    public boolean isAdresseMedPartner() {
        return nonNull(getBorHos()) && (
                getBorHos() == BorHos.DEG ||
                        getBorHos() == BorHos.BEGGE
        );
    }

    @JsonIgnore
    public boolean isDeltAdresse() {
        return nonNull(getBorHos()) &&
                getBorHos() == BorHos.BEGGE;
    }
}