package com.auxesis.explorer.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.auxesis.explorer.entities.Block;
import com.auxesis.explorer.entities.Transaction;

@Repository
public interface TransactionJpaRepository extends PagingAndSortingRepository<Transaction, String> {
	Page<Transaction> findByFromAddrOrToAddr(@Param("fromAddr") String fromAddr, @Param("toAddr") String toAddr,
			Pageable pageable);

	List<Transaction> findByFromAddrOrToAddr(@Param("fromAddr") String fromAddr, @Param("toAddr") String toAddr);

	List<Transaction> findByBlockNumber(@Param("blockNumber") Long blockNumber);

	List<Transaction> findByBlockHash(@Param("blockHash") String blockHash);

	List<Transaction> findByTransactionHash(@Param("transactionHash") String transactionHash);

	List<Transaction> findByToAddr(@Param("toAddr") String toAddr);

	List<Transaction> findByFromAddr(@Param("fromAddr") String fromAddr);

	List<Transaction> findTop10ByOrderByBlockNumberDesc();

	List<Transaction> findTop128ByOrderByBlockNumberDesc();

	Transaction save(List<Transaction> entity);

	long count();

	boolean existsByTransactionHash(String txHash);
}
