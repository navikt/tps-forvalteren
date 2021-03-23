package no.nav.tps.forvalteren.domain.jpa;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_POSTADRESSE")
public class Postadresse {

    private static final String SEQ = "T_POSTADRESSE_SEQ";

    @Id
    @EqualsAndHashCode.Exclude
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @Column(name = "POSTADRESSE_ID", nullable = false, updatable = false)
    private Long id;

    @JsonIgnore
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_ID", nullable = false)
    private Person person;

    @Column(name = "POST_LINJE_1", length = 30)
    private String postLinje1;

    @Column(name = "POST_LINJE_2", length = 30)
    private String postLinje2;

    @Column(name = "POST_LINJE_3", length = 30)
    private String postLinje3;

    @Column(name = "POST_LAND", length = 3)
    private String postLand;

    public Postadresse toUppercase() {
        if (isNotBlank(getPostLinje1())) {
            setPostLinje1(getPostLinje1().toUpperCase());
        }
        if (isNotBlank(getPostLinje2())) {
            setPostLinje2(getPostLinje2().toUpperCase());
        }
        if (isNotBlank(getPostLinje3())) {
            setPostLinje3(getPostLinje3().toUpperCase());
        }
        if (isNotBlank(getPostLand())) {
            setPostLand(getPostLand().toUpperCase());
        }
        return this;
    }
}
