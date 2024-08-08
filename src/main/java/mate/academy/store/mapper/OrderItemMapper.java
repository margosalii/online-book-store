package mate.academy.store.mapper;

import mate.academy.store.config.MapperConfig;
import mate.academy.store.dto.orderitem.OrderItemDto;
import mate.academy.store.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDto(OrderItem orderItem);
}
