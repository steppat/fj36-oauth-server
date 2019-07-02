package br.com.caelum.payfast.resource;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.caelum.payfast.oauth.TokenDao;


@RestController
public class OAuthResource {
	
	
	@Autowired private TokenDao dao;
	
	@GetMapping(value="/token/accesstoken/{code}")
	public ResponseEntity<?> existsAccessToken(@PathParam("code") String accessToken) { 
		if(dao.existsAccessToken(accessToken)) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
}
