package no.nav.tps.vedlikehold.domain.service.jpa;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Peter Fløgstad on 15.02.2017.
 */

@Entity
@Getter
@Setter
@Table(name = TestGruppe.TABLE_NAME)
public class TestGruppe {

    static final String TABLE_NAME = "T_TEST_GRUPPE";

    //TODO Trenger man en sequence her
    @Id
    @Column(name = "GRUPPE_ID", nullable = false)
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "OWNER_ID", nullable = false)
    private String owner;

}
