package br.com.granit.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.com.granit.persistencia.excecao.JPAUtilityException;

/**
 * Classe utilitária de persistência.
 * @author andre
 */
public class JPAUtility {

	private static final String DEFAULT_TYPE = "JPA";

	//@PersistenceUnit
	private static EntityManagerFactory factory;
	
	private static boolean transacoesAtivadas = true;
	
	private static EntityManager entityManagerCorrente;

	/**
	 * Método que verifica se há transações ativadas.
	 * @return Indicação de há ou não transações ativadas.
	 */
	public static synchronized boolean isTransacoesAtivadas() {
		return transacoesAtivadas;
	}

	/**
	 * Método que atualiza o valor do campo <strong>transacoesAtivadas</strong>.
	 * @param transacoesAtivadas O novo valor do campo.
	 */
	public static synchronized void setTransacoesAtivadas(
			boolean transacoesAtivadas) {
		JPAUtility.transacoesAtivadas = transacoesAtivadas;
	}

	/**
	 * Método que cria um EntityManager.
	 * @param persistenceUnitType O tipo de EntityManager a ser criado.
	 */
	private static EntityManager createEntityManager(String persistenceUnitType) {
		if (factory== null || !factory.isOpen()){
			factory = Persistence.createEntityManagerFactory(persistenceUnitType);			
		} 
		if (isTransacoesAtivadas()){
			entityManagerCorrente = factory.createEntityManager();			
		}else{
			if (entityManagerCorrente == null || !entityManagerCorrente.isOpen()){
				entityManagerCorrente = factory.createEntityManager();
			}
		}
		return entityManagerCorrente;
	}
	
	/**
	 * Método que recupera o EntityManager de acordo com o tipo passado. 
	 * @param type O tipo do EntityManager.
	 * @return O EntityManager do tipo especificado.
	 * @throws JPAUtilityException Se houver erro no acesso a base de dados.
	 */
	public static EntityManager getEntityManager(){		
		return createEntityManager(DEFAULT_TYPE);
	}

	/**
	 * Método que inicializa uma transação num determinado EntityManager.
	 * @param em O EntityManager no qual a transação será iniciada.
	 */
	public static void beginTransaction(EntityManager em) {
		if (isTransacoesAtivadas()){
			em.getTransaction().begin();
		}
	}
	
	/**
	 * Método que inicializa um grupo de transações num determinado EntityManager independentemente
	 * do flag <strong>transacoesAtivadas</strong> está ativado ou não.
	 * @param em O EntityManager no qual a transação será iniciada.
	 */
	public static void beginTransactionGroup(EntityManager em) {		
		em.getTransaction().begin();		
	}
	
	/**
	 * Método que finaliza um grupo de transações.
	 * @param em O EntityManager no qual a transação será finalizada.
	 */
	public static void commitTransactionGroup(EntityManager em) {		
		em.getTransaction().commit();	
		em.close();
	}
	
	/**
	 * Método que cancela um grupo de transações.
	 * @param em O EntityManager no qual a transação será cancelada.
	 */
	public static void rollbackTransactionGroup(EntityManager em) {
		em.getTransaction().rollback();
		em.close();
	}
	
	/**
	 * Método que finaliza uma transação.
	 * @param em O EntityManager no qual a transação será finalizada.
	 * @param clazz
	 */
	public static void commitTransaction(EntityManager em) {
		if (isTransacoesAtivadas()){
			em.getTransaction().commit();			
			em.close();
		}
	}
	
	/**
	 * Método que finaliza uma transação.
	 * @param em O EntityManager no qual a transação será finalizada.
	 * @param clazz
	 */
	public static void commitTransactionAndCloseFactory(EntityManager em) {
		if (isTransacoesAtivadas()){
			em.getTransaction().commit();			
			em.close();
		}
	}

	/**
	 * Método que cancela uma transação.
	 * @param em O EntityManager no qual a transação será cancelada.
	 */
	public static void rollbackTransaction(EntityManager em) {
		if (isTransacoesAtivadas()){
			if (em!=null && em.isOpen() ){
				if (em.getTransaction().isActive())
					em.getTransaction().rollback();
				em.close();
			}
		}
	}	
	
}
