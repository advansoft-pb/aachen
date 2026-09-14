package pl.advansoft.aachen.webapp.clients.orders;

public record OrderConfirmationDTO(String orderNumber, OrderStatus status) {}
