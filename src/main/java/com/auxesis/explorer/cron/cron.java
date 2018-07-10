/*package com.auxesis.explorer.cron;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.ethereum.util.BIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;

import com.auxesis.explorer.entities.Block;
import com.auxesis.explorer.entities.Transaction;
import com.auxesis.explorer.repository.BlockJpaRepository;
import com.auxesis.explorer.repository.TransactionJpaRepository;

import rx.Subscription;

@Service
public class cron implements CommandLineRunner {

	@Autowired
	private BlockJpaRepository blockJpaRepository;

	@Autowired
	private TransactionJpaRepository transactionJpaRepository;

	private String webUrl = "http://159.89.14.24:8545";
	private Web3j web3j = Web3j.build(new HttpService(webUrl));
	private static final int COUNT = 10;

	final static Logger log = LoggerFactory.getLogger(cron.class);

	private static final int thou = 1000;

	public String calcNetHashRate(EthBlock.Block block) {
		String response = "Net hash rate not available";
		try {
			if (block.getNumber().intValue() > thou) {
				long timeDelta = 0;
				for (int i = 0; i < thou; ++i) {
					EthBlock parent = web3j.ethGetBlockByHash(block.getParentHash(), true).sendAsync().get();
					timeDelta += Math
							.abs(block.getTimestamp().longValue() - parent.getResult().getTimestamp().longValue());
				}
				response = String.valueOf(block.getDifficulty().divide(BIUtil.toBI(timeDelta / thou))
						.divide(new BigInteger("1000000000")).doubleValue()) + " GH/s";
				log.error("response  :  \n" + response);
			}
		} catch (Exception e) {
			log.error("Exception in cron  :  \n" + e.getMessage());
		}
		return response;
	}

	@Override
	public void run(String... args) {
		try {
			CountDownLatch countDownLatch = new CountDownLatch(COUNT);
			while (true) {
				BigInteger blockNumber = web3j.ethBlockNumber().send().getBlockNumber();
				List<Block> dbBlockNum = blockJpaRepository.findTop1ByOrderByBlockNumberDesc();
				if (dbBlockNum.size() == 0) {
					log.info("Blockchain sync..... :  \n");
					block(0);
					log.info("Blockchain sync finished..... :  \n");
				}
				long dbBlocknumber = dbBlockNum.get(0).getBlockNumber();
				long startBlock = dbBlocknumber + 1;
				long endBlock = blockNumber.longValue();
				log.info("check block number :Last block of db : " + dbBlocknumber + " Last block of blockchain : "
						+ endBlock);
				if (dbBlocknumber < endBlock) {
					block(startBlock);
				} else {
					log.info("No block found !!!!!! : Last block of db : " + dbBlocknumber
							+ " Last block of blockchain : " + endBlock);
				}
				// if (dbBlocknumber == endBlock) {
				// log.info("No block found !!!!!! : Last block of db : " + dbBlocknumber
				// + " Last block of blockchain : " + endBlock);
				// } else {
				// if (dbBlocknumber < endBlock) {
				// block(startBlock);
				// }
				// }
				countDownLatch.await(5, TimeUnit.SECONDS);
			}
		} catch (Exception e) {
			log.error("Exception in cron  :  \n" + e.getMessage());
		}
	}

	public void block(long startBlock) throws InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(COUNT);
		Subscription subscription = web3j
				.catchUpToLatestBlockObservable(new DefaultBlockParameterNumber(BigInteger.valueOf(startBlock)), true)
				.subscribe(ethBlock -> {
					EthBlock.Block block = ethBlock.getBlock();
					calcNetHashRate(block);
					List<EthBlock.TransactionResult> transactionList = block.getTransactions();
					if (!blockJpaRepository.existsByBlockHash(block.getHash())) {
						log.info("########################################################## \n");
						log.info("New block found : " + block.getNumber() + "block hash " + block.getHash());
						LocalDateTime timestamp = Instant.ofEpochSecond(block.getTimestamp().longValueExact())
								.atZone(ZoneId.of("UTC")).toLocalDateTime();
						int transactionCount = block.getTransactions().size();
						String hash = block.getHash();
						Block b = new Block();
						b.setId(UUID.randomUUID().toString());
						b.setBlockHash(block.getParentHash());
						b.setBlockNumber(block.getNumber().longValue());
						b.setDifficulty(block.getDifficulty().doubleValue());
						b.setExtraData(block.getExtraData());
						b.setMinerAddress(block.getMiner());
						b.setNonce(block.getNonce().longValue());
						b.setBlockNumber(block.getNumber().longValue());
						b.setParentHash(block.getParentHash());
						b.setReceiptTxRoot(block.getReceiptsRoot());
						b.setSize(block.getSize().doubleValue());
						b.setStateRoot(block.getStateRoot());
						b.setTimestampVal(block.getTimestamp().longValue());
						b.setBloom(block.getLogsBloom());
						b.setNumTransactions(transactionCount);
						b.setTxTrieRoot(block.getTransactionsRoot());
						b.setTotalDifficulty(block.getTotalDifficulty().doubleValue());
						b.setGasUsed(block.getGasUsed().doubleValue());
						b.setGasLimit(block.getGasUsed().doubleValue());
						b.setReceiptTxRoot(block.getReceiptsRoot());
						b.setMixHash(block.getMixHash());
						blockJpaRepository.save(b);
						List<Transaction> allTx = new ArrayList<Transaction>();
						for (int i = 0; i < transactionList.size(); i++) {
							EthBlock.TransactionObject _txObj = (EthBlock.TransactionObject) transactionList.get(i);
							Transaction transaction = new Transaction();
							transaction.setBlockHash(_txObj.getBlockHash());
							transaction.setBlockNumber(_txObj.getBlockNumber().longValue());
							transaction.setFromAddr(_txObj.getFrom());
							transaction.setNonce(_txObj.getNonce().toString());
							transaction.setValue(_txObj.getValue().doubleValue());
							transaction.setToAddr(_txObj.getTo());
							transaction.setTransactionLog(_txObj.getTransactionIndexRaw());
							transaction.setTransactionHash(_txObj.getHash());
							transaction.setId(UUID.randomUUID().toString());
							transaction.setGasPrice(_txObj.getGas().doubleValue());
							transaction.setGasUsed(_txObj.getGas().doubleValue());
							allTx.add(transaction);
						}
						transactionJpaRepository.save(allTx);
						log.info("\n " + allTx.size() + "New Tx save in block : " + block.getNumber());
					} else {
						log.info("This block already present in bd: " + block.getNumber());
					}
				});
	}
}
*/