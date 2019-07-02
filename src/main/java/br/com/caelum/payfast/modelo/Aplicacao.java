package br.com.caelum.payfast.modelo;

import javax.validation.constraints.NotEmpty;

public class Aplicacao {

	@NotEmpty
	private String nome;
	@NotEmpty
	private String clientId;
	@NotEmpty
	private String clientSecret;
	@NotEmpty
	private String appcallbackUrl;
	
	public Aplicacao() { 
	}
	
	public Aplicacao(String clientId, String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
		
	public String getAppcallbackUrl() {
		return appcallbackUrl;
	}

	public void setAppcallbackUrl(String appcallbackUrl) {
		this.appcallbackUrl = appcallbackUrl;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aplicacao other = (Aplicacao) obj;
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		return true;
	}
	
	
	
}
