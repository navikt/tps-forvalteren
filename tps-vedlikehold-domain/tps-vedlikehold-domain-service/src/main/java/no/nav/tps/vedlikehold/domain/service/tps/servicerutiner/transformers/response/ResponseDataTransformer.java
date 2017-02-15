package no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDataTransformer implements ResponseTransformer {

    private String xmlElement;

    public static ResponseTransformer extractDataFromXmlElement(String xmlElement) {
        return new ResponseDataTransformer(xmlElement);
    }
}
