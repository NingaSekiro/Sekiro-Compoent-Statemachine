package com.alibaba.cola.test;

import com.alibaba.cola.statemachine.*;
import com.alibaba.cola.statemachine.builder.AlertFailCallback;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderImpl;
import com.alibaba.cola.statemachine.exception.TransitionFailException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;


import java.util.List;

/**
 * StateMachineTest
 *
 * @author Frank Zhang
 * @date 2020-02-08 12:19 PM
 */
public class StateMachineTest {

    static String MACHINE_ID = "TestStateMachine";

    static enum States {
        STATE1,
        STATE2,
        STATE3,
        STATE4
    }

    static enum Events {
        EVENT1,
        EVENT2,
        EVENT3,
        EVENT4,
        INTERNAL_EVENT
    }

    static class Context {
        String operator = "frank";
        String entityId = "123465";
    }

    @Test
    public void testExternalNormal() {
        StateMachineBuilder<States, Events> builder = new StateMachineBuilderImpl<>();
        builder.externalTransition()
                .from(States.STATE1)
                .to(States.STATE2)
                .on(Events.EVENT1)
                .when(checkCondition())
                .perform(doAction(), errorAction());
//                .perform(doAction());
        StateMachine<States, Events> stateMachine = builder.build(MACHINE_ID);

        Message<Events> message =
                MessageBuilder.withPayload(Events.EVENT1).setHeader("context",
                        new Context()).build();
        States target = stateMachine.fireEvent(States.STATE1, message);
        Assertions.assertEquals(States.STATE2, target);
    }

    private Condition<States, Events> checkCondition() {
        return new Condition<States, Events>() {
            @Override
            public boolean isSatisfied(StateContext<States, Events> context) {
                System.out.println("Check condition : " + context);
                return true;
            }
        };
    }

    private Action<States, Events> doAction() {
        return (from, to, stateContext) -> {
            System.out.println(" from:" + from + " to:" + to );
//            throw new RuntimeException("ddd");
        };
    }

    private Action<States, Events> errorAction() {
        return (from, to, stateContext) -> {
            System.out.println(" from:" + from + " to:" + to);
        };
    }

}
