package no.nav.tps.vedlikehold.domain.service.jpa;

/**
 * Created by Peter Fløgstad on 26.01.2017.
 */

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import static javax.persistence.GenerationType.SEQUENCE;


@Entity
@Getter
@Setter
@Table(name = TPSKTestPerson.TABLE_NAME)
public class TPSKTestPerson {

    static final String TABLE_NAME = "T_TEST_PERSON";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "personIdGenerator")
    @SequenceGenerator(name = "personIdGenerator", sequenceName = "T_PERSON_SEQUENCE", allocationSize = 100)
    @Column(name = "TEST_PERSON_ID", nullable = false)
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Long id;

//    @Column(name = "GRUPPE_ID", nullable = false)
//    private int gruppeId;

    @Column(name = "FORNAVN", nullable = false)
    private String fornavn;

    @Column(name = "SLEKTSNAVN", nullable = false)
    private String slektsnavn;

    @Column(name = "MELLOMNAVN")
    private String mellomnavn;

    @Column(name = "PERSONNUMMER", nullable = false)
    private String personnummer;

    @Column(name = "FODSELSNUMMER", nullable = false)
    private String fodselsnummer;

}
