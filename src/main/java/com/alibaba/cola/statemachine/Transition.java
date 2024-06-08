package com.alibaba.cola.statemachine;

import com.alibaba.cola.statemachine.impl.TransitionType;
import org.springframework.messaging.Message;

/**
 * {@code Transition} is something what a state machine associates with a state
 * changes.
 *
 * @param <S> the type of state
 * @param <E> the type of event
 * @author Frank Zhang
 * @date 2020-02-07 2:20 PM
 */
public interface Transition<S, E> {
    /**
     * Gets the source state of this transition.
     *
     * @return the source state
     */
    State<S, E> getSource();

    void setSource(State<S, E> state);

    E getEvent();

    void setEvent(E event);

    void setType(TransitionType type);

    /**
     * Gets the target state of this transition.
     *
     * @return the target state
     */
    State<S, E> getTarget();

    void setTarget(State<S, E> state);

    /**
     * Gets the guard of this transition.
     *
     * @return the guard
     */
    Condition<S, E> getCondition();

    void setCondition(Condition<S, E> condition);

    Action<S, E> getAction();

    void setAction(Action<S, E> action);

    /**
     * Do transition from source state to target state.
     *
     * @return the target state
     */

    State<S, E> transit(Message<E> ctx, boolean checkCondition);

    /**
     * Verify transition correctness
     */
    void verify();
}
