package br.com.southsystem.receita.config;


import br.com.southsystem.receita.model.dto.AccountDTO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBatchTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {BatchConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class SpringBatchIntegrationTest {


    private static final String TEST_INPUT = "input/contas.csv";
    private static final String TEST_OUTPUT = "output/processadas.csv";
    private static final String EXPECTED_OUTPUT = "output/expected-output.csv";
    private static final String EXPECTED_INPUT = "input/expected-input.csv";


    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private FlatFileItemReader itemReader;

    @Autowired
    private FlatFileItemWriter itemWriter;

    @After
    public void cleanUp() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    private JobParameters defaultJobParameters() {
        JobParametersBuilder paramsBuilder = new JobParametersBuilder();
        paramsBuilder.addString("file.input", TEST_INPUT);
        paramsBuilder.addString("file.output", TEST_OUTPUT);
        return paramsBuilder.toJobParameters();
    }

    @Test
    public void testeJobExecutedWithSuccess() throws Exception {

        FileSystemResource expectedResult = new FileSystemResource(EXPECTED_OUTPUT);
        FileSystemResource actualResult = new FileSystemResource(TEST_OUTPUT);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(defaultJobParameters());
        JobInstance actualJobInstance = jobExecution.getJobInstance();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

        assertThat(actualJobInstance.getJobName(), is("readCSVFilesJob"));
        assertThat(actualJobExitStatus.getExitCode(), is("COMPLETED"));
        AssertFile.assertFileEquals(expectedResult, actualResult);
    }

    @Test
    public void testReaderCalledWithSuccess() throws Exception {

        StepExecution stepExecution = MetaDataInstanceFactory
                .createStepExecution(defaultJobParameters());

        List<AccountDTO> items = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            List<AccountDTO> result = new ArrayList<>();
            AccountDTO item;
            itemReader.open(stepExecution.getExecutionContext());
            while ((item = (AccountDTO) itemReader.read()) != null) {
                result.add(item);
            }
            itemReader.close();
            return result;
        });

        Assert.assertEquals(getAccountsListReadTest(), items);
    }

    @Test
    public void testStep1ExecutedWithSuccess() throws Exception {

        FileSystemResource expectedResult = new FileSystemResource(EXPECTED_OUTPUT);
        FileSystemResource actualResult = new FileSystemResource(TEST_OUTPUT);


        JobExecution jobExecution = jobLauncherTestUtils.launchStep(
                "step1", defaultJobParameters());
        Collection actualStepExecutions = jobExecution.getStepExecutions();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();


        assertThat(actualStepExecutions.size(), is(1));
        assertThat(actualJobExitStatus.getExitCode(), is("COMPLETED"));
        AssertFile.assertFileEquals(expectedResult, actualResult);
    }

    @Test
    public void testWriterCalledWithSuccess() throws Exception {

        FileSystemResource expectedResult = new FileSystemResource(EXPECTED_OUTPUT);
        FileSystemResource actualResult = new FileSystemResource(TEST_OUTPUT);

        List<AccountDTO> accounts = getAccountsListWriteTest();

        StepExecution stepExecution = MetaDataInstanceFactory
                .createStepExecution(defaultJobParameters());

        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            itemWriter.open(stepExecution.getExecutionContext());
            itemWriter.write(accounts);
            itemWriter.close();
            return accounts;
        });


        AssertFile.assertFileEquals(expectedResult, actualResult);
    }

    private List<AccountDTO> getAccountsListWriteTest() {
        AccountDTO accountLine1 = new AccountDTO();
        accountLine1.setAgencia("0101");
        accountLine1.setConta("12225-6");
        accountLine1.setSaldo(10000.0);
        accountLine1.setStatus("A");
        accountLine1.setResultado(true);

        AccountDTO accountLine2 = new AccountDTO();
        accountLine2.setAgencia("0101");
        accountLine2.setConta("12226-8");
        accountLine2.setSaldo(320050.0);
        accountLine2.setStatus("A");
        accountLine2.setResultado(true);

        AccountDTO accountLine3 = new AccountDTO();
        accountLine3.setAgencia("3202");
        accountLine3.setConta("40011-1");
        accountLine3.setSaldo(-3512.0);
        accountLine3.setStatus("I");
        accountLine3.setResultado(true);

        AccountDTO accountLine4 = new AccountDTO();
        accountLine4.setAgencia("3202");
        accountLine4.setConta("54001-2");
        accountLine4.setSaldo(0.0);
        accountLine4.setStatus("P");
        accountLine4.setResultado(true);

        AccountDTO accountLine5 = new AccountDTO();
        accountLine5.setAgencia("3202");
        accountLine5.setConta("00321-2");
        accountLine5.setSaldo(3450000.0);
        accountLine5.setStatus("B");
        accountLine5.setResultado(true);

        List<AccountDTO> accounts = new java.util.ArrayList<>(Collections.emptyList());

        accounts.add(accountLine1);
        accounts.add(accountLine2);
        accounts.add(accountLine3);
        accounts.add(accountLine4);
        accounts.add(accountLine5);
        return accounts;
    }

    private List<AccountDTO> getAccountsListReadTest() {
        AccountDTO accountLine1 = new AccountDTO();
        accountLine1.setAgencia("0101");
        accountLine1.setConta("12225-6");
        accountLine1.setSaldo(10000.0);
        accountLine1.setStatus("A");

        AccountDTO accountLine2 = new AccountDTO();
        accountLine2.setAgencia("0101");
        accountLine2.setConta("12226-8");
        accountLine2.setSaldo(320050.0);
        accountLine2.setStatus("A");

        AccountDTO accountLine3 = new AccountDTO();
        accountLine3.setAgencia("3202");
        accountLine3.setConta("40011-1");
        accountLine3.setSaldo(-3512.0);
        accountLine3.setStatus("I");

        AccountDTO accountLine4 = new AccountDTO();
        accountLine4.setAgencia("3202");
        accountLine4.setConta("54001-2");
        accountLine4.setSaldo(0.0);
        accountLine4.setStatus("P");

        AccountDTO accountLine5 = new AccountDTO();
        accountLine5.setAgencia("3202");
        accountLine5.setConta("00321-2");
        accountLine5.setSaldo(3450000.0);
        accountLine5.setStatus("B");

        List<AccountDTO> accounts = new java.util.ArrayList<>(Collections.emptyList());

        accounts.add(accountLine1);
        accounts.add(accountLine2);
        accounts.add(accountLine3);
        accounts.add(accountLine4);
        accounts.add(accountLine5);
        return accounts;
    }
}