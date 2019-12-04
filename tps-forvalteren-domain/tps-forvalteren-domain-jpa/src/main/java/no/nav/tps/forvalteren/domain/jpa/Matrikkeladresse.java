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
@Table(name = "T_MATRIKKELADRESSE")
public class Matrikkeladresse extends Adresse {

    @Column(name = "MELLOMNAVN", length = 30)
    private String mellomnavn;

    @Column(name = "GARDSNR", length = 5)
    private String gardsnr;

    @Column(name = "BRUKSNR", length = 4)
    private String bruksnr;

    @Column(name = "FESTENR", length = 4)
    private String festenr;

    @Column(name = "UNDERNR", length = 3)
    private String undernr;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Matrikkeladresse))
            return false;

        Matrikkeladresse that = (Matrikkeladresse) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getMellomnavn(), that.getMellomnavn())
                .append(getGardsnr(), that.getGardsnr())
                .append(getBruksnr(), that.getBruksnr())
                .append(getFestenr(), that.getFestenr())
                .append(getUndernr(), that.getUndernr())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getMellomnavn())
                .append(getGardsnr())
                .append(getBruksnr())
                .append(getFestenr())
                .append(getUndernr())
                .toHashCode();
    }

    @Override public Adresse toUppercase() {
        setMellomnavn(isNotBlank(getMellomnavn()) ? getMellomnavn().toUpperCase() : "");
        return this;
    }

}
