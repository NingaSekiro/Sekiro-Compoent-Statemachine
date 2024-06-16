package com.github.ningasekiro.test;

import com.github.ningasekiro.Condition;
import com.github.ningasekiro.StateMachine;
import com.github.ningasekiro.builder.StateMachineBuilder;
import com.github.ningasekiro.builder.StateMachineBuilderFactory;
import com.github.ningasekiro.builder.StateMachineBuilderImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;


import java.util.Arrays;
import java.util.List;

import static com.github.ningasekiro.test.CommonCompoent.*;
import static com.github.ningasekiro.test.Events.*;
import static com.github.ningasekiro.test.States.*;


public class StateMachineTest {
    private static final Log LOGGER = LogFactory.getLog(StateMachineTest.class);


    /**
     * STATE1->STATE2->STATE3
     *
     * @author NingaSekiro
     * @date 2024/06/16
     */
    @Test
    public void testExternalNormal() {
        StateMachineBuilder<States, Events> stateMachineBuilder = getStateMachineBuilder();
        StateMachine<States, Events> stateMachine = stateMachineBuilder.build("TestStateMachine");
        Message<Events> message =
                MessageBuilder.withPayload(EVENT1).setHeader("context",
                        new Context()).build();
        stateMachine.fireEvent(STATE1, message);
        Message<Events> message2 =
                MessageBuilder.withPayload(EVENT2).copyHeaders(message.getHeaders()).build();
        States target2 = stateMachine.fireEvent(STATE2, message2);
        Assertions.assertEquals(STATE3, target2);
    }

    /**
     * no STATE1->STATE2,but STATE2->STATE3
     *
     * @author NingaSekiro
     * @date 2024/06/16
     */
    @Test
    public void testMiddleStart() {
        StateMachineBuilder<States, Events> stateMachineBuilder = getConditioonalStateMachineBuilder();
        StateMachine<States, Events> stateMachine = stateMachineBuilder.build(
                "TestStateMiddleMachine");
        Context context = new Context("2", STATE2);
        Message<Events> message =
                MessageBuilder.withPayload(EVENT1).setHeader("context",
                        context).build();
        stateMachine.fireEvent(STATE1, message);
        Message<Events> message2 =
                MessageBuilder.withPayload(EVENT2).copyHeaders(message.getHeaders()).build();
        States target2 = stateMachine.fireEvent(STATE2, message2);
        Assertions.assertEquals(STATE3, target2);
    }


    /**
     * 工作流，状态自动转换 STATE1->STATE2->STATE3
     *
     * @author NingaSekiro
     * @date 2024/06/16
     */
    @Test
    public void testExternalTaskFlow() {
        StateMachineBuilder<States, Events> stateMachineBuilder = getStateMachineBuilder();
        StateMachine<States, Events> stateMachine = stateMachineBuilder.build("TaskFlowMachine");
        Message<Events> message =
                MessageBuilder.withPayload(EVENT1).setHeader("context",
                        new Context()).build();
        States states = stateMachine.fireTask(STATE1, message, Arrays.asList(EVENT1, EVENT2));
        Assertions.assertEquals(STATE3, states);
    }

    /**
     * 工作流，从中间状态自动转换 STATE2->STATE3->STATE4
     *
     * @author NingaSekiro
     * @date 2024/06/16
     */
    @Test
    public void testMiddleStartExternalTaskFlow() {
        StateMachineBuilder<States, Events> stateMachineBuilder = getConditioonalStateMachineBuilder();
        stateMachineBuilder.externalTransition().from(STATE3).to(STATE4).on(EVENT3);
        StateMachine<States, Events> stateMachine = stateMachineBuilder.build(
                "TaskFlowMiddleMachine");
        Context context = new Context("2", STATE2);
        Message<Events> message =
                MessageBuilder.withPayload(EVENT1).setHeader("context",
                        context).build();
        States states = stateMachine.fireTask(context.getStates(), message, Arrays.asList(EVENT1,
                EVENT2,EVENT3));
        Assertions.assertEquals(STATE4, states);
    }


    /**
     * 根据condition选择路线
     *
     * @author NingaSekiro
     * @date 2024/06/16
     */
    @Test
    public void testChoice() {
        StateMachineBuilder<States, Events> stateMachineBuilder = StateMachineBuilderFactory.create();
        stateMachineBuilder.internalTransition()
                .within(STATE1)
                .on(EVENT1)
                .when(checkCondition1())
                .perform(doAction());
        stateMachineBuilder.externalTransition()
                .from(STATE1)
                .to(STATE2)
                .on(EVENT1)
                .when(checkCondition2())
                .perform(doAction());
        StateMachine<States, Events> stateMachine = stateMachineBuilder.build("ChoiceConditionMachine");
        Message<Events> message =
                MessageBuilder.withPayload(EVENT1).setHeader("context",
                        new Context("1")).build();
        States target1 = stateMachine.fireEvent(STATE1, message);
        Assertions.assertEquals(STATE1, target1);
        Message<Events> message2 =
                MessageBuilder.withPayload(EVENT1).setHeader("context",
                        new Context("2")).build();
        States target = stateMachine.fireEvent(STATE1, message2);
        Assertions.assertEquals(STATE2, target);
    }

    /**
     * 单->多状态
     *
     * @author NingaSekiro
     * @date 2024/06/16
     */
    @Test
    public void testParallel() {
        StateMachineBuilder<States, Events> builder = new StateMachineBuilderImpl<>();
        builder.externalTransition().from(STATE1, STATE2).to(STATE3, STATE4).on(EVENT3);
        StateMachine<States, Events> stateMachine = builder.build("ParallelStatesMachine");
        Message<Events> message =
                MessageBuilder.withPayload(EVENT3).build();
        List<States> states = stateMachine.fireParallelEvent(STATE1, message);
        Assertions.assertArrayEquals(new States[]{STATE3, STATE4}, states.toArray());
    }


    private StateMachineBuilder<States, Events> getStateMachineBuilder() {
        StateMachineBuilder<States, Events> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(STATE1)
                .to(STATE2)
                .on(EVENT1)
                .perform(doAction(), errorAction())
                .listen(listener());
        builder.externalTransition()
                .from(STATE2)
                .to(STATE3)
                .on(EVENT2)
                .perform(doAction(), errorAction());
        return builder;
    }

    private StateMachineBuilder<States, Events> getConditioonalStateMachineBuilder() {
        StateMachineBuilder<States, Events> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(STATE1)
                .to(STATE2)
                .on(EVENT1)
                .when(checkCondition1())
                .perform(doAction(), errorAction())
                .listen(listener());
        builder.externalTransition()
                .from(STATE2)
                .to(STATE3)
                .on(EVENT2)
                .when(checkCondition2())
                .perform(doAction(), errorAction());
        return builder;
    }

    private static Condition<States, Events> checkCondition1() {
        return context -> {
            Context context1 = context.getMessage().getHeaders().get("context",
                    Context.class);
            if (context1 != null) {
                return "1".equals(context1.getEntityId());
            }
            return false;
        };
    }

    private static Condition<States, Events> checkCondition2() {
        return context -> {
            Context context1 = context.getMessage().getHeaders().get("context",
                    Context.class);
            if (context1 != null) {
                return "2".equals(context1.getEntityId());
            }
            return false;
        };
    }
}
