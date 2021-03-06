/****************************************
 * Title: Mascara
 * Description: Mascara global
 * Copyright:    Copyright (c) 2003
 * Company: OMEGA
 * @author ANTONIO JAIME MOREIRA DE ALMEIDA
 * @version 1.0
 ***************************************/
/*
 As seguintes conven��es devem ser utilizadas:

	- Para bloquear a entrada somente para digitos deve-se formatar a mascara utilizando '9'
			Ex: 99/99/9999  formatando datas
	- Para bloquear a entrada somente para letras e digitos deve-se formatar a mascara utilizando '#'
			Ex: ###.###
	- A quantidade de caracteres permitida ser� limitada pela propriedade maxlength e pela mascara especificada
	- Pode-se especificar mais de uma mascara. Neste caso, utiliza-se o operador || para separar as mascaras
	- A fun��o ir� ordenar as mascaras de acordo com o tamanho delas. A primeira mascara a ser utilizada ser� a menor
	  passando para as outras quando a entrada for maior que o tamanho da mascara atual.
	- Deve-se utilizar colchetes para especificar caracteres de repeti��o
			Ex: [###.]###,##    formatando valores

	Segue alguns exemplos das mascaras mais comuns:
      - (###)               --> DDD.  
      - ##                  --> N�mero inteiro, ignora do 3 em diante      
	  - ###-####||####-#### --> Telefone de 7 ou 8 digitos<br>
      - ##/##/####          --> Data com dia, mes e ano
      - ##/##               --> Data com dia, mes
      - ##:##:##            --> Hor�rio com: hora, minuto, segundo
      - ##:##               --> Hor�rio com: hora, minuto
      - ####/##/##          --> Data (invertida) ano, mes, dia
      - ##.###-###          --> Cep
      - ###.###.###-##      --> Cpf
      - ##.###.###/####-##  --> Cgc
      - ###.###.###-##||##.###.###/####-## -->Cpf (pondendo ser cgc tamb�m)           
      - [###.]###           --> N�mero inteiro (que pode variar)
      - [###.]###,##        --> Dinheiro (que pode variar)<br>
*/


//Ponto de partida para as outras functions
function mascara(obj,mascara){		
	var texto = obj.value;
	var max = obj.maxLength;
	
	if(limparEntrada(obj).length > max-1){
		event.keyCode = 0;
		return;
	}
		
	//Default
	if( buscaDigito(mascara,'#') != -1){//Utiliza Lazanha		
		mascaraLazanha(obj,mascara);
	}
	else if( buscaDigito(mascara,'9') != -1){//Utiliza Digito	
		mascaraDigito(obj,substituiPorLazanha(mascara));
	}
	return;
}	

function mascara_cnpj(campo)
	{
		if ((event.keyCode >= 47 && event.keyCode <= 57) || (event.keyCode >= 96 && event.keyCode <= 105)){
		}else{
			event.keyCode = 0;
			return "";
		}
		cnpj = campo.value;
		if(cnpj.length == 2)
		{
			cnpj = cnpj + ".";
		}
		else if(cnpj.length == 6)
		{
			cnpj = cnpj + ".";
		}	
		else if(cnpj.length == 10)
		{
			cnpj = cnpj + "/";
		}	
		else if(cnpj.length == 15)
		{
			cnpj =cnpj + "-";
		}
		campo.value = cnpj;
	}


