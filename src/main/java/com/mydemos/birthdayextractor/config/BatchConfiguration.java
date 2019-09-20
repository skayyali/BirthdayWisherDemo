package com.mydemos.birthdayextractor.config;

import javax.sql.DataSource;

import com.mydemos.birthdayextractor.processor.BirthdayFFProcessor;
import com.mydemos.birthdayextractor.writer.NoOpItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.mydemos.birthdayextractor.mapper.PersonRowMapper;
import com.mydemos.birthdayextractor.model.Person;
import com.mydemos.birthdayextractor.processor.PersonItemProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public DataSource dataSource;

	@Bean
	public JdbcCursorItemReader<Person> dbReader() {
		JdbcCursorItemReader<Person> reader = new JdbcCursorItemReader<Person>();
		reader.setDataSource(dataSource);
		reader.setSql("SELECT id, name, dob FROM person");
		reader.setRowMapper(new PersonRowMapper());

		return reader;
	}
	
	@Bean
	public PersonItemProcessor dbProcessor(){
		return new PersonItemProcessor();
	}
	
	@Bean
	public FlatFileItemWriter<Person> ffWriter() {
		FlatFileItemWriter<Person> writer = new FlatFileItemWriter<Person>();
		writer.setResource(new FileSystemResource("output/Todays_Birthdays.csv"));

		writer.setLineAggregator(new DelimitedLineAggregator<Person>() {{
			setDelimiter(", ");
			setFieldExtractor(new BeanWrapperFieldExtractor<Person>() {{
				setNames(new String[] { "id", "name", "age" });
			}});
		}});

		return writer;
	}

	@Bean
	public FlatFileItemReader<Person> ffReader() {
		FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
		reader.setResource(new FileSystemResource("output/Todays_Birthdays.csv"));

		reader.setLineMapper(new DefaultLineMapper() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
					setNames(new String[]{"id", "firstName", "age"});
			}});
			setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
					setTargetType(Person.class);
			}});

		}});
		return reader;
	}

	@Bean
	public BirthdayFFProcessor ffProcessor(){
		return new BirthdayFFProcessor();
	}

	@Bean
	public NoOpItemWriter<Person> noOpWriter()
	{
		return new NoOpItemWriter<Person>();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<Person, Person> chunk(10)
				.reader(dbReader())
				.processor(dbProcessor())
				.writer(ffWriter())
				.build();
	}

	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2").<Person, Person>chunk(5)
				.reader(ffReader())
				.processor(ffProcessor())
				.writer(noOpWriter())
				.build();
	}

	@Bean
	public Job exportUserJob() {
		return jobBuilderFactory.get("exportBirthdayJob")
				.incrementer(new RunIdIncrementer())
				.flow(step1())
				.next(step2())
				.end()
				.build();
	}
}
