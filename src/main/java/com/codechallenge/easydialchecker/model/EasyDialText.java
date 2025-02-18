package com.codechallenge.easydialchecker.model;

import java.util.Objects;

public class EasyDialText {
    private String value;
    private boolean isEasyToDial;

    public EasyDialText() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isEasyToDial() {
        return isEasyToDial;
    }

    public void setEasyToDial(boolean easyToDial) {
        isEasyToDial = easyToDial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EasyDialText that)) return false;
        return isEasyToDial() == that.isEasyToDial() && Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue(), isEasyToDial());
    }

    @Override
    public String toString() {
        return value + "," + isEasyToDial;
    }
}
