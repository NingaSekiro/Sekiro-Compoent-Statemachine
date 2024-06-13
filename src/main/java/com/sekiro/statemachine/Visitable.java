package com.sekiro.statemachine;

public interface Visitable {
    String accept(final Visitor visitor);
}