package com.reactive.programming.challenge.domain.exceptions;

import com.reactive.programming.challenge.domain.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class TechnicalException extends ProcessorException {

    public TechnicalException(Throwable cause, TechnicalMessage technicalMessage) {
        super(cause, technicalMessage);
    }

    public TechnicalException(TechnicalMessage technicalMessage) {
        super(technicalMessage.getMessage(), technicalMessage);
    }
}