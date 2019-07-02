package br.com.caelum.payfast.oauth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class TokenDao {

	private static List<AuthToken> authTokens = new ArrayList<>(101);
	private static List<AccessToken> accessTokens = new ArrayList<>(101);

	void clearAll() {
		authTokens.clear();
		accessTokens.clear();
	}
	
	public void addAuthToken(String authToken) {
		
		if(authTokens.size() > 100) {
			this.clearAll();
		}
		
		AuthToken token = new AuthToken(authToken);
		authTokens.add(token);
	}
	
	public void addAccessToken(String accessToken) {
		
		if(accessTokens.size() > 100) {
			this.clearAll();
		}
		
		AccessToken token = new AccessToken(accessToken);
		accessTokens.add(token);
	}

	public boolean existsAuthToken(String code) {

		AuthToken token = authTokens.stream().filter(t -> t.getCode().equals(code)).findFirst().orElse(null);

		if (token == null) {
			throw new RuntimeException("AuthToken nao existe");
		}

		return true;
	}

	public boolean existsAccessToken(String code) {

		AccessToken token = accessTokens.stream().filter(t -> t.getCode().equals(code)).findFirst().orElse(null);

		if (token == null) {
			throw new RuntimeException("AccessToken nao existe");
		}

		return true;
	}

}
