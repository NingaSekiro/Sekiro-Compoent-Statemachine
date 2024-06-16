package com.github.ningasekiro.test;


public class Context {
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