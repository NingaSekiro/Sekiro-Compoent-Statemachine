package com.alibaba.cola.test;


import com.sekiro.statemachine.Action;
import com.sekiro.statemachine.Condition;
import com.sekiro.statemachine.StateMachine;
import com.sekiro.statemachine.builder.StateMachineBuilder;
import com.sekiro.statemachine.builder.StateMachineBuilderFactory;
import com.sekiro.statemachine.impl.StateMachineException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class StateMachineUnNormalTest {

    @Test
    public void testConditionNotMeet() {
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(StateMachineTest.States.STATE1)
                .to(StateMachineTest.States.STATE2)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkConditionFalse())
                .perform(doAction());

        StateMachine<StateMachineTest.States, StateMachineTest.Events> stateMachine = builder.build("NotMeetConditionMachine");
        Message<StateMachineTest.Events> message =
                MessageBuilder.withPayload(StateMachineTest.Events.EVENT1).setHeader("context",
                        new StateMachineTest.Context("2")).build();
        StateMachineTest.States target = stateMachine.fireEvent(StateMachineTest.States.STATE1, message);
        Assertions.assertEquals(StateMachineTest.States.STATE1, target);
    }


    @Test
    public void testDuplicatedTransition() {
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events> builder = StateMachineBuilderFactory.create();
        assertThrows(StateMachineException.class, () -> {
            builder.externalTransition()
                    .from(StateMachineTest.States.STATE1)
                    .to(StateMachineTest.States.STATE2)
                    .on(StateMachineTest.Events.EVENT1)
                    .when(checkCondition())
                    .perform(doAction());

            builder.externalTransition()
                    .from(StateMachineTest.States.STATE1)
                    .to(StateMachineTest.States.STATE2)
                    .on(StateMachineTest.Events.EVENT1)
                    .when(checkCondition())
                    .perform(doAction());
        });
    }

    @Test
    public void testDuplicateMachine() {
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events> builder = StateMachineBuilderFactory.create();
        assertThrows(StateMachineException.class, () -> {
            builder.externalTransition()
                    .from(StateMachineTest.States.STATE1)
                    .to(StateMachineTest.States.STATE2)
                    .on(StateMachineTest.Events.EVENT1)
                    .when(checkCondition())
                    .perform(doAction());

            builder.build("DuplicatedMachine");
            builder.build("DuplicatedMachine");
        });
    }

    private Condition<StateMachineTest.States, StateMachineTest.Events> checkCondition() {
        return (ctx) -> {
            return true;
        };
    }

    private Condition<StateMachineTest.States, StateMachineTest.Events> checkConditionFalse() {
        return (ctx) -> {
            return false;
        };
    }

    private Action<StateMachineTest.States, StateMachineTest.Events> doAction() {
        return (ctx) -> {
            System.out.println("dd");
        };
    }
}
