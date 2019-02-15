package no.nav.tps.forvalteren.domain.jpa;

import static javax.persistence.GenerationType.SEQUENCE;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "T_TPS_AVSPILLER_PROGRESS")
public class TpsAvspillerProgress {

    private static final String SEQ = "T_TPS_AVSPILLER_PROGRESS_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ)
    @Column(name="PROGRESS_ID", nullable = false)
    private Long progressId;

    @Column(name="INDEKS", nullable = false)
    private Long indeks;

    @Column(name="MILJOE", nullable = false)
    private String miljoe;

    @Column(name="MELDING_NR", nullable = false)
    private String meldingNr;

    @Column(name = "TIDSPUNKT", nullable = false)
    private LocalDateTime tidspunkt;

    @Column(name="SEND_STATUS", nullable = false)
    private String sendStatus;
}
