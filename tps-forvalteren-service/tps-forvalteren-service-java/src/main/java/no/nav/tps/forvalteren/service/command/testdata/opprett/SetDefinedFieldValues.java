package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertStringToDate;
import org.springframework.stereotype.Service;

@Service
public class SetDefinedFieldValues {

    private boolean adresseErSatt = false;

    public Person execute(String fieldName, Object fieldValue, Person person) {
        switch (fieldName) {
        case "statsborgerskap":
            if(isTripleCharNumber(fieldValue)){
                person.setStatsborgerskap(fieldValue.toString());
            } else {
                person.setStatsborgerskap(null);
            }
            break;

        case "spesreg":
            if (isSingleCharNumber(fieldValue)) {
                person.setSpesreg(fieldValue.toString());
            } else {
                person.setSpesreg(null);
            }
            break;

        case "spesregDato":
            if (isDateFormat(fieldValue)) {
                person.setSpesregDato(ConvertStringToDate.yyyyMMdd(fieldValue.toString()));
            } else {
                person.setSpesregDato(null);
            }
            break;

        case "doedsdato":
            if (isDateFormat(fieldValue)) {
                person.setDoedsdato(ConvertStringToDate.yyyyMMdd((fieldValue.toString())));
            } else {
                person.setDoedsdato(null);
            }
            break;

        case "sivilstand":
            if (isSingleCharNumber(fieldValue)) {
                person.setSivilstand(fieldValue.toString());
            } else {
                person.setSivilstand(null);
            }
            break;

        case "adresse":
        case "gateHusnr":
        case "gatePostnr":
        case "gateKommunenr":
        case "gateFlyttedatoFra":
        case "gateFlyttedatoTil":
            if(!adresseErSatt){
                setGateAdresse();
                adresseErSatt = true;
            }
            break;

        }
        return person;
    }

    private void setGateAdresse() {
        // Servicerutine S051.
    }


    private boolean isDateFormat(Object fieldvalue) {
        Pattern requiredPattern = Pattern.compile("\\d{8}");
        Matcher matcher = requiredPattern.matcher(fieldvalue.toString());
        return matcher.matches();
    }

    private boolean isSingleCharNumber(Object fieldvalue) {
        Pattern requiredPattern = Pattern.compile("\\d{1}");
        Matcher matcher = requiredPattern.matcher(fieldvalue.toString());
        return matcher.matches();
    }

    private boolean isTripleCharNumber(Object fieldvalue) {
        Pattern requiredPattern = Pattern.compile("\\d{3}");
        Matcher matcher = requiredPattern.matcher(fieldvalue.toString());
        return matcher.matches();
    }
}
