package com.alibaba.cola.statemachine;

/**
 * Generic strategy interface used by a state machine to respond
 * events by executing an {@code Action} with a {@link StateContext}.
 *
 * @author Frank Zhang
 * @date 2020-02-07 2:51 PM
 */
public interface Action<S, E> {
    public void execute(StateContext<S, E> stateContext);
}
