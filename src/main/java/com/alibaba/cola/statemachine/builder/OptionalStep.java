package com.alibaba.cola.statemachine.builder;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.Listener;

/**
 * On
 *
 * @author Frank Zhang
 * @date 2020-02-07 6:14 PM
 */
public interface OptionalStep<S, E> {
    /**
     * Add condition for the transition
     *
     * @param condition transition condition
     * @return When clause builder
     */
    OptionalStep<S, E> when(Condition<S, E> condition);

    OptionalStep<S, E> perform(Action<S, E> action);

    OptionalStep<S, E> perform(Action<S, E> action, Action<S, E> errorAction);

    OptionalStep<S, E> listen(Listener<S, E> listener);

}
