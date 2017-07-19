// Fun��o.....: f_ValidaForm
// Objetivo...: Validar o Formul�rio com exce��o dos campos definidos pelo programador.
// Entrada....: nomeForm: nome do formul�rio, passado como uma string. Ex: "form1"
//				mensagemObrigatorio: mensagem de campo obrigat�rio
//				CamposN�oValidados	<- nome dos campos n�o validados, pode conter quantos campos necess�rio, o nome do campo
//										� passado como uma string. Ex: "txtNome"
// Exemplo....: f_ValidaForm("form1", "Campo Obrigat�rio", "txtNome")
// Autor......: Adriano Pamplona
// Scripts....: Global.f_EhVazio
function f_ValidaForm(nomeForm, mensagemObrigatorio){
	oForm = f_GetObject(nomeForm)
	
	for (var i = 0; i < oForm.length; i++){
		var bEParaValidar = true;
		for (var j = 2; j < f_ValidaForm.arguments.length; j++){
			var sNomeNaoValidar = f_ValidaForm.arguments[j];
			if (sNomeNaoValidar == oForm[i].name){
				bEParaValidar = false;
			}
		}

		if (bEParaValidar == true){
			if (oForm[i].type == "text" || 
					oForm[i].type == "textarea" ||
						oForm[i].type == "password" ||
							oForm[i].type == "file" ||
								oForm[i].type == "select-one" ||
									oForm[i].type == "select-multiple"){
				if (oForm[i].style.display == ""){
					var sValor = f_EhVazio(oForm[i].value)
					if (sValor == true){
						alert(mensagemObrigatorio);
						oForm[i].value = "";
						oForm[i].focus();
						return false;
					}
				}
			}
			if (oForm[i].type == "radio" || oForm[i].type == "checkbox"){
				if (oForm[i].style.display == ""){
					var sNome = oForm[i].name;
					var bTodosDesmarcados = true;
					for (var k = 0; k < oForm.length; k++){
						if (oForm[k].type == "radio" && oForm[k].name == sNome){
							if (oForm[k].checked == true){
								bTodosDesmarcados = false;
							}
						}
						if (oForm[k].type == "checkbox" && oForm[k].name == sNome){
							if (oForm[k].checked == true){
								bTodosDesmarcados = false;
							}
						}							
					}
									
					if (bTodosDesmarcados == true){
						alert(mensagemObrigatorio);
						oForm[i].value = "";
						oForm[i].focus();
						return false;						
					}
				}
			}	
		}
	}
	return true;
}

//#########################################################
// IN�CIO MASCARA
//#########################################################
// Fun��o.....: f_MascaraData
// Objetivo...: Permitir que a m�scara de data seja aplicada durante a digita��o.
// Entrada....: evento: event do html
//				objeto: objeto (this)
// Exemplo....: onKeyPress="return f_MascaraData(event, this);"
//				onKeyUp="return f_MascaraData(event, this);"
//				� fundamental aplicar o script nos dois eventos.
// Autor......: Adriano Pamplona
// Scripts....:	f_KeyData, f_Replace, f_Mascara
function f_MascaraData(evento, objeto){
	if (f_KeyNumero(evento, objeto) == false){
		return false;
	}

	var pattern = "##/##/####";
	var valor = objeto.value;
	
	if (valor.length >= pattern.length){
		if (f_KeyEspecial(evento)){
			return true;
		}		
		return false;
	}
	
	valor = f_Replace(valor, "/", "");
	valor = f_Mascara(valor, pattern, null);
	objeto.value = valor;
}

// Fun��o.....: f_MascaraHora
// Objetivo...: Permitir que a m�scara de hora seja aplicada durante a digita��o.
// Entrada....: evento: event do html
//				objeto: objeto (this)
// Exemplo....: onKeyPress="return f_MascaraHora(event, this);"
//				onKeyUp="return f_MascaraHora(event, this);"
//				� fundamental aplicar o script nos dois eventos.
// Autor......: Adriano Pamplona
// Scripts....:	f_KeyData, f_Replace, f_Mascara
function f_MascaraHora(evento, objeto){
	if (f_KeyNumero(evento, objeto) == false){
		return false;
	}

	var pattern = "##:##";
	var valor = objeto.value;
	
	if (valor.length >= pattern.length){
		if (f_KeyEspecial(evento)){
			return true;
		}		
		return false;
	}
	
	valor = f_Replace(valor, ":", "");
	valor = f_Mascara(valor, pattern, null);
	objeto.value = valor;
}

