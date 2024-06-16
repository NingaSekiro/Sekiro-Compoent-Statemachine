package com.github.ningasekiro.builder;

import com.github.ningasekiro.StateMachine;


/**
 * state machine builder
 *
 * @author NingaSekiro
 * @date 2024/06/13
 */
public interface StateMachineBuilder<S, E> {
    ExternalTransitionBuilder<S, E> externalTransition();

    InternalTransitionBuilder<S, E> internalTransition();


    void setFailCallback(FailCallback<S, E> callback);

    StateMachine<S, E> build(String machineId);

}
