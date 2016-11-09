package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response;

import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.person.Person;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestContext;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
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

    @JsonIgnore
    private TpsRequestContext context;

    public ServiceRoutineResponse(String xml, Object data, TpsRequestContext context) {
        this.xml = xml;
        this.data = data;
        this.context = context;
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

    public String getEnvironment() {
        return context.getEnvironment();
    }

    public List<Person> getPersons(){
        if(persons != null){
            return persons;
        }
        persons = new ArrayList<>();
        String extractPersonsDataRegex = "<enPersonRes>.*?</enPersonRes>";
        Matcher matcher = Pattern.compile(extractPersonsDataRegex, Pattern.DOTALL).matcher(xml);
        while(matcher.find()){
           persons.add(new Person(matcher.group()));
        }
        return persons;
    }
}
