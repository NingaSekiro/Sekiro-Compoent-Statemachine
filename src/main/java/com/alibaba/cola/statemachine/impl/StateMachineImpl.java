package com.alibaba.cola.statemachine.impl;

import java.util.List;
import java.util.Map;

import com.alibaba.cola.statemachine.State;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.Transition;
import com.alibaba.cola.statemachine.builder.FailCallback;
import org.springframework.messaging.Message;

/**
 * For performance consideration,
 * The state machine is made "stateless" on purpose.
 * Once it's built, it can be shared by multi-thread
 * <p>
 * One side effect is since the state machine is stateless, we can not get current state from State Machine.
 *
 * @author Frank Zhang
 * @date 2020-02-07 5:40 PM
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
        return transition.transit(ctx, false).getId();
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
