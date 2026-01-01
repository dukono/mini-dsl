package dukono.minidsl.example.generated;

import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.example.dto.OrderDto;

@DslDomain(name = "Order", dtoClass = OrderDto.class, fieldsConstants = OrderFieldConstants.class, operationsEnum = OrderOperationsEnum.class)
public class OrderDomainDefinitionConcise {

}
