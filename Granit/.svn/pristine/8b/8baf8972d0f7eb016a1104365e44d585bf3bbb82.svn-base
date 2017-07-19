package br.com.granit.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.struts.action.ActionServlet;

public class Formatador {

	public final static String FORMATO_DATA_HORA_BACKUP = "dd-MM-yyyy_HH-mm-ss";
	public final static String DATA_PATTERN = "dd/MM/yy";
    public final static String FORMATO_DATA_PADRAO = "dd/MM/yyyy";
    public final static String FORMATO_DATA_DIA_MES = "dd/MM";
    public final static String FORMATO_DATA_HORA = "dd/MM/yyyy HH:mm:ss";
    public final static String FORMATO_HORA_PADRAO = "HH:mm:ss";
    public final static int DATA_SIZE = 10;
    public final static int MILISEGUNDOS_DIA = 24 * 3600 * 1000;
    public final static String FORMATO_NUMERO_PADRAO = "#,##0.00";
	
	public static String currency(double NUMBER) {
		String number = format(NUMBER, "#,##0.00");
		return "R$ " + number;
	}

	public static String percentual(double NUMBER) {
		String number = format(NUMBER, "#,##0.00");
		return number + "%";
	}
	
	public static String doubleValue(double NUMBER) {
		String number = format(NUMBER, "#,##0.00");
		return number;
	}

	public static String currency(String NUMBER) {
		return currency(parseDouble(NUMBER));
	}
	
	public static String doubleValue(String NUMBER) {
		return currency(parseDouble(NUMBER));
	}
	
	public static String format(double NUMBER, String FORMAT) {
		if (FORMAT == null || FORMAT.length() == 0) {
			FORMAT = FORMATO_NUMERO_PADRAO;
		}
		DecimalFormat df = new DecimalFormat(FORMAT);
		String number = df.format(NUMBER);
		return number;
	}
	
	public static double parseDouble(String NUMBER) {
		double resultado = 0;
		if (NUMBER != null && NUMBER.length() > 0) {
			NUMBER = NUMBER.trim();
			NUMBER = checkDouble(NUMBER);
			try {
				resultado = (new Double(NUMBER)).doubleValue();
			} catch (Exception e) {
				resultado = 0;
			}
		}
		return resultado;
	}
	
	private static String checkDouble(String NUMBER) {
		if (NUMBER != null && NUMBER.length() > 0) {
			if (NUMBER.indexOf(",") != -1 || NUMBER.indexOf(".") != -1) {
				int limit = NUMBER.length() - 4;
				String new_number = "";
				for (int i = 0; i < NUMBER.length(); i++) {
					if (NUMBER.charAt(i) == '.' || NUMBER.charAt(i) == ',') {
						if (i > limit) {
							new_number += ".";
						} else if (NUMBER.substring(i + 1).indexOf(".") == -1 && NUMBER.substring(i + 1).indexOf(",") == -1) {
							new_number += ".";
						}
					} else {
						new_number += NUMBER.charAt(i);
					}
				}
				NUMBER = new_number;
			}
		}
		return NUMBER;
	}
	
	
    /**
     * Converte um String em um java.sql.Date. O formato especificado deve ser
     * aquele em que a data deverá estar. Abaixo segue alguns formatos e seus
     * respectivos valores originais                                       <BR>
     *                                                                     <BR>
     * formato             valor original                                  <BR>
     * -------             --------------                                  <BR>
     * dd/MM/yy               10/03/02                                     <BR>
     * dd/MM/yy hh:mm:ss      10/03/02 12:35:00                            <BR>
     *                                                                     <BR>
     * @param formato - Formato em que a data deverá estar                 <BR>
     * @param data - String contendo a data a ser convertida               <BR>
     *                                                                     <BR>
     * @return Date - Retorna o valor da data no formato java.sql.Date     <BR>
     * @throws ParseException Caso não seja possível converter a data para o
     * formato especificado.
     */
   public static java.sql.Date converterStringParaSQLDate(String data,
           String formato) throws ParseException {
       return new java.sql.Date(
           (converterStringParaDate(formato, data)).getTime());
   }
       
