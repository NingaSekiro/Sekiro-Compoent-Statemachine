package com.alibaba.cola.statemachine.impl;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.StateContext;

public class ActionHelper {
    public static <S, E> Action<S, E> errorCallingAction(final Action<S, E> action,
                                                         final Action<S, E> errorAction) {
        return new Action<S, E>() {
            @Override
            public void execute(S from, S to, StateContext<S, E> stateContext) {
                try {
                    action.execute(from, to, stateContext);
                } catch (Exception exception) {
                    // notify something wrong is happening in actions execution.
                    errorAction.execute(from, to,
                            new StateContextImpl<>(stateContext.getMessage(),
                                    stateContext.getTransition(),
                                    stateContext.getSource(), stateContext.getTarget()
                                    , exception));
                    throw exception;
                }
            }
        };
    }
}
