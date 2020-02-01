package no.nav.tps.forvalteren.domain.service.tps;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JacksonXmlRootElement(localName = "systemInfo")
public class TpsSystemInfo {

    private String kilde;
    private String brukerID;

}
