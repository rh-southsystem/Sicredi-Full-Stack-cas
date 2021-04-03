package br.com.southsystem.receita.config;

import br.com.southsystem.receita.mapper.AccountMapper;
import br.com.southsystem.receita.model.dto.AccountDTO;
import br.com.southsystem.receita.service.processor.AccountProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    private Resource outputResource = new FileSystemResource("output/processadas.csv");

    @Bean
    public Job readCSVFilesJob() throws Exception {
        return jobBuilderFactory
                .get("readCSVFilesJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("step1").<AccountDTO, AccountDTO>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .faultTolerant()
                .retryLimit(5)
                .retry(RuntimeException.class)
                .build();
    }

    private ItemProcessor<AccountDTO, AccountDTO> processor() {
        return new AccountProcessor();
    }

    @Bean
    public FlatFileItemReader<AccountDTO> reader() {

        FlatFileItemReader<AccountDTO> reader = new FlatFileItemReader<>();

        reader.setResource(new FileSystemResource("input/contas.csv"));

        reader.setLinesToSkip(1);

        reader.setLineMapper(getLineMapper());

        return reader;
    }

    private DefaultLineMapper<AccountDTO> getLineMapper() {

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(";");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("agencia", "conta", "saldo", "status");


        DefaultLineMapper<AccountDTO> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(lineTokenizer);

        AccountMapper accountMapper = new AccountMapper();

        defaultLineMapper.setFieldSetMapper(accountMapper);

        return defaultLineMapper;
    }

    @Bean
    public FlatFileItemWriter<AccountDTO> writer() throws Exception {

        FlatFileItemWriter<AccountDTO> writer = new FlatFileItemWriter<>();
        writer.setResource(outputResource);

        BeanWrapperFieldExtractor<AccountDTO> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        beanWrapperFieldExtractor.setNames(new String[]{"agencia", "conta", "saldo", "status", "resultado"});

        DelimitedLineAggregator<AccountDTO> delimitedLineAggregator = new DelimitedLineAggregator<>();
        delimitedLineAggregator.setDelimiter(";");
        delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);


        writer.setAppendAllowed(false);
        writer.setLineAggregator(delimitedLineAggregator);

        return writer;
    }
}
