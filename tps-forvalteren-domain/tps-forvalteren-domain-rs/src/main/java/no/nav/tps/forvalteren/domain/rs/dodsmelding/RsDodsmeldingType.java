package no.nav.tps.forvalteren.domain.rs.dodsmelding;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsDodsmeldingType {

    private Long[] identer;

    private String tidspunkt;

    private String bruker;

    private String handling;

    private String dato;

    private String miljo;

    private String status;

    private String tilstand;

}


