package br.com.caelum.payfast.oauth;

public class AccessToken {
	
	private String code;

	@SuppressWarnings("unused")
	private AccessToken() {
	}
	
	public AccessToken(String accessToken) {
		this.code = accessToken;
	}

	public String getCode() {
		return code;
	}

}
