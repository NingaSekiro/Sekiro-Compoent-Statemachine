package com.github.ningasekiro;

public interface Visitable {
    String accept(final Visitor visitor);
}