package no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStatusTransformer implements ResponseTransformer {

    private String xmlElement;

    public static ResponseTransformer extractStatusFromXmlElement(String xmlElement) {
        return new ResponseStatusTransformer(xmlElement);
    }
}
