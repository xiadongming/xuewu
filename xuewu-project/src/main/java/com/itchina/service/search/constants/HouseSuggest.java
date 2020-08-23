package com.itchina.service.search.constants;

/**
 * @Author: xiadongming
 * @Date: 2020/8/22 21:12
 */
public class HouseSuggest {

    private String input;

    private int weight = 10;

    public String getInput() {
        return input;
    }

    public int getWeight() {
        return weight;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
