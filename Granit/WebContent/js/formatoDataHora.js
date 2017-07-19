
/**
 * Validação de datas no formato: dd/mm/aaaa
 *
 * @param data Data a ser validada.
 * @return true se for uma data válida ou false caso contrário.
 */
function validaData(data) {

	var padrao = /^\d{1,2}\/\d{1,2}\/\d{4}$/;

	if (!padrao.test(data)) {
		return false;
	}

    var idBarra1 = parseInt(data.indexOf("/"), 10);
    var idBarra2 = parseInt(data.lastIndexOf("/"), 10);

    var v_dia = parseInt(data.substring(0, idBarra1), 10);
    var v_mes = parseInt(data.substring(idBarra1 + 1, idBarra2), 10) - 1;
    var v_ano = parseInt(data.substr(idBarra2 + 1), 10);

    data = new Date(v_ano, v_mes, v_dia);

	if (v_dia != data.getDate() || v_mes != data.getMonth()
			|| v_ano != data.getFullYear()) {
		return false;
	}

    return true;
}

/**
 * Validação de mes e ano no formato: mm/aaaa
 *
 * @param data Data a ser validada.
 * @return true se for uma data válida ou false caso contrário.
 */
function validaMesAno(data) {

	data = "1/" + data;

    return validaData(data);
}

/**
 * Validação de mes
 *
 * @param mes Mes a ser validado.
 * @return true se for um mes válido ou false caso contrário.
 */
function validaMes(mes) {

    if (isNaN(mes)) {
        return false;
    }

    return (mes > 0 && mes < 13);
}

/**
 * Validação de dia e mes no formato: dd/mm.
 *
 * @param data Data a ser validada.
 * @return true se for uma data válida ou false caso contrário.
 */
function validaDiaMes(data) {

	var data1 = data + "/2000";

	if (validaData(data1)) {
		return true;
	}

	var data2 = data + "/2001";

    return validaData(data2);
}

/**
 * Validação de ano no formato: aaaa
 *
 * @param data Data a ser validada.
 * @return true se for uma data válida ou false caso contrário.
 */
function validaAno(data) {

	data = "1/1/" + data;

    return validaData(data);
}

/**
 * Validação de horario no formato hh:MM:ss
 *
 * @param valor Horário a ser validada.
 * @return true se for um Horário válido ou false caso contrário.
 */
function validaHora(valor) {

	var padrao = /^\d{1,2}:\d{1,2}:\d{1,2}$/;

	if (!padrao.test(valor)) {
		return false;
	}

	var idSeparador1 = valor.indexOf(":");
	var idSeparador2 = valor.lastIndexOf(":");

	var v_hora = parseInt(valor.substring(0, idSeparador1), 10);
	var v_minuto = parseInt(valor.substring(idSeparador1 + 1, idSeparador2), 10);
	var v_segundo = parseInt(valor.substr(idSeparador2 + 1), 10);

	var dh = new Date (2000, 1, 1, v_hora, v_minuto, v_segundo);

	if (v_hora != dh.getHours() || v_minuto != dh.getMinutes()
			|| v_segundo != dh.getSeconds()) {

		return false;
	}

	return true;
}

/**
 * Valida data/hora no formato dd/mm/aaaa hh:MM:ss
 *
 * @param texto Data/Hora a ser validada.
 * @return true se for uma Data/Hora válida ou false caso contrário.
 */
function validaDataHora(texto) {

    var data = obterDataDH(texto);
    var hora = obterHoraDH(texto);

    if (!validaData(data) || !validaHora(hora)) {
        return false;
    }

    if ((data + " " + hora) != texto) {
        return false;
    }

    return true;
}

