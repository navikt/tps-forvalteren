package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDataListTransformer implements ResponseTransformer {

    private String xmlElement;
    private String xmlElementSingleResource;
    private String xmlElementTotalHits;

    public static ResponseTransformer extractDataListFromXml(String xmlElement, String xmlElementSingleResource, String xmlElementTotalHits ) {
        return new ResponseDataListTransformer(xmlElement, xmlElementSingleResource, xmlElementTotalHits);
    }

}
