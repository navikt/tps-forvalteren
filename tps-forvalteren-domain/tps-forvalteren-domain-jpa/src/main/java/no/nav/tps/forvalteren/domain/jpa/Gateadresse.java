package no.nav.tps.forvalteren.domain.jpa;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "T_GATEADRESSE")
public class Gateadresse extends Adresse {

    @Column(name = "GATEADRESSE", length = 50)
    private String adresse;

    @Column(name = "HUSNUMMER", length = 5)
    private String husnummer;

    @Column(name = "GATEKODE", length = 5)
    private String gatekode;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Gateadresse)) {
            return false;
        }

        Gateadresse that = (Gateadresse) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getAdresse(), that.getAdresse())
                .append(getHusnummer(), that.getHusnummer())
                .append(getGatekode(), that.getGatekode())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getAdresse())
                .append(getHusnummer())
                .append(getGatekode())
                .toHashCode();
    }

    @Override public Adresse toUppercase() {
        if (isNotBlank(getAdresse())) {
            setAdresse(getAdresse().toUpperCase());
        }
        if (isNotBlank(getHusnummer())) {
            setHusnummer(getHusnummer().toUpperCase());
        }
        return this;
    }
}