/**
 * Mascara para Data Hora, deve ser usada no evento onKeyPress,
 * o parametro evento só deve ser passado caso a função seja executada
 * em IFRAMES <BR><BR>
 * Ex.: ... onKeyPress="this.value = mascaraDataHora(this.value)" ... NORMAL <BR>
 * Ex.: ... onKeyPress="this.value = mascaraDataHora(this.value, event)" ... IFRAME
 *
 * @param texto Texto onde vai ser aplicado a mascara.
 * @param evento? Propriedade event do objeto atual.
 */
 function mascaraDataHora (texto, evento) {

	var tamanho = texto.length;
	var ev = (evento != null)? evento: event;
	var hora = "";
	var data = "";

	if (ev.keyCode == 13) {
		return texto;
	}

	if(ev.keyCode < 48 || ev.keyCode > 57) {
		ev.keyCode = 0;
	}

	if (tamanho < 10) {
		texto = mascaraData(texto, ev);
	}else if (tamanho == 10) {
		texto += " ";
	}else {
		hora = texto.substr (11);
		hora = mascaraHora(hora, ev);
		texto = texto.substring (0,11);
	}

	return texto + hora;
}

/**
 * Mascara para Hora, deve ser usada no evento onKeyPress,
 * o parametro evento só deve ser passado caso a função seja executada
 * em IFRAMES <BR><BR>
 * Ex.: ... onKeyPress="this.value = mascaraHora(this.value)" ... NORMAL <BR>
 * Ex.: ... onKeyPress="this.value = mascaraHora(this.value, event)" ... IFRAME
 *
 * @param texto Texto onde vai ser aplicado a mascara.
 * @param evento? Propriedade event do objeto atual.
 */
function mascaraHora(texto, evento) {

	var ev = (evento != null)? evento: event;

	if (ev.keyCode == 13) {
		return texto;
	}

	if(ev.keyCode < 48 || ev.keyCode > 57) {
		ev.keyCode = 0;
	}

	if (texto.length==1) {
	    if (texto.charAt(0)>"2") {
		    ev.keyCode=0;
		} else if (texto.charAt(0)=="2" && ev.keyCode>51) {
		    ev.keyCode=0;
		}
	} else if (texto.length==2 || texto.length==3) {
	    if (ev.keyCode>53) {
		   ev.keyCode=0;
		} else {
		    if (texto.length==2)
		        texto += ":";
		}
	} else if (texto.length==5 || texto.length==6) {
	    if (ev.keyCode>53) {
		    ev.keyCode=0;
		} else {
		    if (texto.length==5)
	            texto += ":";
		}
	} else if (texto.length>=8) {
	    ev.keyCode=0;
	}

	return texto;
}

/**
 * Retorna um inteiro representando o dia e o mes passados como
 * parâmetro da seguinte forma: 18/12
 *
 * @param valor Valor Informado.
 * @return Dia mes do valor informado. Ex: 1812
 */
function retornaDiaMes(valor) {

    return valor.substring(0,2) + valor.substring(3,5);
}

/**
 * Mascara para Dia Mês, deve ser usada no evento onKeyPress,
 * o parametro evento só deve ser passado caso a função seja executada
 * em IFRAMES <BR><BR>
 * Ex.: ... onKeyPress="mascaraDiaMes(this)" ... NORMAL <BR>
 * Ex.: ... onKeyPress="mascaraDiaMes(this, event)" ... IFRAME
 *
 * @param obj Objeto onde vai ser aplicado a mascara.
 * @param evento? Propriedade event do objeto atual.
 */
