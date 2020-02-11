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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.jpa.embedded.ChangeStamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_IDENT_HISTORIKK")
public class IdentHistorikk extends ChangeStamp {

    private static final String SEQ = "T_IDENT_HISTORIKK_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @JoinColumn(name = "PERSON_ID", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Person person;

    @JoinColumn(name="HISTORIC_PERSON_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Person aliasPerson;

    @Column (name = "HISTORIC_IDENT_ORDER", nullable = false)
    private Integer historicIdentOrder;

    @Column (name="REGDATO")
    private LocalDateTime regdato;
}