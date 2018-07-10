package com.auxesis.explorer.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.auxesis.explorer.entities.Block;

@Repository
public interface BlockJpaRepository extends PagingAndSortingRepository<Block, String> {

	List<Block> findByBlockNumber(@Param("blockNumber") Long blockNumber);

	List<Block> findByMinerAddress(@Param("minerAddress") String minerAddress);

	Page<Block> findAll(Pageable pageable);

	List<Block> findByBlockHash(@Param("blockHash") String blockHash);

	List<Block> findTop8640ByOrderByBlockNumberDesc();

	List<Block> findTop20ByOrderByBlockNumberDesc();

	List<Block> findTop1ByOrderByBlockNumberDesc();

	Block save(Block entity);

	long count();
	boolean existsByBlockHash(String blockHash);
}
