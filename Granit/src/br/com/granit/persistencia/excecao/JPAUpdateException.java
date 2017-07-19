package br.com.granit.persistencia.excecao;

/**
 * Exceção que indica erro no acesso a base de dados do sistema. Erro 
 * ao tentar atualizar um registro na base de dados.
 * @author andre. 
 */
public class JPAUpdateException extends JPAUtilityException {

	/**
	 * Número de versão.
	 */
	private static final long serialVersionUID = -2772296934794173859L;

	/**
	 * Construtor.
	 * @param message Mensagem de erro.
	 */
	public JPAUpdateException(String message) {
		super(message);
	}

	/**
	 * Construtor.
	 * @param message Mensagem de erro.
	 * @param cause A exceção que originou o erro.
	 */
	public JPAUpdateException(String message, Throwable cause) {
		super(message, cause);
	}

}
