package com.alibaba.cola.test;

import com.alibaba.cola.statemachine.*;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderImpl;
import com.alibaba.cola.statemachine.exception.TransitionFailException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;


import java.util.List;

import static com.alibaba.cola.test.StateMachineTest.Events.EVENT3;
import static com.alibaba.cola.test.StateMachineTest.States.*;


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

        public Context() {
        }

        public Context(String entityId) {
            this.entityId = entityId;
        }

        public String getEntityId() {
            return entityId;
        }
    }

    @Test
    public void testExternalNormal() {
        StateMachineBuilder<States, Events> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(STATE1)
                .to(STATE2)
                .on(Events.EVENT1)
                .when(trueCondition())
                .perform(doAction(), errorAction())
                .listen(listener());
        builder.externalTransition()
                .from(STATE2)
                .to(States.STATE3)
                .on(Events.EVENT2)
                .perform(doAction(), errorAction());
        StateMachine<States, Events> stateMachine = builder.build(MACHINE_ID);
        Message<Events> message =
                MessageBuilder.withPayload(Events.EVENT1).setHeader("context",
                        new Context()).build();
        stateMachine.fireEvent(STATE1, message);

        Message<Events> message2 =
                MessageBuilder.withPayload(Events.EVENT2).copyHeaders(message.getHeaders()).build();
        States target2 = stateMachine.fireEvent(STATE2, message2);
        Assertions.assertEquals(States.STATE3, target2);
    }

    @Test
    public void testChoice() {
        StateMachineBuilder<States, Events> builder = StateMachineBuilderFactory.create();
        builder.internalTransition()
                .within(STATE1)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkCondition1())
                .perform(doAction());
        builder.externalTransition()
                .from(STATE1)
                .to(STATE2)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkCondition2())
                .perform(doAction());
        StateMachine<States, Events> stateMachine = builder.build("ChoiceConditionMachine");
        Message<Events> message =
                MessageBuilder.withPayload(Events.EVENT1).setHeader("context",
                        new Context("1")).build();
        StateMachineTest.States target1 = stateMachine.fireEvent(STATE1, message);
        Assertions.assertEquals(STATE1, target1);

        Message<Events> message2 =
                MessageBuilder.withPayload(Events.EVENT1).setHeader("context",
                        new Context("2")).build();
        StateMachineTest.States target = stateMachine.fireEvent(STATE1, message2);
        Assertions.assertEquals(STATE2, target);
    }

    @Test
    public void testParallel() {
        StateMachineBuilder<States, Events> builder = new StateMachineBuilderImpl<>();
        builder.externalTransition().from(STATE1, STATE2).to(STATE3, STATE4).on(Events.EVENT3);
        StateMachine<States, Events> stateMachine = builder.build("ParallelStatesMachine");
        Message<Events> message =
                MessageBuilder.withPayload(EVENT3).build();
        List<States> states = stateMachine.fireParallelEvent(STATE1, message);
        System.out.println("states1");
    }


    @Test
    public void testFail() {
        StateMachineBuilder<States, Events> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(States.STATE1)
                .to(STATE2)
                .on(Events.EVENT1)
                .perform(doAction());
        StateMachine<States, Events> stateMachine = builder.build("FailedMachine");
        Message<Events> message2 =
                MessageBuilder.withPayload(Events.EVENT1).setHeader("context",
                        new Context("2")).build();
        Assertions.assertThrows(TransitionFailException.class,
                () -> stateMachine.fireEvent(STATE2, message2));
    }

    private Condition<States, Events> checkCondition1() {
        return context -> {
            Context context1 = context.getMessage().getHeaders().get("context",
                    Context.class);
            if (context1 != null) {
                return "1".equals(context1.getEntityId());
            }
            return false;
        };
    }

    private Condition<States, Events> checkCondition2() {
        return context -> {
            Context context1 = context.getMessage().getHeaders().get("context",
                    Context.class);
            if (context1 != null) {
                return "2".equals(context1.getEntityId());
            }
            return false;
        };
    }

    private Condition<States, Events> trueCondition() {
        return context -> true;
    }

    private Action<States, Events> doAction() {
        return (stateContext) -> {
            System.out.println(" from:" + stateContext.getSource() + " to:" + stateContext.getTarget());
//            throw new RuntimeException("ddd");
        };
    }

    private Action<States, Events> errorAction() {
        return (stateContext) -> {
            System.out.println(" from:" + stateContext.getSource() + " to:" + stateContext.getTarget());
        };
    }

    private Listener<States, Events> listener() {
        return (stateContext) -> {
            System.out.println(" from:" + stateContext.getSource() + " to:" + stateContext.getTarget());
            throw new RuntimeException("listener don't effect the state change");
        };
    }

}
