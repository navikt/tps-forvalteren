package no.nav.tps.forvalteren.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "T_MATRIKKELADRESSE")
public class Matrikkeladresse extends Adresse {

    @Column(name = "MELLOMNAVN", length = 30)
    private String mellomnavn;

    @Column(name = "GARDSNR", length = 5)
    private String gardsnr;

    @Column(name = "BRUKSNR", length = 4)
    private String bruksnr;

    @Column(name = "FESTENR", length = 4)
    private String festenr;

    @Column(name = "UNDERNR", length = 3)
    private String undernr;

}
