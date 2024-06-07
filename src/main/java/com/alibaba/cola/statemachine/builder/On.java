package com.alibaba.cola.statemachine.builder;

import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateContext;

/**
 * On
 *
 * @author Frank Zhang
 * @date 2020-02-07 6:14 PM
 */
public interface On<S, E> extends When<S, E> {
    /**
     * Add condition for the transition
     *
     * @param condition transition condition
     * @return When clause builder
     */
    When<S, E> when(Condition<S, E> condition);
}
