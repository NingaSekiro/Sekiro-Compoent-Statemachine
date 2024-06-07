/*
 * Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.cola.statemachine;

import org.springframework.messaging.Message;

public interface StateContext<S, E> {

    Message<E> getMessage();

    E getEvent();

    /**
     * Gets the transition.
     *
     * @return the transition
     */
    Transition<S, E> getTransition();

    /**
     * Gets the source state of this context. Generally source
     * is where a state machine is coming from which may be different
     * than what the transition source is.
     *
     * @return the source state
     */
    State<S, E> getSource();

    /**
     * Gets the target state of this context. Generally target
     * is where a state machine going to which may be different
     * than what the transition target is.
     *
     * @return the target state
     */
    State<S, E> getTarget();

    /**
     * Gets the exception associated with a context.
     *
     * @return the exception
     */
    Exception getException();
}
