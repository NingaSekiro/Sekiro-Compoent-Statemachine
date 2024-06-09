package com.alibaba.cola.statemachine;

import org.springframework.messaging.Message;

import java.util.List;


/**
 * StateMachine
 *
 * @author Frank Zhang
 *
 * @param <S> the type of state
 * @param <E> the type of event
 * @date 2020-02-07 2:13 PM
 */
public interface StateMachine<S, E>{

    /**
     * Send an event {@code E} to the state machine.
     *
     * @param sourceState the source state
     * @return the target state
     */
     S fireEvent(S sourceState, Message<E> message);

    List<S> fireParallelEvent(S sourceState, Message<E> message);


    /**
     * MachineId is the identifier for a State Machine
     */
    String getMachineId();
}
