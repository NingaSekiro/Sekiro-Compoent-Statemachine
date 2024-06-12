package com.alibaba.cola.statemachine.impl;


/**
 * state machine exception
 *
 * @author NingaSekiro
 * @date 2024/06/13
 */
public class StateMachineException extends RuntimeException{
    public StateMachineException(String message){
        super(message);
    }
}
