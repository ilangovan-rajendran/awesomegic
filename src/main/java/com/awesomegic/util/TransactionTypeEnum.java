package com.awesomegic.util;

import java.util.Arrays;

public enum TransactionTypeEnum {
    WITHDRAWAL("W"),DEPOSIT("D"),INTEREST("I");

    public String code;

    TransactionTypeEnum(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }
    public static TransactionTypeEnum getTransactiionEnum(String code) {
        return Arrays.stream(TransactionTypeEnum.values()).filter(actionEnum -> actionEnum.getCode().equals(code)).findFirst().orElse(null);
    }

    public static void main(String[] args) {
        System.out.println(TransactionTypeEnum.WITHDRAWAL.code);
    }


}
