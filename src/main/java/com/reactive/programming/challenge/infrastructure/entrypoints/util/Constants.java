package com.reactive.programming.challenge.infrastructure.entrypoints.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String X_MESSAGE_ID = "x-message-id";
    public static final String CAPACITY_ERROR = "Error on Capacity - [ERROR]";
    public static final String UNEXPECTED_ERROR = "Unexpected error occurred while creating capacity for messageId";
    public static final String CAPACITY_CREATED = "Capacity created successfully with messageId";
}