   /**
    * Converte um String em um Date. O formato especificado deve ser aquele em
    * que a data deverá estar. Abaixo segue alguns formatos e seus respectivos
    * valores originais                                                    <BR>
    *                                                                      <BR>
    * formato             valor original                                   <BR>
    * -------             --------------                                   <BR>
    * dd/MM/yy               10/03/02                                      <BR>
    * dd/MM/yy hh:mm:ss      10/03/02 12:35:00                             <BR>
    *                                                                      <BR>
    * @param formato - Formato em que a data deverá estar                  <BR>
    * @param data - String contendo a data a ser convertida                <BR>
    *                                                                      <BR>
    * @return Date - Retorna o valor da data no formato java.util.Date     <BR>
    * @throws ParseException Caso não seja possível converter a data para o
    * formato especificado.
    */
   public static Date converterStringParaDate(String data, 
           String formato) throws ParseException {
       SimpleDateFormat sdf = new SimpleDateFormat(formato);
       return sdf.parse(data);
   }
   
   /**
    * Converte uma data para o formato especificado pela máscara informada.<BR>
    *                                                                      <BR>
    * @param mascara - Máscara para o retorno da data                      <BR>
    * @param data - Data que será formatada                                <BR>
    *                                                                      <BR>
    *   Ano com 4 dígitos - yyyy                                           <BR>
    *   Ano com 2 dígitos - yy                                             <BR>
    *   Mês - MM                                                           <BR>
    *   Dia - dd                                                           <BR>
    *   Hora  (00 - 23) - HH                                               <BR>
    *   Hora (1 - 12) - hh                                                 <BR>
    *   Minuto - mm                                                        <BR>
    *   Segundo - ss                                                       <BR>
    *                                                                      <BR>
    *  Exemplo para máscaras             Retorno                           <BR>
    *     dd-MM-yyyy                    09-11-2001                         <BR>
    *     dd/MM/yyyy                    09/11/2001                         <BR>
    *     yyyy-MM-dd                    2001-11-09                         <BR>
    *     yyyy-MM-dd  HH:mm:ss          2001-11-09 15:25:10                <BR>
    *                                                                      <BR>
    * @return String - String contendo a data de acordo com o formato      <BR>
    *                  especificado                                        <BR>
    */
    public static String converterDataString(Date data, String mascara) {
   	if (data != null && mascara != null){ 
   		SimpleDateFormat formatoData = new SimpleDateFormat(mascara);
       	return formatoData.format(data);
   	} else return "";
   	
    }
    
   /**
    * Converte uma data, do tipo java.sql.Date, para o formato especificado
    * pela máscara informada.                                              <BR>
    *                                                                      <BR>
    * @param mascara - Máscara para o retorno da data                      <BR>
    * @param data - Data que será formatada                                <BR>
    *                                                                      <BR>
    *   Ano com 4 dígitos - yyyy                                           <BR>
    *   Ano com 2 dígitos - yy                                             <BR>
    *   Mês - MM                                                           <BR>
    *   Dia - dd                                                           <BR>
    *   Hora  (00 - 23) - HH                                               <BR>
    *   Hora (1 - 12) - hh                                                 <BR>
    *   Minuto - mm                                                        <BR>
    *   Segundo - ss                                                       <BR>
    *                                                                      <BR>
    *  Exemplo para máscaras             Retorno                           <BR>
    *     dd-MM-yyyy                    09-11-2001                         <BR>
    *     dd/MM/yyyy                    09/11/2001                         <BR>
    *     yyyy-MM-dd                    2001-11-09                         <BR>
    *     yyyy-MM-dd  HH:mm:ss          2001-11-09 15:25:10                <BR>
    *                                                                      <BR>
    * @return String - String contendo a data de acordo com o formato      <BR>
    *                  especificado                                        <BR>
    */
    public static String converterSQLDataString(java.sql.Date data,
           String mascara) {
       SimpleDateFormat formatoData = new SimpleDateFormat(mascara);
       return formatoData.format(data);
    }
    
