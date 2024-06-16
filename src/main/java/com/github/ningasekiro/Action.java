package com.github.ningasekiro;

public interface Action<S, E> {
    public void execute(StateContext<S, E> stateContext);
}