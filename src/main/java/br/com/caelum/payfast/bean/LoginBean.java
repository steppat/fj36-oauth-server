package br.com.caelum.payfast.bean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.caelum.payfast.dao.AplicacaoDao;
import br.com.caelum.payfast.dao.UsuarioDao;
import br.com.caelum.payfast.modelo.Aplicacao;
import br.com.caelum.payfast.modelo.Usuario;
import br.com.caelum.payfast.oauth.TokenDao;

@Controller
public class LoginBean {
	
	@Autowired private AplicacaoDao aplicacaoDao;
	@Autowired private UsuarioDao usuarioDao;
	@Autowired	private TokenDao tokens;
	
	public boolean verificaCredenciais(Aplicacao aplicacao) {
		return aplicacaoDao.existeClientId(aplicacao);
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/")
	public ModelAndView login(Aplicacao aplicacao) {
		boolean valido = this.verificaCredenciais(aplicacao);

		ModelAndView mv = new ModelAndView("index");
		mv.addObject("valido", valido);
		return mv;
	}
	

	@RequestMapping(method=RequestMethod.POST,value="/login")
	public String login(@ModelAttribute Usuario usuario, HttpServletRequest request) {
		if (usuarioDao.existe(usuario)) {
			try {
				OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
				
				String authorizationToken = oauthIssuerImpl.authorizationCode();
				tokens.addAuthToken(authorizationToken);
				
				OAuthResponse response = buildOkResponse(authorizationToken, request);

				return "redirect:" + response.getLocationUri();
			} catch (OAuthSystemException e) {
				throw new RuntimeException(e);
			}
		}
		throw new RuntimeException("Usuario inexistente");

	}

	private OAuthResponse buildOkResponse(String authorizationToken, HttpServletRequest request) throws OAuthSystemException {
		OAuthResponse response = OAuthASResponse
				.authorizationResponse(request, HttpServletResponse.SC_FOUND)
				.setCode(authorizationToken)
		        .location("callbackURL")
		        .buildQueryMessage();
		
		return response;
	}
	
}
