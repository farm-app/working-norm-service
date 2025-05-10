package ru.rtln.workingnormservice.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public abstract class BaseException extends RuntimeException {

    private Map<String, Object> args;

    private String messageParam;

    protected BaseException(String msg, Throwable ex) {
        super(msg, ex);
    }

    public BaseException setArgs(Map<String, Object> args) {
        this.args = args;
        return this;
    }

    public BaseException setMessageParam(String messageParam) {
        this.messageParam = messageParam;
        return this;
    }

    public String getArgsAsString() {
        if (this.args == null) {
            return "";
        }
        return args.toString();
    }
}
