package br.com.granit.persistencia.excecao;

/**
 * Exceção que indica erro no acesso a base de dados do sistema. 
 * @author andre.
 *
 */
public class JPAUtilityException extends Exception {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 8950615696827788501L;
	
	/**
	 * Construtor.
	 * @param message Mensagem de erro.
	 */
	public JPAUtilityException(String  message){
		super(message);
	}
	
	/**
	 * Construtor.
	 * @param message Mensagem de erro.
	 * @param cause A exceção que originou o erro.
	 */
	public JPAUtilityException(String  message, Throwable cause){
		super(message, cause);		
	}

}
