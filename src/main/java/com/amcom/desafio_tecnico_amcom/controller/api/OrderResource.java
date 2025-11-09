package com.amcom.desafio_tecnico_amcom.controller.api;

import com.amcom.desafio_tecnico_amcom.model.dto.out.FindOrderResponse;
import com.amcom.desafio_tecnico_amcom.model.dto.out.ListOrdersResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Orders", description = "Operations related to orders")
@RequestMapping("/v1/orders")
public interface OrderResource {

    @Operation(summary = "List all orders with pagination")
    @ApiResponse(responseCode = "200", description = "Orders listed successfully", content = @io.swagger.v3.oas.annotations.media.Content(
            mediaType = "application/json",
            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ListOrdersResponse.class)
    ))
    @GetMapping
    ResponseEntity<Page<ListOrdersResponse>> ListOrders(Pageable pageable);

    @Operation(summary = "Find an order by its external ID")
    @ApiResponse(responseCode = "200", description = "Order found successfully", content = @io.swagger.v3.oas.annotations.media.Content(
            mediaType = "application/json",
            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = FindOrderResponse.class)
    ))
    @GetMapping("/{externalId}")
    ResponseEntity<FindOrderResponse> findOrder(@PathVariable String externalId);
}
