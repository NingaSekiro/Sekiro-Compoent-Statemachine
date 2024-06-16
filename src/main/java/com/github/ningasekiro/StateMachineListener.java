package com.github.ningasekiro;


/**
 * state machine listener
 *
 * @author NingaSekiro
 * @date 2024/06/14
 */
public interface StateMachineListener<S,E> {

	void stateChanged(State<S,E> from, State<S,E> to);

}
