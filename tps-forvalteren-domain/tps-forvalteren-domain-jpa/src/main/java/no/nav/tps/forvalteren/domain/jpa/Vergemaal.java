package no.nav.tps.forvalteren.domain.jpa;

import static javax.persistence.GenerationType.SEQUENCE;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name = "T_VERGEMAAL")
@NoArgsConstructor
@AllArgsConstructor
public class Vergemaal {

    private static final String SEQ = "T_VERGEMAAL_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ)
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    private Person person;

    @Column(name = "EMBETE", length = 4)
    private String embete;

    @Column(name = "SAKSTYPE", length = 3)
    private String sakstype;

    @Column(name = "VEDTAKSDATO")
    private LocalDateTime vedtaksdato;

    @ManyToOne
    @JoinColumn(name = "VERGE_PERSON_ID")
    private Person verge;

    @Column(name = "MANDATTYPE", length = 3)
    private String mandattype;
}
