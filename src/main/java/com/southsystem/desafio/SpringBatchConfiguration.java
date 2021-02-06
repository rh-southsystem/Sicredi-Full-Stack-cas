package com.southsystem.desafio;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfiguration {

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                   ItemReader<BankAccountVO> itemReader,
                   ItemProcessor<BankAccountVO, BankAccountVO> itemProcessor,
                   ItemWriter<BankAccountVO> itemWriter) {

        Step step = stepBuilderFactory.get("step1")
                .<BankAccountVO, BankAccountVO>chunk(100)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();

        return jobBuilderFactory.get("ProcessCSVFileAccount")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public FlatFileItemReader<BankAccountVO> reader() {
        FlatFileItemReader<BankAccountVO> fileItemReader = new FlatFileItemReader<>();
        fileItemReader.setResource(new ClassPathResource("contas.csv"));
        fileItemReader.setEncoding("ISO-8859-3");
        fileItemReader.setName("CSV-Reader");
        fileItemReader.setLinesToSkip(1);
        fileItemReader.setLineMapper(lineMapper());
        return fileItemReader;
    }

    @Bean
    public FlatFileItemWriter<BankAccountVO> writer() {
        BeanWrapperFieldExtractor<BankAccountVO> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        beanWrapperFieldExtractor.setNames(new String[] { "agencia", "conta", "saldo", "status", "resultado"});

        DelimitedLineAggregator<BankAccountVO> delimitedLineAggregator = new DelimitedLineAggregator();
        delimitedLineAggregator.setDelimiter(";");
        delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

        FlatFileItemWriter<BankAccountVO> fileItemWriter = new FlatFileItemWriter<>();
        fileItemWriter.setResource(new FileSystemResource("contasProcessadas.csv"));
        fileItemWriter.setAppendAllowed(false);
        fileItemWriter.setLineAggregator(delimitedLineAggregator);

        return fileItemWriter;
    }

    @Bean
    public LineMapper<BankAccountVO> lineMapper() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(";");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("agencia", "conta", "saldo", "status");

        BankAccountFieldSetMapper dailyInformFieldSetMapper = new BankAccountFieldSetMapper();

        DefaultLineMapper<BankAccountVO> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(dailyInformFieldSetMapper);

        return defaultLineMapper;
    }

}
