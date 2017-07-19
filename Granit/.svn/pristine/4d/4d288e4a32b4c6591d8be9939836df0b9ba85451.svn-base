package br.com.granit.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Classe utilitária de Strings.
 * @author André Gomes
 */
public class StringUtil {

	private static final String ESPACO = " ";
	private static final String PONTO_ = ".";
	private static final String BARRA = "/";
	private static final String TRACO = "-";
	private static final String ESPACO_BRANCO = "";
	private static final String PONTO = "\\.";

	/**
	 * Método que procura um padrão e o substitui por outro valor.
	 * @param line A linha na qual se busca o padrão.
	 * @param values O valores a serem substituídos.
	 * @return A nova linha com as substituições.
	 */
	public static String matchAndReplace(String line, String... values) {
		Pattern pattern = Pattern.compile("\\{[0-9]{1,4}\\}");
		Matcher matcher = pattern.matcher(line);
		String resultLine = ESPACO_BRANCO;
		for (String param : values) {
			resultLine = matcher.replaceFirst(param);
			matcher = pattern.matcher(resultLine);
		}
		return resultLine;
	}

	/**
	 * Método que valida e-mail.
	 * @param email O e-mail a ser validado.
	 * @return Indicação de sucesso ou não.
	 */
	public static boolean validateEmail(String email) {
		Pattern pattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-z]{2,4}");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	/**
	 * Método que valida uma URL.
	 * @param url A URL a ser validada.
	 * @return Indicação de sucesso ou não.
	 */
	public static boolean validateUrl(String url) {
		final String QUALQUER_CARACTER = "\\w+";
		final String PONTO = "\\.";
		final String INICIO = "((ftp|http|https|gopher|mailto|news|nntp|" +
				"telnet|wais|file|prospero|aim|webcal)://)?";
		final String FIM = "(\\.(\\w+)(.+))?";
		final String EXPRESSAO = INICIO + QUALQUER_CARACTER + PONTO + QUALQUER_CARACTER + PONTO
				+ QUALQUER_CARACTER + FIM;
		Pattern pattern = Pattern.compile(EXPRESSAO);
		Matcher matcher = pattern.matcher(url);
		return matcher.matches();
	}

	/**
	 * Método que valida um CEP.
	 * @param cep O CEP a ser validado.
	 * @return Indicação de sucesso ou não.
	 */
	public static boolean validateCep(String cep) {
		Pattern pattern = Pattern.compile("[0-9]{2}\\.[0-9]{3}-[0-9]{3}");
		Matcher matcher = pattern.matcher("58.102-400");
		return matcher.matches();
	}

	/**
	 * Método que remove a máscara de um CPF.
	 * @param cpf O CPF o qual deve ter a máscara removida.
	 * @return O CPF sem a máscara.
	 */
	public static String retiraMascaraCpf(String cpf) {
		String retorno = cpf.replaceAll(PONTO, ESPACO_BRANCO);
		retorno = retorno.replaceAll(TRACO, ESPACO_BRANCO);
		retorno = retorno.trim();
		return retorno;
	}

	/**
	 * Método que remove a máscara de uma DATA.
	 * @param data A DATA a qual deve ter a máscara removida.
	 * @return A DATA sem máscara.
	 */
	public static String retiraMascaraData(String data) {
		String retorno = data.replaceAll(BARRA, ESPACO_BRANCO);
		retorno.replaceAll(ESPACO, ESPACO_BRANCO);
		retorno = retorno.trim();
		return retorno;
	}

	/**
	 * Método que remove a máscara de um VALOR.
	 * @param valor O VALOR o qual deve ter a máscara removida.
	 * @return O VALOR sem máscara.
	 */
	public static String retiraMascaraValor(String valor) {
		String retorno = valor.replaceAll(PONTO, ESPACO_BRANCO);
		retorno.replaceAll(ESPACO, ESPACO_BRANCO);
		retorno = retorno.trim();
		return retorno;
	}

