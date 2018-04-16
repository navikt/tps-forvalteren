package no.nav.tps.forvalteren.domain.jpa;

import static javax.persistence.CascadeType.ALL;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.jpa.embedded.ChangeStamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_PERSON")
public class Person extends ChangeStamp {
	
	private static final String SEQ = "T_PERSON_SEQ";
	
	@Id
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@Column(name = "PERSON_ID", nullable = false, updatable = false)
	private Long id;
	
	@Column(name = "IDENT", nullable = false, unique = true, length = 11)
	private String ident;
	
	@Column(name = "IDENTTYPE", nullable = false, length = 3)
	private String identtype;
	
	@Column(name = "KJONN", nullable = false)
	private Character kjonn;
	
	@Column(name = "FORNAVN", nullable = false, length = 50)
	private String fornavn;
	
	@Column(name = "MELLOMNAVN", length = 50)
	private String mellomnavn;
	
	@Column(name = "ETTERNAVN", nullable = false, length = 50)
	private String etternavn;
	
	@Column(name = "STATSBORGERSKAP", length = 3)
	private String statsborgerskap;
	
	@Column(name = "SPESREG", length = 1)
	private String spesreg;
	
	@Column(name = "SPESREG_DATO")
	private LocalDateTime spesregDato;
	
	@Column(name = "DOEDSDATO")
	private LocalDateTime doedsdato;
	
	@Column(name = "UTVANDRET_TIL_LAND", length = 2)
	private String utvandretTilLand;
	
	@Column(name = "REGISTRERT_UTVANDR_DATO")
	private LocalDateTime registertUtvandringsdato;
	
	@Column(name = "FLYTTET_TIL_LAND_DATO")
	private LocalDateTime flyttetTilLandDato;
	
	@JoinColumn(name = "ADRESSE_ID")
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "person", cascade = ALL)
	private Adresse boadresse;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "person", cascade = ALL)
	private List<Postadresse> postadresse = new ArrayList<>();
	
	@Column(name = "REGDATO", nullable = false)
	private LocalDateTime regdato;
	
	@JoinColumn(name = "GRUPPE_ID")
	@ManyToOne(fetch = FetchType.LAZY)
	private Gruppe gruppe;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "person", cascade = ALL)
	private List<Relasjon> relasjoner = new ArrayList<>();
}