package com.summer.common.feign.result;

import lombok.Data;

import java.math.BigInteger;

@Data
public class TranscationByIdView {


    private String result;
    private int fee;
    private BigInteger blockNumber;
    private long blockTimeStamp;
    private String resMessage;
    private ReceiptBean receipt;
    private String id;
    private String contract_address;
    private java.util.List<String> contractResult;

    public static class ReceiptBean {
        private int energy_fee;
        private int energy_usage_total;
        private int net_usage;
        private String result;

        public int getEnergy_fee() {
            return energy_fee;
        }

        public void setEnergy_fee(int energy_fee) {
            this.energy_fee = energy_fee;
        }

        public int getEnergy_usage_total() {
            return energy_usage_total;
        }

        public void setEnergy_usage_total(int energy_usage_total) {
            this.energy_usage_total = energy_usage_total;
        }

        public int getNet_usage() {
            return net_usage;
        }

        public void setNet_usage(int net_usage) {
            this.net_usage = net_usage;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}
