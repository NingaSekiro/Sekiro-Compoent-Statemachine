package com.alibaba.cola.statemachine.builder;

/**
 * From
 *
 * @author Frank Zhang
 * @date 2020-02-07 6:13 PM
 */
public interface From<S, E> {
    public To<S, E> to(S... stateIds);
}
