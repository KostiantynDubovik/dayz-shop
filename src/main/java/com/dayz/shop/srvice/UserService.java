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
		return storeConfigRepository.getValueByKeyAndStore("steam.api.key", store);
	}

	//parse json to get nickname and avatar url
	public JSONObject getSteamUserInfo(String steamId, Store store) throws JSONException {
		String userURI = getApiUrl() + "/ISteamUser/GetPlayerSummaries/v0002/?key=" + getApiKey(store) + "&steamids=" + steamId;
		return new JSONObject(new RestClient().resource(userURI).get(String.class));
	}

	//method for updating user;
	@SuppressWarnings("unchecked")
	public User createUser(String steamId, Store store) throws JSONException {
		User user = new User();
		Map<String, Object> userInfo = getSteamUserInfo(steamId, store);
		Map<String, String > stringObjectMap = ((List<Map<String, String >>) ((Map<String, Object>) userInfo.get("response")).get("players")).get(0);
		user.setSteamNickName(stringObjectMap.get("personaname"));
		user.setSteamAvatarUrl(stringObjectMap.get("avatar"));
		user.setSteamId(steamId);
		user.setRoles(roleRepository.findAllByName("USER"));
		user.setStore(store);
		user.setActive(true);
		return user;
	}

	public User findOne(Long id) {
		return userRepository.getById(id);
	}

}
