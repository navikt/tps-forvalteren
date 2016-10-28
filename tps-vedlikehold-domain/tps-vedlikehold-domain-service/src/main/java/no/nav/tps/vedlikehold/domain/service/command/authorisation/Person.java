package no.nav.tps.vedlikehold.domain.service.command.authorisation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by F148888 on 19.10.2016.
 */
public class Person {
    String fnr;
    String xml;

    public Person(String personXml){
        this.xml = personXml;
        String extractPersonsDataRegex = "<fnr>(\\d{11})</fnr>";
        Matcher matcher = Pattern.compile(extractPersonsDataRegex, Pattern.DOTALL).matcher(xml);
        matcher.find();
        this.fnr = matcher.group(1);
    }

    public String getFnr(){
        return this.fnr;
    }

    public String getXml(){
        return this.xml;
    }
}
