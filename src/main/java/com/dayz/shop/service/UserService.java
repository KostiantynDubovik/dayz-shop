package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.jpa.entities.User;
import com.dayz.shop.repository.LanguageRepository;
import com.dayz.shop.repository.RoleRepository;
import com.dayz.shop.repository.UserRepository;
import com.dayz.shop.utils.Utils;
import org.apache.wink.client.RestClient;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {
	public static final String STEAM_API_KEY = "steam.api.key";
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	@Value("${steam.api.url}")
	private String apiUrl;
	private final LanguageRepository languageRepository;


	@Autowired
	public UserService(UserRepository userRepository, RoleRepository roleRepository,
	                   LanguageRepository languageRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.languageRepository = languageRepository;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getApiKey(Store store) {
		return store.getString(STEAM_API_KEY);
	}

	//parse json to get nickname and avatar url
	public JSONObject getSteamUserInfo(String steamId, Store store) {
		String userURI = getApiUrl() + "/ISteamUser/GetPlayerSummaries/v0002/?key=" + getApiKey(store) + "&steamids=" + steamId;
		try {
			return new JSONObject(new RestClient().resource(userURI).get(String.class));
		} catch (JSONException e) {
			return new JSONObject();
		}
	}

	public User createUser(String steamId, Store store) {
		User user = new User();
		user.setSteamId(steamId);
		user.setLanguage(languageRepository.getById(store.getLong("language.default")));
		user.setRoles(roleRepository.findAllByName("USER"));
		user.setStore(store);
		return updateUser(user, store);
	}

	public User updateUser(User user, Store store) {
		fillUserSteamData(user, store);
		return userRepository.save(user);
	}

	@SuppressWarnings("unchecked")
	private void fillUserSteamData(User user, Store store) {
		Map<String, Object> userInfo = getSteamUserInfo(user.getSteamId(), store);
		Map<String, String> stringObjectMap = ((List<Map<String, String>>) ((Map<String, Object>) userInfo.get("response")).get("players")).stream().findFirst().get();
		user.setSteamNickName(stringObjectMap.get("personaname"));
		user.setSteamAvatarUrl(stringObjectMap.get("avatar"));
		user.setActive(true);
	}

	public User findOne(Long id) {
		return userRepository.getById(id);
	}

	public Map<String, String > findFriends(Long userFromId, Long storeId, int limit) {
		List<List<String>> resultSet = userRepository.findFriends(userFromId, storeId, limit);
		return resultSet.stream()
				.map(input -> new AbstractMap.SimpleEntry<>(input.get(0), input.get(1)))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

}
