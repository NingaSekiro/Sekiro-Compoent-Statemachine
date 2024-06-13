package com.sekiro.statemachine;


/**
 * 被动监听，不影响状态
 *
 * @author Changeme_q
 */
public interface Listener <S, E> {
    void stateChanged(StateContext<S, E> stateContext);
}
