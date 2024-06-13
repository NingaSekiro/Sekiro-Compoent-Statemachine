package com.sekiro.statemachine;

import com.sekiro.statemachine.impl.TransitionType;

import java.util.Collection;
import java.util.List;


public interface State<S, E> extends Visitable {

    S getId();

    Transition<S, E> addTransition(E event, State<S, E> target, TransitionType transitionType);

    List<Transition<S, E>> addTransitions(E event, List<State<S, E>> targets, TransitionType transitionType);

    Collection<Transition<S, E>> getAllTransitions();

    List<Transition<S, E>> getEventTransitions(E event);

}
