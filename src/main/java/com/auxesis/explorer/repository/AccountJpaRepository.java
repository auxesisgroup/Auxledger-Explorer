package com.auxesis.explorer.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.auxesis.explorer.entities.Account;


@Repository
public interface AccountJpaRepository extends PagingAndSortingRepository<Account, Long> {
	List<Account> findByAddr(@Param("addr") String addr);
}