   /**
    * Verifica se uma data, no formato String, é válida, considerando o
    * formato especificado pela máscara informada também como String       <BR>
    *                                                                      <BR>
    * @param data - Data que será validada                                 <BR>
    * @param mascara - Máscara para a validação da data                    <BR>
    *                                                                      <BR>
    *   Ano com 4 dígitos - yyyy                                           <BR>
    *   Ano com 2 dígitos - yy                                             <BR>
    *   Mês - MM                                                           <BR>
    *   Dia - dd                                                           <BR>
    *   Hora  (00 - 23) - HH                                               <BR>
    *   Hora (1 - 12) - hh                                                 <BR>
    *   Minuto - mm                                                        <BR>
    *   Segundo - ss                                                       <BR>
    *                                                                      <BR>
    *  Exemplo para máscaras             Retorno                           <BR>
    *     dd-MM-yyyy                    09-11-2001                         <BR>
    *     dd/MM/yyyy                    09/11/2001                         <BR>
    *     yyyy-MM-dd                    2001-11-09                         <BR>
    *     yyyy-MM-dd  HH:mm:ss          2001-11-09 15:25:10                <BR>
    *                                                                      <BR>
    * @return boolean - boolean contendo o resultado de se a data é ou não <BR>
    *                   válida                                             <BR>
    */
    public static boolean isDataValida(String data, String mascara) {
       boolean dataValida = false;
       DateFormat formatoData = new SimpleDateFormat(mascara);
       
       // faz com que o SimpleDateFormat passe a lançar ParseException quando a data não for válida
       formatoData.setLenient(false);

       try {
           if (data.length() == DATA_SIZE){
               formatoData.parse(data);
               dataValida = true;
           }
       } catch (ParseException e) {
           return dataValida;
       }
       return dataValida;
    }
    
   /**
    * Verifica se um dado período é válido, ou seja, se sua data inicial   <BR>
    * é maior ou igual do que sua data final, ambas do tipo String e especificadas  <BR>
    * pela máscara informada                                               <BR>
    *                                                                      <BR>
    * @param dataInicial - Data inicial                                    <BR>
    * @param dataFinal - Data final                                        <BR>
    * @param mascara - Máscara para as datas                               <BR>
    *                                                                      <BR>
    *   Ano com 4 dígitos - yyyy                                           <BR>
    *   Ano com 2 dígitos - yy                                             <BR>
    *   Mês - MM                                                           <BR>
    *   Dia - dd                                                           <BR>
    *   Hora  (00 - 23) - HH                                               <BR>
    *   Hora (1 - 12) - hh                                                 <BR>
    *   Minuto - mm                                                        <BR>
    *   Segundo - ss                                                       <BR>
    *                                                                      <BR>
    *  Exemplo para máscaras             Retorno                           <BR>
    *     dd-MM-yyyy                    09-11-2001                         <BR>
    *     dd/MM/yyyy                    09/11/2001                         <BR>
    *     yyyy-MM-dd                    2001-11-09                         <BR>
    *     yyyy-MM-dd  HH:mm:ss          2001-11-09 15:25:10                <BR>
    *                                                                      <BR>
    * @return boolean - boolean contendo true se a dataFinal for           <BR>
    * maior do que a dataInicial                                           <BR>
    */
    public static boolean isPeriodoValido(String dataInicial, String dataFinal, String mascara) {
       DateFormat formatoData = new SimpleDateFormat(mascara);
       
       // faz com que o SimpleDateFormat passe a lançar ParseException quando a data não for válida
       formatoData.setLenient(false);
       GregorianCalendar data01 = new GregorianCalendar();
       GregorianCalendar data02 = new GregorianCalendar();

       try {
           data01.setTime(formatoData.parse(dataInicial));
           data02.setTime(formatoData.parse(dataFinal));
       } catch (ParseException e) {
           // TODO
       }
       if (data02.getTime().getTime() - data01.getTime().getTime() >= 0)
           return true;
       return false;
    }
    
	public static boolean isPeriodoValido(Date dataInicio, Date dataFim) {
		 return Formatador.isPeriodoValido(Formatador.converterDataString(dataInicio, "dd/MM/yyyy"),
						Formatador.converterDataString(dataFim, "dd/MM/yyyy"), "dd/MM/yyyy");
	}
    
