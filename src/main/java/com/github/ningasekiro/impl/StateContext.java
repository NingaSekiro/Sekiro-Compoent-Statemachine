
package com.github.ningasekiro.impl;

import com.github.ningasekiro.State;
import com.github.ningasekiro.Transition;
import lombok.Data;
import org.springframework.messaging.Message;


/**
 * state context impl
 *
 * @author NingaSekiro
 * @date 2024/06/14
 */
@Data
public class StateContext<S, E> {
    private final Message<E> message;
    private final Transition<S, E> transition;
    private final Exception exception;

    public S getSource() {
        return transition.getSource().getId();
    }

    public S getTarget() {
        return transition.getTarget().getId();
    }
}
