package com.github.ningasekiro.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.ningasekiro.State;
import com.github.ningasekiro.StateMachine;
import com.github.ningasekiro.Transition;
import com.github.ningasekiro.builder.FailCallback;
import com.github.ningasekiro.PlantUMLVisitor;
import org.springframework.messaging.Message;


/**
 * state machine impl
 *
 * @author NingaSekiro
 * @date 2024/06/14
 */
public class StateMachineImpl<S, E> implements StateMachine<S, E> {

    private String machineId;

    private final Map<S, State<S, E>> stateMap;

    private FailCallback<S, E> failCallback;

    public StateMachineImpl(Map<S, State<S, E>> stateMap) {
        this.stateMap = stateMap;
    }

    @Override
    public S fireEvent(S sourceStateId, Message<E> ctx) {
        E event = ctx.getPayload();
        Transition<S, E> transition = routeTransition(sourceStateId, event, ctx);
        if (transition == null) {
            failCallback.onFail(sourceStateId, event);
            return sourceStateId;
        }
        return transition.transit(ctx).getId();
    }

    @Override
    public S fireTask(S sourceState, Message<E> message, List<E> events) {
        for (E event : events) {
            Transition<S, E> transition = routeTransition(sourceState, event, message);
            if (transition == null) {
                failCallback.onFail(sourceState, event);
                return sourceState;
            }
            sourceState = transition.transit(message).getId();
        }
        return sourceState;
    }

    @Override
    public List<S> fireParallelEvent(S sourceState, Message<E> message) {
        E event = message.getPayload();
        List<Transition<S, E>> transitions = routeTransitions(sourceState, event, message);
        List<S> result = new ArrayList<>();
        if (transitions == null || transitions.isEmpty()) {
            failCallback.onFail(sourceState, event);
            result.add(sourceState);
            return result;
        }
        for (Transition<S, E> transition : transitions) {
            S id = transition.transit(message).getId();
            result.add(id);
        }
        return result;
    }

    @Override
    public String generatePlantUml() {
        PlantUMLVisitor plantUmlVisitor = new PlantUMLVisitor();
        StringBuilder sb = new StringBuilder();
        for (State<S, E> state : stateMap.values()) {
            sb.append(state.accept(plantUmlVisitor));
        }
        return sb.toString();
    }

    private Transition<S, E> routeTransition(S sourceStateId, E event, Message<E> ctx) {
        State<S, E> sourceState = getState(sourceStateId);

        List<Transition<S, E>> transitions = sourceState.getEventTransitions(event);

        if (transitions == null || transitions.isEmpty()) {
            return null;
        }
        Transition<S, E> transit = null;
        for (Transition<S, E> transition : transitions) {
            StateContextImpl<S, E> stateContext = new StateContextImpl<>(ctx, transition, sourceState
                    , transition.getTarget(), null);
            if (transition.getCondition() == null) {
                transit = transition;
            } else if (transition.getCondition().isSatisfied(stateContext)) {
                transit = transition;
                break;
            }
        }

        return transit;
    }

    private List<Transition<S, E>> routeTransitions(S sourceStateId, E event, Message<E> message) {
        State<S, E> sourceState = getState(sourceStateId);
        List<Transition<S, E>> result = new ArrayList<>();
        List<Transition<S, E>> transitions = sourceState.getEventTransitions(event);
        if (transitions == null || transitions.isEmpty()) {
            return null;
        }
        for (Transition<S, E> transition : transitions) {
            StateContextImpl<S, E> stateContext = new StateContextImpl<>(message, transition, sourceState
                    , transition.getTarget(), null);
            Transition<S, E> transit = null;
            if (transition.getCondition() == null) {
                transit = transition;
            } else if (transition.getCondition().isSatisfied(stateContext)) {
                transit = transition;
            }
            result.add(transit);
        }
        return result;
    }

    private State<S, E> getState(S currentStateId) {
        return StateHelper.getState(stateMap, currentStateId);
    }


    @Override
    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public void setFailCallback(FailCallback<S, E> failCallback) {
        this.failCallback = failCallback;
    }
}
