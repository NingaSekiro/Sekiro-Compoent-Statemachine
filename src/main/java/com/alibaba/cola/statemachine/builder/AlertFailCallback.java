package com.alibaba.cola.statemachine.builder;

import com.alibaba.cola.statemachine.exception.TransitionFailException;


/**
 * alert fail callback
 *
 * @author NingaSekiro
 * @date 2024/06/13
 */
public class AlertFailCallback<S, E> implements FailCallback<S, E> {

    @Override
    public void onFail(S sourceState, E event) {
        throw new TransitionFailException(
            "Cannot fire event [" + event + "] on current state [" + sourceState + "] ]"
        );
    }
}
