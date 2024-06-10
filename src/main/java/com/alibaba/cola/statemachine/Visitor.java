package com.alibaba.cola.statemachine;

public interface Visitor {

    char LF = '\n';

    String visitOnEntry(State<?, ?> visitable);

    String visitOnExit(State<?, ?> visitable);
}