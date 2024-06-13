package com.sekiro.statemachine;

public interface Visitor {

    char LF = '\n';

    String visitOnEntry(State<?, ?> visitable);

    String visitOnExit(State<?, ?> visitable);
}