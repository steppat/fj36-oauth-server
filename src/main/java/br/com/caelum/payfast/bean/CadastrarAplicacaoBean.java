package br.com.caelum.payfast.bean;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.caelum.payfast.dao.AplicacaoDao;
import br.com.caelum.payfast.modelo.Aplicacao;

@Controller
@RequestMapping("/aplicacao")
public class CadastrarAplicacaoBean {

	@Autowired private AplicacaoDao aplicacaoDao;

	public List<Aplicacao> getAplicacoes() {
		return aplicacaoDao.getAplicacoes();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index() {
		return createMV();
	}

	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView cadastrar(@Valid Aplicacao aplicacao, BindingResult result) {
		if(!result.hasErrors()) {
			aplicacaoDao.inserir(aplicacao);
		}
		return createMV();
	}

	private ModelAndView createMV() {
		ModelAndView mv = new ModelAndView("aplicacao");
		List<Aplicacao> aplicacoes = this.getAplicacoes();
		mv.addObject("aplicacoes", aplicacoes);
		return mv;
	}

}
