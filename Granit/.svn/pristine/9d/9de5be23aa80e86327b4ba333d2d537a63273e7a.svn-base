package br.com.granit.util.decoradores;

import java.math.BigDecimal;

import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

import br.com.granit.util.Formatador;

public class DoubleDecorator implements DisplaytagColumnDecorator    {

	@Override
	public Object decorate(Object coluna, PageContext arg1, MediaTypeEnum arg2)
			throws DecoratorException {
		if (coluna == null)
			return "";
		if (coluna instanceof String) {			
			String valor = (String) coluna;
			if (valor.isEmpty()) return valor;
			return Formatador.doubleValue(valor);
		} else if (coluna instanceof Double) {
			Double valor = (Double) coluna;
			return Formatador.doubleValue(valor);
		} else if (coluna instanceof BigDecimal){
			return Formatador.doubleValue(((BigDecimal)coluna).doubleValue());
		}
		
		return "";
		
	}

}
