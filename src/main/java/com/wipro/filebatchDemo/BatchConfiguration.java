package com.wipro.filebatchDemo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableBatchProcessing
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class BatchConfiguration {
	
	@Autowired
    private JobBuilderFactory jobBuilderFactory;
     
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    
    private int maxThreads=5;
	
    @Bean//To be used in Step
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
		taskExecutor.setConcurrencyLimit(maxThreads);
		return taskExecutor;
	}

    @Bean
    public Job imporFileJob() {
        return jobBuilderFactory.get("imporFileJob")
            .incrementer(new RunIdIncrementer())
            .flow(step1())
            .end()
            .build();
    }
 
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").<LineModel, LineModel>chunk(5)
        		.reader(reader())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .throttleLimit(maxThreads).build();
    }
 
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public synchronized FlatFileItemReader<LineModel> reader()
    {
     /*   //Create reader instance
        FlatFileItemReader<String> reader = new FlatFileItemReader<String>();
         
        //Set input file location//
        //USE OWN FILE HERE OR WEB IMPORTED FILE
        reader.setResource(new ClassPathResource("input.txt"));
         
        
        //Configure how each line will be parsed and mapped to different values
        reader.setLineMapper(new DefaultLineMapper() {
            {
                //3 columns in each row
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] {"Token"});
                    }
                });
                //Set values in String class
                setFieldSetMapper(new BeanWrapperFieldSetMapper<String>() {
                    {
                        setTargetType(String.class);
                    }
                });
            }
        });
        return reader;*/
    	return new FlatFileItemReaderBuilder()
                .name("FileItemReader")
                .resource(new ClassPathResource("input.txt"))
                .delimited()
                .names(new String[]{"lineToProcess"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper() {{
                    setTargetType(LineModel.class);
                }})
                .build();
    }
    @Bean
	public FileItemProcessor pocessor() {
    	return new FileItemProcessor();
    }
 
    
	@Bean
	public FileItemWriter<LineModel> writer()
	{
	    return new FileItemWriter<LineModel>();
	}
	}