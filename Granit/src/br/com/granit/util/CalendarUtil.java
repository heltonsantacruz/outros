package br.com.granit.util;

import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {

	private int mes;
	private int ano;
	
	private Date dtInicioSemana1;
	private Date dtFimSemana1;
	private Date dtInicioSemana2;
	private Date dtFimSemana2;
	private Date dtInicioSemana3;
	private Date dtFimSemana3;
	private Date dtInicioSemana4;
	private Date dtFimSemana4;
	private Date dtInicioMes;
	private Date dtFimMes;
	
	private static CalendarUtil instance = null;
	
	private CalendarUtil(){		
	}
	
	public static CalendarUtil getInstance(){
		if (instance == null){
			instance = new CalendarUtil();
		}
		return instance;
	}
	
	public Date getDataInicioMes() {
		return dtInicioMes;
	}

	public void setDtInicioMes(Date dtInicioMes) {
		this.dtInicioMes = dtInicioMes;
	}

	public Date getDataFimMes() {
		return dtFimMes;
	}

	public void setDtFimMes(Date dtFimMes) {
		this.dtFimMes = dtFimMes;
	}
	
	public Date getDataInicioSemana(int semana){
		switch (semana) {
		case 1:
			return dtInicioSemana1;
		case 2:
			return dtInicioSemana2;
		case 3:
			return dtInicioSemana3;
		case 4:
			return dtInicioSemana4;
		default:
			return null;
		}
	}
	
	public Date getDataFimSemana(int semana){
		switch (semana) {
		case 1:
			return dtFimSemana1;
		case 2:
			return dtFimSemana2;
		case 3:
			return dtFimSemana3;
		case 4:
			return dtFimSemana4;
		default:
			return null;
		}
	}

	public void defineMesAno(int mes, int ano){
		this.mes = mes;
		this.ano = ano;
		calculaDatasDoMes();
	}
	
	private void calculaDatasDoMes(){
		//Semana 1
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, ano);  
		cal.set(Calendar.MONTH, mes-1);  
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));  
		cal.set(Calendar.HOUR_OF_DAY, 0);  
		cal.set(Calendar.MINUTE, 0);  
		cal.set(Calendar.SECOND, 0);  
		cal.set(Calendar.MILLISECOND, 0);
		this.dtInicioSemana1 = cal.getTime();
		this.dtInicioMes = cal.getTime();
		
		cal.add(Calendar.WEEK_OF_MONTH, 1);		
		cal.add(Calendar.SECOND, -1);
		this.dtFimSemana1 = cal.getTime();
		
		//Semana 2
		cal.add(Calendar.SECOND, 1);
		this.dtInicioSemana2 = cal.getTime();
		cal.add(Calendar.WEEK_OF_MONTH, 1);		
		cal.add(Calendar.SECOND, -1);
		this.dtFimSemana2 = cal.getTime();
		
		//Semana 3
		cal.add(Calendar.SECOND, 1);
		this.dtInicioSemana3 = cal.getTime();
		cal.add(Calendar.WEEK_OF_MONTH, 1);		
		cal.add(Calendar.SECOND, -1);
		this.dtFimSemana3 = cal.getTime();		
		
		//Semana 4
		cal.add(Calendar.SECOND, 1);
		this.dtInicioSemana4 = cal.getTime();
		cal = Calendar.getInstance();  
		cal.setTime(new Date());  
		cal.set(Calendar.YEAR, ano);  
		cal.set(Calendar.MONTH, mes-1);  
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));  
		cal.set(Calendar.HOUR_OF_DAY, 23);  
		cal.set(Calendar.MINUTE, 59);  
		cal.set(Calendar.SECOND, 59);  
		cal.set(Calendar.MILLISECOND, 999);  		
		this.dtFimSemana4 = cal.getTime();
		this.dtFimMes = cal.getTime();
	}
	
	public int getMes() {
		return mes;
	}

	public int getAno() {
		return ano;
	}

	public Date getDtInicioSemana1() {
		return dtInicioSemana1;
	}

	public Date getDtFimSemana1() {
		return dtFimSemana1;
	}

	public Date getDtInicioSemana2() {
		return dtInicioSemana2;
	}

	public Date getDtFimSemana2() {
		return dtFimSemana2;
	}

	public Date getDtInicioSemana3() {
		return dtInicioSemana3;
	}

	public Date getDtFimSemana3() {
		return dtFimSemana3;
	}

	public Date getDtInicioSemana4() {
		return dtInicioSemana4;
	}

	public Date getDtFimSemana4() {
		return dtFimSemana4;
	}

	public Date getDtInicioMes() {
		return dtInicioMes;
	}

	public Date getDtFimMes() {
		return dtFimMes;
	}
}
