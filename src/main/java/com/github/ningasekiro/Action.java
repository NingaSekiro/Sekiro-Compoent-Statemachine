package com.github.ningasekiro;

import com.github.ningasekiro.impl.StateContext;

public interface Action<S, E> {
    public void execute(StateContext<S, E> stateContext);
}