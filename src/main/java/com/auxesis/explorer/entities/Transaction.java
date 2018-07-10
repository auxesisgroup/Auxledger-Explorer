package com.auxesis.explorer.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Transaction")
public class Transaction {

	// @GeneratedValue(strategy = GenerationType.AUTO)
	String id;
	@Id
	String transactionHash;
	Long blockNumber;
	String data;
	String fromAddr;
	String toAddr;
	String nonce;
	Double gasUsed;
	Double gasPrice;
	Double value;
	String transactionLog;
	Long timestampVal;
	String blockHash;
	int status;;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getBlockHash() {
		return blockHash;
	}

	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
	}

	public String getTransactionHash() {
		return transactionHash;
	}

	public void setTransactionHash(String transactionHash) {
		this.transactionHash = transactionHash;
	}

	public Long getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(Long blockNumber) {
		this.blockNumber = blockNumber;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getFromAddr() {
		return fromAddr;
	}

	public void setFromAddr(String fromAddr) {
		this.fromAddr = fromAddr;
	}

	public String getToAddr() {
		return toAddr;
	}

	public void setToAddr(String toAddr) {
		this.toAddr = toAddr;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public Double getGasUsed() {
		return gasUsed;
	}

	public void setGasUsed(Double gasUsed) {
		this.gasUsed = gasUsed;
	}

	public Double getGasPrice() {
		return gasPrice;
	}

	public void setGasPrice(Double gasPrice) {
		this.gasPrice = gasPrice;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getTransactionLog() {
		return transactionLog;
	}

	public void setTransactionLog(String transactionLog) {
		this.transactionLog = transactionLog;
	}

	public Long getTimestampVal() {
		return timestampVal;
	}

	public void setTimestampVal(Long timestampVal) {
		this.timestampVal = timestampVal;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", transactionHash=" + transactionHash + ", blockNumber=" + blockNumber
				+ ", data=" + data + ", fromAddr=" + fromAddr + ", toAddr=" + toAddr + ", nonce=" + nonce
				+ ", nrgConsumed=" + gasUsed + ", nrgPrice=" + gasPrice + ", value=" + value + ", transactionLog="
				+ transactionLog + ", timestampVal=" + timestampVal + ", blockHash=" + blockHash + "]";
	}
}
