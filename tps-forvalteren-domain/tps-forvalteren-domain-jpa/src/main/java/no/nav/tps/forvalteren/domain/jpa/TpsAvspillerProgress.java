package no.nav.tps.forvalteren.domain.jpa;

import static javax.persistence.GenerationType.SEQUENCE;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_TPS_AVSPILLER_PROGRESS")
public class TpsAvspillerProgress {

    private static final String SEQ = "T_TPS_AVSPILLER_PROGRESS_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ)
    @Column(name="PROGRESS_ID", nullable = false)
    private Long progressId;

    @Column(name="BESTILLING_ID", nullable = false)
    private Long bestillingId;

    @Column(name="INDEKS_NR", nullable = false)
    private Long indeksNr;

    @Column(name="MELDING_NR", nullable = false)
    private Long meldingNr;

    @Column(name = "TIDSPUNKT", nullable = false)
    private LocalDateTime tidspunkt;

    @Column(name="SEND_STATUS", nullable = false)
    private String sendStatus;
}