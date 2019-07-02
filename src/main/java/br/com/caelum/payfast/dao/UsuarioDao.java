package br.com.caelum.payfast.dao;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.caelum.payfast.modelo.Usuario;

@Component
public class UsuarioDao {
	
	private static List<Usuario> users = new ArrayList<>(1);

	public UsuarioDao() {
		createUser();
	}

	private void createUser() {
		Usuario user = new Usuario();
		user.setEmail("usuario@email.com");
		user.setSenha("1234");
		users.add(user);
	}
	
	public boolean existe(Usuario usuario) {
		String email = usuario.getEmail();
		String senha = usuario.getSenha();
		Usuario u= users.stream()
                .filter(user -> user.getEmail().equals(email) && user.getSenha().equals(senha) )
                .findFirst()
                .orElse(null);
		
		return u != null;
	}

}
