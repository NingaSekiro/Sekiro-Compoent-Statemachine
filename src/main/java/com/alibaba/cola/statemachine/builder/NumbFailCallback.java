package com.alibaba.cola.statemachine.builder;

/**
 * Default fail callback, do nothing.
 *
 * @author 龙也
 * @date 2022/9/15 12:02 PM
 */
public class NumbFailCallback<S, E> implements FailCallback<S, E> {

    @Override
    public void onFail(S sourceState, E event) {
        //do nothing
    }
}
