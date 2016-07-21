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

    private static final String SERVICE_RUTINER_FILE_PATH = "ServiceRutiner.xml";

    @Override
    public String exectue() {

        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(SERVICE_RUTINER_FILE_PATH);

            String servicesAsXML = IOUtils.toString(inputStream);

            JSONObject servicesAsJson = XML.toJSONObject(servicesAsXML);

            return servicesAsJson.toString();
        } catch (JSONException exception) {
            LOGGER.error("Failed to convert services XML to a JSON object with exception: {}", exception.toString());
        } catch (IOException exception) {
            LOGGER.error("Failed to read file '{}' with exception: {}", SERVICE_RUTINER_FILE_PATH, exception.toString());
        }

        return null;
    }
}