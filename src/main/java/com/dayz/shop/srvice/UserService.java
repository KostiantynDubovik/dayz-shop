package com.dayz.shop.srvice;

import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.jpa.entities.User;
import com.dayz.shop.repository.RoleRepository;
import com.dayz.shop.repository.StoreConfigRepository;
import com.dayz.shop.repository.UserRepository;
import org.apache.wink.client.RestClient;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {
	public static final String STEAM_API_KEY = "steam.api.key";
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final StoreConfigRepository storeConfigRepository;

	@Value("${steam.api.url}")
	private String apiUrl;


	@Autowired
	public UserService(UserRepository userRepository, RoleRepository roleRepository, StoreConfigRepository storeConfigRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.storeConfigRepository = storeConfigRepository;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getApiKey(Store store) {
		return storeConfigRepository.findByKeyAndStore(STEAM_API_KEY, store).getValue();
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
		Map<String, String> stringObjectMap = ((List<Map<String, String>>) ((Map<String, Object>) userInfo.get("response")).get("players")).get(0);
		user.setSteamNickName(stringObjectMap.get("personaname"));
		user.setSteamAvatarUrl(stringObjectMap.get("avatar"));
		user.setActive(true);
	}

	public User findOne(Long id) {
		return userRepository.getById(id);
	}

}
