package com.github.ningasekiro.test;

import com.github.ningasekiro.Action;
import com.github.ningasekiro.Condition;
import com.github.ningasekiro.Listener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class CommonCompoent {

    private static final Log LOGGER = LogFactory.getLog(CommonCompoent.class);

    public static Condition<States, Events> checkCondition1() {
        return context -> {
            Context context1 = context.getMessage().getHeaders().get("context",
                    Context.class);
            if (context1 != null) {
                return "1".equals(context1.getEntityId());
            }
            return false;
        };
    }

    public static Condition<States, Events> checkCondition2() {
        return context -> {
            Context context1 = context.getMessage().getHeaders().get("context",
                    Context.class);
            if (context1 != null) {
                return "2".equals(context1.getEntityId());
            }
            return false;
        };
    }

    public static Condition<States, Events> trueCondition() {
        return context -> true;
    }

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
