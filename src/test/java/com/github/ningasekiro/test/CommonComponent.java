package com.github.ningasekiro.test;

import com.github.ningasekiro.Action;
import com.github.ningasekiro.Listener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class CommonComponent {

    private static final Log LOGGER = LogFactory.getLog(CommonComponent.class);

     public static Action<States, Events> doAction() {
        return (stateContext) -> {
            LOGGER.info("action from:" + stateContext.getSource() + " to:" + stateContext.getTarget());
        };
    }

    public static Action<States, Events> doErrorAction() {
        return (stateContext) -> {
            LOGGER.info("action from:" + stateContext.getSource() + " to:" + stateContext.getTarget());
            throw new RuntimeException("ddd");
        };
    }

    public static Action<States, Events> errorAction() {
        return (stateContext) -> {
            if (stateContext.getException() != null) {
                LOGGER.error("errorAction from:" + stateContext.getSource() + " to:" + stateContext.getTarget(),
                        stateContext.getException());
            }
        };
    }

    public static Listener<States, Events> listener() {
        return (stateContext) -> {
            LOGGER.info("listener from:" + stateContext.getSource() + " to:" + stateContext.getTarget());
            throw new RuntimeException("listener don't effect the state change");
        };
    }
}
