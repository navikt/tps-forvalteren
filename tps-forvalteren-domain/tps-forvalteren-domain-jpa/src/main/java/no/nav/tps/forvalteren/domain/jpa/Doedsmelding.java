package no.nav.tps.forvalteren.domain.jpa;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
@Table(name = "T_DOEDSMELDING")
public class Doedsmelding {

    private static final String SEQ = "T_DOEDSMELDING_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ)
    @Column(name = "DOEDSMELDING_ID", nullable = false, updatable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "PERSON_ID")
    private Person person;

    @Column(name = "IS_MELDING_SENT")
    private Boolean isMeldingSent;
}
