package no.nav.tps.forvalteren.domain.jpa;

import static javax.persistence.GenerationType.SEQUENCE;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
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

    @Column(name = "MILJOE_FRA", nullable = false)
    private String miljoeFra;

    @Column(name = "MILJOE_TIL", nullable = false)
    private String miljoeTil;

    @Column(name = "MELDING_KOE", nullable = false)
    private String meldingKoe;

    @Column(name = "KOE_MANAGER", nullable = false)
    private String koeManager;

    @Column(name = "format", nullable = false)
    private String format;

    @Column(name="REQUEST", nullable = false)
    private String request;

    @Column(name="ANTALL", nullable = false)
    private String antall;

    @Column(name = "TIDSPUNKT", nullable = false)
    private LocalDateTime tidspunkt;

    @Column(name = "FERDIG", nullable = false)
    private boolean ferdig;

    @OneToMany
    private List<TpsAvspillerProgress> progressList;
}