function mascaraDiaMes(obj, evento) {

    var texto = obj.value;
    var tamanho = obj.value.length;
    var ev = (evento != null)? evento: event;

	if (ev.keyCode == 13) {
		return;
	}

    if (tamanho == 0) {
        if (ev.keyCode < 48 || ev.keyCode > 51) {
            ev.keyCode = 0;
        }
    }else if (tamanho == 1) {
        if ((texto.charAt(tamanho-1) == 3) && (ev.keyCode < 48 || ev.keyCode > 49)) {
            ev.keyCode = 0;
        }
        if ((texto.charAt(tamanho-1) == 0) && (ev.keyCode <= 48 || ev.keyCode > 57)) {
            ev.keyCode = 0;
        }
    }else if (tamanho == 2) {
        obj.value += "/";
        if (ev.keyCode < 48 || ev.keyCode > 49) {
            ev.keyCode = 0;
        }
    }else if (tamanho ==3) {
        if (ev.keyCode < 48 || ev.keyCode > 49) {
            ev.keyCode = 0;
        }
    }else if (tamanho == 4) {
        if ((texto.charAt(tamanho-1) == 1) && (ev.keyCode < 48 || ev.keyCode > 50)) {
            ev.keyCode = 0;
        }
    }else if (tamanho > 4 && ev.keyCode != 13) {
		ev.keyCode = 0;
    }
}


/**
 * Mascara para Data, deve ser usada no evento onKeyPress,
 * o parametro evento só deve ser passado caso a função seja executada
 * em IFRAMES <BR><BR>
 * Ex.: ... onKeyPress="this.value = mascaraData(this.value)" ... NORMAL <BR>
 * Ex.: ... onKeyPress="this.value = mascaraData(this.value, event)" ... IFRAME
 *
 * @param texto Texto onde vai ser aplicado a mascara.
 * @param evento? Propriedade event do objeto atual.
 */
function mascaraData(texto, evento) {

	var ev = (evento != null)? evento: event;

	if (ev.keyCode == 13) {
		return texto;
	}

	if(ev.keyCode < 48 || ev.keyCode > 57) {
		ev.keyCode = 0;
	}

	if (texto.length==0) {
	    if (ev.keyCode>51) {
		    ev.keyCode=0;
		}
	} else if (texto.length==1) {
	    if (texto.charAt(0)=="0" && ev.keyCode==48) {
		    ev.keyCode=0;
		}
	    if (texto.charAt(0)=="3" && ev.keyCode>49) {
		    ev.keyCode=0;
		}
	} else if (texto.length==2 || texto.length==3) {
	    if (ev.keyCode>49) {
		    ev.keyCode=0;
		} else {
		    if (texto.length==2)
		        texto += "/";
		}
	} else if (texto.length==4) {
	    if (texto.charAt(3)=="0" && ev.keyCode==48) {
		    ev.keyCode=0;
		}
		if (texto.charAt(texto.length - 1)==1 && ev.keyCode>50) {
		    ev.keyCode=0;
		}
	} else if (texto.length==5 || texto.length==6) {
	    if (ev.keyCode>50) {
		    ev.keyCode=0;
		} else {
		    if (texto.length==5)
	            texto += "/";
		}
	} else if (texto.length>=10 && ev.keyCode != 13) {
	    ev.keyCode=0;
	}

	return texto;
}

/**
 * Mascara para Dia, deve ser usada no evento onKeyPress,
 * o parametro evento só deve ser passado caso a função seja executada
 * em IFRAMES <BR><BR>
 * Ex.: ... onKeyPress="mascaraDia(mes, this)" ... NORMAL <BR>
 * Ex.: ... onKeyPress="mascaraDia(mes, this, event)" ... IFRAME
 *
 * @param mes Mes da Data atual
 * @param obj Objeto onde vai ser aplicado a mascara.
 * @param evento? Propriedade event do objeto atual.
 */
