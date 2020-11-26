package no.nav.tps.forvalteren.domain.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Data
@Builder
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

    @ElementCollection(targetClass = String.class)
    @Column(name = "OMRAADER")
    private List<String> omraader;

    @Column(name = "KILDE")
    private String kilde;

    @Column(name = "GYLDIG_FOM")
    private LocalDateTime gyldigFom;

    @Column(name = "GYLDIG_TOM")
    private LocalDateTime gyldigTom;

    @ManyToOne
    @JoinColumn(name = "FULLMEKTIG_PERSON_ID")
    private Person fullmektig;

    public List<String> getOmraader() {
        if (omraader.isEmpty()) {
            return new ArrayList<>();
        }
        return omraader;
    }
}
