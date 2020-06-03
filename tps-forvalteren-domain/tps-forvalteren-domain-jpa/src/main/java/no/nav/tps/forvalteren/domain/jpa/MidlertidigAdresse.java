package no.nav.tps.forvalteren.domain.jpa;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.jpa.embedded.ChangeStamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ADRESSETYPE", discriminatorType = DiscriminatorType.STRING)
@Table(name = "T_MIDLERTIDIG_ADRESSE")
public class MidlertidigAdresse extends ChangeStamp {

    private static final String SEQ = "T_MIDLERTIDIG_ADRESSE_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Getter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_ID", nullable = false)
    private Person person;

    @Column(name = "GYLDIG_TOM")
    private LocalDateTime gyldigTom;

    @Column(name = "TILLEGGSADRESSE")
    private String tilleggsadresse;

    @Column(name = "POSTNR")
    private String postnr;

    @Entity
    @DiscriminatorValue("GATE")
    public class MidlertidigGateAdresse extends MidlertidigAdresse {

        @Column(name = "GATENAVN", length = 30)
        private String gatenavn;

        @Column(name = "GATEKODE", length = 30)
        private String gatekode;

        @Column(name = "HUSNR", length = 30)
        private String husnr;
    }

    @Entity
    @DiscriminatorValue("STED")
    public class MidlertidigStedAdresse extends MidlertidigAdresse {

        @Column(name = "EIENDOMSNAVN", length = 30)
        private String eiendomsnavn;
    }

    @Entity
    @DiscriminatorValue("PBOX")
    public class MidlertidigPboxAdresse extends MidlertidigAdresse {

        @Column(name = "POSTBOKSNR", length = 30)
        private String postboksnr;

        @Column(name = "POSTBOKS_ANLEGG", length = 30)
        private String postboksAnlegg;
    }

    @Entity
    @DiscriminatorValue("UTAD")
    public class MidlertidigUtadAdresse extends MidlertidigAdresse {

        @Column(name = "POST_LINJE_1", length = 30)
        private String postLinje1;

        @Column(name = "POST_LINJE_2", length = 30)
        private String postLinje2;

        @Column(name = "POST_LINJE_3", length = 30)
        private String postLinje3;

        @Column(name = "POST_LAND", length = 3)
        private String postLand;
    }
}
