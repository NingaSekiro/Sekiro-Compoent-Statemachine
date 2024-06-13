package com.sekiro.statemachine;

import org.springframework.messaging.Message;

import java.util.List;

/**
 * state machine
 *
 * @author NingaSekiro
 * @date 2024/06/14
 */
public interface StateMachine<S, E>{


     S fireEvent(S sourceState, Message<E> message);

    List<S> fireParallelEvent(S sourceState, Message<E> message);

    String getMachineId();

    String generatePlantUml();
}
