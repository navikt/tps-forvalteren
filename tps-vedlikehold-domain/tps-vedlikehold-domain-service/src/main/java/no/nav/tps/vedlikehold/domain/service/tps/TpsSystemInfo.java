package no.nav.tps.vedlikehold.domain.service.tps;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by F148888 on 28.10.2016.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JacksonXmlRootElement(localName = "systemInfo")
public class TpsSystemInfo {

    private String kilde;
    private String brukerID;

}
