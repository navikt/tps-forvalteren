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
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "T_SIVILSTAND")
public class Sivilstand {

    private static final String SEQ = "T_SIVILSTAND_SEQ";

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

    @Column(name = "SIVILSTAND", nullable = false)
    private String sivilstand;

    @Column(name = "SIVILSTAND_REGDATO", nullable = false)
    private LocalDateTime sivilstandRegdato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_RELASJON_MED")
    private Person personRelasjonMed;


    public boolean isSivilstandGift() {

        switch (Sivilstatus.lookup(getSivilstand())) {
        case GIFT:
        case SEPARERT:
        case REGISTRERT_PARTNER:
        case SEPARERT_PARTNER:
            return true;
        default:
            return false;
        }
    }
}