function mascaraDia(mes, obj, evento) {

    var texto = obj.value;
    var tamanho = obj.value.length;
    var ev = (evento != null)? evento: event;
    mes = (mes < 1 || mes > 12)? 1: parseInt(mes, 10);

	if (ev.keyCode == 13) {
		return;
	}

	if (ev.keyCode < 48 || ev.keyCode > 57) {
		ev.keyCode = 0;
	}

    if (tamanho == 0) {
		if (mes == 2) {
			if (ev.keyCode < 48 || ev.keyCode > 50) {
			    ev.keyCode = 0;
			}
		}
        if (ev.keyCode < 48 || ev.keyCode > 51) {
            ev.keyCode = 0;
        }
    }else if (tamanho == 1) {
		switch (mes) {
			case 1:	case 3:	case 5:
			case 7:	case 8: case 10:
			case 12:
				if ((texto.charAt(tamanho-1) == 3)
						&& (ev.keyCode < 48 || ev.keyCode > 49)) {
					ev.keyCode = 0;
				}
				break;

			case 4: case 6: case 9:
			case 11:
				if ((texto.charAt(tamanho-1) == 3)
						&& (ev.keyCode != 48)) {
					ev.keyCode = 0;
				}
				break;

			default: break;
		}
    }else if (tamanho > 1) {
		ev.keyCode = 0;
    }
}

/**
 * Mascara para Mês, deve ser usada no evento onKeyPress,
 * o parametro evento só deve ser passado caso a função seja executada
 * em IFRAMES <BR><BR>
 * Ex.: ... onKeyPress="mascaraMes(this)" ... NORMAL <BR>
 * Ex.: ... onKeyPress="mascaraMes(this, event)" ... IFRAME
 *
 * @param obj Objeto onde vai ser aplicado a mascara.
 * @param evento? Propriedade event do objeto atual.
 */
function mascaraMes(obj, evento) {

    var texto = obj.value;
    var tamanho = obj.value.length;
    var ev = (evento != null)? evento: event;

	if (ev.keyCode == 13) {
		return;
	}

    if (tamanho == 0) {
        if (ev.keyCode < 48 || ev.keyCode > 57) {
            ev.keyCode = 0;
        }
    }else if (tamanho == 1) {
        if ((texto.charAt(tamanho-1) == 1) && (ev.keyCode < 48 || ev.keyCode > 50)) {
            ev.keyCode = 0;
        }
        if ((texto.charAt(tamanho-1) == 0) && (ev.keyCode < 49 || ev.keyCode > 57)) {
            ev.keyCode = 0;
        }
        if (texto.charAt(tamanho-1) > 1) {
            ev.keyCode = 0;
        }
    }else if (tamanho > 1) {
		ev.keyCode = 0;
    }

}

/**
 * Mascara para Mês Ano, deve ser usada no evento onKeyPress,
 * o parametro evento só deve ser passado caso a função seja executada
 * em IFRAMES <BR><BR>
 * Ex.: ... onKeyPress="mascaraMesAno(this)" ... NORMAL <BR>
 * Ex.: ... onKeyPress="mascaraMesAno(this, event)" ... IFRAME
 *
 * @param obj Objeto onde vai ser aplicado a mascara.
 * @param evento? Propriedade event do objeto atual.
 */
function mascaraMesAno(obj, evento) {

    var texto = obj.value;
    var tamanho = obj.value.length;
    var ev = (evento != null)? evento: event;

	if (ev.keyCode == 13) {
		return;
	}

    if (tamanho == 0) {
        if (ev.keyCode < 48 || ev.keyCode > 49) {
            ev.keyCode = 0;
        }
    }else if (tamanho == 1) {
        if ((texto.charAt(tamanho-1) == 1) && (ev.keyCode < 48 || ev.keyCode > 50)) {
            ev.keyCode = 0;
        }
        if ((texto.charAt(tamanho-1) == 0) && (ev.keyCode < 48 || ev.keyCode > 57)) {
            ev.keyCode = 0;
        }
    }else if (tamanho == 2) {
        obj.value += "/";
        if (ev.keyCode < 48 || ev.keyCode > 57) {
            ev.keyCode = 0;
        }
	}else if (tamanho >= 3 && tamanho <= 6){
        if (ev.keyCode < 48 || ev.keyCode > 57) {
            ev.keyCode = 0;
        }
    }else if (tamanho > 6) {
		if (ev.keyCode != 13) {
			ev.keyCode = 0;
		}
	}
}

