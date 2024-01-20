package ua.com.oak.shop.listeners;

import ua.com.oak.shop.jpa.entities.Order;
import ua.com.oak.shop.utils.OrderUtils;
import ua.com.oak.shop.utils.Utils;
import org.aspectj.lang.annotation.AfterReturning;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Map;

@Component
public class OrderListener {
	@PrePersist
	@PreUpdate
	protected void recalculate(Order orderItem) {
		OrderUtils.recalculateOrder(orderItem);
	}

	@AfterReturning(pointcut="execution(* com.dayz.shop.controllers.OrderController.*) && @annotation(ProcessMessage)", returning="returnValue")
	public void localizeMessage(Object returnValue) {
		if (returnValue != null) {
			Order order = (Order) returnValue;
			Map<String, String> properties = order.getProperties();
			if (properties.containsKey("message")) {
				String message = Utils.getMessage(properties.get("message"), order.getStore());
				properties.put("message", message);
			}
		}
	}
}
