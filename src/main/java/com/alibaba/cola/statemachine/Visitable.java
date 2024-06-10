package com.alibaba.cola.statemachine;

public interface Visitable {
    String accept(final Visitor visitor);
}