package com.shifthunter.readerfile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shifthunter.readerfile.model.UserBatch;

public interface UserBatchRepository extends JpaRepository<UserBatch, Long> {

}
