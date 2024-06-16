package com.github.ningasekiro.builder;


/**
 * fail callback
 * 找不到transition时回调
 *
 * @author NingaSekiro
 * @date 2024/06/13
 */
@FunctionalInterface
public interface FailCallback<S, E> {

    /**
     * Callback function to execute if failed to trigger an Event
     *
     */
    void onFail(S sourceState, E event);
}
