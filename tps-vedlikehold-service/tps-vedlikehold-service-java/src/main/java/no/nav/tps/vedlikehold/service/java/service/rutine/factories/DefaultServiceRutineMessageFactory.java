package no.nav.tps.vedlikehold.service.java.service.rutine.factories;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Generates a service rutine request message from the provided service rutine name and parameters.
 * Supports all service rutine, but is case sensitive in respect to the parameter names.
 *
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@Service
public class DefaultServiceRutineMessageFactory implements ServiceRutineMessageFactory {

    private static final String XML_PROPERTIES_PREFIX  = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><tpsPersonData><tpsServiceRutine>";
    private static final String XML_PROPERTIES_POSTFIX = "</tpsServiceRutine></tpsPersonData>";

    //TODO: This can probably be achieved using Jackson
    public String createMessage(ServiceRutineMessageFactoryStrategy strategy) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(XML_PROPERTIES_PREFIX);

        for (Map.Entry<String, Object> entry : strategy.getParameters().entrySet()) {
            String attribute = String.format("<%s>%s</%s>", entry.getKey(), entry.getValue(), entry.getKey());
            stringBuilder.append(attribute);
        }

        stringBuilder.append(XML_PROPERTIES_POSTFIX);

        return stringBuilder.toString();
    }

}
