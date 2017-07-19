package br.com.granit.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Stack;

public class CurrencyUtil {
	
	
	public static String getNumberFormatRelString(double valor){	
   		String retorno = "";
   		int cte = 3;
   		Stack pilha = new Stack();
   		String stringValor = String.valueOf(valor);
   		String st1,st2;
   		st1 = stringValor.substring(0,stringValor.indexOf("."));
   		st2 = stringValor.substring(stringValor.indexOf(".")+1);
   		
   		while(true){
   			if(st1.length() > cte){
   				pilha.add(st1.substring(st1.length()-cte,st1.length()));
   				st1 = st1.substring(0,st1.length()-cte);
   			}
   			else{
   				pilha.add(st1);
   				break;
   			}
   		}   		
   		while(!pilha.empty()){   			
   			if(pilha.size() > 1){
   				String g = (String)pilha.pop();   				
   				retorno +=  g+ ".";   		
   			}
   			else{
   				String g = (String)pilha.pop();   				
   				retorno += g + ",";
   			}
		}
   		if(st2.length() < 2){
   			st2 += "000";
   		} 
   		else if(st2.length() >= 4){
   			st2 = st2.substring(0,4);
   		}
   		else if(st2.length() == 3){
   			st2 = st2.substring(0,3);
   			st2+= "0";
   		}
   		retorno += st2;
   		return retorno;    
   }
	
	
	
	public static String getNumberFormatString(double valor){	
   		String retorno = "";
   		int cte = 3;
   		Stack pilha = new Stack();
   		String stringValor = String.valueOf(valor);
   		String st1,st2;
   		st1 = stringValor.substring(0,stringValor.indexOf("."));
   		st2 = stringValor.substring(stringValor.indexOf(".")+1);
   		
   		while(true){
   			if(st1.length() > cte){
   				pilha.add(st1.substring(st1.length()-cte,st1.length()));
   				st1 = st1.substring(0,st1.length()-cte);
   			}
   			else{
   				pilha.add(st1);
   				break;
   			}
   		}   		
   		while(!pilha.empty()){   			
   			if(pilha.size() > 1){
   				String g = (String)pilha.pop();   				
   				retorno +=  g+ ".";   		
   			}
   			else{
   				String g = (String)pilha.pop();   				
   				retorno += g + ",";
   			}
		}		
   		if(st2.length() < 2){
   			st2 += "0";
   		} 
   		else if(st2.length() >= 2){
   			st2 = st2.substring(0,2);
   		}   		
   		retorno += st2;
   		return retorno;    
   }
	
	
	
	public static double getNumberFormatReal(String valor)throws Exception{
		
		String st1 = valor.substring(0,valor.indexOf(","));
		String st2 = valor.substring(valor.indexOf(",")+1); 
		Stack pilha = new Stack();
		int cte = 3;
		String retorno = "";
		String ponto;
		while(true){
   			if(st1.length() > cte){
   				pilha.add(st1.substring(st1.length()-cte,st1.length()));
  				st1 = st1.substring(0,st1.length()-cte);
  				ponto = st1.substring(st1.length()-1,st1.length());
   				if(!ponto.equalsIgnoreCase(".")){
   					throw new Exception("Número inválido"); 
   				}   				
   				st1 = st1.substring(0,st1.length()-1);  				
   			}
   			else{
   				pilha.add(st1);
   				break;
   			}
   		}
		while(!pilha.empty()){   			
   			if(pilha.size() > 1){
   				String g = (String)pilha.pop();   				
   				retorno +=  g;
   			}
   			else{
   				String g = (String)pilha.pop();   				
   				retorno += g + ".";
   			}
		}
   		if(st2.length() != 2){
   			throw new Exception("Número inválido");
   		}   		   		
		double t = Double.parseDouble(retorno+st2);   		
		return t;
	}
	
	public static boolean faltaCentavo(double valorTotal, int quantidadeParcelas) {

		double somaParcelas = 0.0;
		double diferencaParcelas = 0.0;
		BigDecimal valorParcela = new BigDecimal(Formatador.arredondaDouble(valorTotal / quantidadeParcelas));
		for (int i = 0; i < quantidadeParcelas; i++)
			somaParcelas += Formatador.arredondaDouble(valorParcela.doubleValue());
		diferencaParcelas = Formatador.arredondaDouble(valorTotal - somaParcelas);
		if (diferencaParcelas == 0.01)
			return true;
		return false;
		
	}

	public static double sobraCentavosArredondamento(double valorTotal, int quantidadeParcelas) {

		double somaParcelas = 0.0;
		double diferencaParcelas = 0.0;
		BigDecimal valorParcela = new BigDecimal(Formatador.arredondaDouble(valorTotal / quantidadeParcelas));
		for (int i = 0; i < quantidadeParcelas; i++)
			somaParcelas += Formatador.arredondaDouble(valorParcela.doubleValue());
		diferencaParcelas = Formatador.arredondaDouble(valorTotal - somaParcelas);
		return diferencaParcelas;
	}
	
