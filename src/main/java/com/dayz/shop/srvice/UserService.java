package com.dayz.shop.srvice;

import com.dayz.shop.jpa.entities.User;
import com.dayz.shop.repository.RoleRepository;
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

	@Value("${steam.api.url}")
	private String apiUrl;

	@Value("${steam.api.key}")
	private String apiKey;


	@Autowired
	public UserService(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	//parse json to get nickname and avatar url
	public JSONObject getSteamUserInfo(String steamId) throws JSONException {
		String userURI = getApiUrl() + "/ISteamUser/GetPlayerSummaries/v0002/?key=" + getApiKey() + "&steamids=" + steamId;
		return new JSONObject(new RestClient().resource(userURI).get(String.class));
	}

	//method for updating user;
	@SuppressWarnings("unchecked")
	public User createUser(String steamId) throws JSONException {
		User user = new User();
		Map<String, Object> userInfo = getSteamUserInfo(steamId);
		Map<String, String > stringObjectMap = ((List<Map<String, String >>) ((Map<String, Object>) userInfo.get("response")).get("players")).get(0);
		user.setSteamNickName(stringObjectMap.get("personaname"));
		user.setSteamAvatarUrl(stringObjectMap.get("avatar"));
		user.setSteamId(steamId);
		user.setRoles(roleRepository.findAllByName("USER"));
		return user;
	}

	public User findOne(Long id) {
		return userRepository.getById(id);
	}

}
