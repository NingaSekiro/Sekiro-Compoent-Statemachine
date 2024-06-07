package com.alibaba.cola.statemachine.impl;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.StateContext;

public class ActionHelper {
    public static <S, E> Action<S, E> errorCallingAction(final Action<S, E> action,
                                                         final Action<S, E> errorAction) {
        return new Action<S, E>() {
            @Override
            public void execute(S from, S to, E event, StateContext<S, E> stateContext) {
                try {
                    action.execute(from, to, event, stateContext);
                } catch (Exception exception) {
                    // notify something wrong is happening in actions execution.
                    try {
                        errorAction.execute(from, to, event,
                                new StateContextImpl<>(stateContext.getMessage(),
                                        stateContext.getTransition(),
                                        stateContext.getSource(), stateContext.getTarget()
                                        , exception));
                    } catch (Exception e) {
                        // not interested
                    }
                    throw exception;
                }
            }
        };
    }
}
