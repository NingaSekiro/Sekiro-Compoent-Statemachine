package com.alibaba.cola.statemachine;

import org.springframework.messaging.Message;


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
     * Verify if an event {@code E} can be fired from current state {@code S}
     * @param sourceStateId
     * @param event
     * @return
     */
    boolean verify(S sourceStateId,E event);

    /**
     * Send an event {@code E} to the state machine.
     *
     * @param sourceState the source state
     * @param event the event to send
     * @return the target state
     */
     S fireEvent(S sourceState, E event, Message<E> message);

    /**
     * MachineId is the identifier for a State Machine
     * @return
     */
    String getMachineId();
}
