package com.github.ningasekiro.builder;

import com.github.ningasekiro.Action;
import com.github.ningasekiro.Condition;
import com.github.ningasekiro.Listener;


/**
 * optional step(可选配置)
 *
 * @author NingaSekiro
 * @date 2024/06/14
 */
public interface OptionalStep<S, E> {

    OptionalStep<S, E> when(Condition<S, E> condition);

    OptionalStep<S, E> perform(Action<S, E> action);

    OptionalStep<S, E> perform(Action<S, E> action, Action<S, E> errorAction);

    OptionalStep<S, E> listen(Listener<S, E> listener);

}
