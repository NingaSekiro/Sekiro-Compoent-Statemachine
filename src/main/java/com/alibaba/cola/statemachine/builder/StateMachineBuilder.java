package com.alibaba.cola.statemachine.builder;

import com.alibaba.cola.statemachine.StateMachine;


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
