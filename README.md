## 作用
简单、轻量、性能极高的状态机DSL实现

特性：

1. 无状态statemachine，支持多线程高并发
2. dsl构建StateMachineBuilder，方便快速
3. 支持一对多，多对一，多对多的状态转移（不过尽量少用）
4. 原生支持spring message消息的传递
5. 支持plantUml的生成

## QuickStart:

```
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
                
        StateMachine<States, Events> stateMachine = builder.build(MACHINE_ID);
        Message<Events> message =
                MessageBuilder.withPayload(Events.EVENT1).setHeader("context",
                        new Context()).build();
        stateMachine.fireEvent(STATE1, message);
    }
```



## 原理

参考状态机模型：

spring-statemachine

cola-statemachine

squirrel-statemachine

