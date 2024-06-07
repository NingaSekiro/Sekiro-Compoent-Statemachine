package com.alibaba.cola.statemachine.builder;

import com.alibaba.cola.statemachine.exception.TransitionFailException;

/**
 * Alert fail callback, throw an {@code TransitionFailException}
 *
 * @author 龙也
 * @date 2022/9/15 12:02 PM
 */
public class AlertFailCallback<S, E> implements FailCallback<S, E> {

    @Override
    public void onFail(S sourceState, E event) {
        throw new TransitionFailException(
            "Cannot fire event [" + event + "] on current state [" + sourceState + "] ]"
        );
    }
}
