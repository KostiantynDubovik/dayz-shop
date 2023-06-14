package com.dayz.shop.utils;

import com.dayz.shop.jpa.entities.ItemType;
import com.dayz.shop.jpa.entities.OrderItem;
import com.dayz.shop.jpa.entities.OrderStatus;
import com.dayz.shop.jpa.entities.SubItem;
import com.dayz.shop.json.MCodeArray;
import com.dayz.shop.json.MItemsArray;
import com.dayz.shop.json.MVehicles;
import com.dayz.shop.json.Root;
import com.dayz.shop.repository.OrderItemRepository;
import com.dayz.shop.repository.UserRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.dayz.shop.service.SendToServerService.PIPE;

@Service
public class MCodeMapper {

	final OrderItemRepository orderItemRepository;
	final UserRepository userRepository;

	@Autowired
	public MCodeMapper(OrderItemRepository orderItemRepository, UserRepository userRepository) {
		this.orderItemRepository = orderItemRepository;
		this.userRepository = userRepository;
	}

	public Root mapOrderToRoot(List<OrderItem> orderItems) {
		Root root = new Root();
		root.setM_CodeArray(mapMCodeArray(orderItems));
		return root;
	}

	private List<MCodeArray> mapMCodeArray(List<OrderItem> orderItems) {
		List<MCodeArray> mCodeArrays = new ArrayList<>();
		for (OrderItem orderItem : orderItems) {
			for (int i = 0; i < orderItem.getCount(); i++) {
				MCodeArray mCodeArray = new MCodeArray();
				mCodeArray.setM_code(StringUtils.joinWith(PIPE, orderItem.getId().toString(), i + 1));
				mCodeArray.setM_name(orderItem.getItem().getDescription().getName());
				fillSubItems(orderItem, mCodeArray);
				mCodeArrays.add(mCodeArray);
			}
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
		List<SubItem> subItems = orderItem.getItem().getSubItems();
		if (CollectionUtils.isEmpty(subItems)) {
			MItemsArray mItemsArray = new MItemsArray();
			mItemsArray.setM_item(orderItem.getItem().getInGameId());
			mItemsArray.setM_count(orderItem.getItem().getCount());
			mItemsArrays.add(mItemsArray);
		} else {
			for (SubItem subItem : subItems) {
				MItemsArray mItemsArray = new MItemsArray();
				mItemsArray.setM_item(subItem.getSubItem().getInGameId());
				mItemsArray.setM_count(subItem.getQuantity() * subItem.getSubItem().getCount());
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
		m_vehicles.setM_item(orderItem.getItem().getInGameId());
		List<String> attachments = new ArrayList<>();
		for (SubItem subItem : orderItem.getItem().getSubItems()) {
			for (int i = 0; i < subItem.getQuantity(); i++) {
				attachments.add(subItem.getSubItem().getInGameId());
			}
		}
		m_vehicles.setM_attachments(attachments);
		mCodeArray.setM_vehicles(m_vehicles);
	}

	public List<OrderItem> mapRootToOrderItems(Root root) {
		List<Long> mCodes = root.getM_CodeArray().stream().map(input -> Long.parseLong(input.getM_code())).collect(Collectors.toList());
		List<OrderItem> orderItems = orderItemRepository.findAllByUserAndReceivedAndStatus(userRepository.getBySteamId(root.getUserId()), false, OrderStatus.COMPLETE);
		orderItems.removeAll(orderItemRepository.findAllById(mCodes));
		orderItems.forEach(orderItem -> orderItem.setReceived(true));
		orderItemRepository.saveAll(orderItems);
		return orderItems;
	}
}
