package com.github.ningasekiro.test;


import com.github.ningasekiro.Condition;
import com.github.ningasekiro.StateMachine;
import com.github.ningasekiro.builder.AlertFailCallback;
import com.github.ningasekiro.builder.StateMachineBuilder;
import com.github.ningasekiro.builder.StateMachineBuilderFactory;
import com.github.ningasekiro.exception.TransitionFailException;
import com.github.ningasekiro.impl.StateMachineException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static com.github.ningasekiro.test.CommonComponent.*;
import static com.github.ningasekiro.test.Events.EVENT1;
import static com.github.ningasekiro.test.States.STATE1;
import static com.github.ningasekiro.test.States.STATE2;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StateMachineUnNormalTest {
    /**
     * 未找到Transition
     *
     * @author NingaSekiro
     * @date 2024/06/16
     */
    @Test
    public void testNotFoundTransition() {
        StateMachineBuilder<States, Events> builder = StateMachineBuilderFactory.create();
        builder.setFailCallback(new AlertFailCallback<>());
        builder.externalTransition()
                .from(STATE1)
                .to(STATE2)
                .on(EVENT1)
                .perform(doAction());
        StateMachine<States, Events> stateMachine = builder.build("FailedMachine");
        Message<Events> message2 =
                MessageBuilder.withPayload(EVENT1).setHeader("context",
                        new Context("2")).build();
        Assertions.assertThrows(TransitionFailException.class,
                () -> stateMachine.fireEvent(STATE2, message2));
    }

    @Test
    public void testConditionNotMeet() {
        StateMachineBuilder<States, Events> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(STATE1)
                .to(STATE2)
                .on(EVENT1)
                .when(checkConditionFalse())
                .perform(doAction());

        StateMachine<States, Events> stateMachine = builder.build("NotMeetConditionMachine");
        Message<Events> message =
                MessageBuilder.withPayload(EVENT1).setHeader("context",
                        new Context("2")).build();
        States target = stateMachine.fireEvent(STATE1, message);
        Assertions.assertEquals(STATE1, target);
    }


    /**
     * Action抛出的RuntimeException由errorAction捕获，不经过Listener
     *
     * @author NingaSekiro
     * @date 2024/06/16
     */
    @Test
    public void testFailedActionHandler() {
        StateMachineBuilder<States, Events> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(STATE1)
                .to(STATE2)
                .on(EVENT1)
                .perform(doErrorAction(), errorAction())
                .listen(listener());
        StateMachine<States, Events> stateMachine = builder.build("FailedActionMachine");
        Message<Events> message =
                MessageBuilder.withPayload(EVENT1).setHeader("context",
                        new Context("2")).build();
        Assertions.assertThrows(RuntimeException.class,
                () -> stateMachine.fireEvent(STATE1, message));
    }


    @Test
    public void testDuplicatedTransition() {
        StateMachineBuilder<States, Events> builder = StateMachineBuilderFactory.create();
        assertThrows(StateMachineException.class, () -> {
            builder.externalTransition()
                    .from(STATE1)
                    .to(STATE2)
                    .on(EVENT1)
                    .when(checkCondition())
                    .perform(doAction());

            builder.externalTransition()
                    .from(STATE1)
                    .to(STATE2)
                    .on(EVENT1)
                    .when(checkCondition())
                    .perform(doAction());
        });
    }

    @Test
    public void testDuplicateMachine() {
        StateMachineBuilder<States, Events> builder = StateMachineBuilderFactory.create();
        assertThrows(StateMachineException.class, () -> {
            builder.externalTransition()
                    .from(STATE1)
                    .to(STATE2)
                    .on(EVENT1)
                    .when(checkCondition())
                    .perform(doAction());

            builder.build("DuplicatedMachine");
            builder.build("DuplicatedMachine");
        });
    }

    private Condition<States, Events> checkCondition() {
        return (ctx) -> {
            return true;
        };
    }

    private Condition<States, Events> checkConditionFalse() {
        return (ctx) -> {
            return false;
        };
    }
}