/**
 * Mascara para Ano, deve ser usada no evento onKeyPress,
 * o parametro evento só deve ser passado caso a função seja executada
 * em IFRAMES <BR><BR>
 * Ex.: ... onKeyPress="mascaraAno(this)" ... NORMAL <BR>
 * Ex.: ... onKeyPress="mascaraAno(this, event)" ... IFRAME
 *
 * @param obj Objeto onde vai ser aplicado a mascara.
 * @param evento? Propriedade event do objeto atual.
 */
function mascaraAno(obj, evento) {

    var tamanho = obj.value.length;
    var ev = (evento != null)? evento: event;

	if (ev.keyCode == 13) {
		return;
	}

	if (ev.keyCode < 48 || ev.keyCode > 57) {
		ev.keyCode = 0;
	}else {
		if (tamanho > 3) {
			ev.keyCode = 0;
		}
	}
}

/**
 * Recebe uma data no formato (dd-mm-yyyy) e retorna o ano
 *
 * @param data Data informada
 * @return Ano contido na data.
 */
function retornaAno(data) {

	return data.substr(6,4);
}

/**
 * Converte a string de data no formato yyyy-mm-dd para dd/mm/yyyy
 *
 * @param texto Data no Formato yyyy-mm-dd
 * @return Data no Formato dd/mm/yyyy
 */
function converteData(texto) {

	if (texto.length != 10) {
		return "";
	}

	var ano = texto.substr(0,4);
	var mes = texto.substr(5,2);
	var dia = texto.substr(8,2);

	return (dia + "/" + mes + "/" + ano);
}

/**
 * Converte a string de data no formato yyyy-mm-dd... para mm/yyyy
 *
 * @param texto Data no Formato yyyy-mm-dd.
 * @return Data no Formato mm/yyyy.
 */
function converteMesAno(texto) {

	var ano = texto.substr(0,4);
	var mes = texto.substr(5,2);
	var dia = texto.substr(8,2);

	return (mes + "/" + ano);
}

/**
 * Compara as duas datas (dd/mm/aaaa) passadas como parametro
 *
 * @param data1 Primeira Data Informada
 * @param data2 Segunda Data Informada
 * @return 0 - Datas iguais. <br> 1 - Primeira data maior<br>2 - Segunda data maior
 */
function comparaDatas(data1, data2) {

	var idBarra1 = data1.indexOf("/");
	var idBarra2 = data1.lastIndexOf("/");

    var v_dia = parseInt(data1.substring(0, idBarra1), 10);
    var v_mes = parseInt(data1.substring(idBarra1 + 1, idBarra2), 10) - 1;
    var v_ano = parseInt(data1.substr(idBarra2 + 1), 10);

    data1 = new Date(v_ano, v_mes, v_dia);

	idBarra1 = data2.indexOf("/");
	idBarra2 = data2.lastIndexOf("/");

    v_dia = parseInt(data2.substring(0, idBarra1), 10);
    v_mes = parseInt(data2.substring(idBarra1 + 1, idBarra2), 10) - 1;
    v_ano = parseInt(data2.substr(idBarra2 + 1), 10);

    data2 = new Date(v_ano, v_mes, v_dia);

	if (data1 > data2) {
		return 1;
	}else if (data1 < data2) {
		return 2;
	}else {
		return 0;
	}
}

/**
 * Compara as duas datas (dd/mm/aaaa hh:MM:ss) passadas como parametro
 *
 * @param dh1 Primeira Data Hora Informada
 * @param dh2 Segunda Data Hora Informada
 * @return 0 - Datas iguais. <br> 1 - Primeira data maior<br>2 - Segunda data maior
 */
function comparaDataHora(dh1, dh2) {

	var data1 = obterDataDH(dh1);
	var data2 = obterDataDH(dh2);
	var hora1 = obterHoraDH(dh1);
	var hora2 = obterHoraDH(dh2);

	var compDatas = comparaDatas(data1, data2);

	if (compDatas != 0) {
		return compDatas;
	}

	return comparaHoras(hora1, hora2);
}

