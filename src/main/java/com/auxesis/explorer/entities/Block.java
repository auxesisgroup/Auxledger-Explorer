package com.auxesis.explorer.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Block")
public class Block {

	// @GeneratedValue(strategy = GenerationType.AUTO)
	String id;
	Long blockNumber;
	@Id
	String blockHash;
	Double difficulty;
	String extraData;
	String minerAddress;
	Long nonce;
	String parentHash;
	String receiptTxRoot;
	Double size;
	String stateRoot;
	Long timestampVal;
	String txTrieRoot;
	String mixHash;
	String bloom;
	String solution;
	Double totalDifficulty;
	Long numTransactions;
	Double blockReward;
	Long blockTime;
	Double gasLimit;
	Double gasUsed;

	public String getMixHash() {
		return mixHash;
	}

	public void setMixHash(String mixHash) {
		this.mixHash = mixHash;
	}

	public Double getGasLimit() {
		return gasLimit;
	}

	public void setGasLimit(Double gasLimit) {
		this.gasLimit = gasLimit;
	}

	public Double getGasUsed() {
		return gasUsed;
	}

	public void setGasUsed(Double gasUsed) {
		this.gasUsed = gasUsed;
	}

	public void setNumTransactions(Long numTransactions) {
		this.numTransactions = numTransactions;
	}

	public Long getBlockTime() {
		return blockTime;
	}

	public void setBlockTime(Long blockTime) {
		this.blockTime = blockTime;
	}

	public String getBlockHash() {
		return blockHash;
	}

	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
	}

	public Double getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Double difficulty) {
		this.difficulty = difficulty;
	}

	public String getExtraData() {
		return extraData;
	}

	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}

	public String getMinerAddress() {
		return minerAddress;
	}

	public void setMinerAddress(String minerAddress) {
		this.minerAddress = minerAddress;
	}

	public Long getNonce() {
		return nonce;
	}

	public void setNonce(Long nonce) {
		this.nonce = nonce;
	}

	public Long getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(Long blockNumber) {
		this.blockNumber = blockNumber;
	}

	public String getParentHash() {
		return parentHash;
	}

	public void setParentHash(String parentHash) {
		this.parentHash = parentHash;
	}

	public String getReceiptTxRoot() {
		return receiptTxRoot;
	}

	public void setReceiptTxRoot(String receiptTxRoot) {
		this.receiptTxRoot = receiptTxRoot;
	}

	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}

	public String getStateRoot() {
		return stateRoot;
	}

	public void setStateRoot(String stateRoot) {
		this.stateRoot = stateRoot;
	}

	public Long getTimestampVal() {
		return timestampVal;
	}

	public void setTimestampVal(Long timestampVal) {
		this.timestampVal = timestampVal;
	}

	public String getTxTrieRoot() {
		return txTrieRoot;
	}

	public void setTxTrieRoot(String txTrieRoot) {
		this.txTrieRoot = txTrieRoot;
	}

	public String getBloom() {
		return bloom;
	}

	public void setBloom(String bloom) {
		this.bloom = bloom;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public Double getTotalDifficulty() {
		return totalDifficulty;
	}

	public void setTotalDifficulty(Double totalDifficulty) {
		this.totalDifficulty = totalDifficulty;
	}

	public Long getNumTransactions() {
		return numTransactions;
	}

	public void setNumTransactions(long i) {
		this.numTransactions = i;
	}

	public Double getBlockReward() {
		return blockReward;
	}

	public void setBlockReward(Double blockReward) {
		this.blockReward = blockReward;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Block [id=" + id + ", blockHash=" + blockHash + ", difficulty=" + difficulty + ", extraData="
				+ extraData + ", minerAddress=" + minerAddress + ", nonce=" + nonce + ", nrgConsumed=" + ", nrgLimit="
				+ ", blockNumber=" + blockNumber + ", parentHash=" + parentHash + ", receiptTxRoot=" + receiptTxRoot
				+ ", size=" + size + ", stateRoot=" + stateRoot + ", timestampVal=" + timestampVal + ", txTrieRoot="
				+ txTrieRoot + ", bloom=" + bloom + ", solution=" + solution + ", totalDifficulty=" + totalDifficulty
				+ ", numTransactions=" + numTransactions + ", blockReward=" + blockReward + ", blockTime=" + blockTime
				+ "]";
	}

	public void setId(String id) {
		this.id = id;
	}

}
