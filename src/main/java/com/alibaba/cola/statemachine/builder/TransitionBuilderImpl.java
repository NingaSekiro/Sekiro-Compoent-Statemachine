package com.alibaba.cola.statemachine.builder;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.State;
import com.alibaba.cola.statemachine.Transition;
import com.alibaba.cola.statemachine.impl.ActionHelper;
import com.alibaba.cola.statemachine.impl.StateHelper;
import com.alibaba.cola.statemachine.impl.TransitionType;

import java.util.Map;

/**
 * TransitionBuilderImpl
 *
 * @author Frank Zhang
 * @date 2020-02-07 10:20 PM
 */
class TransitionBuilderImpl<S, E> extends AbstractTransitionBuilder<S, E> implements ExternalTransitionBuilder<S, E>, InternalTransitionBuilder<S, E> {


    private State<S, E> source;
    private Transition<S, E> transition;

    public TransitionBuilderImpl(Map<S, State<S, E>> stateMap, TransitionType transitionType) {
        super(stateMap, transitionType);
    }

    @Override
    public From<S, E> from(S stateId) {
        source = StateHelper.getState(stateMap, stateId);
        return this;
    }

    @Override
    public To<S, E> within(S stateId) {
        source = target = StateHelper.getState(stateMap, stateId);
        return this;
    }

    @Override
    public When<S, E> when(Condition<S,E> condition) {
        transition.setCondition(condition);
        return this;
    }

    @Override
    public On<S, E> on(E event) {
        transition = source.addTransition(event, target, transitionType);
        return this;
    }

    @Override
    public void perform(Action<S, E> action) {
        transition.setAction(action);
    }

    @Override
    public void perform(Action<S, E> action, Action<S, E> error) {
        transition.setAction(ActionHelper.errorCallingAction(action, error));
    }


}
