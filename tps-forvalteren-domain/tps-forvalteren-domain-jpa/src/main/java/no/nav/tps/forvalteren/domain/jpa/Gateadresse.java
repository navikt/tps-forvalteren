package no.nav.tps.forvalteren.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_GATEADRESSE")
public class Gateadresse extends Adresse {

    @Column(name = "GATEADRESSE", length = 50)
    private String adresse;

    @Column(name = "HUSNUMMER", length = 5)
    private String husnummer;

    @Column(name = "GATEKODE", length = 5)
    private String gatekode;

}
