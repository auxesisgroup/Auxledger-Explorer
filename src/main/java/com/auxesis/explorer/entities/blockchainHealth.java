package com.auxesis.explorer.entities;

public class blockchainHealth {

	long currentBlock;
	long totalNumberOfTRansaction;
	long transactionRate;
	long NetworkHashRate;
	long networkDifficulty;
	long numberOfConnecedNodes;
	long numberOfAppllicationRunning;

	public long getCurrentBlock() {
		return currentBlock;
	}

	public void setCurrentBlock(long currentBlock) {
		this.currentBlock = currentBlock;
	}

	public long getTotalNumberOfTRansaction() {
		return totalNumberOfTRansaction;
	}

	public void setTotalNumberOfTRansaction(long totalNumberOfTRansaction) {
		this.totalNumberOfTRansaction = totalNumberOfTRansaction;
	}

	public long getTransactionRate() {
		return transactionRate;
	}

	public void setTransactionRate(long transactionRate) {
		this.transactionRate = transactionRate;
	}

	public long getNetworkHashRate() {
		return NetworkHashRate;
	}

	public void setNetworkHashRate(long networkHashRate) {
		NetworkHashRate = networkHashRate;
	}

	public long getNetworkDifficulty() {
		return networkDifficulty;
	}

	public void setNetworkDifficulty(long networkDifficulty) {
		this.networkDifficulty = networkDifficulty;
	}

	public long getNumberOfConnecedNodes() {
		return numberOfConnecedNodes;
	}

	public void setNumberOfConnecedNodes(long numberOfConnecedNodes) {
		this.numberOfConnecedNodes = numberOfConnecedNodes;
	}

	public long getNumberOfAppllicationRunning() {
		return numberOfAppllicationRunning;
	}

	public void setNumberOfAppllicationRunning(long numberOfAppllicationRunning) {
		this.numberOfAppllicationRunning = numberOfAppllicationRunning;
	}

}
