package com.sekiro.statemachine.builder;

/**
 * numb fail callback
 * numb不处理，不抛异常
 *
 * @author NingaSekiro
 * @date 2024/06/13
 */
public class NumbFailCallback<S, E> implements FailCallback<S, E> {

    @Override
    public void onFail(S sourceState, E event) {
        //do nothing
    }
}