package br.com.caelum.payfast.authorization;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;

import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.caelum.payfast.dao.AplicacaoDao;
import br.com.caelum.payfast.modelo.Aplicacao;
import br.com.caelum.payfast.oauth.TokenDao;

@RestController
public class AccessCodeResource {
	
    @Autowired private TokenDao tokens;
    @Autowired private AplicacaoDao aplicacaoDao;
    
    @GetMapping(value="/access",consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> returnAccessToken(HttpServletRequest request) {
		String authorizationToken = request.getParameter("code");
		String clientId = request.getParameter("client_id");
		String clientSecret = request.getParameter("client_secret");
		
		try {
			if(existsAuthToken(authorizationToken) && isValidClientIdAndSecret(clientId, clientSecret)) {
				OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
				
				String accessToken = oauthIssuerImpl.accessToken();
				tokens.addAccessToken(accessToken);
				
				return buildOkMessage(accessToken);
			}
			return buildErrorResponse();
		} catch (OAuthSystemException e) {
			throw new RuntimeException(e);
		}
    }

	private ResponseEntity<?> buildOkMessage(String accessToken) throws OAuthSystemException {
		OAuthResponse response = OAuthASResponse
				.tokenResponse(HttpServletResponse.SC_OK)
				.setAccessToken(accessToken)
				.buildJSONMessage();
		
		return buildResponse(response);
	}

	private ResponseEntity<?> buildErrorResponse() throws OAuthSystemException {
		OAuthResponse response = OAuthASResponse
				.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
				.setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
				.buildJSONMessage();
		
		return buildResponse(response);
	}
	
	private ResponseEntity<String> buildResponse(OAuthResponse response) {
		return ResponseEntity.status(response.getResponseStatus()).body(response.getBody());
	}
	
	private boolean existsAuthToken(String authToken) { 
		return this.tokens.existsAuthToken(authToken);
	}
	
	private boolean isValidClientIdAndSecret(String clientId, String clientSecret) { 
		return this.aplicacaoDao.existeAplicacao(new Aplicacao(clientId, clientSecret));
	}
}
