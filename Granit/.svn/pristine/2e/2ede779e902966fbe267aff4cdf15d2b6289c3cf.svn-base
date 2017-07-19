//Faz a validacao de data, retorna false se a data estiver incorreta e true se estiver correta*************************


function examinaData(dia,mes,ano)
	{
		alert('Validando a data');
		var Data = new Date();			//Data atual
		var anoHoje = Data.getYear();	//Ano atual

		//O if abaixo conserta um problema que dá em alguns compiladores da Netscape
		//onde o ano 2000 retorna como 100 e não 2000 (Bug do ano 2000!)
		if (parseFloat(anoHoje) < 2000)
		{
			anoHoje = parseFloat(anoHoje) + 1900;
		}
		var v_dia;
		var v_mes;
		var v_ano;
		v_dia = dia;
		v_mes = mes;
		v_ano = ano;


		tamanho_v_dia = v_dia.length;
		teste_ponto = "false";

		if (tamanho_v_dia != 2) return false;


		if (isNaN(v_dia)) //valor digitado não é numérico
		{
			return false;
		}
		else //valor digitado é um numérico válido
		{

			for (k = 0; k < tamanho_v_dia; k++)
			{if ((v_dia.charAt(k) == '.') || (v_dia.charAt(k) == '-') || (v_dia.charAt(k) == '+'))
				{
					teste_ponto = "true"; /*existe caracter ponto*/
				}
			}

			if (teste_ponto == "true") //encontrou caracter ponto(numero real)
			{
				return false;
			}

		}



		tamanho_v_mes = v_mes.length;
		teste_ponto = "false";

		if (tamanho_v_mes != 2) return false;


		if (isNaN(v_mes)) //valor digitado não é numérico
		{
			return false;
		}
		else //valor digitado é um numérico válido
		{

			for (k = 0; k < tamanho_v_mes; k++)
			{if ((v_mes.charAt(k) == '.') || (v_mes.charAt(k) == '-') || (v_mes.charAt(k) == '+'))
				{
					teste_ponto = "true"; /*existe caracter ponto*/
				}
			}

			if (teste_ponto == "true") //encontrou caracter ponto(numero real)
			{
				return false;
			}

		}



		tamanho_v_ano = v_ano.length;
		teste_ponto = "false";

		if (tamanho_v_ano != 4) return false;


		if (isNaN(v_ano)) //valor digitado não é numérico
		{
			return false;
		}
		else //valor digitado é um numérico válido
		{

			for (k = 0; k < tamanho_v_ano; k++)
			{if ((v_ano.charAt(k) == '.') || (v_ano.charAt(k) == '-') || (v_ano.charAt(k) == '+'))
				{
					teste_ponto = "true"; /*existe caracter ponto*/
				}
			}

			if (teste_ponto == "true") //encontrou caracter ponto(numero real)
			{
				return false;
			}

		}


/*Alterado para permitir qualquer ano
		if (((parseFloat(v_ano) < 1964) || (parseFloat(v_ano) > parseFloat(anoHoje))) && (v_ano.length != 0))
		{
			return(false);
		}
*/
		if (parseFloat(v_ano) < 1900)
		{
			return(false);
		}

		if (v_dia > 31 || v_dia < 1)
		{
			return(false);
		}

		if (v_mes > 12 || v_mes < 1)
		{
			return(false);
		}

		if (v_dia == "31")
		{
			if ((v_mes == "04") || (v_mes == "06") || (v_mes == "09") || (v_mes == "11"))
			{
				return(false);
			}
		}


		if (v_mes == "02")
		{
			if (!(v_ano%4))
			{
				if (v_dia > 29)
				{
					return(false);
				}
			}
			else if (v_dia > 28)
			{
				return(false);
			}
		}

		//o -if- abaixo testa se algum campo foi preenchido e outro deixado em branco deixando a data incompleta

		if (((v_dia != "") || (v_mes != "") || (v_ano != "")) && ((v_dia == "") || (v_mes == "") || (v_ano == "")))
		{
			return(false);
		}

		return(true);
	}

	function validaData(data)
	{
		if (data == ""){
		   return false;
		}
		
		var dia = "";
		var mes = "";
		var ano = "";
		if(data.indexOf("/") > 0)
		{
			dia = data.substring(0,data.indexOf("/"));
			data = data.substring(data.indexOf("/")+1);
		}
		else
			return false;
		if(data.indexOf("/") > 0)
		{
			mes = data.substring(0,data.indexOf("/"));
			ano = data.substring(data.indexOf("/")+1);
		}
		else
			return false;
		return examinaData(dia,mes,ano);

	}
	
	/**
	 * Verifica se a data inicial eh menor ou igual a data final, 
	 * retornando true se estiver correto.
	 * Caso contrario, ja exibe a mensagem e retorna false
	 */
	function checkInterval(fieldStart, fieldEnd) {
		var dateStart = toDate(fieldStart.value);
		var dateEnd = toDate(fieldEnd.value);

		dateStart.setHours(0);
		dateStart.setMinutes(0);
		dateStart.setSeconds(0);
		dateEnd.setHours(0);
		dateEnd.setMinutes(0);
		dateEnd.setSeconds(0);

		if ( dateStart.getTime() > dateEnd.getTime() ) {
			alert("Data inicial maior que a data final.");
			
			fieldStart.focus();
			fieldStart.select();
			
			return false;
		}
		
		return true;
	}

	
	/** Verifica se a data inicial eh menor ou igual a data final, 
	 * retornando true se estiver correto.
	 *
	 */
	function checaIntervalo(fieldStart, fieldEnd) {
		var dateStart = toDate(fieldStart.value);
		var dateEnd = toDate(fieldEnd.value);

		dateStart.setHours(0);
		dateStart.setMinutes(0);
		dateStart.setSeconds(0);
		dateEnd.setHours(0);
		dateEnd.setMinutes(0);
		dateEnd.setSeconds(0);

		if ( dateStart.getTime() > dateEnd.getTime() ) {
			
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * Converte uma data no formato dd/MM/yyyy para um objeto Date
	 */
	function toDate(txtDate) {
		var changeFormatDate = txtDate.substring(3, 5) 
			+ "/" + txtDate.substring(0, 2) 
			+ "/" + txtDate.substring(6, 10);
		return new Date(changeFormatDate);
	}

	/**
	 * Retorna a diferença (em módulo) de duas datas em dias
	 */
	function daysBetween(txtDateStart, txtDateEnd) {
		var DSTAdjust = 0;
		oneMinute = 1000*60;
		
		var oneDay = oneMinute * 60 * 24;
		
		//Converte datas
		date1 = toDate(txtDateStart);
		date2 = toDate(txtDateEnd);
		
		//equaliza horas caso as datas possuem estes campos
		date1.setHours(0);
		date1.setMinutes(0);
		date1.setSeconds(0);
		date2.setHours(0);
		date2.setMinutes(0);
		date2.setSeconds(0);
		
		if ( date2.getTime() > date1.getTime() ) {
			DSTAdjust = (date2.getTimezoneOffset() - date1.getTimezoneOffset()) * oneMinute;
		} else {
			DSTAdjust = (date1.getTimezoneOffset() - date2.getTimezoneOffset()) * oneMinute;
		}
		
		var diff = Math.abs(date2.getTime() - date1.getTime()) - DSTAdjust;
		
		return Math.ceil(diff/oneDay)+1;
	}
	
	function validaCampoData(objeto){
	if (f_EhData(objeto.value) == false){
		alert('Data inválida!');
		objeto.value = "";
		objeto.focus();
		objeto.select();
		return false;
	}
}


function f_EhData(valor){
	var vDia, vMes, vAno, vNDia, vNMes, vNAno, vNData;

	if (valor.length==0){return true;}
	if (valor.length == 7){
		valor = "01/"+ valor;
	}
	if (valor.length!=10){return false;}
	if (valor.charAt(2) != "/" || valor.charAt(5) != "/"){
		return false;
	}
	vDia = valor.substr(0,2);
	vMes = valor.substr(3,2);
	vAno = valor.substr(6,4);
	vDia = parseInt(vDia, 10);
	vMes = parseInt(vMes, 10);
	vAno = parseInt(vAno, 10);

	vNData = new Date()
	vNData.setFullYear(vAno,vMes-1,vDia);
	vNDia = vNData.getDate();
	vNMes = vNData.getMonth()+1;
	vNAno = vNData.getFullYear();
	vNDia = parseInt(vNDia, 10);
	vNMes = parseInt(vNMes, 10);
	vNAno = parseInt(vNAno, 10);				
	return ((vDia==vNDia) && (vMes==vNMes) && (vAno==vNAno));
}
	