	/**
	 * Método que remove a mácara de um CNPJ.
	 * @param cnpj O CNPJ o qual deve ter a mácara removida.
	 * @return O CNPJ sem máscara.
	 */
	public static String retiraMascaraCnpj(String cnpj) {
		String retorno = cnpj.replaceAll(PONTO, ESPACO_BRANCO);
		retorno = retorno.replaceAll(TRACO, ESPACO_BRANCO);
		retorno = retorno.replaceAll(BARRA, ESPACO_BRANCO);
		return retorno;
	}

	/**
	 * Método que remove a máscara de um CEP.
	 * @param cep O CEP o qual deve ter a máscara removida.
	 * @return O CEP sem a máscara.
	 */
	public static String retiraMascaraCep(String cep) {
		String retorno = cep.replaceAll(PONTO, ESPACO_BRANCO);
		retorno = retorno.replaceAll(TRACO, ESPACO_BRANCO);
		return retorno;
	}

	/**
	 * Método que coloca uma máscara no CEP.
	 * @param cep O CEP o qual a máscara deve ser colocada.
	 * @return O CEP com máscara.
	 */
	public static String colocaMascaraCep(String cep) {
		StringBuffer retorno = new StringBuffer();
		retorno.append(cep.substring(0, 2));
		retorno.append(PONTO_);
		retorno.append(cep.substring(3, 6));
		retorno.append(TRACO);
		retorno.append(cep.substring(7));
		return retorno.toString();
	}

	/**
	 * Método que coloca uma máscara no CNPJ.
	 * @param cnpj O CNPJ o qual a máscara deve ser colocada.
	 * @return O CNPJ com máscara.
	 */
	public static String colocaMascaraCnpj(String cnpj) {
		StringBuffer retorno = new StringBuffer();
		retorno.append(cnpj.substring(0, 2));
		retorno.append(PONTO_);
		retorno.append(cnpj.substring(2, 5));
		retorno.append(PONTO_);
		retorno.append(cnpj.substring(5, 8));
		retorno.append(BARRA);
		retorno.append(cnpj.substring(8, 12));
		retorno.append(TRACO);
		retorno.append(cnpj.substring(12));
		return retorno.toString();
	}

	/**
	 * Método que coloca uma máscara na DATA.
	 * @param data A DATA a qual a máscara deve ser colocada. 
	 * @return A DATA com máscara.
	 */
	public static String colocaMascaraData(String data) {
		StringBuffer retorno = new StringBuffer();
		retorno.append(data.substring(0, 1));
		retorno.append(BARRA);
		retorno.append(data.substring(2, 3));
		retorno.append(BARRA);
		return retorno.toString();
	}

	/**
	 * Método que coloca uma máscara no CPF.
	 * @param cpf O CPF o qual a máscara deve ser colocada. 
	 * @return O CPF com máscara.
	 */
	public static String colocaMascaraCpf(String cpf) {
		StringBuffer retorno = new StringBuffer();
		retorno.append(cpf.substring(0, 3));
		retorno.append(PONTO_);
		retorno.append(cpf.substring(3, 6));
		retorno.append(PONTO_);
		retorno.append(cpf.substring(6, 9));
		retorno.append(TRACO);
		retorno.append(cpf.substring(9));
		return retorno.toString();
	}

	/**
	 * Método que verifica se uma string é vazia ou nula.
	 * @param texto A string a ser verificada.
	 * @return Indicação de sucesso ou não.
	 */
	public static boolean isBlankOrNull(String texto) {
		return texto == null || texto.equals("") || texto.trim().equals("");
	}

	/**
	 * Método que preenche um campo com máscara.
	 * @param value O valor a ser colocado no campo.
	 * @param sizeOfMask Tamanho da máscara.
	 * @return O campo preenchido.
	 */
	public static String fillField(String value, int sizeOfMask) {
		int sizeOfValue = value.substring(0, value.indexOf('.')).length();
		if (sizeOfValue > sizeOfMask) {
			return null;
		}
		boolean hasFourDigits = ((sizeOfValue == sizeOfMask) ? true : false);
		if (hasFourDigits) {
			return value.concat("0");
		}
		int difference = sizeOfMask - sizeOfValue;
		StringBuffer newValue = new StringBuffer(value);
		char append = '0';
		while (difference != 0) {
			newValue.insert(0, append);
			difference--;
		}
		return newValue.append('0').toString();
	}

}
