package com.shifthunter.readerfile.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoadController {

	@Autowired // Job Laucher it Runs the Job
	JobLauncher jobLauncher; // Job Laucher it Runs the Job

	@Autowired
	Job job;

//	@GetMapping
	@RequestMapping("/load")
	public BatchStatus load(Model model) throws JobParametersInvalidException, JobExecutionException{

		Map<String, JobParameter> mapParams = new HashMap<>();
		mapParams.put("time", new JobParameter(System.currentTimeMillis()));

		JobParameters parameters = new JobParameters(mapParams);

		JobExecution jobExecution = jobLauncher.run(job, parameters);
		
		System.out.println("JobExecution: " + jobExecution.getStatus());

		while (jobExecution.isRunning()) {
			System.out.println("..");
		}

		return jobExecution.getStatus();

	}

}
