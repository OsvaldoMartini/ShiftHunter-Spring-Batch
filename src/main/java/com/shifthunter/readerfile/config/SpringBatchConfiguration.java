package com.shifthunter.readerfile.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import com.shifthunter.readerfile.model.UserBatch;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfiguration {

	
//	... extends DefaultBatchConfigurer 
//	@Autowired
//	private DataSource dataSource;
//
//	@Autowired
//	private PlatformTransactionManager transactionManager;
//
//	@Override
//	protected JobRepository createJobRepository() throws Exception {
//		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
//		factory.setDataSource(dataSource);
//		factory.setTransactionManager(transactionManager);
//		factory.setTablePrefix("ShiftHunter.BATCH_");
//		factory.afterPropertiesSet();
//		return factory.getObject();
//	}

	@Bean
	public Job job(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
			ItemReader<UserBatch> itemReader, ItemProcessor<UserBatch, UserBatch> itemProcessor,
			ItemWriter<UserBatch> itemWriter) {
		// Any time I run the Batch it will increment One by One (1)
		// ## * "RunIdIncrementer" It can be customized for personal Usages like "names"
		// Take a Lonn on the Implementation "Increment the run.id parameter (starting
		// with 1)."
		// Register the Step
		// When Exists Many Steps it Can Use Start / Flow /, and Step Calls

		Step step = stepBuilderFactory.get("ETL-file-load")
//				.<UserBatch, UserBatch>chunk(100) // Do in chunks Reading as UserBatch to Put in UserBatch
				.<UserBatch, UserBatch>chunk(100) // Do in chunks Reading as UserBatch to Put in UserBatch
				.reader(itemReader).processor(itemProcessor).writer(itemWriter).build();

		return jobBuilderFactory.get("ETL-Load").incrementer(new RunIdIncrementer())
//				.flow(step1)
//				.next(step2)
				.start(step).build();

	}

	// Now Creating the Reader
	// With FlatFileItemReader
	// InBuilt Class have few functions
	// Auto wire with @Value
	@Bean
	public FlatFileItemReader<UserBatch> itemReader(@Value("${input}") Resource resource) {
		FlatFileItemReader<UserBatch> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setResource(resource);
		flatFileItemReader.setName("CSV-Reader");
		// Header first Line
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setLineMapper(lineMapper());
		return flatFileItemReader;
	}

	@Bean
	public LineMapper<UserBatch> lineMapper() {
		DefaultLineMapper<UserBatch> defaultLineMapper = new DefaultLineMapper<>(); // Accepts types of Map
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer(); // To Identify the Delimeter

		lineTokenizer.setDelimiter(",");

// ### -> Public setter for the strict flag. If true (the default) then number of tokens in line must match the number of tokens defined (by Range, columns, etc.) in LineTokenizer. 
// ### -> If false then lines with less tokens will be tolerated and padded with empty columns, and lines with more tokens will simply be truncated.
		lineTokenizer.setStrict(false); // I don't want to be Strict ?????? What this doest mean ????

		lineTokenizer.setDelimiter(",");
		// Now the names of the columns
		lineTokenizer.setNames(new String[] { "id", "name", "dept", "salary" });

		defaultLineMapper.setLineTokenizer(lineTokenizer);

//	###	Setting each field to the Pojo -> "UserBatch"
		BeanWrapperFieldSetMapper<UserBatch> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(UserBatch.class);

//	###	shove into DefaultLineMapper 
		defaultLineMapper.setFieldSetMapper(fieldSetMapper);
		return defaultLineMapper;
	}

//	####  NEXT STEP IS TO CREATE THE "PROCESSOR"

}
