package no.nav.tps.forvalteren.domain.jpa;

import lombok.AccessLevel;
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
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_GATEADRESSE")
public class Gateadresse {

    private static final String SEQ = "T_GATEADRESSE_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @Column(name = "GATEADRESSE_ID", nullable = false, updatable = false)
    private Long id;

    @Getter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_ID", nullable = false)
    private Person person;

    @Column(name = "BO_GATEADRESSE", length = 50)
    private String boGateadresse;

    @Column(name = "BO_HUSNUMMER", length = 4)
    private String boHusnummer;

    @Column(name = "BO_GATEKODE", length = 5)
    private String boGatekode;

    @Column(name = "BO_POSTNUMMER", length = 4)
    private String boPostnummer;

    @Column(name = "BO_KOMMUNENR", length = 4)
    private String boKommunenr;

    @Column(name = "BO_FLYTTE_DATO")
    private LocalDateTime boFlytteDato;

}