/**
 * Compara as duas datas (mm/aaaa) passadas como parametro
 *
 * @param data1 Primeira Data Informada
 * @param data2 Segunda Data Informada
 * @return 0 - Datas iguais. <br> 1 - Primeira data maior<br>2 - Segunda data maior
 */
function comparaDatasMesAno(data1, data2) {

	data1 = "1/" + data1;
	data2 = "1/" + data2;

	return comparaDatas(data1, data2);
}

/**
 * Compara as duas datas (aaaa) passadas como parametro
 *
 * @param data1 Primeira Data Informada
 * @param data2 Segunda Data Informada
 * @return 0 - Datas iguais. <br> 1 - Primeira data maior<br>2 - Segunda data maior
 */
function comparaDatasAno(data1, data2) {

	data1 = "1/1/" + data1;
	data2 = "1/1/" + data2;

	return comparaDatas(data1, data2);
}

/**
 * Compara as duas datas (mm) passadas como parametro
 *
 * @param data1 Primeira Data Informada
 * @param data2 Segunda Data Informada
 * @return 0 - Datas iguais. <br> 1 - Primeira data maior<br>2 - Segunda data maior
 */
function comparaDatasMes(data1, data2) {

	data1 = data1 + "/2003";
	data2 = data2 + "/2003";

	return comparaDatasMesAno(data1, data2);
}

/**
 * Converte a string de data no formato yyyy-mm-dd hh:MM:ss.s
 * para dd/mm/yyyy
 *
 * @param texto Data no formato yyyy-mm-dd hh:MM:ss.s
 * @return Data no formato dd/mm/yyyy.
 */
function converterTS2Data(texto) {

    if (texto.length != 21) {
        return "";
    }

    var ano = texto.substr(0,4);
    var mes = texto.substr(5,2);
    var dia = texto.substr(8,2);

    return (dia + "/" + mes + "/" + ano);
} // converterTS2Data


/**
 * Converte a string de data no formato yyyy-mm-dd hh:MM:ss.s
 * para hh:MM:ss
 *
 * @param texto Data no formato yyyy-mm-dd hh:MM:ss.s
 * @return Data no formato hh:MM:ss.
 */
function converterTS2Hora(texto) {

    if (texto.length != 21) {
        return "";
    }

    var hora = texto.substr(11,8);

    return (hora);
} // converterTS2Hora


/**
 * Retorna a data a partir de uma data/hora (dd/mm/aaaa hh:MM:ss)
 *
 * @param texto Data no formato yyyy-mm-dd hh:MM:ss.s
 * @return Data no formato dd/mm/aaaa
 */
function obterDataDH(texto) {

    var idFinal = parseInt(texto.lastIndexOf("/"), 10) + 5;
    var data = texto.substring(0, idFinal);

	if (validaData(data)) {
		return data;
	}
}

/**
 * Obtém o dia de uma data no formato dd/mm/aaaa
 *
 * @param texto Data no formato dd/mm/aaaa
 * @return Dia contido na data.
 */
function obterDiaData(texto) {

    if (validaData(texto)) {
        return parseInt(texto.substring(0, texto.indexOf("/")), 10);
    }
}

/**
 * Obtém o mês de uma data no formtao dd/mm/aaaa
 *
 * @param texto Data no formato dd/mm/aaaa
 * @return Mês contido na data.
 */
function obterMesData(texto) {

    if (validaData(texto)) {
        return parseInt(texto.substring(texto.indexOf("/") + 1,
                texto.lastIndexOf("/")), 10) - 1;
    }
}

/**
 * Obtém o ano de uma data no formato dd/mm/aaaa
 *
 * @param texto Data no formato dd/mm/aaaa
 * @return Ano contido na data.
 */