//Fun��o que coloca as mascaras
function mascarar(obj,mascara){
	
		var valor_duplicar = "";
		var valor_completar = "";
		var colchete_abre = abriuColchete(mascara);
		var colchete_fecha = fechouColchete(mascara)
		var retorno = "";
		var tam_lazanha = contaLazanha(mascara);		
		var texto = obj.value+retornaValor(event);
		event.keyCode = 0;
		if(colchete_abre ==-1 && texto.length > tam_lazanha){			
			texto = obj.value;			
		}

		mascara = limpaMascara(mascara);
				
		//aumenta a mascara quando existem colchetes
		if(colchete_abre < colchete_fecha  && colchete_abre!=-1 && colchete_fecha!=-1){
			var tamanhoDoTexto = 1+obj.value.length;			
			valor_duplicar = mascara.substring(colchete_abre+1,colchete_fecha);
			valor_completar = mascara.substring(colchete_fecha+1);

			//valor_completar = valor_duplicar + valor_completar;
			while(contaLazanha(valor_completar) < tamanhoDoTexto){
				valor_completar = valor_duplicar + valor_completar;				
			}			
			mascara = valor_completar;

			var j = texto.length-1;											
			for (i = mascara.length-1; i>-1 && j>-1; i--) {		
				if (mascara.substring(i,i+1)=="#") {								
					retorno = texto.substring(j,j+1) + retorno;
					j--;							
				}
				else{
					retorno = mascara.substring(i,i+1) + retorno;
				}				
			}
		}//Caso n�o tem colchetes e n�o tem ||
		else if(mascara.split('||').length == 1){
			var j = 0;											
			for(i = 0 ; i<mascara.length && j<texto.length; i++) {		
				if (mascara.substring(i,i+1)=="#") {								
					retorno = retorno + texto.substring(j,j+1);
					j++;							
				}
				else{
					retorno = retorno + mascara.substring(i,i+1) ;
				}				
			}
			var f = mascara.length;
			while(mascara.substring(f-1,f)!="#"){
				retorno = retorno + mascara.substring(f-1,f);
				f--;
			}

		}//N�o tem Colchetes e Tem ||
		else if(mascara.split('||').length != 1){

			///////////Trecho de c�digo que quebra a mascara para pegar a mascara adequada////
			var array = mascara.split('||');			
			array = tamanho(array);		
			var i = 0;
			mascara = array[i];			
			while(contaLazanha(mascara) < texto.length  && i+1 < array.length){
				i=i+1;
				mascara = array[i];		
			}
			/////////////////////////////////////////////////////////////////////////////////

			var tam_lazanha = contaLazanha(mascara);
			if(texto.length > tam_lazanha){			
				texto = obj.value;			
			}	
		
			//mascarando			
			var j = 0;											
			for(i = 0 ; i<mascara.length && j<texto.length; i++) {		
				if (mascara.substring(i,i+1)=="#") {								
					retorno = retorno + texto.substring(j,j+1);
					j++;							
				}
				else{
					retorno = retorno + mascara.substring(i,i+1) ;
				}				
			}
			var f = mascara.length;
			while(mascara.substring(f-1,f)!="#"){
				retorno = retorno + mascara.substring(f-1,f);
				f--;
			}			
		}
				
		obj.value = retorno;
		return obj;
}

//Funcao que retira os espacos em branco antes e depois da mascara
function limpaMascara(mascara){
	var masc = "";

    for (i = 0; i < mascara.length; i++) {
		var sub = mascara.substring(i,i+1);
		if (sub != " "){
            masc=masc+sub;
        }
    }
	return masc;
}

//Fun��o que ordena um array, de acordo com o tamanho dos seus elementos. Ordena��o Ascendente.
function tamanho(array){
	var temp1;
	var temp2;
	for(i=0; i<array.length-1; i++){
		temp1 = array[i];
		for(j=i+1; j<array.length; j++){
			temp2 = array[j];
			if(temp2.length < temp1.length){				
				array[i] = temp2;
				array[j] = temp1;
				temp1= temp2;
				temp2= array[j]; 
			}
		}
	}	
	return array;
}

//funcao que conta quantas lazanhas tem na palavra
function contaLazanha(mascara){
	var cont_lazanha = 0;
	for (i = 0; i < mascara.length; i++){
		if (mascara.substring(i,i+1) == "#"){
			cont_lazanha++;
        }
	}
	return cont_lazanha;
}

//fun��o que deixa apenas os digitos e as letras
function limparEntrada(obj){
	var texto = "";
    for (i = 0; i < obj.value.length; i++) {
		var temp = obj.value.substring(i,i+1);
		if( validar(temp)  ){
			texto= texto+obj.value.substring(i,i+1);
        }	
    }
	return texto;
}

//retorna true quando o parametro � um digito ou uma letra
function validar(temp){
	if( !isNaN(temp) || temp=='a' || temp=='A' || temp=='b' || temp=='B' || temp=='c' || temp=='C' || temp=='d' || temp=='D' || temp=='e' || temp=='E'
			 || temp=='f' || temp=='F' || temp=='g' || temp=='G' || temp=='h' || temp=='H' || temp=='i' || temp=='I' || temp=='j' || temp=='J'
			 || temp=='k' || temp=='K' || temp=='l' || temp=='L' || temp=='m' || temp=='M' || temp=='n' || temp=='N' || temp=='o' || temp=='O'
			 || temp=='p' || temp=='P' || temp=='q' || temp=='Q' || temp=='r' || temp=='R' || temp=='s'|| temp=='S' || temp=='t' || temp=='T'
			 || temp=='u' || temp=='U' || temp=='v' || temp=='V' || temp=='x' || temp=='X' || temp=='y' || temp=='Y' || temp=='z' || temp=='Z' ){
		return true;
	}
	else{
		return false;
	}
}

