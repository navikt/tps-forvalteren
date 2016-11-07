package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers;

import org.codehaus.jackson.annotate.JsonIgnoreType;

public class DefaultServiceRoutineXmlTransform implements XmlTransformStrategy {

    private static final String XML_PROPERTIES_PREFIX  = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><tpsPersonData>";
    private static final String XML_PROPERTIES_POSTFIX = "</tpsPersonData>";

    @Override
    public String execute(String xml) {
        return XML_PROPERTIES_PREFIX + xml + XML_PROPERTIES_POSTFIX;
    }
}