function obterAnoData(texto) {

    if (validaData(texto)) {
        return parseInt(texto.substr(texto.lastIndexOf("/") + 1), 10);
    }
}

/**
 * Retorna a hora a partir de uma data/hora (dd/mm/aaaa hh:MM:ss)
 *
 * @param texto Data no formato dd/mm/aaaa hh:MM:ss
 * @return Hora contida na data informada.
 */
function obterHoraDH(texto) {

    var id = parseInt(texto.indexOf(":"), 10);
	var hora = texto.substring(id - 2);

    // Retirando os espacos em branco
    var padrao = /\s/g;
    hora = hora.replace(padrao, "");

	if (validaHora(hora)) {
		return hora;
	}
}


/**
 * Valida o intervalo das datas. O valor do parametro intervalo
 * deve ser passsado em dias.
 *
 * @param data1 Primeira Data Informada
 * @param data2 Segunda Data Informada
 * @param invervalo número de dias do intervalo entre as datas.
 * @return true ou false
 */
function validaIntervalo(data1, data2, intervalo) {

	// Verificando parametros
	if (!validaData(data1) || !validaData(data2) || isNaN(intervalo)) {
		return false;
	}

	// Verifica se a data inicial é maior do que a final
	if (comparaDatas(data1, data2) == 1) {
		return false;
	}

	// Valores da primeira data
	var dia1 = data1.substr(0,2);
	var mes1 = data1.substr(3,2);
	var ano1 = data1.substr(6);

	// Valores da segunda data
	var dia2 = data2.substr(0,2);
	var mes2 = data2.substr(3,2);
	var ano2 = data2.substr(6);

	// Calcula o numero de dias transcorridos
	var difDias = (dia2	- dia1) + ((mes2 - mes1)*30) + ((ano2 - ano1)*360);

	// Compara o numero de dias trasncorridos com o número de dias do intervalo
	return (intervalo >= difDias);
}

/**
 * Compara 2 horas no formato hh:mm:ss
 *
 * @param hora1 Primeira Hora Informada
 * @param hora2 Segunda Hora Informada
 * @return 1 - hora1 > hora2. <br> 2 - hora2 > hora1. <br> 0 - hora1 = hora2.
 */
function comparaHoras(hora1, hora2) {

    var h1 = hora1.substr(0,2);
    var h2 = hora2.substr(0,2);
    var m1 = hora1.substr(3,2);
    var m2 = hora2.substr(3,2);
    var s1 = hora1.substr(6);
    var s2 = hora2.substr(6);

    if (h1 > h2) {
        return 1;
    }else if (h1 < h2) {
        return 2;
    }

    if (m1 > m2) {
        return 1;
    }else if (m1 < m2) {
        return 2;
    }

    if (s1 > s2) {
        return 1;
    }else if (s1 < s2) {
        return 2;
    }

    return 0;

}

/**
 * Valida o Intervalo de Anos entre duas datas.
 * Se a diferenca de anos entre a data inicial e a data final for maior
 * que a qtdAno, retorna false, caso contrario retorna true.
 *
 * @param dataInicial Data Inicial
 * @param dataFinal Data Final
 * @param qtdAno quantidade de anos
 * @return booleano
 * @throws Exception
 */
function validaIntervaloAno(dataInicial, dataFinal, qtdAno) {

	// Informações da data inicial:
	var anoInicial =
			parseInt(dataInicial.substr(dataInicial.lastIndexOf("/") + 1), 10);

	// Montando uma possivel data final:
	var anoFinal = anoInicial + qtdAno;

	if (dataInicial.length == 7) {
		dataInicial = "01/" + dataInicial;
	}

	if (dataFinal.length == 7) {
		dataFinal = "01/" + dataFinal;
	}

	var dataFinalValida = dataInicial.substring(0,5) + "/" + anoFinal;

    return ((comparaDatas(dataFinal, dataFinalValida)) == 2);

} //validaIntervaloAno
