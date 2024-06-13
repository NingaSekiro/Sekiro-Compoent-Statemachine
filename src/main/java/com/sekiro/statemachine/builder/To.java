package com.sekiro.statemachine.builder;


/**
 * to
 *
 * @author NingaSekiro
 * @date 2024/06/14
 */
public interface To<S, E> {
    OptionalStep <S, E> on(E event);
}
