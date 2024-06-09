package com.alibaba.cola.statemachine.builder;


public interface ExternalTransitionBuilder<S, E> {
    From<S, E> from(S... stateIds);
}
