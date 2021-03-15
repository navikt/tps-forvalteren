package no.nav.tps.forvalteren.domain.rs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsForeldreRequest extends RsSimplePersonRequest {

    public enum ForeldreType {MOR, FAR}

    private ForeldreType foreldreType;
    private Boolean harFellesAdresse;
}