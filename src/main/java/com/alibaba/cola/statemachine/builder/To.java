package com.alibaba.cola.statemachine.builder;

/**
 * To
 *
 * @author Frank Zhang
 * @date 2020-02-07 6:14 PM
 */
public interface To<S, E> {
    /**
     * Build transition event
     * @param event transition event
     * @return On clause builder
     */
    OptionalStep <S, E> on(E event);
}
