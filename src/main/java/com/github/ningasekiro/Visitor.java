package com.github.ningasekiro;

public interface Visitor {

    char LF = '\n';

    String visitOnEntry(State<?, ?> visitable);

    String visitOnExit(State<?, ?> visitable);
}