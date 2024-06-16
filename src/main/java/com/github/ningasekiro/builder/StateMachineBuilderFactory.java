package com.github.ningasekiro.builder;

/**
 * state machine builder factory
 *
 * @author NingaSekiro
 * @date 2024/06/14
 */
public class StateMachineBuilderFactory {
    public static <S, E> StateMachineBuilder<S, E >create(){
        return new StateMachineBuilderImpl<>();
    }
}