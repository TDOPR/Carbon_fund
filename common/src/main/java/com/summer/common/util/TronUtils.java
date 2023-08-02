package com.summer.common.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.tron.common.utils.Base58;
import org.tron.common.utils.ByteArray;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * utls
 *
 * @Autor Shadow 2020/10/22
 * @Date 2020-10-22 16:05:10
 */
public class TronUtils {
    static int ADDRESS_SIZE = 21;
    private static byte addressPreFixByte = (byte) 0x41; // 41 + address (byte) 0xa0; //a0 + address

    public static String toHexAddress(String tAddress) {
        return ByteArray.toHexString(decodeFromBase58Check(tAddress));
    }

    public static String fromHexAddress(String hexAddress) {
        byte[] bytes = ByteArray.fromHexString(hexAddress);
        return encode58Check(bytes);
    }

    private static byte[] decodeFromBase58Check(String addressBase58) {
        if (StringUtils.isEmpty(addressBase58)) {
            return null;
        }
        byte[] address = decode58Check(addressBase58);
        if (!addressValid(address)) {
            return null;
        }
        return address;
    }

    private static byte[] decode58Check(String input) {

        byte[] decodeCheck = Base58.decode(input);
        if (decodeCheck.length <= 4) {
            return null;
        }
        byte[] decodeData = new byte[decodeCheck.length - 4];
        System.arraycopy(decodeCheck, 0, decodeData, 0, decodeData.length);
        byte[] hash0 = Sha256Hash.hash(true, decodeData);
        byte[] hash1 = Sha256Hash.hash(true, hash0);
        if (hash1[0] == decodeCheck[decodeData.length] && hash1[1] == decodeCheck[decodeData.length + 1]
                && hash1[2] == decodeCheck[decodeData.length + 2] && hash1[3] == decodeCheck[decodeData.length + 3]) {
            return decodeData;
        }
        return null;
    }

    public static String encode58Check(byte[] input) {
        byte[] hash0 = Sha256Hash.hash(true, input);
        byte[] hash1 = Sha256Hash.hash(true, hash0);
        byte[] inputCheck = new byte[input.length + 4];
        System.arraycopy(input, 0, inputCheck, 0, input.length);
        System.arraycopy(hash1, 0, inputCheck, input.length, 4);
        return Base58.encode(inputCheck);
    }

    private static boolean addressValid(byte[] address) {
        if (ArrayUtils.isEmpty(address)) {
            return false;
        }
        if (address.length != ADDRESS_SIZE) {
            return false;
        }
        byte preFixbyte = address[0];
        return preFixbyte == getAddressPreFixByte();
        // Other rule;
    }

    private static byte getAddressPreFixByte() {
        return addressPreFixByte;
    }


    /**
     * BigInteger 转 BigDecimal(wei 换算成平常单位)
     *
     * @param value
     * @return
     */
    public static BigDecimal bigIntegerToBigDecimal(BigInteger value, int round) {
        BigDecimal PASS_VALUE_BIGDECIMAL = revertBigDecimalByRound(round);
        return new BigDecimal(value.toString())
                .divide(PASS_VALUE_BIGDECIMAL).setScale(round, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal revertBigDecimalByRound(int round) {
        double pow = Math.pow(Double.parseDouble("10"), Double.parseDouble((round) + ""));
        return new BigDecimal(pow);
    }

    public static BigInteger bigDecimalToBigInteger(BigDecimal value, int round) {
        BigDecimal PASS_VALUE_BIGDECIMAL = revertBigDecimalByRound(round);
        return new BigDecimal(value.toString()).multiply(PASS_VALUE_BIGDECIMAL).toBigInteger();
    }

    public static void main(String args[]) {
        byte[] bytes = ByteArray.fromHexString("419E211C9E4E004D37765393CEF79BDB0B5259CD20");
        String encode = encode58Check(bytes);
        System.out.println(encode);
        System.out.println(toHexAddress("TAwXr4R2v8sEAiHpzX3BTsQG6uza6oNpVF"));

        //	System.out.println(toHexAddress("TYeAt1DW74hFN3Zd3f4nvbRYpkXDBmQAkh").equals("41f8b137fd24bf1e480f2504d19bcdcac10c98468f"));
//		414c488a9061587ecf9bdd654c1ba1123371e7f732
    }

}
