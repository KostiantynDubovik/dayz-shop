package com.dayz.shop;

import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.service.SendToServerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class SendToServerServiceTest {

	SendToServerService sendToServerService;

	@Autowired
	public SendToServerServiceTest(SendToServerService sendToServerService) {
		this.sendToServerService = sendToServerService;
	}

	@Test
	public void sendOrder() throws Exception {

		Order order = new Order();

		Server server = new Server();
		server.setServerName("1PP");
		Store store = new Store();
		store.setId(-2L);
		order.setServer(server);
		order.setStore(store);
		List<OrderItem> orderItems = getOrderItems(order);
		order.setOrderItems(orderItems);
		sendToServerService.sendOrder(order);
	}

	private ArrayList<OrderItem> getOrderItems(Order order) {
		ArrayList<OrderItem> orderItems = new ArrayList<>();
		OrderItem orderItem = new OrderItem();
		orderItem.setOrder(order);
		orderItem.setMCode("HT-pwV2-fia1");
		orderItem.setStatus(OrderStatus.COMPLETE);
		orderItem.setServer(order.getServer());
		Item item = new Item();
		item.setStore(order.getStore());
		item.setName("jopa");
		item.setInGameId("jopa_itm");
		item.setCount(1);
		item.setItemType(ItemType.ITEM);
		orderItem.setItem(item);
		orderItems.add(orderItem);
		return orderItems;
	}
}
