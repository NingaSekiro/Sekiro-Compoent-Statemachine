package com.github.ningasekiro.builder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.ningasekiro.State;
import com.github.ningasekiro.StateMachine;
import com.github.ningasekiro.StateMachineFactory;
import com.github.ningasekiro.impl.StateMachineImpl;
import com.github.ningasekiro.impl.TransitionType;


/**
 * state machine builder impl
 *
 * @author NingaSekiro
 * @date 2024/06/14
 */
public class StateMachineBuilderImpl<S, E> implements StateMachineBuilder<S, E> {

    private final Map<S, State<S, E>> stateMap = new ConcurrentHashMap<>();
    private final StateMachineImpl<S, E> stateMachine = new StateMachineImpl<>(stateMap);
    private FailCallback<S, E> failCallback = new NumbFailCallback<>();

    @Override
    public ExternalTransitionBuilder<S, E> externalTransition() {
        return  new TransitionBuilderImpl<>(stateMap, TransitionType.EXTERNAL);
    }

    @Override
    public InternalTransitionBuilder<S, E> internalTransition() {
        return new TransitionBuilderImpl<>(stateMap, TransitionType.INTERNAL);
    }

    @Override
    public void setFailCallback(FailCallback<S, E> callback) {
        this.failCallback = callback;
    }

    @Override
    public StateMachine<S, E> build(String machineId) {
        stateMachine.setMachineId(machineId);
        stateMachine.setFailCallback(failCallback);
        StateMachineFactory.register(stateMachine);
        return stateMachine;
    }

}
