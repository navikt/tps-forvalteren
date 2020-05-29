package no.nav.tps.forvalteren.domain.jpa;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Adresse {

    private static final String SEQ = "ADRESSE_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @Column(name = "ADRESSE_ID", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    private Person person;

    @Column(name = "KOMMUNENR", length = 4)
    private String kommunenr;

    @Column(name = "FLYTTE_DATO")
    private LocalDateTime flyttedato;

    @Column(name = "POSTNR", length = 4)
    private String postnr;

    @Column(name ="TILLEGGSADRESSE")
    private String tilleggsadresse;

    @Transient
    private String bolignr;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Adresse)) {
            return false;
        }

        Adresse adresse = (Adresse) o;

        return new EqualsBuilder()
                .append(getKommunenr(), adresse.getKommunenr())
                .append(getPostnr(), adresse.getPostnr())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getKommunenr())
                .append(getPostnr())
                .toHashCode();
    }

    public abstract Adresse toUppercase();
}
