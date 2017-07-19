package br.com.granit.util;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class Validador {

	public static boolean ehValorValido(String valor) {
		try {
			new Double(valor);
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}

	public static boolean validaDatasOrdemCronologica(List<String> datas) {
		Date dataAnterior = null;
		for (String dataStr : datas) {
			try {
				Date data = Formatador.converterStringParaDate(dataStr, "dd/MM/yyyy");
				if (dataAnterior != null && data.compareTo(dataAnterior) < 0) {
					return false;
				}
				dataAnterior = data;
			} catch (ParseException e) {	
				return false;				
			}
		}
		return true;
	}

}
