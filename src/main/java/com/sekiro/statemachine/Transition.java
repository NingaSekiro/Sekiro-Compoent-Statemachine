package com.sekiro.statemachine;

import com.sekiro.statemachine.impl.TransitionType;
import org.springframework.messaging.Message;


public interface Transition<S, E> {

    State<S, E> getSource();

    void setSource(State<S, E> state);

    E getEvent();

    void setEvent(E event);

    void setType(TransitionType type);

    State<S, E> getTarget();

    void setTarget(State<S, E> state);

    Condition<S, E> getCondition();

    void setCondition(Condition<S, E> condition);

    Action<S, E> getAction();

    void setAction(Action<S, E> action);

    Listener<S, E> getListener();

    void setListener(Listener<S, E> listener);

    State<S, E> transit(Message<E> ctx);

    void verify();
}
