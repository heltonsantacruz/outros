package br.com.granit.persistencia.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import br.com.granit.persistencia.dao.generico.DAOGenerico;
import br.com.granit.persistencia.filtro.FiltroUsuario;
import br.com.granit.persistencia.to.UsuarioTO;

public class UsuarioDAO extends DAOGenerico<UsuarioTO, Integer>{
	
	public List<UsuarioTO> consultarFiltro(FiltroUsuario filtro) {
		List<UsuarioTO> resultados = new ArrayList<UsuarioTO>();
		
		StringBuffer queryBuf = new StringBuffer(
				"select distinct usuario from " + tipo.getSimpleName()
						+ " usuario ");
		boolean firstClause = true;
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		if (filtro.getPerfil() != null && !filtro.getPerfil().trim().equals("")){	
			queryBuf.append("inner join usuario.perfis p where p.idPerfil = :perfil ");
			firstClause = false;
			parametros.put("perfil", Integer.parseInt(filtro.getPerfil()));
		}	
		
		if (filtro.getLogin() != null && !filtro.getLogin().trim().equals("")){
			queryBuf.append(firstClause ? " where " : " and ");
			queryBuf.append("LOWER (usuario.login)  like :login");
			firstClause = false;
			parametros.put("login", filtro.getLogin());
		}
		
		if (filtro.getNome() != null && !filtro.getNome().trim().equals("")){
			queryBuf.append(firstClause ? " where " : " and ");
			queryBuf.append("LOWER (usuario.nome) like :nome");
			firstClause = false;
			parametros.put("nome", filtro.getNome());
			
		}
		
		String hqlQuery = queryBuf.toString();
		Query query = getEntityManager().createQuery(hqlQuery);
		for (Map.Entry<String, Object> entry : parametros.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());									
		}
		resultados = query.getResultList();
		
		return resultados;
	}
}