   /**
    * Retorna a diferença, em dias, entre duas datas                       <BR>
    *                                                                      <BR>
    * @param dataInicial - Data inicial                                    <BR>
    * @param dataFinal - Data final                                        <BR>
    * @param mascara - Máscara para as datas                               <BR>
    *                                                                      <BR>
    *   Ano com 4 dígitos - yyyy                                           <BR>
    *   Ano com 2 dígitos - yy                                             <BR>
    *   Mês - MM                                                           <BR>
    *   Dia - dd                                                           <BR>
    *   Hora  (00 - 23) - HH                                               <BR>
    *   Hora (1 - 12) - hh                                                 <BR>
    *   Minuto - mm                                                        <BR>
    *   Segundo - ss                                                       <BR>
    *                                                                      <BR>
    *  Exemplo para máscaras             Retorno                           <BR>
    *     dd-MM-yyyy                    09-11-2001                         <BR>
    *     dd/MM/yyyy                    09/11/2001                         <BR>
    *     yyyy-MM-dd                    2001-11-09                         <BR>
    *     yyyy-MM-dd  HH:mm:ss          2001-11-09 15:25:10                <BR>
    *                                                                      <BR>
    * @return int - int contendo o diferença entre as duas datas         <BR>
    */
    public static int diferencaEntreDatas(String dataInicial, String dataFinal, String mascara) {
       DateFormat formatoData = new SimpleDateFormat(mascara);
       
       // faz com que o SimpleDateFormat passe a lançar ParseException quando a data não for válida
       formatoData.setLenient(false);
       GregorianCalendar data01 = new GregorianCalendar();
       GregorianCalendar data02 = new GregorianCalendar();

       try {
           data01.setTime(formatoData.parse(dataInicial));
           data02.setTime(formatoData.parse(dataFinal));
       } catch (ParseException e) {
           e.printStackTrace();
       }
       
       String diferenca = String.valueOf((data02.getTime().getTime() - data01.getTime().getTime()) / MILISEGUNDOS_DIA);
       return (Integer.parseInt(diferenca));
    }
    
   /**
    * Retorna a diferença, em dias, entre duas datas                       <BR>
    *                                                                      <BR>
    * @param dataInicial - Data inicial                                    <BR>
    * @param dataFinal - Data final                                        <BR>
    * @param mascara - Máscara para as datas                               <BR>
    *                                                                      <BR>
    *   Ano com 4 dígitos - yyyy                                           <BR>
    *   Ano com 2 dígitos - yy                                             <BR>
    *   Mês - MM                                                           <BR>
    *   Dia - dd                                                           <BR>
    *   Hora  (00 - 23) - HH                                               <BR>
    *   Hora (1 - 12) - hh                                                 <BR>
    *   Minuto - mm                                                        <BR>
    *   Segundo - ss                                                       <BR>
    *                                                                      <BR>
    *  Exemplo para máscaras             Retorno                           <BR>
    *     dd-MM-yyyy                    09-11-2001                         <BR>
    *     dd/MM/yyyy                    09/11/2001                         <BR>
    *     yyyy-MM-dd                    2001-11-09                         <BR>
    *     yyyy-MM-dd  HH:mm:ss          2001-11-09 15:25:10                <BR>
    *                                                                      <BR>
    * @return int - int contendo o diferença entre as duas datas         <BR>
    */
    public static int diferencaEntreDatas(Date dataInicial, Date dataFinal) {	
		GregorianCalendar data01 = new GregorianCalendar();
		GregorianCalendar data02 = new GregorianCalendar();	
		data01.setTime(dataInicial);
		data02.setTime(dataFinal);	
		
		String diferenca = String.valueOf((dataFinal.getTime() - dataInicial.getTime()) / MILISEGUNDOS_DIA);
		return (Integer.parseInt(diferenca));
    }
    
    public static Date diferencaEmDiasEntreDatas(Date dataInicial, int diferenca) throws ParseException{
		
		Long dataFinal = dataInicial.getTime() + (diferenca*MILISEGUNDOS_DIA)   ; 		
 		Date data = new Date(dataFinal);
	 				
		return data;
    }

   /**
    * 
    * Objetivo: Retornar a hora atual no formato HH:mm:ss 
    * Autor:    d332011 - Antonio Jaime
    * Data:     29/11/2007
    * @return A hora atual no formato HH:mm:ss 
    */ 
	public static String getHoraAtual() {
		return converterDataString(new Date(), FORMATO_HORA_PADRAO);
	} 
    
