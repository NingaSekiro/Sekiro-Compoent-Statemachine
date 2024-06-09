package com.alibaba.cola.statemachine;

/**
 * {@code StateMachineListener} for various state machine events.
 *
 * @author Janne Valkealahti
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public interface StateMachineListener<S,E> {

	/**
	 * Notified when state is changed.
	 *
	 * @param from the source state
	 * @param to the target state
	 */
	void stateChanged(State<S,E> from, State<S,E> to);

}
