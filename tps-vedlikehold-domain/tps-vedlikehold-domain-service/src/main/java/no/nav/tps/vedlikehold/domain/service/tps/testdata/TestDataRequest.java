package no.nav.tps.vedlikehold.domain.service.tps.testdata;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Rizwan Ali Ahmed, Visma Consulting AS.
 */
@Getter
@Setter
@NoArgsConstructor
public class TestDataRequest {

    private LocalDate dato;
    private String kjonn;
    private String identType;
    private int antallIdenter;
}
