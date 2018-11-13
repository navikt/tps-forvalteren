package no.nav.tps.forvalteren.domain.rs.dolly;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class RsIdenterMiljoer {

    @NotNull
    private List<String> identer;

    @NotNull
    private List<String> miljoer;

    public List<String> getIdenter() {
        if (identer == null) {
            identer = new ArrayList<>();
        }
        return identer;
    }

    public List<String> getMiljoer() {
        if (miljoer == null) {
            miljoer = new ArrayList<>();
        }
        return miljoer;
    }
}
