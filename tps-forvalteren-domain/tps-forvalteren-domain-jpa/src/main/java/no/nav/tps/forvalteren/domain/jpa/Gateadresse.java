package no.nav.tps.forvalteren.domain.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_GATEADRESSE")
public class Gateadresse extends Adresse {

    @Column(name = "GATEADRESSE", length = 50)
    private String gateadresse;

    @Column(name = "HUSNUMMER", length = 4)
    private String husnummer;

    @Column(name = "GATEKODE", length = 5)
    private String gatekode;



}
