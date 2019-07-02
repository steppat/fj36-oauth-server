package br.com.caelum.payfast.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.caelum.payfast.modelo.Aplicacao;

@Component
public class AplicacaoDao {

	private static List<Aplicacao> aplicacoes = new ArrayList<>();

	//
	// INSERT INTO Usuario(email, senha) VALUES ('usuario@email.com', '1234');
	// INSERT INTO Aplicacao(nome, clientId, clientSecret) VALUES ('fj-36', '12345',
	// '123');
	public AplicacaoDao() {
		createAplicacao();
	}

	private void createAplicacao() {
		Aplicacao ap = new Aplicacao();
		ap.setClientId("12345");
		ap.setClientSecret("123");
		ap.setNome("fj36");
		ap.setAppcallbackUrl("http://localhost:8080/fj36-livraria/redirect");
		aplicacoes.add(ap);
	}

	public void inserir(Aplicacao aplicacao) { 
		
		if(aplicacoes.size() > 100) {
			aplicacoes.clear();
			this.createAplicacao();
		}
		aplicacoes.add(aplicacao);
	}

	public List<Aplicacao> getAplicacoes() {
		return aplicacoes;
	}

	public boolean existeClientId(Aplicacao aplicacao) {
		return aplicacoes.contains(aplicacao);
	}

	public boolean existeAplicacao(Aplicacao aplicacao) {
		
		Aplicacao found = aplicacoes.
					stream().
					filter((elemento) -> {
						return elemento.getClientId().equals(aplicacao.getClientId())
								&& elemento.getClientSecret().equals(aplicacao.getClientSecret());
						}
					).
					findFirst().
					orElse(null);
		
		return found != null;
	}

}
