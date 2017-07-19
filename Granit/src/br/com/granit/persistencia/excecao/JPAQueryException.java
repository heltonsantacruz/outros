package br.com.granit.persistencia.excecao;

/**
 * Exce��o que indica erro no acesso a base de dados do sistema. Erro 
 * ao tentar consultar a base de dados.
 * @author andre. 
 */
public class JPAQueryException extends JPAUtilityException {

	/**
	 * N�mero de vers�o.
	 */
	private static final long serialVersionUID = -2772296934794173859L;

	/**
	 * Construtor.
	 * @param message Mensagem de erro.
	 */
	public JPAQueryException(String message) {
		super(message);
	}

	/**
	 * Construtor.
	 * @param message Mensagem de erro.
	 * @param cause A exce��o que originou o erro.
	 */
	public JPAQueryException(String message, Throwable cause) {
		super(message, cause);
	}

}
