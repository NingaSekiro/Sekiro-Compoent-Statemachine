package com.alibaba.cola.statemachine.exception;


/**
 * transition fail exception
 *
 * @author NingaSekiro
 * @date 2024/06/14
 */
public class TransitionFailException extends RuntimeException {

    public TransitionFailException(String errMsg) {
        super(errMsg);
    }
}