// Fun��o.....: f_MascaraReal
// Objetivo...: Permitir que a m�scara de real seja aplicada durante a digita��o.
// Entrada....: evento: event do html
//				objeto: objeto (this)
// Exemplo....: onKeyPress="return f_MascaraReal(event, this);"
//				onKeyUp="return f_MascaraReal(event, this);"
//				� fundamental aplicar o script nos dois eventos.
// Autor......: Adriano Pamplona
// Scripts....:	f_KeyNumero, f_Replace, f_RetiraStringChar
function f_MascaraReal(evento, objeto){
	if (f_KeyNumero(evento) == false){
		return false;
	}

	var retorno = "";
	var valor = f_Replace(objeto.value, "[.,]", "");
	valor = f_RetiraStringChar(valor, "0", true);
	
	var indicePonto = 0;
	for (var indice = (valor.length-1); indice >= 0; indice--){
		if (retorno.length == 2){
			retorno = "," + retorno;
			indicePonto = 0;
		}
		
		if (indicePonto == 3){
			retorno = "." + retorno;
			indicePonto = 0;	
		}
		
		retorno = valor.charAt(indice) + retorno;
		indicePonto++;
	}
	
	objeto.value = retorno;
}

// Fun��o.....: f_MascaraDinheiro
// Objetivo...: Permitir que a m�scara de dinheiro seja aplicada durante a digita��o.
// Entrada....: evento: event do html
//				objeto: objeto (this)
// Exemplo....: onKeyPress="return f_MascaraDinheiro(event, this);"
//				onKeyUp="return f_MascaraDinheiro(event, this);"
//				� fundamental aplicar o script nos dois eventos.
// Autor......: Adriano Pamplona
// Scripts....:	f_KeyNumero, f_Replace, f_RetiraStringChar
function f_MascaraDinheiro(evento, objeto){
	if (f_KeyNumero(evento) == false){
		return false;
	}

	var retorno = new String("");
	var valor = f_Replace(objeto.value, "[.,]|[ ]|["+ DINHEIRO +"]", "");
	valor = f_RetiraStringChar(valor, "0", true);
	
	var indicePonto = 0;
	for (var indice = (valor.length-1); indice >= 0; indice--){
		if (retorno.length == 2){
			retorno = "," + retorno;
			indicePonto = 0;
		}
		
		if (indicePonto == 3){
			retorno = "." + retorno;
			indicePonto = 0;	
		}
		
		retorno = valor.charAt(indice) + retorno;
		indicePonto++;
	}
	
	if (retorno.length == 1){
		retorno = "0,0"+ retorno
	} else if (retorno.length == 2){
		retorno = "0,"+ retorno
	}
	
	if (retorno != ""){
		retorno = DINHEIRO +" "+ retorno;
	} else {
		retorno = DINHEIRO +" 0,00";
	}
	
	if (evento.type == "keyup"){
		if (retorno == (DINHEIRO + " 0,00")){
			retorno = "";
		}
	}
	
	objeto.value = retorno;
}

// Fun��o.....: f_MascaraInteiro
// Objetivo...: Permitir que a m�scara de inteiro seja aplicada durante a digita��o.
// Entrada....: evento: event do html
//				objeto: objeto (this)
// Exemplo....: onKeyPress="return f_MascaraInteiro(event, this);"
//				onKeyUp="return f_MascaraInteiro(event, this);"
//				� fundamental aplicar o script nos dois eventos.
// Autor......: Adriano Pamplona
// Scripts....:	f_KeyNumero, f_Replace, f_RetiraStringChar
function f_MascaraInteiro(evento, objeto){
	if (f_KeyNumero(evento) == false){
		return false;
	}

	var retorno = "";
	var valor = f_Replace(objeto.value, "[.]", "");
	valor = f_RetiraStringChar(valor, "0", true);
	
	var indicePonto = 0;
	for (var indice = (valor.length-1); indice >= 0; indice--){
		if (indicePonto == 3){
			retorno = "." + retorno;
			indicePonto = 0;	
		}
		
		retorno = valor.charAt(indice) + retorno;
		indicePonto++;
	}
	
	objeto.value = retorno;
}

