package no.nav.tps.forvalteren.domain.jpa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_POSTADRESSE")
@Entity
public class Postadresse {

    private static final String SEQ = "T_POSTADRESSE_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @Column(name = "POSTADRESSE_ID", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("postadresse")
    @JoinColumn(name = "K_PERSON_ID", nullable = false)
    private Person person;

    @Column(name = "POST_LINJE_1", length = 30)
    private String postLinje1;

    @Column(name = "POST_LINJE_2", length = 30)
    private String postLinje2;

    @Column(name = "POST_LINJE_3", length = 30)
    private String postLinje3;

    @Column(name = "POST_LAND", length = 3)
    private String postLand;

}
