
package com.alibaba.cola.statemachine.impl;

import com.alibaba.cola.statemachine.State;
import com.alibaba.cola.statemachine.StateContext;
import com.alibaba.cola.statemachine.Transition;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

public class StateContextImpl<S, E> implements StateContext<S, E> {

    private final Message<E> message;
    private final Transition<S, E> transition;
    private final State<S, E> source;
    private final State<S, E> target;
    private final Exception exception;

    public StateContextImpl(Message<E> message,
                            Transition<S, E> transition, State<S, E> source, State<S, E> target, Exception exception) {
        this.message = message;
        this.transition = transition;
        this.source = source;
        this.target = target;
        this.exception = exception;
    }


    @Override
    public E getEvent() {
        return message != null ? message.getPayload() : null;
    }

    @Override
    public Message<E> getMessage() {
        return message;
    }

    @Override
    public Transition<S, E> getTransition() {
        return transition;
    }

    @Override
    public State<S, E> getSource() {
        return source != null ? source : (transition != null ? transition.getSource() : null);
    }

    @Override
    public State<S, E> getTarget() {
        return target != null ? target : (transition != null ? transition.getTarget() : null);
    }

    @Override
    public Exception getException() {
        return exception;
    }

    @Override
    public String toString() {
        return "DefaultStateContext [ message=" + message + ", transition=" + transition + ", source=" + source + ", target="
                + target + ",exception=" + exception + "]";
    }
}
