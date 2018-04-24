package no.nav.tps.forvalteren.service.command.testdatamal;

import java.lang.reflect.Field;
import java.util.List;

import lombok.Getter;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonMal;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetDefinedFieldValues;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetRandomFieldValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PersonmalPersonMapper {

    List<String> listOfRandomFields;

    @Autowired
    private SetRandomFieldValues setRandomFieldValues;

    @Autowired
    private SetDefinedFieldValues setDefinedFieldValues;


    public Person execute(RsPersonMal inputPerson, Person person) {
        for (Field field : inputPerson.getClass().getDeclaredFields()) {
            try {

                Object fieldValue = checkFieldValue(inputPerson, field);

                if (fieldValue != null) {

                    if (fieldValue.equals("*")) {
                        setRandomFieldValues.execute(checkFieldName(field), person);
                    } else {
                        setDefinedFieldValues.execute(checkFieldName(field), fieldValue, person);
                    }
                }

            } catch (IllegalAccessException ex) {

                System.out.println("failed");
            }
        }
        return person;
    }

    private Object checkFieldValue(Object thisObj, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        return field.get(thisObj);
    }

    private String checkFieldName(Field field) {
        return field.getName();
    }
}
