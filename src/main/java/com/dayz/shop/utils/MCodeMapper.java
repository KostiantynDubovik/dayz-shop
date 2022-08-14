package com.dayz.shop.utils;

import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.json.MCodeArray;
import com.dayz.shop.json.MItemsArray;
import com.dayz.shop.json.MVehicles;
import com.dayz.shop.json.Root;
import com.dayz.shop.repository.OrderItemRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MCodeMapper {

	final OrderItemRepository orderItemRepository;

	@Autowired
	public MCodeMapper(OrderItemRepository orderItemRepository) {
		this.orderItemRepository = orderItemRepository;
	}

	public Root mapOrderToRoot(Order order) {
		Root root = new Root();
		root.setM_CodeArray(mapMCodeArray(order));
		return root;
	}

	private List<MCodeArray> mapMCodeArray(Order order) {
		List<OrderItem> orderItems = order.getOrderItems();
		List<MCodeArray> mCodeArrays = new ArrayList<>();
		for (OrderItem orderItem : orderItems) {
			MCodeArray mCodeArray = new MCodeArray();
			mCodeArray.setM_code(orderItem.getMCode());
			mCodeArray.setM_name(orderItem.getItem().getName());
			fillSubItems(orderItem, mCodeArray);
			mCodeArrays.add(mCodeArray);
		}
		return mCodeArrays;
	}

	private void fillSubItems(OrderItem orderItem, MCodeArray mCodeArray) {
		ItemType itemType = orderItem.getItem().getItemType();
		mCodeArray.setM_type(itemType.toString().toLowerCase());
		switch (itemType) {
			case ITEM:
				fillItems(orderItem, mCodeArray);
				break;
			case VEHICLE:
				fillVehicle(orderItem, mCodeArray);
				break;
		}
	}

	private void fillItems(OrderItem orderItem, MCodeArray mCodeArray) {
		MVehicles m_vehicles = getEmptyMVehicles();
		mCodeArray.setM_vehicles(m_vehicles);
		List<MItemsArray> mItemsArrays = new ArrayList<>();
		List<Item> subItems = orderItem.getItem().getSubItems();
		if (CollectionUtils.isEmpty(subItems)) {
			MItemsArray mItemsArray = new MItemsArray();
			mItemsArray.setM_item(orderItem.getItem().getInGameId());
			mItemsArray.setM_count(orderItem.getItem().getCount());
			mItemsArrays.add(mItemsArray);
		} else {
			for (Item subItem : subItems) {
				MItemsArray mItemsArray = new MItemsArray();
				mItemsArray.setM_item(subItem.getInGameId());
				mItemsArray.setM_count(subItem.getCount());
				mItemsArrays.add(mItemsArray);
			}
		}
		mCodeArray.setM_itemsArray(mItemsArrays);
	}

	private MVehicles getEmptyMVehicles() {
		MVehicles m_vehicles = new MVehicles();
		m_vehicles.setM_item(StringUtils.EMPTY);
		m_vehicles.setM_attachments(new ArrayList<>());
		return m_vehicles;
	}

	private void fillVehicle(OrderItem orderItem, MCodeArray mCodeArray) {
		mCodeArray.setM_itemsArray(new ArrayList<>());
		MVehicles m_vehicles = new MVehicles();
		m_vehicles.setM_item(orderItem.getItem().getName());
		List<String> attachments = new ArrayList<>();
		for (Item subItem : orderItem.getItem().getSubItems()) {
			attachments.add(subItem.getName());
		}
		m_vehicles.setM_attachments(attachments);
		mCodeArray.setM_vehicles(m_vehicles);
	}

	public List<OrderItem> mapRootToOrderItems(Root root) {
		List<String> mCodes =  root.getM_CodeArray().stream().map(MCodeArray::getM_code).collect(Collectors.toList());
		List<OrderItem> orderItems = orderItemRepository.findAllByUserSteamIdAndReceivedAndStatus(root.getUserId(), false, OrderStatus.COMPLETE);
		orderItems.removeAll(orderItemRepository.findAllByMCode(mCodes));
		orderItems.forEach(asd -> asd.setReceived(true));
		orderItemRepository.saveAll(orderItems);
		return orderItems;
	}
}
