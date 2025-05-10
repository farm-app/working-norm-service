package ru.rtln.workingnormservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class WorkingNormException extends BaseException {

    protected final Code code;

    protected WorkingNormException(String msg, Throwable ex, Code code) {
        super(msg, ex);
        this.code = code;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Code {

        INTERNAL_ERROR("error-message.internal-error"),
        WORKING_NORM_NOT_FOUND("error-message.working-norm.not-found"),
        INVALID_DEADLINE("error-message.working-norm.invalid-deadline"),
        WORKING_NORM_ALREADY_EXISTS("error-message.working-norm.already-exists"),
        USER_NO_ACTIVE("error-message.working-norm.user-no-active"),
        ;

        /**
         * The key to retrieve the user  message from the properties file.
         */
        private final String userMessageProperty;

        public WorkingNormException getWith(String msg) {
            return new WorkingNormException(msg, null, this);
        }

        public WorkingNormException getWith(String msg, Throwable ex) {
            return new WorkingNormException(msg, ex, this);
        }
    }
}
