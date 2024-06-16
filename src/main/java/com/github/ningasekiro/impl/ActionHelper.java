package com.github.ningasekiro.impl;

import com.github.ningasekiro.Action;


/**
 * action helper
 *
 * @author NingaSekiro
 * @date 2024/06/14
 */
public class ActionHelper {
    public static <S, E> Action<S, E> errorCallingAction(final Action<S, E> action,
                                                         final Action<S, E> errorAction) {
        return stateContext -> {
            try {
                action.execute(stateContext);
            } catch (Exception exception) {
                // notify something wrong is happening in actions execution.
                errorAction.execute(
                        new StateContextImpl<>(stateContext.getMessage(), stateContext.getTransition(), stateContext.getSource(), stateContext.getTarget(), exception));
                throw exception;
            }
        };
    }
}
