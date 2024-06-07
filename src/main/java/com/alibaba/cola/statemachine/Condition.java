package com.alibaba.cola.statemachine;

/**
 * Condition
 *
 * @author Frank Zhang
 * @date 2020-02-07 2:50 PM
 */
public interface Condition<S, E> {

    /**
     * @param context context object
     * @return whether the context satisfied current condition
     */
    boolean isSatisfied(StateContext<S, E> context);

    default String name() {
        return this.getClass().getSimpleName();
    }
}
