package no.nav.tps.forvalteren.domain.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_RELASJON")
public class Relasjon {

    private static final String SEQ = "T_RELASJON_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @Column(name = "RELASJON_ID", nullable = false, updatable = false)
    private Long id;

    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "person_id")
    @ManyToOne
    private Person person;

    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "person_relasjon_id")
    @ManyToOne
    private Person personRelasjonMed;

    @Column(name = "RELASJON_TYPE_NAVN", nullable = false)
    private String relasjonTypeNavn;

}
