package com.alibaba.cola.statemachine.builder;

import com.alibaba.cola.statemachine.State;
import com.alibaba.cola.statemachine.impl.StateHelper;
import com.alibaba.cola.statemachine.impl.TransitionType;

import java.util.Map;

/**
 * Take TransitionBuilderImpl and TransitionsBuilderImpl sharing variables
 * and methods to that abstract class, which acts as their parent,
 * instead of having TransitionsBuilderImpl inherit from
 * TransitionsBuilderImpl. I think that the multi-flow
 * builder(TransitionsBuilderImpl) and single-flow
 * builder(TransitionBuilderImpl) are equal and not supposed to be
 * parent-child relationship, they from, when, and perform methods
 * are not the same, and although it looks like just a set of loops
 * but logically should not be inherited over Override.
 * ( Just as there was no relationship, why should we talk to each other,
 * say a we are not suitable). With the abstract class, multi-flow and single-flow
 * only use to focus on their respective functions are single-flow,
 * or multi-flow. Conform to a single duty.
 * @author welliem
 * @date 2023-07-14 12:13
 */
 abstract class AbstractTransitionBuilder<S,E> implements  From<S,E>,On<S,E>,To<S,E>{

    final Map<S, State<S, E>> stateMap;

    protected State<S, E> target;

    final TransitionType transitionType;

    public AbstractTransitionBuilder(Map<S, State<S, E>> stateMap, TransitionType transitionType) {
        this.stateMap = stateMap;
        this.transitionType = transitionType;
    }
    @Override
    public To<S, E> to(S stateId) {
        target = StateHelper.getState(stateMap, stateId);
        return this;
    }
}
