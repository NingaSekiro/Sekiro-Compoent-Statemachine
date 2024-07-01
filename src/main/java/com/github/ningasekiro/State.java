package com.github.ningasekiro;

import java.util.Collection;
import java.util.List;


public interface State<S, E> extends Visitable {

    S getId();

    Transition<S, E> addTransition(E event, State<S, E> target);

    List<Transition<S, E>> addTransitions(E event, List<State<S, E>> targets);

    Collection<Transition<S, E>> getAllTransitions();

    List<Transition<S, E>> getEventTransitions(E event);

}
