package com.toptal.soccer.repo;

import com.toptal.soccer.model.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;



public interface TransferRepo extends CrudRepository<Transfer, Long> {

    Page<Transfer> findAll(Pageable pageable);
}