// Fun��o.....: f_Numerico
// Objetivo...: Somente n�meros ser�o digitados.
// Entrada....: evento: event do html
//				objeto: objeto (this)
// Exemplo....: onKeyPress="return f_Numerico(event, this);"
//				onKeyUp="return f_Numerico(event, this);"
// Autor......: Adriano Pamplona
// Scripts....:	f_KeyNumero, f_Replace, f_RetiraStringChar
function f_Numerico(evento, objeto){
	if (f_KeyNumero(evento) == false){
		tamanho = objeto.value.length;
		for (var i = 0; i < tamanho; i++) {
			if (!parseInt(objeto.value.charAt(i))) {
				objeto.value = "";
				break;
			}
		}
		return false;
	}

	return true;
}

// Fun��o.....: f_MascaraCep
// Objetivo...: Mascara de CEP.
// Entrada....: evento: event do html
//				objeto: objeto (this)
// Exemplo....: onKeyPress="return f_MascaraCep(event, this);"
//				onKeyUp="return f_MascaraCep(event, this);"
//				� fundamental aplicar o script nos dois eventos.
// Autor......: Adriano Pamplona
// Scripts....:	f_KeyNumero, f_Replace, f_RetiraStringChar
function f_MascaraCep(evento, objeto){
	if (f_KeyNumero(evento, objeto) == false){
		return false;
	}

	var pattern = "##.###-###";
	var valor = objeto.value;
	
	if (valor.length >= pattern.length){
		if (f_KeyEspecial(evento)){
			return true;
		}	
		return false;
	}
	
	valor = f_Replace(valor, "[.-]", "");
	valor = f_Mascara(valor, pattern, null);
	objeto.value = valor;
}

// Fun��o.....: f_MascaraCpf
// Objetivo...: Permitir que a m�scara de cpf seja aplicada durante a digita��o.
// Entrada....: evento: event do html
//				objeto: objeto (this)
// Exemplo....: onKeyPress="return f_MascaraCpf(event, this);"
//				onKeyUp="return f_MascaraCpf(event, this);"
//				� fundamental aplicar o script nos dois eventos.
// Autor......: Adriano Pamplona
// Scripts....:	f_KeyData, f_Replace, f_Mascara
function f_MascaraCpf(evento, objeto){
	if (f_KeyNumero(evento, objeto) == false){
		return false;
	}

	var pattern = "###.###.###-##";
	var valor = objeto.value;
	
	if (valor.length >= pattern.length){
		if (f_KeyEspecial(evento)){
			return true;
		}		
		return false;
	}
	
	valor = f_Replace(valor, "[.-]", "");
	valor = f_Mascara(valor, pattern, null);
	objeto.value = valor;
}

// Fun��o.....: f_MascaraCnpj
// Objetivo...: Permitir que a m�scara de cpf seja aplicada durante a digita��o.
// Entrada....: evento: event do html
//				objeto: objeto (this)
// Exemplo....: onKeyPress="return f_MascaraCnpj(event, this);"
//				onKeyUp="return f_MascaraCnpj(event, this);"
//				� fundamental aplicar o script nos dois eventos.
// Autor......: Adriano Pamplona
// Scripts....:	f_KeyData, f_Replace, f_Mascara
function f_MascaraCnpj(evento, objeto){
	if (f_KeyNumero(evento, objeto) == false){
		return false;
	}

	var pattern = "##.###.###/####-##";
	var valor = objeto.value;
	
	if (valor.length >= pattern.length){
		if (f_KeyEspecial(evento)){
			return true;
		}		
		return false;
	}
	
	valor = f_Replace(valor, "[./]|[-]", "");
	valor = f_Mascara(valor, pattern, null);
	objeto.value = valor;
}

