package com.mydemos.birthdayextractor.processor;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import com.mydemos.birthdayextractor.writer.EMailWriter;
import org.springframework.batch.item.ItemProcessor;

import com.mydemos.birthdayextractor.model.Person;
import org.springframework.beans.factory.annotation.Autowired;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {
	@Autowired
	private EMailWriter test;

	@Override
	public Person process(Person personItem) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		DateTimeFormatter format = DateTimeFormatter.ofPattern("MM-dd");
		SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
		LocalDate today = LocalDate.now();
		String todaysDate = format.format(today);
		
		if(sdf.format(personItem.getDob()).equals(todaysDate)) {
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int yearBorn = Integer.parseInt(sdfYear.format(personItem.getDob()));
			personItem.setAge(year - yearBorn);
			test.sendBirthdayEmail("Said", "skayyali@hotmail.com", "Happy Birthday DEMO");
			return personItem;
		}
		else
			return null;
	}
}
