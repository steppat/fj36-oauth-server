package br.com.caelum.payfast.bean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.caelum.payfast.dao.AplicacaoDao;
import br.com.caelum.payfast.dao.UsuarioDao;
import br.com.caelum.payfast.modelo.Aplicacao;
import br.com.caelum.payfast.modelo.Usuario;
import br.com.caelum.payfast.oauth.TokenDao;

@Controller
@Scope("session")
public class LoginBean {
	
	@Autowired private AplicacaoDao aplicacaoDao;
	@Autowired private UsuarioDao usuarioDao;
	@Autowired	private TokenDao tokens;
	
	
	private Aplicacao aplicacao;
	
	
	@RequestMapping(method=RequestMethod.GET,value="/")
	public ModelAndView login(@RequestParam("clientId") String clientId, 
								@RequestParam("clientSecret") String clientSecret, 
									@RequestParam("redirect_uri") String redirect_uri,
										@RequestParam(value="message", required=false) String message) {
		
		this.aplicacao = new Aplicacao();
		aplicacao.setClientId(clientId);
		aplicacao.setClientSecret(clientSecret);
		aplicacao.setAppcallbackUrl(redirect_uri);
		
		boolean valido = aplicacaoDao.existeClientId(aplicacao);

		ModelAndView mv = new ModelAndView("index");
		if(message == null ||  message.isEmpty()) {
			mv.addObject("message", "Client Id " + (valido ? "valido" : "nao valido"));
		} else {
			mv.addObject("message", message);
		}
		return mv;
	}
	

	@RequestMapping(method=RequestMethod.POST,value="/login")
	public String login(@ModelAttribute @Valid Usuario usuario, HttpServletRequest request, BindingResult result) {
		
		if(result.hasErrors() || !usuarioDao.existe(usuario)) {
			return createRedirectUrl();
		}
		
		try {
			String authorizationToken = geraOAuthToken();
			tokens.addAuthToken(authorizationToken);
			OAuthResponse response = buildOkResponse(authorizationToken, request);
			return "redirect:" + response.getLocationUri();
		} catch (OAuthSystemException e) {
			throw new RuntimeException(e);
		}
	}


	private String geraOAuthToken() throws OAuthSystemException {
		OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
		String authorizationToken = oauthIssuerImpl.authorizationCode();
		return authorizationToken;
	}


	private String createRedirectUrl() {
		return String.format("redirect:/?clientId=%s&clientSecret=%s&redirect_uri=%s&message=Login invalido", 
				aplicacao.getClientId(), 
				aplicacao.getClientSecret(),
				aplicacao.getAppcallbackUrl());
	}

	private OAuthResponse buildOkResponse(String authorizationToken, HttpServletRequest request) throws OAuthSystemException {
		OAuthResponse response = OAuthASResponse
				.authorizationResponse(request, HttpServletResponse.SC_FOUND)
				.setCode(authorizationToken)
		        .location(this.aplicacao.getAppcallbackUrl())
		        .buildQueryMessage();
		
		return response;
	}
	
}
