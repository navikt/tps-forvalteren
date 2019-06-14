package no.nav.tps.forvalteren.domain.jpa;

import static javax.persistence.GenerationType.SEQUENCE;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "T_TPS_AVSPILLER")
public class TpsAvspiller {

    private static final String SEQ = "T_TPS_AVSPILLER_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ)
    @Column(name="BESTILLING_ID", nullable = false)
    private Long bestillingId;

    @Column(name = "PERIODE_FRA", nullable = false)
    private LocalDateTime periodeFra;

    @Column(name = "PERIODE_TIL", nullable = false)
    private LocalDateTime periodeTil;

    @Column(name = "KILDE_KOE", nullable = false)
    private String kildeKoe;

    @Column(name = "UTLOEP_KOE", nullable = false)
    private String utloepKoe;

    @Column(name = "format", nullable = false)
    private String format;

    @Column(name="REQUEST", nullable = false)
    private String request;

    @Column(name="ANTALL", nullable = false)
    private Long antall;

    @Column(name = "TIDSPUNKT", nullable = false)
    private LocalDateTime tidspunkt;

    @Column(name = "FERDIG", nullable = false)
    private boolean ferdig;

    @OneToMany(mappedBy = "bestillingId", fetch = FetchType.LAZY)
    private List<TpsAvspillerProgress> progressList;
}