package no.nav.tps.vedlikehold.service.services;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */

@Service
public class DefaultGetTpsServiceRutinerService implements GetTpsServiceRutinerService{

    private final String SERVICE_RUTINER_FILE_PATH = "ServiceRutiner.xml";

    public String getTpsServiceRutiner() {

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(SERVICE_RUTINER_FILE_PATH);

            String servicesAsXML = IOUtils.toString(inputStream);

            JSONObject servicesAsJSON = XML.toJSONObject(servicesAsXML);

            return servicesAsJSON.toString();
        } catch (JSONException e) {
            e.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
