package no.nav.tps.forvalteren.domain.rs;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsMeldingerResponse {

    private Long antallTotalt;
    private List<RsMelding> meldinger;

    public List<RsMelding> getMeldinger() {
        if (isNull(meldinger)) {
            meldinger = new ArrayList<>();
        }
        return meldinger;
    }
}
