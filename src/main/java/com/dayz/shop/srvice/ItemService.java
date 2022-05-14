package com.dayz.shop.srvice;

import com.dayz.shop.jpa.entities.Item;
import com.dayz.shop.jpa.entities.User;
import com.dayz.shop.repository.ItemRepository;
import com.dayz.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class ItemService {
	private ItemRepository itemRepository;
	private UserRepository userRepository;

	@Autowired
	public ItemService(ItemRepository itemRepository, UserRepository userRepository) {
		this.itemRepository = itemRepository;
		this.userRepository = userRepository;
	}


	public Optional<Item> createItem(Item item, Principal principal) {
		User user = userRepository.findBySteamId(principal.getName());
		if (user != null) {
			item.setDeletable(true);
			item = itemRepository.save(item);
		}
		return Optional.ofNullable(item); // TODO
	}
}