	public static String calculaTotalizador(List colecao, String nomeCampo) {
		try{
			double total = 0.0;
			for (Object objeto : colecao) {
				String nomeMetodo = "get" + nomeCampo.substring(0, 1).toUpperCase()
						+ nomeCampo.substring(1, nomeCampo.length());
				Method metodo = objeto.getClass().getDeclaredMethod(nomeMetodo, new Class[0]);
				Object value = metodo.invoke(objeto, new Object[0]);
				if (value == null || "".equals(value)){
					continue;
				}
				if (value instanceof BigDecimal){
					total+= ((BigDecimal)value).doubleValue();
				}else if (value instanceof Double){
					total+= ((Double)value).doubleValue();
				}
			}
			return Formatador.currency(total);
		}catch(Exception e){
			return "R$";
		}
	}
	
	public static String calculaTotalizadorDouble(List colecao, String nomeCampo) {
		try{
			double total = 0.0;
			for (Object objeto : colecao) {
				String nomeMetodo = "get" + nomeCampo.substring(0, 1).toUpperCase()
						+ nomeCampo.substring(1, nomeCampo.length());
				Method metodo = objeto.getClass().getDeclaredMethod(nomeMetodo, new Class[0]);
				Object value = metodo.invoke(objeto, new Object[0]);
				if (value == null || "".equals(value)){
					continue;
				}
				if (value instanceof BigDecimal){
					total+= ((BigDecimal)value).doubleValue();
				}else if (value instanceof Double){
					total+= ((Double)value).doubleValue();
				}
			}
			return Formatador.doubleValue(total);
		}catch(Exception e){
			return "";
		}
	}



	public static String getNumberFormatStringQuatroCasas(String valor) {
		String retorno = "";
   		int cte = 5;
   		Stack pilha = new Stack();
   		String stringValor = String.valueOf(valor);
   		String st1,st2;
   		System.out.println(stringValor);
   		if(stringValor.indexOf(".") < 0){
   			return stringValor + ",0000";
   		}
   		st1 = stringValor.substring(0,stringValor.indexOf("."));   	
   		st2 = stringValor.substring(stringValor.indexOf(".")+1);
   		if(st2.length() > 4){
   			st2 = st2.substring(0, 4);
   		}
   		
   		while(true){
   			if(st1.length() > cte){
   				pilha.add(st1.substring(st1.length()-cte,st1.length()));
   				st1 = st1.substring(0,st1.length()-cte);
   			}
   			else{
   				pilha.add(st1);
   				break;
   			}
   		}   		
   		while(!pilha.empty()){   			
   			if(pilha.size() > 1){
   				String g = (String)pilha.pop();   				
   				retorno +=  g+ ".";   		
   			}
   			else{
   				String g = (String)pilha.pop();   				
   				retorno += g + ",";
   			}
		}
   		if(st2.length() < 1){
   			st2 += "0000";
   		} 
   		else if(st2.length() < 2){
   			st2 += "000";
   		} 
   		else if(st2.length() < 3){
   			st2 += "00";
   		}else if(st2.length() < 4){
   			st2 += "0";
   		}  
   		else if(st2.length() <= 4){
   			st2 = st2.substring(0,4);
   		}   		
   		retorno += st2;
   		return retorno;    
	}
	
	public static void main(String[] args) {
		
		
	}



	public static double getNumberFormatRealQuatroCasas(String valor) throws Exception {
		String st1 = valor.substring(0,valor.indexOf(","));
		String st2 = valor.substring(valor.indexOf(",")+1); 
		Stack pilha = new Stack();
		int cte = 5;
		String retorno = "";
		String ponto;
		while(true){
   			if(st1.length() > cte){
   				pilha.add(st1.substring(st1.length()-cte,st1.length()));
  				st1 = st1.substring(0,st1.length()-cte);
  				ponto = st1.substring(st1.length()-1,st1.length());
   				if(!ponto.equalsIgnoreCase(".")){
   					throw new Exception("Número inválido"); 
   				}   				
   				st1 = st1.substring(0,st1.length()-1);  				
   			}
   			else{
   				pilha.add(st1);
   				break;
   			}
   		}
		while(!pilha.empty()){   			
   			if(pilha.size() > 1){
   				String g = (String)pilha.pop();   				
   				retorno +=  g;
   			}
   			else{
   				String g = (String)pilha.pop();   				
   				retorno += g + ".";
   			}
		}
   		if(st2.length() != 4){
   			throw new Exception("Número inválido");
   		}   		   		
		double t = Double.parseDouble(retorno+st2);   		
		return t;
	}

}
