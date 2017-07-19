package br.com.granit.util.decoradores;

import java.sql.Date;

import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

import br.com.granit.util.Formatador;

public class DataDecorator implements DisplaytagColumnDecorator    {

	@Override
	public Object decorate(Object coluna, PageContext arg1, MediaTypeEnum arg2)
			throws DecoratorException {
		if (coluna == null)
			return "00/00/0000";
		if (coluna instanceof Date) {			
			Date data = (Date) coluna;
			return Formatador.getDataFormatada(data);
		}
		return "00/00/0000";
		
	}

}
