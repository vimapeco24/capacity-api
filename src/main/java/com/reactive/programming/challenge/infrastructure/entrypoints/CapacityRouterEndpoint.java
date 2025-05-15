package com.reactive.programming.challenge.infrastructure.entrypoints;

import com.reactive.programming.challenge.infrastructure.entrypoints.handler.ICapacityHandler;
import com.reactive.programming.challenge.domain.model.Capacity;
import com.reactive.programming.challenge.infrastructure.adapter.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class CapacityRouterEndpoint {
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = Constants.API_CAPACITY,
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.POST,
                    beanClass = ICapacityHandler.class,
                    beanMethod = Constants.METHOD_NAME,
                    operation = @Operation(
                            operationId = Constants.METHOD_NAME,
                            summary = Constants.OPERATION_SUMMARY,
                            tags = { Constants.CAPACITIES},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = Constants.REQUEST_BODY_DESCRIPTION,
                                    required = true,
                                    content = @io.swagger.v3.oas.annotations.media.Content(
                                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Capacity.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = Constants.OK,
                                            description = Constants.CAPACITY_CREATED,
                                            content = @Content(
                                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                                    schema = @Schema(type = Constants.STRING, example = Constants.CAPACITY_CREATED)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = Constants.BAD_REQUEST_CODE,
                                            description = Constants.BAD_REQUEST_DESCRIPTION,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(type = Constants.OBJECT, example = Constants.ERROR_RESPONSE)
                                            )
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> capacityRouter(ICapacityHandler capacityHandler) {
        return RouterFunctions
                .route(POST(Constants.PATH_CAPACITIES), capacityHandler::createCapacity)
                .andRoute(GET(Constants.PATH_CAPACITIES), capacityHandler::getCapacities)
                .andRoute(GET(Constants.PATH_GET_TECHNOLOGIES_BY_ID_IN), capacityHandler::getCapacitiesByIdIn)
//                .andRoute(GET(Constants.PATH_CAPACITIES_BY_BOOTCAMP_ID), capacityHandler::getCapacitiesByBootcampId)
                .andRoute(POST(Constants.PATH_CAPACITIES_BY_BOOTCAMP_ID), capacityHandler::getCapacitiesWithTechnologiesByBootcampId)
                .andRoute(GET(Constants.PATH_GET_CAPACITY), capacityHandler::getCapacityById)
                .andRoute(DELETE(Constants.PATH_GET_CAPACITY), capacityHandler::deleteCapacity);
    }
}