   /**
    * 
    * Objetivo: Retornar a data atual no formato dd/MM/yyyy 
    * Autor:    d332011 - Antonio Jaime
    * Data:     29/11/2007
    * @return A hora atual no formato dd/MM/yyyy 
    */ 
	public static String getDataAtual() {
		return converterDataString(new Date(), FORMATO_DATA_PADRAO);
	} 	
	
	   /**
    * 
    * Objetivo: Retornar a data atual no formato extenso
    * Autor:    d332011 - Antonio Jaime
    * Data:     29/11/2007
    * @return A hora atual no formato dd/MM/yyyy 
    */ 
	public static String getDataAtual2() {
		
		String dataAtual = getDataAtual();
		
		String dia = dataAtual.substring(0, 2);
		String mesNumero = dataAtual.substring(3, 5);
		String ano = dataAtual.substring(6, 10);
		
		String mesNome="";
		
		if(mesNumero.equals("01"))
			mesNome="janeiro";
		else
			if(mesNumero.equals("02"))
				mesNome="fevereiro";
			else
				if(mesNumero.equals("03"))
					mesNome="março";
				else
					if(mesNumero.equals("04"))
						mesNome="abril";
					else
						if(mesNumero.equals("05"))
							mesNome="maio";
						else
							if(mesNumero.equals("06"))
								mesNome="junho";
							else
								if(mesNumero.equals("07"))
									mesNome="julho";
								else
									if(mesNumero.equals("08"))
										mesNome="agosto";
									else
										if(mesNumero.equals("09"))
											mesNome="setembro";
										else
											if(mesNumero.equals("10"))
												mesNome="outubro";
											else
												if(mesNumero.equals("11"))
													mesNome="novembro";
												else
													if(mesNumero.equals("12"))
														mesNome="dezembro";
		
		return dia + ", " + mesNome + " de " + ano;
	}

	public static String getDataFormatada(Date data) {
		if (data == null)
			return null;
		return converterDataString(data, FORMATO_DATA_PADRAO);
	}

	public static String formatNumeroBR(Double numero) {
		return format(numero, FORMATO_NUMERO_PADRAO);
	}
	
	public static Date addDias(Date date, int dias) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, dias);

		return calendar.getTime();

	}   

	public static Date addMeses(Date date, int meses) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, meses);

		return calendar.getTime();

	} 
	
	
	public static java.sql.Date getDataJDBC() {
		//05/11/2006 > 2006-11-05
		String data = getDataAtual();
		return java.sql.Date.valueOf(data.substring(6,10)+"-"+data.substring(3,5)+"-"+data.substring(0,2));
		}

		public static java.sql.Date getDataJDBC(String data) {
		//05/11/2006 > 2006-11-05
		if ((data == null) || (data != null && !validaData(data)))
		return null;
		return java.sql.Date.valueOf(data.substring(6,10)+"-"+data.substring(3,5)+"-"+data.substring(0,2));
		}

		public static boolean validaData(String data){
		try {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		df.setLenient(false);
		df.parse(data);
		return true;
		} catch (Exception e) {
		return false;
		}
		}

		public static double getDouble(BigDecimal valor) {
			if (valor == null)
				return 0.0;
			return valor.doubleValue();
		}

		public static BigDecimal getValorMonetarioValido(String valor) {
				return new BigDecimal(Formatador.parseDouble(valor));			
		}

		public static BigDecimal getBigDecimal(String valor) {
			if (valor == null
					|| (valor != null && Constantes.VAZIO.equalsIgnoreCase(valor)))
				return new BigDecimal(Formatador.parseDouble("0"));
			else
				return new BigDecimal(Formatador.parseDouble(valor));
		}

		public static String converteEmCaminhoReal(
				String path, ActionServlet servlet) {
			return servlet.getServletContext().getRealPath(path);
		}
		
		public static double arredondaDouble(double valor){
			return ( Math.round( (valor * 100.0) ) ) / 100.0;
		}

		public static String currencyQuatroCasas(String NUMBER) {
			return currency(parseDouble(NUMBER));			
		}

		public static String currencyQuatroCasas(Double NUMBER) {
			String number = format(NUMBER, "#,##0.0000");
			return "R$ " + number;
		}
}
