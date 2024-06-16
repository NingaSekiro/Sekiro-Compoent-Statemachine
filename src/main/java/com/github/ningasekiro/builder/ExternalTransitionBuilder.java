package com.github.ningasekiro.builder;


public interface ExternalTransitionBuilder<S, E> {
    From<S, E> from(S... stateIds);
}