//fun��o que retorna -1 caso naum encotre o digito desejado, se encontrar retorna o indice onde ele foi encontrado
function buscaDigito(valor,digito){
		indice= valor.indexOf(digito);
		return indice;
}

//Fun��o que bloqueia a entrada apenas para digito
function mascaraDigito(obj,mascara){
	if (event.keyCode <= 47 || event.keyCode >57){
		event.keyCode = 0;
		return false;
	}
	else{
		obj.value = limparEntrada(obj);
		mascarar(obj,mascara);
	}
}

//Fun��o que bloqueia a entrada apenas para digito e letras
function mascaraLazanha(obj,mascara){
	if ( !(event.keyCode > 47 && event.keyCode <58) && !(event.keyCode > 64  && event.keyCode < 91) && !(event.keyCode > 96  && event.keyCode < 123)  ){
		event.keyCode = 0;
		return false;
	}
	else{
		obj.value = limparEntrada(obj);
		mascarar(obj,mascara);
	}
	return;
}

//Funcao que retorna o indice onde foi encontrado um ]
function fechouColchete(mascara){
	var posicao = -1;

	//verificar se existe colchete abrindo e fechando
    for (i = 0; i < mascara.length; i++) {
		var sub = mascara.substring(i,i+1);
		if (sub == ']' ){
            posicao = i;
        }
    }
	return posicao;
}

//Funcao que retorna o indice onde foi encontrado um [
function abriuColchete(mascara){
	var posicao  = -1;

	//verificar se existe colchete abrindo e fechando
    for (i = 0; i < mascara.length; i++) {
		var sub = mascara.substring(i,i+1);
		if( sub == '[' ){
			posicao = i;
        }
    }
	return posicao;
}

function substituiPorLazanha(mascara){
	var masc = "";

	//verificar se existe colchete abrindo e fechando
    for (i = 0; i < mascara.length; i++) {
		var sub = mascara.substring(i,i+1);
		if( sub == '9' ){
			masc = masc+'#';
        }
		else{
			masc = masc+sub;
		}
    }
	return masc;
}


//Retorna  o valor de um evento
function retornaValor(ev){
	var temp = ev.keyCode;
	var retorno = "";

	if(temp > 64 && temp<91){
		switch(temp){
		case 65:
			retorno = 'A';
			break;
		case 66:
			retorno = 'B';
			break;
		case 67:
			retorno = 'C';
			break;
		case 68:
			retorno = 'D';
			break;
		case 69:
			retorno = 'E';
			break;
		case 70:
			retorno = 'F';
			break;
		case 71:
			retorno = 'G';
			break;
		case 72:
			retorno = 'H';
			break;
		case 73:
			retorno = 'I';
			break;
		case 74:
			retorno = 'J';
			break;
		case 75:
			retorno = 'K';
			break;
		case 76:
			retorno = 'L';
			break;
		case 77:
			retorno = 'M';
			break;
		case 78:
			retorno = 'N';
			break;
		case 79:
			retorno = 'O';
			break;
		case 80:
			retorno = 'P';
			break;
		case 81:
			retorno = 'Q';
			break;
		case 82:
			retorno = 'R';
			break;
		case 83:
			retorno = 'S';
			break;
		case 84:
			retorno = 'T';
			break;
		case 85:
			retorno = 'U';
			break;
		case 86:
			retorno = 'V';
			break;
		case 87:
			retorno = 'W';
			break;
		case 88:
			retorno = 'X';
			break;
		case 89:
			retorno = 'Y';
			break;
		case 90:
			retorno = 'Z';
			break;
		default:
			retorno = '#';
			break;
		}
	}
	else if(temp>96 && temp<123){
		
		switch (temp){
		case 97:
			retorno = 'a';
			break;
		case 98:
			retorno = 'b';
			break;
		case 99:
			retorno = 'c';
			break;
		case 100:
			retorno = 'd';
			break;
		case 101:
			retorno = 'e';
			break;
		case 102:
			retorno = 'f';
			break;
		case 103:
			retorno = 'g';
			break;
		case 104:
			retorno = 'h';
			break;
		case 105:
			retorno = 'i';
			break;
		case 106:
			retorno = 'j';
			break;
		case 107:
			retorno = 'k';
			break;
		case 108:
			retorno = 'l';
			break;
		case 109:
			retorno = 'm';
			break;
		case 110:
			retorno = 'n';
			break;
		case 111:
			retorno = 'o';
			break;
		case 112:
			retorno = 'p';
			break;
		case 113:
			retorno = 'q';
			break;
		case 114:
			retorno = 'r';
			break;
		case 115:
			retorno = 's';
			break;
		case 116:
			retorno = 't';
			break;
		case 117:
			retorno = 'u';
			break;
		case 118:
			retorno = 'v';
			break;
		case 119:
			retorno = 'w';
			break;
		case 120:
			retorno = 'x';
			break;
		case 121:
			retorno = 'y';
			break;
		case 122:
			retorno = 'z';
			break;
		default:
			retorno = '#';
			break;
		}
	}
	else if(temp>47 && temp<58){
		switch (temp){		
		case 48:
			retorno = '0';
			break;
		case 49:
			retorno = '1';
			break;
		case 50:
			retorno = '2';
			break;
		case 51:
			retorno = '3';
			break;
		case 52:
			retorno = '4';
			break;
		case 53:
			retorno = '5';
			break;
		case 54:
			retorno = '6';
			break;
		case 55:
			retorno = '7';
			break;
		case 56:
			retorno = '8';
			break;
		case 57:
			retorno = '9';
			break;
		default:
			retorno = '#';
			break;
		}
	}

	return retorno;
}

