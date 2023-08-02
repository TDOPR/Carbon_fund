package com.summer.common.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import org.bouncycastle.util.encoders.Hex;
import org.tron.common.crypto.ECKey;
import org.tron.common.crypto.Sha256Sm3Hash;
import org.tron.common.utils.AbiUtil;
import org.tron.common.utils.ByteArray;
import org.tron.protos.Protocol.Block;
import org.tron.protos.Protocol.Transaction;
import org.tron.protos.contract.BalanceContract.TransferContract;
import org.tron.protos.contract.SmartContractOuterClass.TriggerSmartContract;
import org.tron.walletserver.WalletApi;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class TronUtilss {

    /**
     * @param from
     * @param prikey
     * @param to
     * @param value  注意，这个是sun单位，需要自己转换好
     * @return
     * @throws Exception
     */
    public static BroadcastResultBean sendTrxWithdraw(String from, String prikey, String to, long value) throws Exception {
        BroadcastResultBean result = sendTrxTransaction(from, prikey, to, value);
        return result;
    }

    /**
     * @param from
     * @param prikey
     * @param contractAddr 代币地址， 也可以是其他合约地址
     * @param method       "transfer(address,uint256)"
     * @param params       method对应的参数，value要自己转换成sun, address类型需要T开头的字符串，不能0x,41开头，需要调用fromHexString(“41xxx”)转换
     * @return
     * @throws Exception
     */
    public static BroadcastResultBean sendTokenWithdraw(String from, String prikey, String contractAddr, String method, List<Object> params) throws Exception {
        BroadcastResultBean result = sendContractTransaction(from, prikey, contractAddr, method, params);
        return result;
    }

    /**
     * 转换地址，TGtiQ5uQzw3Z3ni3EC 转成 415694e6807bf4685fb6 类型的地址
     *
     * @param address
     * @return
     */
    public static String toHexString(String address) {
        return ByteArray.toHexString(WalletApi.decodeFromBase58Check(address));
    }

    /**
     * 转换地址，415694e6807bf4685fb6 转成 TGtiQ5uQzw3Z3ni3EC 类型的地址
     *
     * @param addressStart41
     * @return
     */
    public static String fromHexString(String addressStart41) {
        return WalletApi.encode58Check(ByteArray.fromHexString(addressStart41));
    }

    private static String URL_LAST_BLOCK = "https://api.trongrid.io/wallet/getnowblock";
    private static String URL_PUSH_MSG = "https://api.trongrid.io/wallet/broadcasthex";

    public static BroadcastResultBean sendTrxTransaction(String from,
                                                         String prikey, String to, long value) throws Exception {
        byte[] privateBytes = ByteArray.fromHexString(prikey);
        String blockStr = doGet(URL_LAST_BLOCK);
        if (isBlank(blockStr)) {
            throw new Exception("获取最新区块出错");
        }
        BlockResultBean blockInfo = (BlockResultBean) JSON.parseObject(
                blockStr, BlockResultBean.class);
        long blockTimestamp = blockInfo.block_header.raw_data.timestamp;// timestamp
        long blockHeight = blockInfo.block_header.raw_data.number;// number
        String uHash = blockInfo.blockID;//
        byte[] blockHash = Hex.decode(uHash);
        byte[] blockHash2 = Sha256Sm3Hash.of(uHash.getBytes()).getBytes();
        Transaction transaction = createTrxTransaction(from, to, value,
                blockTimestamp, blockHeight, blockHash);
        byte[] transactionBytes = transaction.toByteArray();
        byte[] transaction4 = signTransaction2Byte(transactionBytes,
                privateBytes);
        String lastStr = ByteArray.toHexString(transaction4);
        JSONObject ob = new JSONObject();
        ob.put("transaction", lastStr);
        String broadcastResult = doPost(URL_PUSH_MSG, ob.toJSONString());
        System.out.println("--发送结果待解析 " + broadcastResult);
        return JSON.parseObject(broadcastResult, BroadcastResultBean.class);
    }

    public static BroadcastResultBean sendContractTransaction(String from,
                                                              String prikey, String contractAddr, String method, List<Object> args)
            throws Exception {
        byte[] privateBytes = ByteArray.fromHexString(prikey);
        String blockStr = doGet(URL_LAST_BLOCK);
        if (isBlank(blockStr)) {
            throw new Exception("获取最新区块出错");
        }
        BlockResultBean blockInfo = (BlockResultBean) JSON.parseObject(
                blockStr, BlockResultBean.class);
        long blockTimestamp = blockInfo.block_header.raw_data.timestamp;// timestamp
        long blockHeight = blockInfo.block_header.raw_data.number;// number
        String uHash = blockInfo.blockID;//
        byte[] blockHash = Hex.decode(uHash);
        byte[] blockHash2 = Sha256Sm3Hash.of(uHash.getBytes()).getBytes();
        Transaction transaction = createContractTransaction(from, contractAddr,
                method, args, blockTimestamp, blockHeight, blockHash);
        byte[] transactionBytes = transaction.toByteArray();
        byte[] transaction4 = signTransaction2Byte(transactionBytes,
                privateBytes);
        String lastStr = ByteArray.toHexString(transaction4);
        JSONObject ob = new JSONObject();
        ob.put("transaction", lastStr);
        String broadcastResult = doPost(URL_PUSH_MSG, ob.toJSONString());
        return JSON.parseObject(broadcastResult, BroadcastResultBean.class);
    }

    /*public static Long getTrxBalance(String address) throws Exception {
        JSONObject ob = new JSONObject();
        ob.put("address", address);
        if(!address.startsWith("41")){
            ob.put("visible",true);
        }
        String blockStr = doPost(Transfer.URL_TRX_INFO, ob.toJSONString());
        if (isBlank(blockStr)) {
            throw new Exception("获取最新区块出错");
        }
        try {
            JSONObject re = JSONObject.parseObject(blockStr);
            return re.getLongValue("balance");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static long getTRC20Balance(String contractAddress, String address) throws Exception {
        if (!contractAddress.startsWith("41") || !address.startsWith("41")) {
            throw new Exception("地址格式不正确");
        }
        JSONObject ob = new JSONObject();
        String addrParam = addZeroForNum(address, 64);
        ob.put("owner_address", "410000000000000000000000000000000000000000");
        ob.put("contract_address", contractAddress);
        ob.put("function_selector", "balanceOf(address)");
        ob.put("parameter", addrParam);
        ob.put("address", address);
        String blockStr = doPost(Transfer.URL_TRIGGER_CONSTANT_CONTRACT, ob.toJSONString());
        if (isBlank(blockStr)) {
            throw new Exception("获取最新区块出错");
        }

        try {
            JSONObject re = JSONObject.parseObject(blockStr);
            JSONArray arr = re.getJSONArray("constant_result");
            String s = arr.getString(0);
            return ByteUtil.byteArrayToLong(ByteArray.fromHexString(s));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0L;
    }*/

    /**
     * 长度不够前面补0
     *
     * @param str
     * @param strLength
     * @return
     */
    public static String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);// 左补0
                // sb.append(str).append("0");//右补0
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }

    private static Transaction createTrxTransaction(String fromStr,
                                                    String toStr, long amount, long blockTimestamp, long blockHeight,
                                                    byte[] blockHash) {
        byte[] from = WalletApi.decodeFromBase58Check(fromStr);
        byte[] to = WalletApi.decodeFromBase58Check(toStr);
        Transaction.Builder transactionBuilder = Transaction.newBuilder();
        Transaction.Contract.Builder contractBuilder = Transaction.Contract
                .newBuilder();
        TransferContract.Builder transferContractBuilder = TransferContract
                .newBuilder();
        transferContractBuilder.setAmount(amount);
        ByteString bsTo = ByteString.copyFrom(to);
        ByteString bsOwner = ByteString.copyFrom(from);
        transferContractBuilder.setToAddress(bsTo);
        transferContractBuilder.setOwnerAddress(bsOwner);

        Any any = Any.pack(transferContractBuilder.build());
        contractBuilder.setParameter(any);
        contractBuilder
                .setType(Transaction.Contract.ContractType.TransferContract);
        transactionBuilder.getRawDataBuilder().addContract(contractBuilder)
                .setTimestamp(System.currentTimeMillis())
                .setExpiration(blockTimestamp + 10 * 60 * 60 * 1000);
        Transaction transaction = transactionBuilder.build();
        Transaction refTransaction = setReference(transaction, blockHeight,
                blockHash);
        return refTransaction;
    }

    // "transfer(address,uint256)"
    private static Transaction createContractTransaction(String fromStr,
                                                         String contractAddrStr, String method, List<Object> args,
                                                         long blockTimestamp, long blockHeight, byte[] blockHash) {
        byte[] from = WalletApi.decodeFromBase58Check(fromStr);
        byte[] contractAddr = WalletApi.decodeFromBase58Check(contractAddrStr);
        Transaction.Builder transactionBuilder = Transaction.newBuilder();
        Transaction.Contract.Builder contractBuilder = Transaction.Contract
                .newBuilder();
        byte[] input = Hex.decode(AbiUtil.parseMethod(method, args));
        TriggerSmartContract tsc = triggerCallContract(from, contractAddr, 0,
                input, 0, "");
        try {
            Any any = Any.pack(tsc);
            contractBuilder.setParameter(any);
        } catch (Exception e) {
            return null;
        }
        contractBuilder
                .setType(Transaction.Contract.ContractType.TriggerSmartContract);
        transactionBuilder.getRawDataBuilder().addContract(contractBuilder)
                .setTimestamp(System.currentTimeMillis())
                .setExpiration(blockTimestamp + 10 * 60 * 60 * 1000)
                .setFeeLimit(900000L);
        Transaction transaction = transactionBuilder.build();
        Transaction refTransaction = setReference(transaction, blockHeight,
                blockHash);
        return refTransaction;
    }

    private static TriggerSmartContract triggerCallContract(byte[] address,
                                                            byte[] contractAddress, long callValue, byte[] data,
                                                            long tokenValue, String tokenId) {
        TriggerSmartContract.Builder builder = TriggerSmartContract
                .newBuilder();
        builder.setOwnerAddress(ByteString.copyFrom(address));
        builder.setContractAddress(ByteString.copyFrom(contractAddress));
        builder.setData(ByteString.copyFrom(data));
        builder.setCallValue(callValue);
        if (tokenId != null && tokenId != "") {
            builder.setCallTokenValue(tokenValue);
            builder.setTokenId(Long.parseLong(tokenId));
        }
        return builder.build();
    }

    private static Transaction setReference(Transaction transaction,
                                            long blockHeight, byte[] blockHash) {
        // long blockHeight =
        // newestBlock.getBlockHeader().getRawData().getNumber();
        // byte[] blockHash = getBlockHash(newestBlock).getBytes();
        byte[] refBlockNum = ByteArray.fromLong(blockHeight);
        Transaction.raw rawData = transaction
                .getRawData()
                .toBuilder()
                .setRefBlockHash(
                        ByteString.copyFrom(ByteArray
                                .subArray(blockHash, 8, 16)))
                .setRefBlockBytes(
                        ByteString.copyFrom(ByteArray.subArray(refBlockNum, 6,
                                8))).build();
        return transaction.toBuilder().setRawData(rawData).build();
    }

    private static Sha256Sm3Hash getBlockHash(Block block) {
        return Sha256Sm3Hash.of(block.getBlockHeader().getRawData()
                .toByteArray());
    }

    private static String getTransactionHash(Transaction transaction) {
        String txid = ByteArray.toHexString(Sha256Sm3Hash.hash(transaction
                .getRawData().toByteArray()));
        return txid;
    }

    private static byte[] signTransaction2Byte(byte[] transaction,
                                               byte[] privateKey) throws InvalidProtocolBufferException {
        ECKey ecKey = ECKey.fromPrivate(privateKey);
        Transaction transaction1 = Transaction.parseFrom(transaction);
        byte[] rawdata = transaction1.getRawData().toByteArray();
        byte[] hash = Sha256Sm3Hash.hash(rawdata);
        byte[] sign = ecKey.sign(hash).toByteArray();
        return transaction1.toBuilder().addSignature(ByteString.copyFrom(sign))
                .build().toByteArray();
    }

    /**
     * 解析获得hash和返回值的
     */

    public static class BroadcastResultBean {
        public boolean result;// true/false
        public String code;// SUCCESS/OTHER/ERR_TIPS
        public String txid;// HASH
        public String message;// MSG

    }

    /**
     * 以下几个bean用于解析请求的最新block信息
     */
    private static class BlockResultBean {
        public String blockID;
        public BlockBeanHeader block_header;
    }

    private static class BlockBeanHeader {
        public BlockBeanRaw raw_data;
    }

    private static class BlockBeanRaw {
        public long number;
        public long timestamp;
    }

    public static String doGet(String httpurl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {

            URL url = new URL(httpurl);

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            connection.setConnectTimeout(15000);

            connection.setReadTimeout(60000);

            connection.connect();

            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();

                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        return result;
    }

    private static String doPost(String httpUrl, String param) {

        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL(httpUrl);

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");

            connection.setConnectTimeout(15000);

            connection.setReadTimeout(60000);


            connection.setDoOutput(true);

            connection.setDoInput(true);

            connection.setRequestProperty("Content-Type", "application/json");

            os = connection.getOutputStream();

            os.write(param.getBytes());

            if (connection.getResponseCode() == 200) {

                is = connection.getInputStream();

                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                StringBuffer sbf = new StringBuffer();
                String temp = null;

                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            System.out.println("1 " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("2 " + e.getMessage());
            e.printStackTrace();
        } finally {

            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();
        }
        return result;
    }

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}

