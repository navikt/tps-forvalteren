package no.nav.tps.forvalteren.skdavspilleren.domain.jpa;

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

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_SKD_MELDINGER_AVSPILLERDATA")
public class SkdmeldingAvspillerdata {
    
    private static final String SEQ = "T_SKD_AVSPILLERDATA_SEQ";
    
    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;
    
    @Column(name = "SKD_MELDING", nullable = false, length = 1600)
    private String Skdmelding;
    
    @Column(name = "TESTPERSON_ID")
    private int testpersonAvspillerId;
    
    @JoinColumn(name = "GRUPPE_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Avspillergruppe avspillergruppe;
    
    @Column(nullable = false)
    private Long sekvensnummer;
    
    @Column(nullable = false)
    private int aarsakskode;
}
