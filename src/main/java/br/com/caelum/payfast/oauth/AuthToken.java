package br.com.caelum.payfast.oauth;

public class AuthToken {

	private String code;
	
	@SuppressWarnings("unused")
	private AuthToken() { 
	}
	
	public AuthToken(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
