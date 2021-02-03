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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "T_FULLMAKT")
@NoArgsConstructor
@AllArgsConstructor
public class Fullmakt {

    private static final String SEQ = "T_FULLMAKT_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ)
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    private Person person;

    @Column(name = "OMRAADER")
    private String omraader;

    @Column(name = "KILDE")
    private String kilde;

    @Column(name = "GYLDIG_FOM")
    private LocalDateTime gyldigFom;

    @Column(name = "GYLDIG_TOM")
    private LocalDateTime gyldigTom;

    @ManyToOne
    @JoinColumn(name = "FULLMEKTIG_PERSON_ID")
    private Person fullmektig;
}
