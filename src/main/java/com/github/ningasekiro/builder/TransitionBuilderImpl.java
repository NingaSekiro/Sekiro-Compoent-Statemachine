package com.github.ningasekiro.builder;

import com.github.ningasekiro.*;
import com.github.ningasekiro.impl.ActionHelper;
import com.github.ningasekiro.impl.StateHelper;
import com.github.ningasekiro.impl.TransitionType;

import java.util.*;

/**
 * @author NingaSekiro
 * @date 2024/06/10
 */
public class TransitionBuilderImpl<S, E> implements From<S, E>, To<S, E>, OptionalStep<S, E>,
        ExternalTransitionBuilder<S, E>, InternalTransitionBuilder<S, E> {
    private final Map<S, State<S, E>> stateMap;
    protected List<State<S, E>> targets = new ArrayList<>();
    private final TransitionType transitionType;
    private final List<State<S, E>> sources = new ArrayList<>();
    private final List<Transition<S, E>> transitions = new ArrayList<>();

    public TransitionBuilderImpl(Map<S, State<S, E>> stateMap, TransitionType transitionType) {
        this.stateMap = stateMap;
        this.transitionType = transitionType;
    }

    @Override
    public final From<S, E> from(S... stateIds) {
        for (S stateId : stateIds) {
            sources.add(StateHelper.getState(stateMap, stateId));
        }
        return this;
    }

    @Override
    public To<S, E> to(S... stateIds) {
        targets = StateHelper.getStates(stateMap, stateIds);
        return this;
    }

    @Override
    public OptionalStep<S, E> when(Condition<S, E> condition) {
        for (Transition<S, E> transition : transitions) {
            transition.setCondition(condition);
        }
        return this;
    }

    @Override
    public OptionalStep<S, E> on(E event) {
        for (State<S, E> source : sources) {
            List<Transition<S, E>> transitionList = source.addTransitions(event, targets,
                    transitionType);
            transitions.addAll(transitionList);
        }

        return this;
    }

    @Override
    public OptionalStep<S, E> perform(Action<S, E> action) {
        for (Transition<S, E> transition : transitions) {
            transition.setAction(action);
        }
        return this;
    }

    @Override
    public OptionalStep<S, E> perform(Action<S, E> action, Action<S, E> error) {
        for (Transition<S, E> transition : transitions) {
            transition.setAction(ActionHelper.errorCallingAction(action, error));
        }
        return this;
    }

    @Override
    public OptionalStep<S, E> listen(Listener<S, E> listener) {
        for (Transition<S, E> transition : transitions) {
            transition.setListener(listener);
        }
        return this;
    }

    @Override
    public To<S, E> within(S stateId) {
        State<S, E> state = StateHelper.getState(stateMap, stateId);
        sources.add(state);
        targets.add(state);
        return this;
    }
}
