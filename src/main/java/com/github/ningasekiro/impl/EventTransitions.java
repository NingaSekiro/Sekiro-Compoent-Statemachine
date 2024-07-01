package com.github.ningasekiro.impl;

import com.github.ningasekiro.Transition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * event transitions(多transition）
 *
 * @author NingaSekiro
 * @date 2024/06/14
 */
public class EventTransitions<S, E> {
    private final HashMap<E, List<Transition<S, E>>> eventTransitions = new HashMap<>();

    public void put(E event, Transition<S, E> transition) {
        if (eventTransitions.get(event) == null) {
            List<Transition<S, E>> transitions = new ArrayList<>();
            transitions.add(transition);
            eventTransitions.put(event, transitions);
        } else {
            List<Transition<S, E>> existingTransitions = eventTransitions.get(event);
            verify(existingTransitions, transition);
            existingTransitions.add(transition);
        }
    }

    private void verify(List<Transition<S, E>> existingTransitions, Transition<S, E> newTransition) {
        for (Transition<S, E> transition : existingTransitions) {
            if (transition.equals(newTransition)) {
                throw new StateMachineException(transition + " already Exist, you can not add another one");
            }
        }
    }

    public List<Transition<S, E>> get(E event) {
        return eventTransitions.get(event);
    }

    public List<Transition<S, E>> allTransitions() {
        List<Transition<S, E>> allTransitions = new ArrayList<>();
        for (List<Transition<S, E>> transitions : eventTransitions.values()) {
            allTransitions.addAll(transitions);
        }
        return allTransitions;
    }
}
