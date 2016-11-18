package no.nav.tps.vedlikehold.domain.service.command.tps.testdata;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
