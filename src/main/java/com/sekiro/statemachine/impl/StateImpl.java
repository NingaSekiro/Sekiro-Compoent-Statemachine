package com.sekiro.statemachine.impl;

import com.sekiro.statemachine.State;
import com.sekiro.statemachine.Transition;
import com.sekiro.statemachine.Visitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * state impl
 *
 * @author NingaSekiro
 * @date 2024/06/14
 */
public class StateImpl<S, E> implements State<S, E> {
    protected final S stateId;
    private final EventTransitions<S, E> eventTransitions = new EventTransitions<>();
    StateImpl(S stateId) {
        this.stateId = stateId;
    }

    @Override
    public Transition<S, E> addTransition(E event, State<S, E> target, TransitionType transitionType) {
        Transition<S, E> newTransition = new TransitionImpl<>();
        newTransition.setSource(this);
        newTransition.setTarget(target);
        newTransition.setEvent(event);
        newTransition.setType(transitionType);
        eventTransitions.put(event, newTransition);
        return newTransition;
    }

    @Override
    public List<Transition<S, E>> addTransitions(E event, List<State<S, E>> targets, TransitionType transitionType) {
        List<Transition<S, E>> result = new ArrayList<>();
        for (State<S, E> target : targets) {
            Transition<S, E> secTransition = addTransition(event, target, transitionType);
            result.add(secTransition);
        }
        return result;
    }

    @Override
    public Collection<Transition<S, E>> getAllTransitions() {
        return eventTransitions.allTransitions();
    }

    @Override
    public List<Transition<S, E>> getEventTransitions(E event) {
        return eventTransitions.get(event);
    }

    @Override
    public S getId() {
        return stateId;
    }

    @Override
    public boolean equals(Object anObject) {
        if (anObject instanceof State) {
            State other = (State) anObject;
            return this.stateId.equals(other.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        return stateId.toString();
    }

    @Override
    public String accept(Visitor visitor) {
        String entry = visitor.visitOnEntry(this);
        String exit = visitor.visitOnExit(this);
        return entry + exit;
    }
}
