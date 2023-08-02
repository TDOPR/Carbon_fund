package com.summer.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.logging.log4j.util.Strings;
import org.bouncycastle.util.encoders.Hex;
import org.tron.common.crypto.ECKey;
import org.tron.common.crypto.Sha256Sm3Hash;
import org.tron.common.utils.ByteArray;
import org.tron.protos.Protocol;
import org.tron.protos.contract.BalanceContract;
import org.tron.protos.contract.SmartContractOuterClass;
import org.tron.walletserver.WalletApi;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TronUiltNew {

    private static String URL_LAST_BLOCK = "https://api.trongrid.io/wallet/getnowblock";
    private static String URL_PUSH_MSG = "https://api.trongrid.io/wallet/broadcasthex";

    /*private static String URL_LAST_BLOCK = "https://api.shasta.trongrid.io/wallet/getnowblock";
    private static String URL_PUSH_MSG = "https://api.shasta.trongrid.io/wallet/broadcasthex";*/

    public TronUiltNew() {

    }

    public static void main(String[] args) {

        String from = "TQPKS1sDmHxLuUYpFP6C7G8yfbKsJVqseS";
        String prikey = "c1a34e5250f58b6c0a132a954efb7bb320cf5f509d22efd34676de77b623d570";

        String contract = "TGtNLSNaVKXVMXqDQu4BREvvV5dbLfCBgd";
        /**
         * 新增插入数据接口
         *    function insertUser(address _upAddr,uint256 _value, address _addr,uint256 _investTime, uint256 _historyGlcStaticBonus, uint256 _historyFbbStaticBonus, uint256 _historyGlcDyBonus, uint256 _historyPartnerBonus)
         * //参数：用户地址，入金量，上级地址，投资时间，已领取静态GLC数量，已领取静态FBB数量，已领取推荐GLC数量，已领取股东加权GLC数量
         */
        String method = "insertUser(address,uint256,address,uint256,uint256,uint256,uint256,uint256)";

        List<Object> params = new ArrayList();
        params.add("TNEeHVcvPSeVSYX6HwKx4vtVBKyJLe4NoX");
        params.add(10000000000L);
        params.add("TCfFHryA93Cb4hsGPQij2jdwc8PyePS4hE");
        params.add(1608518091L);
        params.add(0L);
        params.add(0L);
        params.add(0L);
        params.add(0L);

        try {
            System.out.println("开始发送合约交易");
            BroadcastResultBean result = sendContractTransaction(from, prikey, contract, method, params);
            System.out.println(result.result + " ; " + JSON.toJSONString(result));
        } catch (Exception var12) {
            var12.printStackTrace();
        }



        /*String from = "TGtiQ5uQzw3Z3ni3ECU1DXsTmAHo9dy6EP";
        String prikey = "c2e70674fb755c8064b86359aea927b7b43f7acb1cc7c05db53ce964797f5cdd";
        String trxTo = "TXehnYHPH1tteZeri9yf8VjxnApe6Y1wFV";
        byte[] fromByte = WalletApi.decodeFromBase58Check(from);
        String addr = org.tron.common.utils.ByteArray.toHexString(fromByte);
        System.out.println("myaddr " + addr);
        fromByte = WalletApi.decodeFromBase58Check(trxTo);
        addr = org.tron.common.utils.ByteArray.toHexString(fromByte);
        System.out.println("to " + addr);
        fromByte = WalletApi.decodeFromBase58Check("TWNzzdSSj6SojrAu4atgfAyoQy1yCBgSQK");
        addr = org.tron.common.utils.ByteArray.toHexString(fromByte);
        System.out.println("contract " + addr);
        String contract = "TWNzzdSSj6SojrAu4atgfAyoQy1yCBgSQK";
        long tokenValue = 90000000000L;
        String method = "transfer(address,uint256)";
        List<Object> params = new ArrayList();
        params.add("TRqNpnjuo44nytQgxW7VW83zFZAV5g4Qtu");
        params.add(tokenValue);*/

        /*String from = "TQPKS1sDmHxLuUYpFP6C7G8yfbKsJVqseS";
        String to = "TRG9uHgDZPAatZVjFCAGnN3bXiXVUFAFSs";
        String prikey = "c1a34e5250f58b6c0a132a954efb7bb320cf5f509d22efd34676de77b623d570";

        String contract = "TWgQGagxMdNPt5fa1XDL4XMSkM1uYrgnG6";
        String method = "transfer(address,uint256)";

        List<Object> params = new ArrayList();
        params.add(to);
        params.add(9000000000000000000L);

        try {
            System.out.println("开始发送合约交易");
            TronUiltNew.BroadcastResultBean result = sendContractTransaction(from, prikey, contract, method, params);
            System.out.println(result.result + " ; " + JSON.toJSONString(result));
        } catch (Exception var12) {
            var12.printStackTrace();
        }*/

        /*try {
            System.out.println("开始发送TRX交易");
            TronUiltNew.BroadcastResultBean result = sendTrxTransaction(from, prikey, to, 10000000L);
            System.out.println(result.result + " ; " + JSON.toJSONString(result));
        } catch (Exception var12) {
            var12.printStackTrace();
        }*/


    }

    public static BroadcastResultBean sendTrxTransaction(String from, String prikey, String to, long value) throws Exception {
        byte[] privateBytes = org.tron.common.utils.ByteArray.fromHexString(prikey);
        String blockStr = doGet(URL_LAST_BLOCK);
        if (Strings.isBlank(blockStr)) {
            throw new Exception("获取最新区块出错");
        } else {
            BlockResultBean blockInfo = (BlockResultBean) JSON.parseObject(blockStr, BlockResultBean.class);
            long blockTimestamp = blockInfo.block_header.raw_data.timestamp;
            long blockHeight = blockInfo.block_header.raw_data.number;
            String uHash = blockInfo.blockID;
            byte[] blockHash = Hex.decode(uHash);
            byte[] blockHash2 = Sha256Sm3Hash.of(uHash.getBytes()).getBytes();
            Protocol.Transaction transaction = createTrxTransaction(from, to, value, blockTimestamp, blockHeight, blockHash);
            byte[] transactionBytes = transaction.toByteArray();
            byte[] transaction4 = signTransaction2Byte(transactionBytes, privateBytes);
            String lastStr = org.tron.common.utils.ByteArray.toHexString(transaction4);
            JSONObject ob = new JSONObject();
            ob.put("transaction", lastStr);
            String broadcastResult = doPost(URL_PUSH_MSG, ob.toJSONString());
            System.out.println("--发送结果待解析 " + broadcastResult);
            return (BroadcastResultBean) JSON.parseObject(broadcastResult, BroadcastResultBean.class);
        }
    }

    public static BroadcastResultBean sendContractTransaction(String from, String prikey, String contractAddr, String method, List<Object> args) throws Exception {
        byte[] privateBytes = org.tron.common.utils.ByteArray.fromHexString(prikey);
        String blockStr = doGet(URL_LAST_BLOCK);
        if (Strings.isBlank(blockStr)) {
            throw new Exception("获取最新区块出错");
        } else {
            BlockResultBean blockInfo = (BlockResultBean) JSON.parseObject(blockStr, BlockResultBean.class);
            long blockTimestamp = blockInfo.block_header.raw_data.timestamp;
            long blockHeight = blockInfo.block_header.raw_data.number;
            String uHash = blockInfo.blockID;
            byte[] blockHash = Hex.decode(uHash);
            byte[] blockHash2 = Sha256Sm3Hash.of(uHash.getBytes()).getBytes();
            Protocol.Transaction transaction = createContractTransaction(from, contractAddr, method, args, blockTimestamp, blockHeight, blockHash);
            byte[] transactionBytes = transaction.toByteArray();
            byte[] transaction4 = signTransaction2Byte(transactionBytes, privateBytes);
            String lastStr = org.tron.common.utils.ByteArray.toHexString(transaction4);
            JSONObject ob = new JSONObject();
            ob.put("transaction", lastStr);
            String broadcastResult = doPost(URL_PUSH_MSG, ob.toJSONString());
            return (BroadcastResultBean) JSON.parseObject(broadcastResult, BroadcastResultBean.class);
        }
    }

    private static Protocol.Transaction createTrxTransaction(String fromStr, String toStr, long amount, long blockTimestamp, long blockHeight, byte[] blockHash) {
        byte[] from = WalletApi.decodeFromBase58Check(fromStr);
        byte[] to = WalletApi.decodeFromBase58Check(toStr);
        Protocol.Transaction.Builder transactionBuilder = Protocol.Transaction.newBuilder();
        org.tron.protos.Protocol.Transaction.Contract.Builder contractBuilder = Protocol.Transaction.Contract.newBuilder();
        org.tron.protos.contract.BalanceContract.TransferContract.Builder transferContractBuilder = BalanceContract.TransferContract.newBuilder();
        transferContractBuilder.setAmount(amount);
        ByteString bsTo = ByteString.copyFrom(to);
        ByteString bsOwner = ByteString.copyFrom(from);
        transferContractBuilder.setToAddress(bsTo);
        transferContractBuilder.setOwnerAddress(bsOwner);
        Any any = Any.pack(transferContractBuilder.build());
        contractBuilder.setParameter(any);
        contractBuilder.setType(Protocol.Transaction.Contract.ContractType.TransferContract);
        transactionBuilder.getRawDataBuilder().addContract(contractBuilder).setTimestamp(System.currentTimeMillis()).setExpiration(blockTimestamp + 36000000L);
        Protocol.Transaction transaction = transactionBuilder.build();
        Protocol.Transaction refTransaction = setReference(transaction, blockHeight, blockHash);
        return refTransaction;
    }

    /**
     * 补充0到64个字节
     *
     * @param dt
     * @return
     */
    private static String addZero(String dt, int length) {
        StringBuilder builder = new StringBuilder();
        final int count = length;
        int zeroAmount = count - dt.length();
        for (int i = 0; i < zeroAmount; i++) {
            builder.append("0");
        }
        builder.append(dt);
        return builder.toString();
    }

    private static String castHexAddress(String address) {
        if (address.startsWith("T")) {
            return TronUtils.toHexAddress(address);
        }
        return address;
    }

    private static Protocol.Transaction createContractTransaction(String fromStr, String contractAddrStr, String method, List<Object> args, long blockTimestamp, long blockHeight, byte[] blockHash) {
        byte[] from = WalletApi.decodeFromBase58Check(fromStr);
        byte[] contractAddr = WalletApi.decodeFromBase58Check(contractAddrStr);
        Protocol.Transaction.Builder transactionBuilder = Protocol.Transaction.newBuilder();
        org.tron.protos.Protocol.Transaction.Contract.Builder contractBuilder = Protocol.Transaction.Contract.newBuilder();
        String addressToParam = addZero(castHexAddress(args.get(0).toString()), 64);
        String amountParam = addZero(new BigInteger(args.get(1).toString()).toString(16), 64);
        byte[] input = Hex.decode("a9059cbb" + addressToParam + amountParam);
        SmartContractOuterClass.TriggerSmartContract tsc = triggerCallContract(from, contractAddr, 0L, input, 0L, "");

        try {
            Any any = Any.pack(tsc);
            contractBuilder.setParameter(any);
        } catch (Exception var17) {
            return null;
        }

        contractBuilder.setType(Protocol.Transaction.Contract.ContractType.TriggerSmartContract);
        transactionBuilder.getRawDataBuilder().addContract(contractBuilder).setTimestamp(System.currentTimeMillis()).setExpiration(blockTimestamp + 36000000L).setFeeLimit(1000000000L);
        Protocol.Transaction transaction = transactionBuilder.build();
        Protocol.Transaction refTransaction = setReference(transaction, blockHeight, blockHash);
        return refTransaction;
    }

    private static SmartContractOuterClass.TriggerSmartContract triggerCallContract(byte[] address, byte[] contractAddress, long callValue, byte[] data, long tokenValue, String tokenId) {
        org.tron.protos.contract.SmartContractOuterClass.TriggerSmartContract.Builder builder = SmartContractOuterClass.TriggerSmartContract.newBuilder();
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

    private static Protocol.Transaction setReference(Protocol.Transaction transaction, long blockHeight, byte[] blockHash) {
        byte[] refBlockNum = org.tron.common.utils.ByteArray.fromLong(blockHeight);
        Protocol.Transaction.raw rawData = transaction.getRawData().toBuilder().setRefBlockHash(ByteString.copyFrom(org.tron.common.utils.ByteArray.subArray(blockHash, 8, 16))).setRefBlockBytes(ByteString.copyFrom(org.tron.common.utils.ByteArray.subArray(refBlockNum, 6, 8))).build();
        return transaction.toBuilder().setRawData(rawData).build();
    }

    private static Sha256Sm3Hash getBlockHash(Protocol.Block block) {
        return Sha256Sm3Hash.of(block.getBlockHeader().getRawData().toByteArray());
    }

    private static String getTransactionHash(Protocol.Transaction transaction) {
        String txid = ByteArray.toHexString(Sha256Sm3Hash.hash(transaction.getRawData().toByteArray()));
        return txid;
    }

    private static byte[] signTransaction2Byte(byte[] transaction, byte[] privateKey) throws InvalidProtocolBufferException {
        ECKey ecKey = ECKey.fromPrivate(privateKey);
        Protocol.Transaction transaction1 = Protocol.Transaction.parseFrom(transaction);
        byte[] rawdata = transaction1.getRawData().toByteArray();
        byte[] hash = Sha256Sm3Hash.hash(rawdata);
        byte[] sign = ecKey.sign(hash).toByteArray();
        return transaction1.toBuilder().addSignature(ByteString.copyFrom(sign)).build().toByteArray();
    }

    public static String doGet(String httpurl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;

        try {
            URL url = new URL(httpurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("TRON-PRO-API-KEY", "c15b86be-e273-462e-a2b3-3ff4fe713dc1");

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
        } catch (MalformedURLException var23) {
            var23.printStackTrace();
        } catch (IOException var24) {
            var24.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException var22) {
                    var22.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException var21) {
                    var21.printStackTrace();
                }
            }

            connection.disconnect();
        }

        return result;
    }

    public static String doPost(String httpUrl, String param) {
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
            connection.addRequestProperty("TRON-PRO-API-KEY", "c15b86be-e273-462e-a2b3-3ff4fe713dc1");
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
        } catch (MalformedURLException var30) {
            System.out.println("1 " + var30.getMessage());
            var30.printStackTrace();
        } catch (IOException var31) {
            System.out.println("2 " + var31.getMessage());
            var31.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException var29) {
                    var29.printStackTrace();
                }
            }

            if (null != os) {
                try {
                    os.close();
                } catch (IOException var28) {
                    var28.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException var27) {
                    var27.printStackTrace();
                }
            }

            connection.disconnect();
        }

        return result;
    }

    public static class BlockBeanRaw {
        public long number;
        public long timestamp;

        private BlockBeanRaw() {
        }
    }

    public static class BlockBeanHeader {
        public BlockBeanRaw raw_data;

        private BlockBeanHeader() {
        }
    }

    public static class BlockResultBean {
        public String blockID;
        public BlockBeanHeader block_header;

        private BlockResultBean() {
        }
    }

    public static class BroadcastResultBean {
        public boolean result;
        public String code;
        public String txid;
        public String message;

        public BroadcastResultBean() {
        }
    }
}
