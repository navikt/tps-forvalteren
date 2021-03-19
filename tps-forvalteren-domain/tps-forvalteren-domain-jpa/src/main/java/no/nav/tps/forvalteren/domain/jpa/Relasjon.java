package no.nav.tps.forvalteren.domain.jpa;

import static javax.persistence.GenerationType.SEQUENCE;
import static no.nav.tps.forvalteren.domain.jpa.Relasjon.ROLLE.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
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

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_RELASJON")
public class Relasjon {

    public enum ROLLE {PARTNER, EKTEFELLE, MOR, FAR, FOEDSEL, BARN}

    private static final String SEQ = "T_RELASJON_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ)
    @Column(name = "RELASJON_ID", nullable = false, updatable = false)
    private Long id;

    @JoinColumn(name = "person_id", nullable =false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Person person;

    @JoinColumn(name = "person_relasjon_id", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Person personRelasjonMed;

    @Column(name = "RELASJON_TYPE_NAVN", nullable = false)
    private String relasjonTypeNavn;

    public boolean isPartner() {
        return PARTNER.name().equals(getRelasjonTypeNavn()) || EKTEFELLE.name().equals(getRelasjonTypeNavn());
    }

    public boolean isBarn() {
        return FOEDSEL.name().equals(getRelasjonTypeNavn()) || BARN.name().equals(getRelasjonTypeNavn());
    }

    public boolean isMor() {
        return MOR.name().equals(getRelasjonTypeNavn());
    }

    public boolean isFar() {
        return FAR.name().equals(getRelasjonTypeNavn());
    }
}