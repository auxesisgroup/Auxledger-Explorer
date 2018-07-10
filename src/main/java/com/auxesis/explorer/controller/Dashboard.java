package com.auxesis.explorer.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.http.HttpService;
import com.auxesis.explorer.entities.Block;
import com.auxesis.explorer.entities.Response;
import com.auxesis.explorer.entities.Transaction;
import com.auxesis.explorer.entities.blockchainHealth;
import com.auxesis.explorer.entities.data;
import com.auxesis.explorer.repository.AccountJpaRepository;
import com.auxesis.explorer.repository.BlockJpaRepository;
import com.auxesis.explorer.repository.TransactionJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import rx.Subscription;

@RestController
public class Dashboard {

	@Autowired
	private BlockJpaRepository blockJpaRepository;

	@Autowired
	private TransactionJpaRepository transactionJpaRepository;

	@Autowired
	private AccountJpaRepository accountJpaRepository;

	// @Autowired
	// private LastBlockReadJpaRepository lastBlockReadJpaRepository;

	// @Autowired
	// CloudantClient cloudant;
	
	private Web3j web3j = Web3j.build(new HttpService(webUrl));
	private static final int COUNT = 10;
	private static Logger log = LoggerFactory.getLogger(Dashboard.class);

	@RequestMapping(value = "/check", method = RequestMethod.GET)
	public String ini2t() throws InterruptedException, ExecutionException {
		EthGetCode code = web3j
				.ethGetCode("0x3c7d6ef048c47f934d86a9b41befce472b6d83c5", DefaultBlockParameterName.LATEST).sendAsync()
				.get();
		code.getCode();
		return code.getCode();
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<Object> init() {
		Response res = new Response();
		try {
			res.setResult("started.....");
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/blockchainHealth", method = RequestMethod.GET)
	public ResponseEntity<Object> blockchainHealth() {
		Response res = new Response();
		try {
			blockchainHealth bh = new blockchainHealth();
			// bh.setTotalNumberOfTRansaction(transactionJpaRepository.count());
			// bh.setCurrentBlock(blockJpaRepository.count());
			// bh.setNetworkHashRate(web3j.ethHashrate().sendAsync().get().getHashrate().longValue());
			// // bh.setNetworkHashRate(web3j.node);
			// res.setResult(bh);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			res.setResult(null);
			res.setError("Exception :" + e);
			return new ResponseEntity<Object>(res, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<Object> search(@RequestParam(value = "searchParam", required = false) String searchParam)
			throws JSONException {
		Response res = new Response();
		data _data = new data();
		try {
			if (searchParam == null && searchParam.trim().isEmpty()) {
				res.setResult(null);
				res.setError("missing search param");
				return new ResponseEntity<Object>(res, HttpStatus.OK);
			}
			List<Transaction> transactionList = transactionJpaRepository.findByTransactionHash(searchParam);
			JSONArray jsonArray = new JSONArray();
			if (transactionList != null && transactionList.size() > 0) {
				Transaction transaction = transactionList.get(0);
				_data.setType("transaction");
				_data.setRes(transaction);
				res.setResult(_data);
				return new ResponseEntity<Object>(res, HttpStatus.OK);
			}
			List<Block> blockList = blockJpaRepository.findByBlockHash(searchParam);
			if (blockList != null && blockList.size() > 0) {
				Block block = blockList.get(0);
				_data.setType("block");
				_data.setRes(block);
				res.setResult(_data);
				return new ResponseEntity<Object>(res, HttpStatus.OK);
			}
			String addressSearch = searchParam;
			if (searchParam.trim().equals("0"))
				addressSearch = "0000000000000000000000000000000000000000000000000000000000000000";
			List<Transaction> accountList = transactionByAccountAddress(addressSearch);
			if (accountList != null && accountList.size() > 0) {
				_data.setType("account");
				_data.setRes(accountList);
				res.setResult(_data);
				return new ResponseEntity<Object>(res, HttpStatus.OK);
			}
			if (validLong(searchParam)) {
				List<Block> blockLists = blockJpaRepository.findByBlockNumber(Long.parseLong(searchParam));
				if (blockLists != null && blockLists.size() > 0) {
					Block block = blockLists.get(0);
					_data.setType("block");
					_data.setRes(blockLists);
					res.setResult(_data);
					return new ResponseEntity<Object>(res, HttpStatus.OK);
				}
			}
			res.setError("No data found.");
			return new ResponseEntity<Object>(res, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			res.setResult(null);
			res.setError("Exception :");
			return new ResponseEntity<Object>(res, HttpStatus.OK);

		}
	}

	@RequestMapping(value = "/blockByBlockNumberOrBlockHash", method = RequestMethod.GET)
	public ResponseEntity<Object> findByBlockNumberOrBlockHash(
			@RequestParam(value = "searchParam", required = false) String searchParam) {
		Response res = new Response();
		try {
			if (searchParam == null && searchParam.trim().isEmpty()) {
				res.setResult(null);
				res.setError("missing search param");
				return new ResponseEntity<Object>(res, HttpStatus.OK);
			}

			List<Block> blockList = blockJpaRepository.findByBlockHash(searchParam);
			if (blockList != null && blockList.size() > 0) {
				Block block = blockList.get(0);
				res.setResult(block);
				return new ResponseEntity<Object>(res, HttpStatus.OK);
			}
			if (validLong(searchParam)) {
				List<Block> blockLists = blockJpaRepository.findByBlockNumber(Long.parseLong(searchParam));
				if (blockLists != null && blockLists.size() > 0) {
					Block block = blockLists.get(0);
					res.setResult(block);
					return new ResponseEntity<Object>(res, HttpStatus.OK);
				}

			}
			res.setResult(null);
			res.setError("No data found.");
			return new ResponseEntity<Object>(res, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			res.setResult(null);
			res.setError("Exception :");
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/transactionByTransactionHash", method = RequestMethod.GET)
	public ResponseEntity<Object> findByTransactionHash(
			@RequestParam(value = "searchParam", required = false) String searchParam) throws JSONException {
		Response res = new Response();
		try {

			if (searchParam == null && searchParam.trim().isEmpty()) {
				res.setResult(null);
				res.setError("missing search param");
				return new ResponseEntity<Object>(res, HttpStatus.OK);
			}
			List<Transaction> transactionList = transactionJpaRepository.findByTransactionHash(searchParam);
			if (transactionList != null && transactionList.size() > 0) {
				Transaction transaction = transactionList.get(0);
				res.setResult(transaction);
				return new ResponseEntity<Object>(res, HttpStatus.OK);
			}

			res.setResult(null);
			res.setError("No data found.");
			return new ResponseEntity<Object>(res, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			res.setResult(null);
			res.setError("Exception :" + e);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/blockAndTransactionDetailsFromBlockNumberOrBlockHash", method = RequestMethod.GET)
	public ResponseEntity<Object> getBlock(@RequestParam(value = "searchParam", required = false) String searchParam)
			throws JSONException {
		Response res = new Response();
		try {
			if (searchParam == null && searchParam.trim().isEmpty()) {
				res.setResult(null);
				res.setError("missing search param");
				return new ResponseEntity<Object>(res, HttpStatus.OK);
			}

			List<Block> blockLists = new ArrayList<Block>();
			// find in block number list
			boolean blockNumberfound = false;
			if (validLong(searchParam)) {
				blockLists = blockJpaRepository.findByBlockNumber(Long.parseLong(searchParam));
				System.out.println("finb by number");
				if (blockLists != null && blockLists.size() > 0) {
					blockNumberfound = true;
				}

			}
			// find in blockHash
			if (!blockNumberfound) {

				blockLists = blockJpaRepository.findByBlockHash(searchParam);
				System.out.println("finb by hash  " + blockLists == null);
				if (blockLists.size() == 0) {
					res.setResult(null);
					res.setError("No data found.");
					return new ResponseEntity<Object>(res, HttpStatus.OK);
				}
			}
			// find in block number list
			List<Transaction> transactionList;
			if (blockNumberfound)
				transactionList = transactionJpaRepository.findByBlockNumber(Long.parseLong(searchParam));
			else
				transactionList = transactionJpaRepository.findByBlockHash(searchParam);

			List<Object> obj = new ArrayList<Object>();
			obj.add(0, blockLists);
			obj.add(1, transactionList);
			res.setResult(obj);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			res.setResult(null);
			res.setError("Exception :" + e);
			return new ResponseEntity<Object>(res, HttpStatus.EXPECTATION_FAILED);
		}

	}

	@RequestMapping(value = "/transactionByBlockNumberOrBlockHash", method = RequestMethod.GET)
	public ResponseEntity<Object> findTransactionByBlockNumberOrBlockHash(
			@RequestParam(value = "searchParam", required = false) String searchParam) {
		Response res = new Response();
		try {

			if (searchParam == null && searchParam.trim().isEmpty()) {
				res.setResult(null);
				res.setError("missing search param");
				return new ResponseEntity<Object>(res, HttpStatus.OK);
			}
			boolean blockNumberfound = false;
			if (validLong(searchParam)) {
				List<Block> blockLists = blockJpaRepository.findByBlockNumber(Long.parseLong(searchParam));
				if (blockLists != null && blockLists.size() > 0) {
					blockNumberfound = true;
				}
			}
			// find in block number list
			List<Transaction> transactionList;
			if (blockNumberfound)
				transactionList = transactionJpaRepository.findByBlockNumber(Long.parseLong(searchParam));
			else
				transactionList = transactionJpaRepository.findByBlockHash(searchParam);

			res.setResult(transactionList);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			res.setResult(null);
			res.setError("Exception :" + e);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/mineBlockAndTransactionDetailsFromAccount", method = RequestMethod.GET)
	public ResponseEntity<Object> getAccount(
			@RequestParam(value = "accountAddress", required = false) String accountAddress) throws JSONException {
		Response res = new Response();
		try {
			if (accountAddress == null && accountAddress.trim().isEmpty()) {
				res.setResult(null);
				res.setError("Adress missing.");
				return new ResponseEntity<Object>(res, HttpStatus.OK);
			}
			if (accountAddress.trim().equals("0"))
				accountAddress = "0000000000000000000000000000000000000000000000000000000000000000";

			List<Transaction> transactions = transactionJpaRepository.findByFromAddrOrToAddr(accountAddress,
					accountAddress);
			List<Block> mineblocks = blockJpaRepository.findByMinerAddress(accountAddress);
			List<Object> obj = new ArrayList<Object>();
			obj.add(0, mineblocks);
			obj.add(1, transactions);
			res.setResult(obj);

			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			res.setResult(null);
			res.setError("Exception :" + e);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/transactionsByAccountAddress", method = RequestMethod.GET)
	public ResponseEntity<Object> findByAccountAddress(
			@RequestParam(value = "searchParam", required = false) String searchParam) throws JSONException {
		Response res = new Response();
		try {
			System.out.println(!searchParam.trim().isEmpty() && searchParam != null);
			if (!searchParam.trim().isEmpty() && searchParam != null) {
				JSONArray jsonArray = new JSONArray();
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String addressSearch = searchParam;
				if (searchParam.trim().equals("0"))
					addressSearch = "0000000000000000000000000000000000000000000000000000000000000000";
				System.out.println("searchParam  " + addressSearch);
				List<Transaction> allTx = transactionByAccountAddress(addressSearch);
				System.out.println("totalTxCount 2  " + allTx.size());
				if (allTx.size() > 0) {
					res.setResult(allTx);
					return new ResponseEntity<Object>(res, HttpStatus.OK);
				}
				res.setResult(null);
				res.setError("No data found.");
				return new ResponseEntity<Object>(res, HttpStatus.NOT_FOUND);
			} else {
				res.setError("missing search param");
				return new ResponseEntity<Object>(res, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			res.setResult(null);
			res.setError("Exception :" + e);
			return new ResponseEntity<Object>(res, HttpStatus.BAD_REQUEST);
		}
	}

	public List<Transaction> transactionByAccountAddress(String address) throws JSONException {
		List<Transaction> allTx = new ArrayList<>();
		try {
			if (!address.trim().isEmpty() && address != null) {
				String addressSearch = address;
				if (address.trim().equals("0"))
					addressSearch = "0000000000000000000000000000000000000000000000000000000000000000";
				allTx = transactionJpaRepository.findByFromAddrOrToAddr(addressSearch, addressSearch);
				System.out.println("totalTxCount  " + allTx.size());
			}
			return allTx;
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			JSONObject result = new JSONObject();
			result.put("message", "Error: Invalid Request");
			return allTx;
		}
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ResponseEntity<Object> viewDashboard() throws JSONException {
		Response res = new Response();
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			JSONObject result = new JSONObject();

			// populate recent n blocks
			List<Block> blockList = blockJpaRepository.findTop20ByOrderByBlockNumberDesc();
			JSONArray blockArray = new JSONArray();
			if (blockList != null && blockList.size() > 0) {
				for (int i = 0; i < blockList.size(); i++) {
					Block block = blockList.get(i);
					blockArray.put(new JSONObject(ow.writeValueAsString(block)));
				}
			}
			// populate recent n transactions
			List<Transaction> transactionList = transactionJpaRepository.findTop10ByOrderByBlockNumberDesc();
			JSONArray transactionArray = new JSONArray();
			if (transactionList != null && transactionList.size() > 0) {
				for (int i = 0; i < transactionList.size(); i++) {
					Transaction transaction = transactionList.get(i);
					transactionArray.put(new JSONObject(ow.writeValueAsString(transaction)));
				}
			}

			List<org.web3j.protocol.core.methods.response.Transaction> pendingTx = new ArrayList<org.web3j.protocol.core.methods.response.Transaction>();
			Subscription subscription = web3j.pendingTransactionObservable().subscribe(tx -> {
				pendingTx.add(tx);
			});

			result.put("blockList", blockArray);
			result.put("transactionList", transactionArray);
			result.put("pendingTransactionList", pendingTx);
			res.setResult(result);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			res.setResult(null);
			res.setError("Exception :" + e);
			return new ResponseEntity<Object>(res, HttpStatus.BAD_REQUEST);
		}
	}

	// @RequestMapping(value = "/couchdb", method = RequestMethod.GET)
	// public void init3() {
	// try {
	// Database db = cloudant.database("mydb", true);
	// JsonObject obj1 = new JsonObject();
	// JsonArray obj = new JsonArray();
	// obj.add("dsd");
	// obj1.add("mane", obj);
	// com.cloudant.client.api.model.Response f = db.save(obj1);
	// System.out.println(f.getError());
	// System.out.println(f.getId());
	// } catch (Exception e) {
	// System.out.println(e.getMessage());
	// }
	// }

	@RequestMapping(value = "/memorypool", method = RequestMethod.GET)
	public ResponseEntity<Object> filter() throws JSONException {
		Response res = new Response();
		try {

			List<org.web3j.protocol.core.methods.response.Transaction> pendingTx = new ArrayList<org.web3j.protocol.core.methods.response.Transaction>();
			Subscription subscription = web3j.pendingTransactionObservable().subscribe(tx -> {
				pendingTx.add(tx);
			});
			res.setResult(pendingTx);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			res.setResult(null);
			res.setError("Exception :" + e);
			return new ResponseEntity<Object>(res, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/accountBalance", method = RequestMethod.GET)
	public ResponseEntity<Object> accountBalance(
			@RequestParam(value = "searchParam", required = false) String searchParam)
			throws InterruptedException, ExecutionException, JSONException {
		Response res = new Response();
		if (!searchParam.trim().isEmpty() && searchParam != null) {
			BigInteger wei = balance(searchParam);
			res.setResult(wei);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} else {
			res.setError("missing search param");
			return new ResponseEntity<Object>(res, HttpStatus.NOT_FOUND);
		}
	}

	public BigInteger balance(String searchParam) {
		BigInteger wei = BigInteger.ZERO;
		try {
			EthGetBalance ethGetBalance = web3j.ethGetBalance(searchParam, DefaultBlockParameterName.LATEST).sendAsync()
					.get();
			wei = ethGetBalance.getBalance();
			return wei;
		} catch (Exception e) {
			return wei;
		}
	}

	public boolean validLong(String searchParam) {
		try {
			long blockNumber = Long.parseLong(searchParam);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean validInt(String searchParam) {
		try {
			int param = Integer.parseInt(searchParam);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public int min(int a, int b) {
		if (a < b)
			return a;
		else
			return b;
	}

}
