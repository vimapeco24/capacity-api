package com.reactive.programming.challenge.infrastructure.adapter.util;

public class Constants {
    public static final String API_CAPACITY = "/capacities/api/capacity";
//    public static final String PATH_POST_CAPACITY = "/capacities/api/capacity";
    public static final String PATH_GET_CAPACITY = "/capacities/api/capacity/{id}";
    public static final String PATH_CAPACITIES = "/capacities/api/capacity";
    public static final String PATH_GET_TECHNOLOGIES_BY_ID_IN = "/capacities/api/capacitiesByIdIn";
    public static final String PATH_CAPACITIES_BY_BOOTCAMP_ID = "/capacities/api/capacitiesByBootcampId";
    public static final String CAPACITY_CREATED = "Capacity created successfully";
    public static final String STRING = "string";
    public static final String OK = "200";
    public static final String BAD_REQUEST_CODE = "400";
    public static final String BAD_REQUEST_DESCRIPTION = "Bad Request, please verify data";
    public static final String OBJECT = "object";
    public static final String REQUEST_BODY_DESCRIPTION = "Object representing the capacity to be created";
    public static final String CAPACITIES = "Capacities";
    public static final String OPERATION_SUMMARY = "Create a new capacity";
    public static final String METHOD_NAME = "createCapacity";
    public static final String INFO_TITLE = "Capacity API";
    public static final String INFO_VERSION = "1.0";
    public static final String INFO_DESCRIPTION = "API para la gestión de capacidades en programación reactiva";
    public static final String ERROR_RESPONSE = """
                {
                    "code": "400",
                    "message": "Bad Parameters, please verify data",
                    "identifier": "bf9e83ca-3fb6-4f54-9913-795ac0dd1241",
                    "date": "2025-04-29T17:01:30.150022600Z",
                    "errors": [
                        {
                            "code": "400",
                            "message": "Capacity name must be less than 50 characters"
                        }
                    ]
                }
            """;
}