// Fun��o.....: f_bEhDataValida
// Objetivo...: Verificar se a o valor digitado � uma data v�lida.
// Entrada....: oObjeto	<- O campo a ser validado.
// Exemplo....: onBlur="f_bEhDataValida(this);"
// Autor......: Adriano Pamplona
// Alterado...: Rafael L. S�
// Scripts....:	Nenhum
function f_bEhDataValida(oObjeto){
	var vDia, vMes, vAno, vNDia, vNMes, vNAno, vNData;
	sValor = oObjeto.value;

	if (sValor.length==0){return true;}
	if (sValor.length == 7){
		sValor = "01/"+ sValor;
	}
	if (sValor.length!=10){return false;}
	if (sValor.charAt(2) != "/" || sValor.charAt(5) != "/"){
		return false;
	}
	vDia = sValor.substr(0,2);
	vMes = sValor.substr(3,2);
	vAno = sValor.substr(6,4);
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

function verifica_data_maior(valorinicial, valorfinal){
	    var diainicial = valorinicial.substring(0,2);		// pega o dia
		var mesinicial = valorinicial.substring(3,5);  		// pega o m�s
		var anoinicial = valorinicial.substring(6,10);		// pega o ano

	    var diafinal = valorfinal.substring(0,2);		// pega o dia
		var mesfinal = valorfinal.substring(3,5);  		// pega o m�s
		var anofinal = valorfinal.substring(6,10);		// pega o ano
	
		if (eval(anofinal + mesfinal + diafinal) < eval(anoinicial + mesinicial + diainicial)){
			return false;
		} else {
			return true;
		}
	}
	
	function verifica_diferenca_data(valorinicial, valorfinal) 
	{
		var diainicial = valorinicial.substring(0,2);		// pega o dia
		var mesinicial = valorinicial.substring(3,5);  		// pega o m�s
		var anoinicial = valorinicial.substring(6,10);		// pega o ano
	
		var diafinal = valorfinal.substring(0,2);		// pega o dia
		var mesfinal = valorfinal.substring(3,5);  		// pega o m�s
		var anofinal = valorfinal.substring(6,10);		// pega o ano
		
		var d1 = cal_to_jd(parseFloat(anoinicial), mesinicial, diainicial);
		var d2 = cal_to_jd(parseFloat(anofinal), mesfinal, diafinal);
				
		return eval(d2 - d1);
	}
	
function validaCNPJ(objeto){	
	CNPJ = objeto.value;    
    if (CNPJ.length < 18) {
    	alert('CNPJ inv�lido');
    	objeto.value = "";	
		objeto.focus();
    	return false;
    }    
    if ((CNPJ.charAt(2) != ".") || (CNPJ.charAt(6) != ".") || 
    	(CNPJ.charAt(10) != "/") || (CNPJ.charAt(15) != "-")){
	    if (erro.length == 0) {
	    	alert('CNPJ inv�lido');
	    	objeto.value = "";	
			objeto.focus();
    		return false;
	    }
    }
    //substituir os caracteres que n�o s�o n�meros
    if(document.layers && parseInt(navigator.appVersion) == 4){
            x = CNPJ.substring(0,2);
            x += CNPJ. substring (3,6);
            x += CNPJ. substring (7,10);
            x += CNPJ. substring (11,15);
            x += CNPJ. substring (16,18);
            CNPJ = x;
    } else {
            CNPJ = CNPJ. replace (".","");
            CNPJ = CNPJ. replace (".","");
            CNPJ = CNPJ. replace ("-","");
            CNPJ = CNPJ. replace ("/","");
    }
    var nonNumbers = /\D/;
    if (nonNumbers.test(CNPJ)){
    	alert('CNPJ inv�lido');
    	objeto.value = "";	
		objeto.focus();
    	return false;
    }
    var a = [];
    var b = new Number;
    var c = [6,5,4,3,2,9,8,7,6,5,4,3,2];
    for (i=0; i<12; i++){
            a[i] = CNPJ.charAt(i);
            b += a[i] * c[i+1];
	}
    if ((x = b % 11) < 2) { 
    	a[12] = 0 
    } 
    else { 
    	a[12] = 11-x 
    }
    b = 0;
    for (y=0; y<13; y++) {
        b += (a[y] * c[y]);
    }
    if ((x = b % 11) < 2) { a[13] = 0; } else { a[13] = 11-x; }
    if ((CNPJ.charAt(12) != a[12]) || (CNPJ.charAt(13) != a[13])){
        alert('CNPJ inv�lido');
        objeto.value = "";	
		objeto.focus();
    	return false;
    }    
    return true;
}



function verificarCpfCpnj(objeto) {	    
   	if (objeto.value != ""){
       	if (objeto.value.length != 14
    			&& objeto.value.length != 18){
       		alert("Dado Inv�lido!"); 
			objeto.value = "";	
			objeto.focus();
   	  	}else if (objeto.value.length == 14){
		   validaCPF(objeto);
		}else if (objeto.value.length == 18){
		   validaCNPJ(objeto);  
		}
	}    
}        
            
function verificarCpf(objeto) {           
   	if (objeto.value != ""){
       	if (objeto.value.length != 14){
       		alert("Dado Inv�lido!"); 
			objeto.value = "";	
			objeto.focus();
   	  	}else if (objeto.value.length == 14){
		   validaCPF(objeto);
		}
	}    
}        
            
            
function verificarCep(objeto){
  	if (objeto.value != ""){
      	if (objeto.value.length != 10){
   			alert("Dado Inv�lido!"); 
			objeto.value = "";	
			objeto.focus();
    	}
	}    
}


function limpaCampo(objeto){
  	objeto.value = "";
}		



function validaCPF(objeto){
	if (objeto.value != ""){
	var i; 
	//alert(c);
    var valor = objeto.value;
	valor = valor.replace(".","");
	valor = valor.replace(".","");
	valor = valor.replace(".","");
	valor = valor.replace("-","");
	s = valor;
	var c = s.substr(0,9); 
	var dv = s.substr(9,2); 
	var d1 = 0; 
	var v = false;

	//alert(c);

	for (i = 0; i < 9; i++) { 
		d1 += c.charAt(i)*(10-i); 
	} 
	if (d1 == 0){ 
		alert("CPF Inv�lido!")
		v = true; 
		objeto.value = "";	
		objeto.focus();
		objeto.select();
		return false; 
	} 
	d1 = 11 - (d1 % 11); 
	if (d1 > 9) d1 = 0; 
	if (dv.charAt(0) != d1) { 
		alert("CPF Inv�lido!") 
		v = true;
		objeto.value = "";	
		objeto.focus();
		objeto.select();
		return false; 
	} 

	d1 *= 2; 
	for (i = 0; i < 9; i++) { 
		d1 += c.charAt(i)*(11-i); 
	} 
	d1 = 11 - (d1 % 11); 
	if (d1 > 9) d1 = 0; 
	if (dv.charAt(1) != d1) { 
		alert("CPF Inv�lido!") 
		v = true;
		objeto.value = "";	
		objeto.focus();
		objeto.select();
		return false; 
	} 
  } else {
     return true;
  } 
 } 
