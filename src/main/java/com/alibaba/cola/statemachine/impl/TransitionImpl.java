package com.alibaba.cola.statemachine.impl;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.State;
import com.alibaba.cola.statemachine.Transition;
import org.springframework.messaging.Message;

/**
 * TransitionImpl。
 * <p>
 * This should be designed to be immutable, so that there is no thread-safe risk
 *
 * @author Frank Zhang
 * @date 2020-02-07 10:32 PM
 */
public class TransitionImpl<S, E> implements Transition<S, E> {

    private State<S, E> source;

    private State<S, E> target;

    private E event;

    private Condition condition;

    private Action<S, E> action;

    private TransitionType type = TransitionType.EXTERNAL;

    @Override
    public State<S, E> getSource() {
        return source;
    }

    @Override
    public void setSource(State<S, E> state) {
        this.source = state;
    }

    @Override
    public E getEvent() {
        return this.event;
    }

    @Override
    public void setEvent(E event) {
        this.event = event;
    }

    @Override
    public void setType(TransitionType type) {
        this.type = type;
    }

    @Override
    public State<S, E> getTarget() {
        return this.target;
    }

    @Override
    public void setTarget(State<S, E> target) {
        this.target = target;
    }

    @Override
    public Condition getCondition() {
        return this.condition;
    }

    @Override
    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public Action<S, E> getAction() {
        return this.action;
    }

    @Override
    public void setAction(Action<S, E> action) {
        this.action = action;
    }

    @Override
    public State<S, E> transit(Message<E> ctx, boolean checkCondition) {
        Debugger.debug("Do transition: " + this);
        this.verify();
        StateContextImpl<S, E> stateContext = new StateContextImpl<S, E>(ctx, this, source, target,
                null);
        if (!checkCondition || condition == null || condition.isSatisfied(stateContext)) {
            if (action != null) {
                action.execute(source.getId(), target.getId(), stateContext);
            }
            return target;
        }

        Debugger.debug("Condition is not satisfied, stay at the " + source + " state ");
        return source;
    }

    @Override
    public final String toString() {
        return source + "-[" + event.toString() + ", " + type + "]->" + target;
    }

    @Override
    public boolean equals(Object anObject) {
        if (anObject instanceof Transition) {
            Transition other = (Transition) anObject;
            if (this.event.equals(other.getEvent())
                    && this.source.equals(other.getSource())
                    && this.target.equals(other.getTarget())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void verify() {
        if (type == TransitionType.INTERNAL && source != target) {
            throw new StateMachineException(String.format("Internal transition source state '%s' " +
                    "and target state '%s' must be same.", source, target));
        }
    }
}
