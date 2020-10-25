package com.shifthunter.readerfile.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shifthunter.readerfile.model.UserBatch;
import com.shifthunter.readerfile.repository.UserBatchRepository;

@Component
public class DBWriter implements ItemWriter<UserBatch>{

	@Autowired
	private UserBatchRepository userRepository;
	
	@Override
	public void write(List<? extends UserBatch> users) throws Exception {

		System.out.println("Data Saved for UserBatch:" + users.toString());
		userRepository.saveAll(users);
	}

	
//	 NEXT STEP IS TO CREATE THE TRIGGER POIN   "CONTROLLER"
	
}
