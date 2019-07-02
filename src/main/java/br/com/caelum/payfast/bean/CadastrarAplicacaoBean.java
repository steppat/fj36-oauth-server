package br.com.caelum.payfast.bean;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.caelum.payfast.dao.AplicacaoDao;
import br.com.caelum.payfast.modelo.Aplicacao;

@Controller
public class CadastrarAplicacaoBean {

	@Autowired private AplicacaoDao aplicacaoDao;

	public List<Aplicacao> getAplicacoes() {
		return aplicacaoDao.getAplicacoes();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/")
	public void index() {
		
	}

	@RequestMapping(method=RequestMethod.POST, value="/cadastrar")
	public void cadastrar(Aplicacao aplicacao) {
		aplicacao.setClientId(Long.toString(System.currentTimeMillis()));
		aplicacao.setClientSecret(Double.toString(Math.random()));
		
		aplicacaoDao.inserir(aplicacao);
	}

}