//#########################################################
// FIM MASCARA
//#########################################################

//#########################################################
// IN�CIO VALIDA��O
//#########################################################
// Fun��o.....: f_ValidaData
// Objetivo...: Verifica se o valor do objeto referenciado � uma data v�lida
// Entrada....: objeto: objeto (this)
// Exemplo....: onBlur="return f_ValidaData(this);"
// Autor......: Adriano Pamplona
// Scripts....:	f_EhData
function f_ValidaData(objeto){
	if (f_EhData(objeto.value) == false){
		alert(msgDataInvalida);
		//IE
		if (document.all != null){
			objeto.focus();
			objeto.select();
		} else {
		//Mozilla, Netscape
		//o valor � apagado porque o foco n�o retorna para o campo
			objeto.value = "";
		}
		return false;
	}
}

// Fun��o.....: f_ValidaHora
// Objetivo...: Verifica se o valor do objeto referenciado � uma hora v�lida
// Entrada....: objeto: objeto (this)
// Exemplo....: onBlur="return f_ValidaHora(this);"
// Autor......: Adriano Pamplona
// Scripts....:	f_EhHora
function f_ValidaHora(objeto){
	if (f_EhHora(objeto.value) == false){
		alert(msgHoraInvalida);
		//IE
		if (document.all != null){
			objeto.focus();
			objeto.select();
		} else {
			//Mozilla, Netscape
			//o valor � apagado porque o foco n�o retorna para o campo
			objeto.value = "";
		}
		return false;
	}
}

// Fun��o.....: f_ValidaCpf
// Objetivo...: Verifica se o valor do objeto referenciado � um cpf v�lido
// Entrada....: objeto: objeto (this)
// Exemplo....: onBlur="return f_ValidaCpf(this);"
// Autor......: Adriano Pamplona
// Scripts....:	f_EhCpf
function f_ValidaCpf(objeto){
	if (f_EhCpf(objeto.value) == false){
		alert(msgCpfInvalido);
		//IE
		if (document.all != null){
			objeto.focus();
			objeto.select();
		} else {
			//Mozilla, Netscape
			//o valor � apagado porque o foco n�o retorna para o campo
			objeto.value = "";
		}
		return false;
	}
}


// Fun��o.....: f_ValidaCnpj
// Objetivo...: Verifica se o valor do objeto referenciado � um cpf v�lido
// Entrada....: objeto: objeto (this)
// Exemplo....: onBlur="return f_ValidaCnpj(this);"
// Autor......: Adriano Pamplona
// Scripts....:	f_EhCnpj
function f_ValidaCnpj(objeto){
	if (f_EhCnpj(objeto.value) == false){
		alert(msgCnpjInvalido);
		//IE
		if (document.all != null){
			objeto.focus();
			objeto.select();
		} else {
			//Mozilla, Netscape
			//o valor � apagado porque o foco n�o retorna para o campo
			objeto.value = "";
		}
		return false;
	}
}

// Fun��o.....: f_ContaCaracter
// Objetivo...: Determina a quantidade m�xima de caracteres de um objeto.
// Entrada....: evento = event HTML
//				object = objeto HTML
//				qtd = quantidade m�xima de caracteres.				
// Exemplo....: <textarea rows="4" cols="20" onKeyPress="return f_ContaCaracter(event, this, 10);" onKeyUp="return f_ContaCaracter(event, this, 10);"></textarea>
// Autor......: Adriano Pamplona
// Scripts....:	f_GetLetra
function f_ContaCaracter(evento, object, qtd) {
	var letra = f_GetLetra(evento, false);

	if (letra == 0 || letra == 16 || (letra >= 37 && letra <= 40)){
		return true;
	}
		
	if (object == null) {
		return;
	}
	
	var qtdObject = parseInt(object.value.length, 10);
	qtd = parseInt(qtd, 10);
	
	if (qtdObject >= qtd) {
		object.value = object.value.substring(0, qtd);
		return false;
	}
	return true;
}
//#########################################################
// FIM VALIDA��O
//#########################################################