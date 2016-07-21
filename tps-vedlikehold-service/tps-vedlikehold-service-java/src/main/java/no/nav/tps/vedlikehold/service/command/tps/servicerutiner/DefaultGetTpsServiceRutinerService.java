package no.nav.tps.vedlikehold.service.command.tps.servicerutiner;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@Service
public class DefaultGetTpsServiceRutinerService implements GetTpsServiceRutinerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultGetTpsServiceRutinerService.class);

    private final String SERVICE_RUTINER_FILE_PATH = "ServiceRutiner.xml";

    @Override
    public String exectue() {

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(SERVICE_RUTINER_FILE_PATH);

            String servicesAsXML = IOUtils.toString(inputStream);

            JSONObject servicesAsJSON = XML.toJSONObject(servicesAsXML);

            return servicesAsJSON.toString();
        } catch (JSONException exception) {
            LOGGER.error("Failed to convert services XML to a JSON object: {}", exception.getMessage());
        } catch (IOException exception) {
            LOGGER.error("Failed to read file '{}': {}", SERVICE_RUTINER_FILE_PATH, exception.getMessage());
        }

        return null;
    }
}