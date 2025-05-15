package com.reactive.programming.challenge.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TechnicalMessage {

    INTERNAL_ERROR("500", "Something went wrong, please try again", ""),
    INTERNAL_ERROR_IN_ADAPTERS("PRC501", "Something went wrong in adapters, please try again", ""),
    INVALID_REQUEST("400", "Bad Request, please verify data", ""),
    INVALID_PARAMETERS(INVALID_REQUEST.getCode(), "Bad Parameters, please verify data", ""),
    INVALID_MESSAGE_ID("404", "Invalid Message ID, please verify", "messageId"),
    UNSUPPORTED_OPERATION("501", "Method not supported, please try again", ""),
    CAPACITY_CREATED("201", "Capacity created successfully", ""),
    ADAPTER_RESPONSE_NOT_FOUND("404-0", "invalid email, please verify", ""),
    CAPACITY_ALREADY_EXISTS("400", "The capacity with the name provided already exists.", ""),
    MIN_NUMBER_TECHNOLOGIES("400", "The capacity must have at least three technologies.", ""),
    MAX_NUMBER_TECHNOLOGIES("400", "The capacity must have a maximum of 20 technologies.", ""),
    TECHNOLOGY_NOT_FOUND("404", "The technology with that id does not exist.", ""),
    DUPLICATED_TECHNOLOGIES("400", "Technologies duplicated was found.", ""),
    NOT_ONLY_NUMBERS("400", "Capacity name cannot contain only numbers.", ""),
    DESCRIPTION_CHARACTER_LIMIT("400", "Capacity description must be less than 200 characters.", ""),
    NAME_CHARACTER_LIMIT("400", "Capacity name must be less than 50 characters.", ""),
    INVALID_CAPACITY_DATA("400", "Invalid capacity data provided.", ""),

    // Mensajes específicos para operaciones
    CAPACITY_CREATION_FAILED("500", "Failed to create capacity.", ""),
    CAPACITY_TECHNOLOGY_CREATION_FAILED("500", "Failed to create capacity technology relations.", ""),
    UNKNOWN_ERROR("500", "An unknown error occurred.", ""),
    VALIDATION_FAILED("400", "Capacity validation failed. Please check the provided data.", "");

    private final String code;
    private final String message;
    private final String param;
}