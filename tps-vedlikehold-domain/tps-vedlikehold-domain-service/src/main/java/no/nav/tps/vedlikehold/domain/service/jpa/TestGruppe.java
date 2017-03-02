package no.nav.tps.vedlikehold.domain.service.jpa;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Peter Fløgstad on 15.02.2017.
 */

@Entity
@Getter
@Setter
@Table(name = TestGruppe.TABLE_NAME)
public class TestGruppe {

    static final String TABLE_NAME = "T_TEST_GRUPPE";

    //private Set<TPSKTestPerson> testpersoner;

    //TODO Trenger man en sequence her
    @Id
    @Column(name = "TEST_GRUPPE_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "testGruppeGenerator")
    @SequenceGenerator(name="testGruppeGenerator", sequenceName = "T_GRUPPE_SEQUENCE", allocationSize = 100)
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "OWNER_ID")
    private String ownerID;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "T_GRUPPE_PERSON",
            joinColumns =  @JoinColumn(name = "GRUPPE_ID", referencedColumnName = "TEST_GRUPPE_ID"),
            inverseJoinColumns = { @JoinColumn(name="PERSON_ID", referencedColumnName = "TEST_PERSON_ID")} )
    private Set<TPSKTestPerson> testpersoner = new HashSet<>();

    public void addTestPerson(TPSKTestPerson person){
        if(!testpersoner.contains(person)){
            testpersoner.add(person);
        }
    }

    public void removeTestPerson(TPSKTestPerson person){
        if(testpersoner.contains(person)){
            testpersoner.remove(person);
        }
    }
}
