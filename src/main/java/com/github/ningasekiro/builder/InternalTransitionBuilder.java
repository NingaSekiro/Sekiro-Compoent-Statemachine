package com.github.ningasekiro.builder;


/**
 * internal transition builder
 *
 * @author NingaSekiro
 * @date 2024/06/14
 */
public interface InternalTransitionBuilder <S, E> {

    To<S, E> within(S stateId);
}
