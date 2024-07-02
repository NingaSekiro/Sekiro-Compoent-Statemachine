package com.github.ningasekiro;


public class Context {
    String entityId = "123465";
    States states = null;

    public Context() {
    }

    public Context(String entityId) {
        this.entityId = entityId;
    }

    public Context(String entityId, States states) {
        this.entityId = entityId;
        this.states = states;
    }

    public String getEntityId() {
        return entityId;
    }
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
    public States getStates() {
        return states;
    }
    public void setStates(States states) {
        this.states = states;
    }
}