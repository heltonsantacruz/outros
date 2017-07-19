package br.com.granit.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.com.granit.persistencia.excecao.JPAUtilityException;

/**
 * Classe utilit�ria de persist�ncia.
 * @author andre
 */
public class JPAUtility {

	private static final String DEFAULT_TYPE = "JPA";

	//@PersistenceUnit
	private static EntityManagerFactory factory;
	
	private static boolean transacoesAtivadas = true;
	
	private static EntityManager entityManagerCorrente;

	/**
	 * M�todo que verifica se h� transa��es ativadas.
	 * @return Indica��o de h� ou n�o transa��es ativadas.
	 */
	public static synchronized boolean isTransacoesAtivadas() {
		return transacoesAtivadas;
	}

	/**
	 * M�todo que atualiza o valor do campo <strong>transacoesAtivadas</strong>.
	 * @param transacoesAtivadas O novo valor do campo.
	 */
	public static synchronized void setTransacoesAtivadas(
			boolean transacoesAtivadas) {
		JPAUtility.transacoesAtivadas = transacoesAtivadas;
	}

	/**
	 * M�todo que cria um EntityManager.
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
	 * M�todo que recupera o EntityManager de acordo com o tipo passado. 
	 * @param type O tipo do EntityManager.
	 * @return O EntityManager do tipo especificado.
	 * @throws JPAUtilityException Se houver erro no acesso a base de dados.
	 */
	public static EntityManager getEntityManager(){		
		return createEntityManager(DEFAULT_TYPE);
	}

	/**
	 * M�todo que inicializa uma transa��o num determinado EntityManager.
	 * @param em O EntityManager no qual a transa��o ser� iniciada.
	 */
	public static void beginTransaction(EntityManager em) {
		if (isTransacoesAtivadas()){
			em.getTransaction().begin();
		}
	}
	
	/**
	 * M�todo que inicializa um grupo de transa��es num determinado EntityManager independentemente
	 * do flag <strong>transacoesAtivadas</strong> est� ativado ou n�o.
	 * @param em O EntityManager no qual a transa��o ser� iniciada.
	 */
	public static void beginTransactionGroup(EntityManager em) {		
		em.getTransaction().begin();		
	}
	
	/**
	 * M�todo que finaliza um grupo de transa��es.
	 * @param em O EntityManager no qual a transa��o ser� finalizada.
	 */
	public static void commitTransactionGroup(EntityManager em) {		
		em.getTransaction().commit();	
		em.close();
	}
	
	/**
	 * M�todo que cancela um grupo de transa��es.
	 * @param em O EntityManager no qual a transa��o ser� cancelada.
	 */
	public static void rollbackTransactionGroup(EntityManager em) {
		em.getTransaction().rollback();
		em.close();
	}
	
	/**
	 * M�todo que finaliza uma transa��o.
	 * @param em O EntityManager no qual a transa��o ser� finalizada.
	 * @param clazz
	 */
	public static void commitTransaction(EntityManager em) {
		if (isTransacoesAtivadas()){
			em.getTransaction().commit();			
			em.close();
		}
	}
	
	/**
	 * M�todo que finaliza uma transa��o.
	 * @param em O EntityManager no qual a transa��o ser� finalizada.
	 * @param clazz
	 */
	public static void commitTransactionAndCloseFactory(EntityManager em) {
		if (isTransacoesAtivadas()){
			em.getTransaction().commit();			
			em.close();
		}
	}

	/**
	 * M�todo que cancela uma transa��o.
	 * @param em O EntityManager no qual a transa��o ser� cancelada.
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
