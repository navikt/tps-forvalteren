package no.nav.tps.forvalteren.domain.jpa;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Table(name = "T_STATSBORGERSKAP")
public class Statsborgerskap {

    private static final String SEQ = "T_STATSBORGERSKAP_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @JsonIgnore
    @Getter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_ID", nullable = false)
    private Person person;

    @Column(name = "STATSBORGERSKAP", nullable = false)
    private String statsborgerskap;

    @Column(name = "STATSBORGERSKAP_REGDATO", nullable = false)
    private LocalDateTime statsborgerskapRegdato;

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Statsborgerskap)) {
            return false;
        }

        Statsborgerskap that = (Statsborgerskap) o;

        return new EqualsBuilder()
                .append(getStatsborgerskap(), that.getStatsborgerskap())
                .append(getStatsborgerskapRegdato(), that.getStatsborgerskapRegdato())
                .isEquals();
    }

    @Override public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getStatsborgerskap())
                .append(getStatsborgerskapRegdato())
                .toHashCode();
    }
}