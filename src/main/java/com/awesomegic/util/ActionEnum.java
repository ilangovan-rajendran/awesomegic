package com.awesomegic.util;

import java.util.Arrays;

public enum ActionEnum {
    INPUT("I"), PRINT("P"),DEFINE_INT("D"),QUIT("Q");

    public String action;

    ActionEnum(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public static ActionEnum getActionEnum(String action) {
        return Arrays.stream(ActionEnum.values()).filter(actionEnum -> actionEnum.getAction().equals(action)).findFirst().orElse(null);
    }


    public static void main(String[] args) {
        System.out.println(ActionEnum.getActionEnum("I"));
    }
}
