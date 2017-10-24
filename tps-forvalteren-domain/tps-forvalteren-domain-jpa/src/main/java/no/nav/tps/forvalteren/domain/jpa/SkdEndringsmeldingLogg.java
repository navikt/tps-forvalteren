package no.nav.tps.forvalteren.domain.jpa;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_SKD_ENDRINGSMELDING_LOGG")
@EntityListeners(AuditingEntityListener.class)
public class SkdEndringsmeldingLogg {

    private static final String SEQ = "T_SKD_ENDRINGSMELDING_LOGG_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @Column(name = "SKD_ENDRINGSMELDING_LOGG_ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "SKD_ENDRINGSMELDING_GRUPPE_ID", nullable = false, updatable = false)
    private Long meldingsgruppeId;

    @Column(name = "ENDRINGSMELDING", nullable = false, updatable = false)
    private String endringsmelding;

    @CreatedDate
    @Column(name = "INNSENDT_DATO", nullable = false, updatable = false)
    private LocalDateTime innsendtDato;

    @CreatedBy
    @Column(name = "INNSENDT_AV", nullable = false, updatable = false)
    private String innsendtAv;

}
