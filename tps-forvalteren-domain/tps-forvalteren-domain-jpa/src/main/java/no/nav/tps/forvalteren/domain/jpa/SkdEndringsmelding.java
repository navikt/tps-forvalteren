package no.nav.tps.forvalteren.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.jpa.embedded.ChangeStamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_SKD_ENDRINGSMELDING")
public class SkdEndringsmelding extends ChangeStamp {

    private static final String SEQ = "T_SKD_ENDRINGSMELDING_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @Column(name = "SKD_ENDRINGSMELDING_ID", nullable = false, updatable = false)
    private Long id;

    @JoinColumn(name = "SKD_ENDRINGSMELDING_GRUPPE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private SkdEndringsmeldingGruppe gruppe;

    @Column(name = "ENDRINGSMELDING_JSON", nullable = false)
    private String endringsmelding;

}
