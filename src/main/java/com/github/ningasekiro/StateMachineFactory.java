package com.github.ningasekiro;

import com.github.ningasekiro.impl.StateMachineException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * state machine factory
 *
 * @author NingaSekiro
 * @date 2024/06/14
 */
public class StateMachineFactory {
    static Map<String, StateMachine<?, ?>> stateMachineMap =
            new ConcurrentHashMap<>();

    public static <S, E> void register(StateMachine<S, E> stateMachine){
        String machineId = stateMachine.getMachineId();
        if(stateMachineMap.get(machineId) != null){
            throw new StateMachineException("The state machine with id ["+machineId+"] is already built, no need to build again");
        }
        stateMachineMap.put(stateMachine.getMachineId(), stateMachine);
    }

    public static StateMachine<?, ?> get(String machineId){
        StateMachine<?, ?> stateMachine = stateMachineMap.get(machineId);
        if(stateMachine == null){
            throw new StateMachineException("There is no stateMachine instance for "+machineId+", please build it first");
        }
        return stateMachine;
    }
}
