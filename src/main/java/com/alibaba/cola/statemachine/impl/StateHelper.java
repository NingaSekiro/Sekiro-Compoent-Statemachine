package com.alibaba.cola.statemachine.impl;

import com.alibaba.cola.statemachine.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * state helper
 *
 * @author NingaSekiro
 * @date 2024/06/14
 */
public class StateHelper {
    public static <S, E> State<S, E> getState(Map<S, State<S, E>> stateMap, S stateId) {
        State<S, E> state = stateMap.get(stateId);
        if (state == null) {
            state = new StateImpl<>(stateId);
            stateMap.put(stateId, state);
        }
        return state;
    }

    public static <S, E> List<State<S, E>> getStates(Map<S, State<S, E>> stateMap, S... stateIds) {
        List<State<S, E>> result = new ArrayList<>();
        for (S stateId : stateIds) {
            State<S, E> state = getState(stateMap, stateId);
            result.add(state);
        }
        return result;
    }
}
