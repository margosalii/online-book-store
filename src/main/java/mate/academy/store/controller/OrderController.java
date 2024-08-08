package mate.academy.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.store.dto.order.RequestOrderDto;
import mate.academy.store.dto.order.ResponseOrderDto;
import mate.academy.store.dto.order.UpdateOrderRequestDto;
import mate.academy.store.dto.orderitem.OrderItemDto;
import mate.academy.store.model.User;
import mate.academy.store.service.order.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Place new order", description = "Place order using shipping address")
    public ResponseOrderDto placeNewOrder(@RequestBody @Valid RequestOrderDto orderDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = ((User) authentication.getPrincipal()).getId();
        return orderService.placeNewOrder(id, orderDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update order status", description = "Update orders status by its ID")
    public ResponseOrderDto updateOrderStatus(@PathVariable @Positive Long id,
                                              @RequestBody @Valid
                                              UpdateOrderRequestDto requestDto) {
        return orderService.updateOrderStatus(id, requestDto);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Get all orders", description = "Get all users orders")
    public Set<ResponseOrderDto> getAllOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = ((User) authentication.getPrincipal()).getId();
        return orderService.getAllUsersOrders(id);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get order item", description = "Get order item by its ID and orders ID")
    public OrderItemDto getItemByOrderAndItemId(@PathVariable @Positive Long orderId,
                                       @PathVariable @Positive Long itemId) {
        return orderService.getItemByOrderIdAndItemId(orderId, itemId);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get order items", description = "Get order items by orders ID")
    public Set<OrderItemDto> getAllItemsByOrderId(@PathVariable @Positive Long orderId) {
        return orderService.getItemsByOrderId(orderId);
    }
}
