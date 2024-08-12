package mate.academy.store.mapper;

import mate.academy.store.config.MapperConfig;
import mate.academy.store.dto.order.ResponseOrderDto;
import mate.academy.store.dto.order.UpdateOrderRequestDto;
import mate.academy.store.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    Order toModel(ResponseOrderDto responseOrderDto);

    @Mapping(target = "userId", source = "user.id")
    ResponseOrderDto toDto(Order order);

    void updateOrderFromDto(UpdateOrderRequestDto requestDto, @MappingTarget Order order);
}
