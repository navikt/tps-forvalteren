package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response;

import no.nav.tps.vedlikehold.domain.service.command.authorisation.Person;
import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class ServiceRoutineResponse {

    /** The raw response from TPS */
    private String xml;

    /** A formatted response */
    private Object data;

    /** Persons found in the response */
    private ArrayList<Person> persons;

    public ServiceRoutineResponse(String xml, Object data) {
        this.xml = xml;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public String getXml() {
        return xml;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getServiceRoutineName(){
        String serviceRoutineNamePattern = "<serviceRutinenavn>(.+?)</serviceRutinenavn>";
        Matcher matcher = Pattern.compile(serviceRoutineNamePattern, Pattern.DOTALL).matcher(this.xml);
        matcher.find();
        return matcher.group(1);
    }

    public String getEnvironment(){
        String responseEnvironmentPattern = "<environment>(.+?)</environment>";
        Matcher matcher = Pattern.compile(responseEnvironmentPattern, Pattern.DOTALL).matcher(this.xml);
        matcher.find();
        return matcher.group(1);
    }

    public ArrayList<Person> getPersons(){
        if(persons != null) return persons;
        persons = new ArrayList<>();
        String extractPersonsDataRegex = "<enPersonRes>.*?</enPersonRes>";
        Matcher matcher = Pattern.compile(extractPersonsDataRegex, Pattern.DOTALL).matcher(xml);
        while(matcher.find()){
           persons.add(new Person(matcher.group()));
        }
        return persons;
    }
}
