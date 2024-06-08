package com.alibaba.cola.test;

import com.alibaba.cola.statemachine.*;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

/**
 * StateMachineTest
 *
 * @author Frank Zhang
 * @date 2020-02-08 12:19 PM
 */
public class StateMachineTest {

    static String MACHINE_ID = "TestStateMachine";

    enum States {
        STATE1,
        STATE2,
        STATE3,
        STATE4
    }

    enum Events {
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

        builder.externalTransition()
                .from(States.STATE2)
                .to(States.STATE3)
                .on(Events.EVENT2)
                .when(checkCondition())
                .perform(doAction(), errorAction());
        StateMachine<States, Events> stateMachine = builder.build(MACHINE_ID);
        Message<Events> message =
                MessageBuilder.withPayload(Events.EVENT1).setHeader("context",
                        new Context()).build();
        States target = stateMachine.fireEvent(States.STATE1, message);
        Message<Events> message2 =
                MessageBuilder.withPayload(Events.EVENT2).copyHeaders(message.getHeaders()).build();
        States target2 = stateMachine.fireEvent(target, message2);
        Assertions.assertEquals(States.STATE3, target2);
    }

    @Test
    public void testChoice() {
        StateMachineBuilder<States, Events> builder = new StateMachineBuilderImpl<>();
        builder.internalTransition()
                .within(StateMachineTest.States.STATE1)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());
        builder.externalTransition()
                .from(StateMachineTest.States.STATE1)
                .to(StateMachineTest.States.STATE2)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());
        builder.externalTransition()
                .from(StateMachineTest.States.STATE1)
                .to(StateMachineTest.States.STATE3)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());
        StateMachine<States, Events> stateMachine = builder.build("ChoiceConditionMachine");
        StateMachineTest.States target1 = stateMachine.fireEvent(StateMachineTest.States.STATE1, StateMachineTest.Events.EVENT1, new Context("1"));
        Assertions.assertEquals(StateMachineTest.States.STATE1, target1);
        StateMachineTest.States target2 = stateMachine.fireEvent(StateMachineTest.States.STATE1, StateMachineTest.Events.EVENT1, new Context("2"));
        Assertions.assertEquals(StateMachineTest.States.STATE2, target2);
        StateMachineTest.States target3 = stateMachine.fireEvent(StateMachineTest.States.STATE1, StateMachineTest.Events.EVENT1, new Context("3"));
        Assertions.assertEquals(StateMachineTest.States.STATE3, target3);
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
            System.out.println(" from:" + from + " to:" + to);
//            throw new RuntimeException("ddd");
        };
    }

    private Action<States, Events> errorAction() {
        return (from, to, stateContext) -> {
            System.out.println(" from:" + from + " to:" + to);
        };
    }

}
