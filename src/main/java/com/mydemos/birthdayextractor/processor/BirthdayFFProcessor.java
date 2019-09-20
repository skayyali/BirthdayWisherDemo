package com.mydemos.birthdayextractor.processor;

import com.mydemos.birthdayextractor.model.Person;
import com.mydemos.birthdayextractor.writer.EMailWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class BirthdayFFProcessor implements ItemProcessor<Person, Person> {

    @Autowired
    private EMailWriter eMailWriter;

    @Override
    public Person process(Person item) throws Exception {
        String ordinalIndicator;
        if (item.getAge() % 10 == 1)
            ordinalIndicator = "st";
        else if (item.getAge() % 10 == 2)
            ordinalIndicator = "nd";
        else if (item.getAge() % 10 == 3)
            ordinalIndicator = "rd";
        else
            ordinalIndicator = "th";

        String emailMessage = "Hey, " + item.getName().split(" ")[0] + "!\n" +
                "Happy " + item.getAge() + ordinalIndicator + " Birthday!\n\n" +
                "Sincerely,\nSaid's Birthday Robot";

        eMailWriter.sendBirthdayEmail("Said", "skayyali@hotmail.com", emailMessage);

        return null;
    }
}
