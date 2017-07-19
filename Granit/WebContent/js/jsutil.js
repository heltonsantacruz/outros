//<script language="javascript">
/**
 * Inicio da Classe 
 * <jsutil>
 */
 
var _js={} //Define pacote de classes utilitarias

var jsutil=_js;

_js.createPackage= function (pacote){
    var  dir = _js;
    var pacotes = pacote.split(".");   
    for (var k=0; k<pacotes.length; k++){
        if (!dir[pacotes[k]])
            dir[pacotes[k]]={};
        dir= dir[pacotes[k]];
    }
}





/*
js.mask; -pacote
js.mask.Util -pacote de funcoes
js.mask.DATE_REGEX -attributos
_i.mask_Util
*/





/** FIM da Classe <jsutil> *//**
 * Classe jsutil.dhtml.DHTMLUtil
 * 
 */

_js.createPackage("dhtml");


_js.dhtml.DHTMLUtil ={
    
    /**
     * Retorna um elemento do html dado seu id ou nome
     * @param idOrName 
     */
    getElement: function(idOrName) {
        var objForm= document.getElementById(idOrName);
        if(!objForm) 
            objForm = document.getElementsByName(idOrName).item(0);
        return objForm;
    },
    
    /**
     * retorna um objeto passado o próprio objeto seu id ou nome
     */
    getObject: function (obj){
        return (typeof(obj) == "string") ? _js.dhtml.DHTMLUtil.getElement(obj) : obj;
    },
    
    /**
     * copia o conteúdo html de um elemento html para outro
     */
    copyInnerHtml: function(from, to) {
        var objFrom = _js.dhtml.DHTMLUtil.getObject(from);
        var objTo = _js.dhtml.DHTMLUtil.getObject(to);        
        objTo.innerHTML = objFrom.innerHTML;       
        return true;
    }
    
}

/* Fim da Classe <jsutil.dhtml.DHTMLUtil> *//**
 * Inicio da Classe 
 * <jsutil.dhtml.StyleUtil>
 *
 * Requirid:
 *    jsutil.dhtml.DHTMLUtil
 */

_js.createPackage("dhtml");

_js.dhtml.StyleUtil = {
        
    hideElement: function (el , iState) {// 1 visible, 0 hidden
        if(document.layers){  //NN4+
            el.visibility = iState ? "show" : "hide";
        }else if(document.getElementById){	  //gecko(NN6) + IE 5+            
            el.style.visibility = iState ? "visible" : "hidden";
            }else if(document.all){	// IE 4
                el.all[szDivID].style.visibility = iState ? "" : "hidden";
            }
    },
    
    setDisplay: function (obj, visible){
        var objeto = _js.dhtml.DHTMLUtil.getObject(obj);
        if (objeto)
	        objeto.style.display = (visible) ? "" : "none";
    },
    
    setVisibility: function (obj, visible){
        var objeto = _js.dhtml.DHTMLUtil.getObject(obj);
        if (objeto)
	        objeto.style.visibility = (visible) ? "visible" : "hidden";
    }

}


/********* Fim da Classe <jsutil.dhtml.StyleUtil> **********/
/**
 * Inicio da Classe 
 * <jsutil.dhtml.SelectUtil>
 *
 * Requirid:
 *    jsutil.dhtml.DHTMLUtil
 */

_js.createPackage("dhtml");

_js.dhtml.SelectUtil = {

    length : function (select){
        select = _js.dhtml.DHTMLUtil.getObject(select);
        return (select.options) ? select.options.length : -1;
    },
    
    indexOfOption : function (select,value,label){
        var opcoes= select.options;        
        for(var k=0; k< opcoes.length; k++){
            if (value && opcoes[k].value==value)
                return k;
        }
        return -1;    
    },    
    
    
    getOptionByValue : function(select, value){
        var ind = _js.dhtml.SelectUtil.indexOfOption(select, value) ;        
        return (ind>=0) ? select.options[ind] : null;
    },
    
    
    addOption : function (select,valor,texto, selecione){
        var ind = _js.dhtml.SelectUtil.indexOfOption(select, valor);
        //Se nao existe adiciona
        if(ind ==-1){
            var opt= new Option();
            opt.text=texto;
            opt.value=valor;
            opt.selected = selecione;
            select.options.add(opt);
            /*select.options[select.options.length]=opt;
            ind=select.options.length-1;*/
        }else{
            select.options[ind].selected =selecione;
        }
        //Seleciona
        //if (selecione) select.selectedIndex=ind;
    },
    
    removeByValue: function(select, value){
        var pos = _js.dhtml.SelectUtil.indexOfOption(select, value);
        var option = (pos>-1)? select.options[pos] : null;
        if(option)
           select.remove(pos);
        return option;
    },
    
    removeOption : function (select, index, valor,texto){
        select.remove(index);
    },
    
    removeAllOptions: function(select){
	    _js.dhtml.SelectUtil.removeIntervalOptions(select, 0);
    },
    
    removeIntervalOptions : function (select, arg1, arg2){        
        //Remove a partir de arg1
        if(!arg2){
            select.options.length=arg1;
            if (arg1 > 0)
                select.selectedIndex=arg1-1;
        }else{
            for (var k=arg1; k< arg2; k++)
                select.remove(arg1);
        }    
    },
    
    trocaPos : function(sel, from, to){
        var aux = sel.options[from];
        var option1= new Option(aux.text, aux.value, false,false);
        aux = sel.options[to];
        var option2= new Option(aux.text, aux.value, false,false);
        sel.options[from]=option2;
        sel.options[to]=option1;
        return to;
    },
    
    optionToUp: function(sel, ind){    
        if(ind > 0)
            return _js.dhtml.SelectUtil.trocaPos(sel,ind, ind-1);
        return ind;
        
    },
    
    optionToDown: function(sel, ind){    
        if(ind >= 0) 
            return _js.dhtml.SelectUtil.trocaPos(sel,ind, ind+1);
        return ind;
    },
    
    createOption: function(value, text, selected){
        return new Option(text, value, false, selected);
    }
    
}





function testJS_dhtml_SelectUtil(){

    var SelectUtil= _js.dhtml.SelectUtil;

    var select1 = document.createElement("SELECT");
    var select2 = document.createElement("SELECT");  
    //var opt1 = document.createElement("OPTION");
    
    SelectUtil.addOption(select2,"0","teste 0", false);
    SelectUtil.addOption(select2,"1","teste 1",false);
    SelectUtil.addOption(select2,"2","teste 2",false);
    SelectUtil.addOption(select2,"1","teste 1-2",true);
    assertEquals("selectedIndex",select2.selectedIndex,1);
    assertEquals("selectedIndex",select2.options[select2.selectedIndex].text,"teste 1");
    
    
    
    //Teste Length    
    assertEquals("countString0",SelectUtil.length(select1),0);
    assertEquals("countString0",SelectUtil.length(select2),3);
    //
    assertEquals("countString0",SelectUtil.indexOfOption(select2,"1"),1);
    assertEquals("countString0",SelectUtil.indexOfOption(select2,"5"),-1);
    //
    SelectUtil.addOption(select1,"2","teste 2-2", false);
    assertEquals("countString0",SelectUtil.getOptionByValue(select2,"2").text,"teste 2");
    assertEquals("countString0",SelectUtil.getOptionByValue(select1,"2").text,"teste 2-2");
    
    SelectUtil.removeOption(select1, 0);
    assertEquals("countString0",SelectUtil.length(select1),0);
    
    //Testa a troca de posicoes
    SelectUtil.trocaPos(select2,0,2);
    assertEquals("trocaPos00",select2.options[0].text,"teste 2");
    assertEquals("trocaPos01",select2.options[2].text,"teste 0");
    SelectUtil.trocaPos(select2, 0,2);
    assertEquals("trocaPos2",select2.options[0].text,"teste 0");
    assertEquals("trocaPos3",select2.options[2].text,"teste 2");    
    SelectUtil.optionToUp(select2, 1);
    assertEquals("optionToUp",select2.options[0].text,"teste 1");
    assertEquals("optionToUp",select2.options[1].text,"teste 0");    
    SelectUtil.optionToDown(select2, 0);
    assertEquals("optionToDown",select2.options[0].text,"teste 0");
    assertEquals("optionToDown",select2.options[1].text,"teste 1");
    assertEquals("optionToDown",select2.options[2].text,"teste 2");
    assertEquals("optionToDown",select2.options[4],null);

/**/
}



/********* Fim da Classe <jsutil.dhtml.SelectUtil> **********/
//<script language="javascript">
/**
 * Inicio da Classe 
 * <jsutil.mask.DateMask>
 * Requerid: 
 *      jsutil.util.StringUtil
 *      jsutil.mask.util.DateUtil
 */

_js.createPackage("mask");//package

_js.mask.DateMask = function (obj, mask){    
    this.initialize(obj, mask);
}

_js.mask.DateMask.prototype.initialize = function(obj, mask){
        obj.mask = this;
        this.obj = obj;
        this.mask = mask;
        this.maskPattern = new _js.mask.MaskPattern(mask);   
        this.objIndice = _js.mask.MaskObjects.addObserver(this.obj);
}

/* Versao antiga
_js.mask.DateMask.prototype.initialize = function(obj, mask){
    if (_js.mask.util.DateUtil.isValidMask(mask)){
        
        obj.mask = this;
        this.obj = obj;
        this.mask = mask;
        
        //Para Esta Mascara especifica
        this.maskDate = _js.mask.util.DateUtil.getDateMask(this.mask);
        this.simpleMask = _js.mask.util.DateUtil.getSimpleMask(this.mask);
        this.objIndice = _js.mask.MaskObjects.addObserver(this.obj);
    }else{
        throw "Invalid mask of DateMask : "+mask;
    }
}

_js.mask.DateMask.prototype.applyMask = function(){
    var val= _js.mask.util.NumberUtil.getNumber(this.obj.value);//Valor Numerico
    //Caso o valor Númerico exceda o tamanho 
    val=(val.length> this.maskDate.length) ? val.substring(0, this.maskDate.length) : val;
    
    var ano = _js.mask.util.DateUtil.getAno(this.maskDate,val);
    var mes = _js.mask.util.DateUtil.getMes(this.maskDate,val);        
    var dia = _js.mask.util.DateUtil.getDia(this.maskDate,val);
    mes = (mes=="00")? "01" :(new Number(mes) > 12)? "12" :mes;
    dia = _js.mask.util.DateUtil.getDiaValido(dia, mes, ano);
    
    try {
        this.obj.value = _js.mask.util.DateUtil.addMask(this.simpleMask, this.maskDate, val, dia, mes, ano);    	
    } catch (e) {}

}*/

_js.mask.DateMask.prototype.applyMask = function(){
    try{
        this.obj.value = this.maskDate(this.obj.value, this.maskPattern);
    }catch(e){}
}

/***/



    _js.mask.DateMask.prototype.maskDate= function(value, maskPattern){
//        
        var sValue= _js.util.StringUtil.filterCharRegex(value, "[0-9]");
        
        var hor = maskPattern.getPatternValue(sValue,"HH","00","23");
        var min = maskPattern.getPatternValue(sValue,"mm","00","59");
        var seg = maskPattern.getPatternValue(sValue,"ss","00","59");
        
        var mes = maskPattern.getPatternValue(sValue,"MM","01","12");
        var ano = maskPattern.getPatternValue(sValue,"yyyy","0001","9999");
        var anoCompleto = ano && ano.length==4;
        if(!ano){
            ano = maskPattern.getPatternValue(sValue,"yy","00","99");
            anoCompleto = ano && ano.length==2;
        }
        
        var nAno = new Number(ano);
        var bissexto = (!anoCompleto || ((nAno % 4 == 0) && ((nAno % 100 !=0)||(nAno % 400 ==0))));
        
        var maxDia= (!mes)? "31" :
                    (",04,06,09,11,".indexOf(","+mes+",")>=0 ) ? "30" :
                    (mes!="02") ? "31" : (bissexto)? "29" : "28";
        
        var dia = maskPattern.getPatternValue(sValue,"dd","01",maxDia);
        return maskPattern.replacedPattern(sValue.length).replace('s',seg)
                    .replace('m',min).replace('H',hor).replace('d',dia)
                    .replace('M',mes).replace('y',ano);
    }

    _js.mask.MaskPattern = function(pattern){
        this.pattern = pattern;
        this.simbolPattern = _js.util.StringUtil.filterCharRegex(this.pattern, _js.mask.MaskPattern.MASK_REGEX);
    }
    
    _js.mask.MaskPattern.MASK_REGEX = new RegExp("^[dMyHms]$");    
    
    _js.mask.MaskPattern.prototype.replacedPattern= function(size){
        
        var result='';
        var ult='';
        var atual,isPattern;
        var loop = size;
        var k=0;
        while(loop>0 && k< this.pattern.length){
            
            atual = this.pattern.charAt(k);
            isPattern = _js.mask.MaskPattern.MASK_REGEX.test(atual);
            if(!isPattern || ult!=atual)
               result+= atual;
            if(isPattern)
                loop--;
            ult = atual;
            k++;
        }
        return result;
    }
    
    
    _js.mask.MaskPattern.prototype.getPatternValue= function(value, patternKey, min, max){
        var ini = this.simbolPattern.indexOf(patternKey);
        if(ini >=0){
            var fim = ini+ patternKey.length;
            if(value.length< ini)
                return null;

            var ret= value.substring(ini,fim);
            
            //se do mesmo tamanho verifica intervalo
            if(ret.length == patternKey.length){
                var retNumber = new Number(ret);
                return (retNumber< new Number(min)) ? min :
                            (retNumber> new Number(max)) ? max : ret;
            }
            
            return ret;
        }
        return null;
    }

/** FIM da Classe <jsutil.mask.DateMask> *///<script language="javascript">
/**
 * Inicio da Classe 
 * <jsutil.mask.DecimalMask>
 * Requerid: 
 *      jsutil.util.StringUtil
 *      jsutil.mask.util.NumberUtil
 *      jsutil.mask.util.DecimalUtil
 */

_js.createPackage("mask");//package



_js.mask.DecimalMask = function(obj, mask){
    this.initialize(obj,mask);
}

_js.mask.DecimalMask.prototype.initialize = function(obj, mask){
    if (_js.mask.util.DecimalUtil.isValidMask(mask)){        
        
        obj.mask = this;
        
        this.obj = obj;
        this.mask = mask;        
        this.objIndice = _js.mask.MaskObjects.addObserver(this.obj);
        
        this.groupSep = _js.mask.util.DecimalUtil.getGroupSep(this.mask);
        this.groupNumber = _js.mask.util.DecimalUtil.getGroupNumber(this.mask);
        this.decimalNumber = _js.mask.util.DecimalUtil.getDecimalNumber(this.mask);
        this.decimalSep = _js.mask.util.DecimalUtil.getDecimalSep(this.mask);
        this.sinal = _js.mask.util.DecimalUtil.getSinal(this.mask);
    }else{
        throw "Invalid mask of DecimalMask : "+mask;
    }
}

_js.mask.DecimalMask.prototype.applyMask = function(){
    try {
    this.obj.value= _js.mask.util.NumberUtil.getDouble(this.obj.value,
            this.groupSep, this.groupNumber, this.decimalSep, this.decimalNumber, this.sinal);	
    } catch (e) { }   
    
}



/** FIM da Classe <jsutil.mask.DecimalMask> *///<script language="javascript">
/**
 * Inicio da Classe 
 * <jsutil.mask.DecimalMask>
 * Requerid: 
 *      jsutil.util.StringUtil
 *      jsutil.mask.util.NumberUtil
 *      jsutil.mask.util.DecimalUtil
 */

_js.createPackage("mask");//package

_js.mask.FixMask = function (obj, mask){    
    this.initialize(obj, mask);
}


_js.mask.FixMask.prototype.initialize = function(obj, mask){
    if (true){//_js.mask.util.DecimalUtil.isValidMask(mask)){
        
        obj.mask = this;
        this.obj = obj;
        this.mask = mask;
        this.objIndice = _js.mask.MaskObjects.addObserver(this.obj);
    }else{
        throw "Invalid mask of Fix : "+mask;
    }
}

_js.mask.FixMask.prototype.applyMask = function(event){  
    try{
        if( !event || event.keyCode != 8){
           var result =_js.mask.util.FixUtil.numberFixUtil(this.mask, this.obj.value);
           this.obj.value = result;
        }
    }catch(e){}
}


/** FIM da Classe <jsutil.mask.DecimalMask> *///<script language="javascript">


/**Adicionado */

_js.mask.RegexCharMask = function (obj, mask){    
    this.initialize(obj, mask);
}


_js.mask.RegexCharMask.prototype.initialize = function(obj, mask){
     obj.mask = this;
     this.obj = obj;
     this.mask = new RegExp(mask);
     this.objIndice = _js.mask.MaskObjects.addObserver(this.obj);
}

_js.mask.RegexCharMask.prototype.applyMask = function(){
    try{
        var value = this.obj.value;
        
        for(var k=value.length; k>=0 ; k--){
            if(!this.mask.test(value.charAt(k)))
                value = value.substring(0,k) + value.substring(k+1);
        }

        this.obj.value = value;
    }catch(e){}
}

















/**
 * Inicio da Classe 
 * <jsutil.mask.DecimalMask>
 * Requerid: 
 *      jsutil.util.StringUtil
 *      jsutil.mask.util.NumberUtil
 *      jsutil.mask.util.DecimalUtil
 */




_js.createPackage("mask");//package


_js.mask.DoubleFixMask = function (obj, mask1, mask2){    
    this.initialize(obj, mask1,mask2);
}

_js.mask.DoubleFixMask.prototype.initialize = function(obj, mask1,mask2){
    if (true){//_js.mask.util.DecimalUtil.isValidMask(mask)){        
        obj.mask = this;        
        this.obj = obj;        
        
        this.mask1 = mask1;
        this.mask2 = mask2;        
        this.sizeMask1 = _js.util.StringUtil.countString(mask1,"#");
        this.sizeMask2 = _js.util.StringUtil.countString(mask2,"#");
        if(this.sizeMask1 >= this.sizeMask2)
            throw "Invalid the size of mask1 and mask2 : ";        
        this.objIndice = _js.mask.MaskObjects.addObserver(this.obj);
    }else{
        throw "Invalid mask of Fix : "+mask1 +" "+mask2;
    }
}

_js.mask.DoubleFixMask.prototype.applyMask = function(){
    var tam = _js.mask.util.NumberUtil.getNumber(this.obj.value).length;
    
    try{
        if(tam > this.sizeMask1){
            this.obj.value = _js.mask.util.FixUtil.numberFixUtil(this.mask2, this.obj.value);
        }else{
            this.obj.value = _js.mask.util.FixUtil.numberFixUtil(this.mask1, this.obj.value);
        }
    }catch(e){}
}


/** FIM da Classe <jsutil.mask.DecimalMask> *///<script language="javascript">
/**
 * Inicio da Classe 
 * <jsutil.mask.DecimalMask>
 * Requerid: 
 *      jsutil.util.StringUtil
 *      jsutil.mask.util.NumberUtil
 *      jsutil.mask.util.DecimalUtil
 */

_js.createPackage("mask");//package


_js.mask.MaskObjects={
    
    objects : new Array(),
    
    add : function (obj){
        return _js.mask.MaskObjects.objects.push(obj) -1 ;    
    },
    
    get : function (index){
        return _js.mask.MaskObjects.objects[index];
    },
    
    addObserver: function(obj, cod){
        _js.mask.MaskObjects.removeObserver(obj);
        
        var cod =_js.mask.MaskObjects.add(obj);
        var func= new Function("_js.mask.MaskObjects.get("+cod+").mask.applyMask();");

        obj.maskFunctions= func;
        _js.util.EventUtil.observeEvent(obj, "keyup", func ,false);
        _js.util.EventUtil.observeEvent(obj, "blur", func ,false);
        return cod;
    },
    
    removeObserver: function(obj){
        if(obj.maskFunctions){
            _js.util.EventUtil.disableObserveEvent(obj, "keyup", obj.maskFunctions);
            _js.util.EventUtil.disableObserveEvent(obj, "blur", obj.maskFunctions);    
            obj.maskFunctions=null;
        }        
    } 
    
}

/** FIM da Classe <jsutil.mask.DecimalMask> *///<script language="javascript">
/**
 * Inicio da Classe 
 * <jsutil.mask.util.DateUtil>
 * Requerid: 
 *      jsutil.util.StringUtil
 */

_js.createPackage("mask.util");//package

_js.mask.util.DateUtil = {

    maskDate: function(mask, valor){
        var maskDate= _js.mask.util.DateUtil.getDateMask(mask);
        var simpleMask= _js.mask.util.DateUtil.getSimpleMask(mask);
        var val= _js.mask.util.NumberUtil.getNumber(valor);//Valor Numerico
        
        //Caso o valor Númerico exceda o tamanho 
        val=(val.length> maskDate.length) ? val.substring(0, maskDate.length) : val;
        
        var ano = _js.mask.util.DateUtil.getAno(maskDate,val);
        var mes = _js.mask.util.DateUtil.getMes(maskDate,val);        
        var dia = _js.mask.util.DateUtil.getDia(maskDate,val);
        mes = (mes=="00")? "01" :(mes>12)? 12 :mes;
        dia = _js.mask.util.DateUtil.getDiaValido(dia, mes, ano);
        
        return _js.mask.util.DateUtil.addMask(simpleMask, maskDate, val, dia, mes, ano);
    },

    isValidMask:function(mask){//TODO
        return true;
        /*var d=/^.*(dd){1}.*(dd){1}$/;
        var m=/^.*(MM){1}.*(MM){1}$/;
        var y1=/^.*(yyyy){1}.*(yyyy){1}$/;
        var y2=/^.*(yy){1}.*(yy){1}$/;
        var val = !d.test(mask) && !m.test(mask);
        return (!val) ? val : (y1.test(mask)|| y2.test(mask));           */
    },

    /**
     * Mascara No formato ddMMyyyy
     */
    getDateMask: function(mask){            
        return _js.util.StringUtil.filterCharRegex(mask,"^[dMy]$");
    },    
    
    /** 
     * Mascara no formato d/M/y
     */
    getSimpleMask: function(mask){
        return mask.replace("dd","d").replace("MM","M").replace("yyyy","y").replace("yy","y");
    },    
    
    /**
     * Adiciona a mascara dado uma 
     * @param dateSimpleMask 
     * @param maskDate
     * @param o valor númerico a ser aplicada a mascara
     * @param dia
     * @param mes
     * @param ano
     */
    addMask: function(simpleMask, maskDate, valNum, dia, mes, ano){        
        var max= maskDate.substring(valNum.length-1,valNum.length);
        var maskToReplace = simpleMask.substring(0,simpleMask.indexOf(max)+1);
        return _js.mask.util.DateUtil.replaceSimpleMask(maskToReplace, dia, mes, ano);
    },
   
    replaceSimpleMask: function(mask, dia, mes, ano){
        mask=mask.replace("d",dia);
        mask=mask.replace("M",mes);
        mask=mask.replace("y",ano);            
        return mask;
    },   
    
    getDiaValido: function(dia, mes, ano){
        var nDia=(dia) ? new Number(dia) : null;
        var nMes=(mes) ? new Number(mes) :null;
        var nAno=(ano && ano.length==4)? new Number(ano) : null;
        
        if (dia){
            dia= (dia=="00")? "01" : (nDia > 31) ? "31" : dia;
            if(mes){
                dia = (",04,06,09,11,".indexOf(","+mes+",")>=0 && nDia > 30) ? "30" :
                    (mes=="02" && nDia > 29)? "29" : dia;
                
                if(nAno)
                    dia = (nAno && (nMes == 2) && (dia=="29") && 
                        !((nAno % 4 == 0) && ((nAno % 100 !=0)||(nAno % 400 ==0)) ) )? "28" : dia;
            }
        }
        return dia;
    },
    
    getDia: function(mask,valor){
        var pos= mask.indexOf("dd");
        return valor.substring(pos,pos+2);
    },
    
    getMes: function(mask,valor){
        var pos= mask.indexOf("MM");
        return valor.substring(pos,pos+2);
    },
    
    getAno: function(mask,valor){
        var pos= mask.indexOf("yyyy");
        if(pos >= 0){
            return valor.substring(pos,pos+4);
        }else{
            var pos= mask.indexOf("yy");
            return valor.substring(pos,pos+2);
        }
    }
}


function testJS_mask_util_DateUtil(){
    var Masks= _js.mask.util.DateUtil;
    
    assertEquals("date.getSimpleMask", Masks.getSimpleMask("dd/MM/yyyy"),"d/M/y");
    assertEquals("date.getSimpleMask", Masks.isValidMask("dd/MM/yyyy"),true);
    assertEquals("date.getSimpleMask", Masks.isValidMask("dd/MM/yyyyy"),true);
    
    assertEquals("maskDate0", Masks.maskDate("dd/MM/yyyy", "12a1a2a1a2a3a4a5"),"12/12/1234");
    assertEquals("maskDate1", Masks.maskDate("dd/MM/yyyy", "12a1a2a1a2a3a4"),"12/12/1234");
    assertEquals("maskDate2", Masks.maskDate("dd/MM/yyyy", "12121234"),"12/12/1234");
    assertEquals("maskDate3", Masks.maskDate("dd/MM/yyyy", "1212123"),"12/12/123");
    assertEquals("maskDate4", Masks.maskDate("dd/MM/yyyy", "12121"),"12/12/1");
    assertEquals("maskDate5", Masks.maskDate("dd/MM/yyyy", "1212"),"12/12");
    assertEquals("maskDate6", Masks.maskDate("dd/MM/yyyy", "121"),"12/1");
    assertEquals("maskDate7", Masks.maskDate("dd/MM/yyyy", "12"),"12");
    assertEquals("maskDate8", Masks.maskDate("dd/MM/yyyy", "1"),"1");

}




/** FIM da Classe <jsutil.mask.util.DateUtil> *///<script language="javascript">
/**
 * Inicio da Classe 
 * <jsutil.mask.util.NumberUtil>
 * Requerid: 
 *      jsutil.util.StringUtil
 */

_js.createPackage("mask.util");//package


_js.mask.util.NumberUtil = {
    
    
    /**
     *
     */
    getDouble: function(valor, groupSep, groupNumber, decimalSep, decimalNumber, sinal){
        
        var sin= (sinal) ? valor.substring(0,1) :"";            
        var sin =(sin=="+" || sin=="-") ? sin : "";
        
        var temGrupos = (groupSep!="" && groupNumber!=0);
        var temDecimal= (decimalSep!="" && valor.indexOf(decimalSep)>0);
        
        var inteiro= (temDecimal)? valor.substring(0,valor.indexOf(decimalSep)) : valor;
        var decimal= (temDecimal)? valor.substring(valor.indexOf(decimalSep)+1) : "";
        
        inteiro=(temGrupos)?
            _js.mask.util.NumberUtil.getInteger(inteiro,groupSep,groupNumber) : 
            _js.mask.util.NumberUtil.getNumber(inteiro);
            
        decimal=(temDecimal)? _js.mask.util.NumberUtil.getNumber(decimal) : "";
        decimal=(temDecimal && decimalNumber!= 0)? decimal.substring(0,decimalNumber): decimal;//Tamanho do decimal
        decimal=(temDecimal)? decimalSep+decimal: "";
        
        return sin + inteiro + decimal;
    },

    /**
     * Retorna um String contendo um número inteiro com seus separadores de grupos
     */
    getInteger : function (valor, groupSep, groupNumber){
        var k=valor.length-1;
        var it=k-groupNumber;
        var aux="";
        while( k>=0 ){            
            if (it==k){
                aux= groupSep+aux;
                it= k-groupNumber;
            }
            if (_js.mask.util.NumberUtil.isNumber(valor.charAt(k))){
                aux= valor.charAt(k)+aux;
            }else{
                it--;//Contador do sep de grupo incrementado
            }
            
            k--;
        }
        return (aux.charAt(0)!=groupSep) ? aux : _js.util.StringUtil.deleteString(aux,0);
    },
    
    /**
     * Retorna um string contendo apenas Números
     */
    getNumber: function (valor){
        return _js.util.StringUtil.filterCharRegex(valor,"^[0-9]$");
    },
    
    isNumber : function (valor){
        return new RegExp("^[0-9]$").test(valor);
    }

}

    
    
    
    
_js.mask.util.DecimalUtil = {
        
    /*   || .999,00 ||  .999,*  || .999 || *,00 || *,* || ,*  || -.999   */
    isValidMask : function(mask){
        var m= /^(-)*([*]{1}||([\D]{1}[9]+))(([\D]{1}[*]{1})||([\D]{1}[0]+))$/
        return m.test(mask);
    },
    
    getSinal : function(mask){
        return mask.substring(0,1) == "-";
    },
    
    getGroupSep: function(mask){
        var aux= mask.substring(0,1);
        aux= (aux == "-") ? mask.substring(1,2) : aux;
        return (aux=="" || aux =="9" || aux =="*")? "" : aux;    
    },
    
    getGroupNumber: function(mask){
        return _js.util.StringUtil.countString(mask,"9");
    },
    
    getDecimalSep: function(mask){        
        var sep = _js.mask.util.DecimalUtil.getGroupSep(mask);
        var it= 1;
        var aux= mask.charAt(it);
        while ( (it<mask.length) && (aux == "9" || aux=="*" || aux=="-" || aux == sep)){
            it++;
            aux= mask.charAt(it);
        }
        return aux;
    },
    
    getDecimalNumber: function(mask){
        return _js.util.StringUtil.countString(mask,"0");        
    }
}



function testJS_mask_util_NumberUtil(){
    var Masks= _js.mask.util.NumberUtil;
    
    
    /*   || .999,00 ||  .999,*  || .999 || *,00 || *,* || ,*  || -.999   */
    /*
    assertEquals("isValidDecimalMask", Masks.isValidDecimalMask("*,*"),true);
    assertEquals("isValidDecimalMask", Masks.isValidDecimalMask(",*"),true);
    assertEquals("isValidDecimalMask", Masks.isValidDecimalMask("*"),true);
    assertEquals("isValidDecimalMask", Masks.isValidDecimalMask("*,"),false);
    */
    assertEquals("isNumber", Masks.isNumber("0"),true);
    assertEquals("isNumber", Masks.isNumber("01"),false);
    assertEquals("isNumber", Masks.isNumber("a"),false);
    assertEquals("getNumber", Masks.getNumber("sds1ddd2fff3fgff4ggg5hgh6.;vc.oio"),"123456");
    assertEquals("getNumber", Masks.getInteger("123456789",".",3),"123.456.789");
    assertEquals("getNumber", Masks.getInteger("1234567890",".",3),"1.234.567.890");
    assertEquals("getNumber", Masks.getDouble("12345,1,11d111",".",3,",",2),"12.345,11");
    
}


/** FIM da Classe <jsutil.mask.util.NumberUtil> *///<script language="javascript">
/**
 * Inicio da Classe 
 * <jsutil.mask.util.DateUtil>
 * Requerid: 
 *      jsutil.util.StringUtil
 */

_js.createPackage("mask.util");//package

_js.mask.util.FixUtil = {

    numberFixUtil: function(mask , valor){
        var val = ""+_js.mask.util.NumberUtil.getNumber(valor);
        if( val.length < 1 ) return "";
        var maskAux = "";    
        var k = 0;
        var i = 0;
        var retorno="";
    
        while (k < mask.length && i <= val.length){        
            maskAux = mask.charAt(k);
            if(maskAux=="#"){
                retorno += val.charAt(i);
                i++;
            }else{
                retorno += maskAux;
            }
            k++;
        }
    
        return retorno;
    }
}


function testJS_mask_util_DateUtil(){
    var Masks= _js.mask.util.DateUtil;
    
    assertEquals("date.getSimpleMask", Masks.getSimpleMask("dd/MM/yyyy"),"d/M/y");
    assertEquals("date.getSimpleMask", Masks.isValidMask("dd/MM/yyyy"),true);
    assertEquals("date.getSimpleMask", Masks.isValidMask("dd/MM/yyyyy"),true);
    
    assertEquals("maskDate0", Masks.maskDate("dd/MM/yyyy", "12a1a2a1a2a3a4a5"),"12/12/1234");
    assertEquals("maskDate1", Masks.maskDate("dd/MM/yyyy", "12a1a2a1a2a3a4"),"12/12/1234");
    assertEquals("maskDate2", Masks.maskDate("dd/MM/yyyy", "12121234"),"12/12/1234");
    assertEquals("maskDate3", Masks.maskDate("dd/MM/yyyy", "1212123"),"12/12/123");
    assertEquals("maskDate4", Masks.maskDate("dd/MM/yyyy", "12121"),"12/12/1");
    assertEquals("maskDate5", Masks.maskDate("dd/MM/yyyy", "1212"),"12/12");
    assertEquals("maskDate6", Masks.maskDate("dd/MM/yyyy", "121"),"12/1");
    assertEquals("maskDate7", Masks.maskDate("dd/MM/yyyy", "12"),"12");
    assertEquals("maskDate8", Masks.maskDate("dd/MM/yyyy", "1"),"1");

}




/** FIM da Classe <jsutil.mask.util.DateUtil> *///<script language="javascript">

/**
 * Inicio da Classe 
 * <jsutil.util.StringUtil> 
 */

_js.createPackage("util");//package

_js.util.StringUtil ={

    /**
     * Conta a quantidade de vezes que se repete um
     * determinada sequencia em um string
     */
    countString: function (valor, pre){        
        if(!valor || !pre) return 0;
        
        var aux= valor;
        var n=0;
        while (aux.indexOf(pre)!= -1){
            aux=aux.substring(aux.indexOf(pre)+pre.length);
            n++;
        }
        return n;
    },

    /**
     * Adiciona um string em uma posicao de outra string
     */
    addString: function (value, add, n){
        return value.substring(0,n)+ add +value.substring(n);
    },
    
    /**
     * Deleta um string a partir de uma posicao
     */
    deleteString : function (value, n){
        return value.substring(0,n)+value.substring(n+1);
    },
    
    
    isNumeric : function (value){
        return /^[0-9]+$/.test(value); //Verifica se string é formada por valores numéricos
    },
    
    /**
     * retorna um string formado por caracteres de acordo 
     * com um regex
     */
    filterCharRegex : function(valor, regex){
        var k=valor.length-1;
        var reg=new RegExp(regex);
        var aux="";
        while( k>=0 ){            
            if (reg.test(valor.charAt(k)))
                aux= valor.charAt(k)+aux;
            k--;
        }
        return aux;
    }
}

/********* Fim da Classe <jsutil.util.StringUtil>  **********/



function testJS_util_StringUtil(){
    var StringUtil= _js.util.StringUtil;
    assertEquals("countString0",StringUtil.countString(),0);
    assertEquals("countString1",StringUtil.countString(""),0);    
    assertEquals("countString2",StringUtil.countString("",""),0);
    assertEquals("countString3",StringUtil.countString("55555","5"),5);
    assertEquals("countString4",StringUtil.countString("","5"),0);
}//<script language="javascript">

/**
 * Inicio da Classe 
 * <jsutil.util.StringUtil> 
 */

_js.createPackage("util");//package

_js.util.EventUtil ={

    observeEvent: function(element, name, observer, useCapture) {
        if (element.addEventListener) {
            element.addEventListener(name, observer, useCapture);
        }else if (element.attachEvent) {
            element.attachEvent('on' + name, observer);
        }
    },
    
    disableObserveEvent: function(element, name, observer, useCapture) {           
        if (element.removeEventListener) {
            element.removeEventListener(name, observer, useCapture);
        } else if (element.detachEvent) {
            element.detachEvent('on' + name, observer);
        }
    }
    
}

/********* Fim da Classe <jsutil.util.StringUtil>  **********/


//<script language="javascript">

/**
 * Inicio da Classe 
 * <jsutil.util.StringUtil> 
 */

_js.createPackage("util");//package

/*
 * Observa um método de um objeto.
 * 
 */
_js.util.FunctionObserver ={
    
    observer : new Array(),
    
    addObserver : function (objeto, nomeDaFuncao, funcao, parametros){        
        return _js.util.FunctionObserver._addObserverToFunction(
                  objeto, nomeDaFuncao, funcao, parametros)
        
    },
    
    removeObserverById : function (id){
        _js.util.FunctionObserver.observer[id]=null;
    },
    
    _addFunction : function(objeto, nomeDaFuncao, funcao, parametros){
        var observer = {}
        observer.onfunction = funcao;
        observer.params = parametros;        
        return _js.util.FunctionObserver.observer.push(observer)-1;
    },
    
    _createFunction : function(id, original){
/*
        var  id = _js.util.FunctionObserver._addFunction(
                                    objeto, nomeDaFuncao, funcao, parametros);
  */      
        return (original) ? "return _js.util.FunctionObserver.executeID(event, '"+id+"');" :
                            "_js.util.FunctionObserver.executeID(event,'"+id+"');";
    },
    
    
    
    
    _addObserverToFunction: function(objeto, nomeDaFuncao, funcao, parametros){
        
        var sfun= ""+ objeto[nomeDaFuncao];
        var ini = "var _FunctionObserver = _js.util.FunctionObserver;";
        var fim = "_FunctionObserver = _FunctionObserver;";
        
        //se a funcao já foi criada
        if(objeto[nomeDaFuncao] && sfun.indexOf(ini) == -1 ){
            var id0 = _js.util.FunctionObserver._addFunction(
                      objeto, nomeDaFuncao, objeto[nomeDaFuncao]);
            var body0 = _js.util.FunctionObserver._createFunction(id0,true);
            objeto[nomeDaFuncao] = new Function("event", ini+body0+fim);
            sfun= ""+ objeto[nomeDaFuncao];
        }
         
        var body = sfun.substring(sfun.indexOf(ini)+ini.length, sfun.indexOf(fim));
        var id = _js.util.FunctionObserver._addFunction(
                      objeto, nomeDaFuncao, funcao, parametros);
                      
        body= _js.util.FunctionObserver._createFunction(id) + body;
        objeto[nomeDaFuncao] = new Function("event", ini+body+fim);
        return id;
    },
    
    
    
    
    
    executeID : function (event , observerID){
        if(!_js.util.FunctionObserver.observer[observerID]) return null;
        
        var param = _js.util.FunctionObserver.observer[observerID].params;        
        var func = "";
        if(param && param.length > 0){
            for(var i=0; i< param.length; i++)
            	func +=", param["+i+"]";            	
        }
        
        var funcao = _js.util.FunctionObserver.observer[observerID].onfunction;        
        return (funcao)? eval("funcao(event"+func+");") : null;
    }
    
    
    
}

/********* Fim da Classe <jsutil.util.StringUtil>  **********/


/** 
 * Begin <jsutil.util.Region> 
 */

_js.createPackage("util");//package

_js.util.Region = function (left, right, top, botton ){    
    this.initialize(left, right, top, botton );
}

_js.util.Region.prototype.initialize = function (left, right, top, botton ){
    this.top = top;
    this.right = right;
    this.botton = botton;
    this.left = left;
}

_js.util.Region.prototype.getWidth=  function(){
    return this.right - this.left;
}

_js.util.Region.prototype.getHeight=  function(){
    return this.botton - this.top;
}

_js.util.Region.prototype.clone = function(){
    return new _js.util.Region(this.left, this.right, this.top, this.botton);
}

_js.util.Region.prototype.toString = function(){
    return 'X('+this.left+','+this.right+'); Y('+this.top+','+this.botton+')';
}

_js.util.Region.prototype.intercept= function(regiao, checkLines){
    var width= this._interceptLines(this.left, this.right, regiao.left, regiao.right);
    var height= this._interceptLines(this.top, this.botton, regiao.top, regiao.botton);
    
    if(checkLines){
        return new _js.util.Region(height.x1,height.x2,width.x1,width.x2);
    }    
    if(width.getWidth()>0 && width.getHeight()>0)
       return new _js.util.Region(height.x1,height.x2,width.x1,width.x2);
    return new _js.util.Region(0,0,0,0);
}

_js.util.Region.prototype._interceptLines= function(i1,f1,i2,f2){
    if(f2>=f1){
        var ponto={}            
        if (f1>=i2){
            if (i1 > i2){                    
                ponto.x1=i1;
                ponto.x2=f1;
                return ponto;
            }else{
                ponto.x1=i2;
                ponto.x2=f1;
                return ponto;
            }                    
        }else{
            ponto.x1=0;
            ponto.x2=0;
            return ponto;
        }              
        
    }else{
        return this._interceptLines(i2,f2,i1,f1);
    }
}


/** End <jsutil.util.Region> */


/*
function testJS_util_StringUtil(){
    var StringUtil= _js.util.StringUtil;
    assertEquals("countString0",StringUtil.countString(),0);
    assertEquals("countString1",StringUtil.countString(""),0);    
    assertEquals("countString2",StringUtil.countString("",""),0);
    assertEquals("countString3",StringUtil.countString("55555","5"),5);
    assertEquals("countString4",StringUtil.countString("","5"),0);
}*//** 
 * Begin <jsutil.util.Point> 
 */

_js.createPackage("util");//package

_js.util.Point = function (x,y){
    this.initialize(x, y);
}

_js.util.Region.prototype.initialize = function (x, y){
    this.x = x;
    this.y = y;
}

_js.util.Region.prototype.getWidth=  function(){
    return this.right - this.left;
}

_js.util.Region.prototype.getHeight=  function(){
    return this.botton - this.top;
}

_js.util.Region.clone = function(){
    return new _js.util.Region(this.left, this.right, this.top, this.botton);
}

_js.util.Region.toString = function(){
    return 'X('+this.left+','+this.right+'); Y('+this.top+','+this.botton+')';
}

_js.util.Region.intercept= function(regiao){
    var width= calculaRetas(this.left, this.right, regiao.left, regiao.right);
    var height= calculaRetas(this.top, this.botton, regiao.top, regiao.botton);
    
    if(width.getWidth()>0 && width.getHeight()>0)
       return new _js.util.Region(height.x1,height.x2,width.x1,width.x2);
    return new _js.util.Region(0,0,0,0);
}


/** End <jsutil.util.Region> */



function testJS_util_StringUtil(){
    var StringUtil= _js.util.StringUtil;
    assertEquals("countString0",StringUtil.countString(),0);
    assertEquals("countString1",StringUtil.countString(""),0);    
    assertEquals("countString2",StringUtil.countString("",""),0);
    assertEquals("countString3",StringUtil.countString("55555","5"),5);
    assertEquals("countString4",StringUtil.countString("","5"),0);
}//<script language="javascript">

/** Inicio da Classe <jsutil.util.DateUtil> */

_js.createPackage("util");//package

_js.util.DateUtil ={

    parse: function (value, pattern){
                
        var day = _getValor(value, pattern, 'dd');
        var month = _getValor(value, pattern, 'MM');
        var year = _getValor(value, pattern, 'yyyy');        
        if(year == 0)
            year = _getValor(value, pattern, 'yy');
        return new Date(year, month, day);
    },
    
    _getPatternValue: function(value, pattern, prefix){        
        var posIni = pattern.indexOf(prefix);
        var posFim = posIni+ prefix.length;
        
        if(posIni >= 0 && posFim<= value.length)
            return new Number(value.substring(posIni, posFim));
                    
        return 0;   
    }

}

/********* Fim da Classe <jsutil.util.DateUtil>  **********/



function testJS_util_DateUtil(){
    var DateUtil= _js.util.DateUtil;
    assertEquals('_getPatternValue',DateUtil._getPatternValue('sçjshk27mkljjk','sçjshkddmkljjk','dd'),27);
    assertEquals('_getPatternValue',DateUtil._getPatternValue('sçjshk27mkljjk','sçjshkddmkljjk','ddd'),0);   
}//<script language="javascript"> 


/**
 * Inicio da Classe 
 * <jsutil.el.ELUtil>
 */ 

_js.createPackage("el");

_js.el.ELUtil ={

    /**
     * Cria um node
     */
    createNode : function (parent, name, value){
        var node = document.createElement(name);
        if(value){
            var text = document.createTextNode(value);
            node.appendChild(text);
        }
        if (parent)
            parent.appendChild(node);
        return node;
    },

    /**
     * Retorna o conteudo da primeira subexpressao da expressao dada
     */
    subExpression : function (expression){
        var exp=expression.substring(expression.indexOf(".{")+2);//retira ate primeiro .{
        var ret="";
        
        var ini= exp.indexOf(".{");
        var fim= exp.indexOf("}");
        while(ini!=-1 && ini < fim){
            ret+=exp.substring(0, fim+1);
            exp=exp.substring(fim+1);
            ini= exp.indexOf(".{");
            fim= exp.indexOf("}");
        }    
        return ret + exp.substring(0, fim);    
    },


    /**
     * Avalia uma expressao
     */
    evaluate : function (node, expressao){    
        
        var exp= expressao
        var ret= "";
        
        var pos1= exp.indexOf(".{");
        var pos2= exp.indexOf("}");
            
        while( pos1 >=0 ){
            
            
            var pos3= exp.substring(pos1+2).indexOf(".{");
            pos3 = (pos3!=-1)? pos3+pos1+2 : pos3;
            
            if (pos3!=-1 && pos3 < pos2 ){// Se .{ for antes de }
                
                ret+= exp.substring(0,pos1);
                var aux = _js.el.ELUtil.subExpression(exp.substring(pos1));
                ret+= _js.el.ELUtil.expressionEL(node, aux);
                exp=exp.substring(pos1+aux.length+3);//ate depois de}
                
            }else{ // avalia expressao
                ret+= exp.substring(0,pos1);
                ret+=_js.el.ELUtil.simpleEL(node,exp.substring(pos1,pos2+1));//TODO
                exp=exp.substring(pos2+1);
                
            }
            pos1= exp.indexOf(".{");
            pos2= exp.indexOf("}");
        }
        
        return ret + exp;
    },


    expressionEL : function (node, expressao){var exp= expressao
        var exp= expressao
        var ret= "";
        
        var pos1= exp.indexOf(".{");
        var pos2= exp.indexOf("}");
            
        while( pos1 >=0 ){        
            
            var pos3= exp.substring(pos1+2).indexOf(".{");
            pos3 = (pos3!=-1)? pos3+pos1+2 : pos3;
            
            if (pos3!=-1 && pos3 < pos2 ){// Se .{ for antes de }
                
                ret+= exp.substring(0,pos1);
                var aux= _js.el.ELUtil.subExpression(exp.substring(pos1));
                ret+= '"' + _js.el.ELUtil.evalueEL(node, aux) + '"';
                exp=exp.substring(pos1 + aux.length + 3);
                
            }else{ // avalia expressao
                ret+= exp.substring(0,pos1);
                ret+='"' + _js.el.ELUtil.simpleEL(node,exp.substring(pos1,pos2+1)) + '"';//TODO
                exp=exp.substring(pos2+1);            
            }
            
            pos1= exp.indexOf(".{");
            pos2= exp.indexOf("}");
        }
        ret+=exp;    
        return eval(_js.el.ELUtil.normalizeEL(ret));
    },

    normalizeEL : function (expression){
        var exp= expression.replace("and","&&")
        if (exp==expression)
            return exp;
        else
            return _js.el.ELUtil.normalizeEL(exp);
    },

    simpleEL : function (node, expressao){
        var resp = expressao;
        var ini = resp.indexOf(".{");
        while(ini >= 0){
            var fim = resp.indexOf("}");
            if (fim == -1)
                return resp;
            var valor = resp.slice(ini + 2, fim);
             
            valor = _js.el.ELUtil.interprete(node, valor);
            var pos = resp.slice(fim + 1);
            var pre = resp.slice(0, ini);
            resp = pre.concat(valor, pos);
             
            ini = resp.indexOf(".{");
        }
        return resp;
    },

    interprete : function (node, expressao){
        var n = node;
        if(!(expressao == "")){
            var atts = expressao.split(".");
            
            for(var i = 0; i < atts.length; i++) {
                if (!n){
                    return "";//caso nao tenha mais nós
                }
                n = n.getElementsByTagName(atts[i])[0];
            }
        }
        if (n.firstChild && n.firstChild.nodeValue){
            return n.firstChild.nodeValue;
        }
        return "";//Caso nao tenha nenhum valor
    },
     
    getNode : function (root, expressao){
        var atts = expressao.split('.');
        var n = root;
        for(var i = 0; i < atts.length; i++) {
            if (!n){
                return false;
            }
                
            n = n.getElementsByTagName(atts[i])[0];
        }
        return n;
    }
}


//********* Fim da Classe <jsutil.el.ELUtil> **********


/**
 * Testes de Unidade
 */
function testJS_el_ELUtil(){
    
    var ELUtil=_js.el.ELUtil;
    
    var node= ELUtil.createNode(null,"pai",null);    
    var aux1;
    aux1= ELUtil.createNode(node,"filho1","joao");
    aux1= ELUtil.createNode(aux1,"neto1","joaozim");
    aux1= ELUtil.createNode(aux1,"neto2","joaozinho");
    aux1= ELUtil.createNode(node,"filho2","pedro");
    
    /**/
    assertEquals("createNode",aux1!=null,true);
    assertEquals("createNode",ELUtil.interprete(aux1,""),"pedro");
    assertEquals("createNode",ELUtil.interprete(node,"filho1"),"joao");
    assertEquals("createNode",ELUtil.interprete(node,"filho1.neto1"),"joaozim");
    
    /**/
    assertEquals("subExpression",ELUtil.subExpression(".{ct1}"),"ct1");
    assertEquals("subExpression",ELUtil.subExpression("akjskl.{ct1}.{ct2}"),"ct1");
    assertEquals("subExpression",ELUtil.subExpression("akjskl.{.{ct1}}.{ct2}"),".{ct1}");
    assertEquals("subExpression",ELUtil.subExpression("akjskl.{aaa.{ct1}bbb}.{ct2}"),"aaa.{ct1}bbb");
    assertEquals("subExpression",ELUtil.subExpression("akj}skl.{.{ct1}.{ct2}ddf.{ct3}xjj}.{ct2}"),".{ct1}.{ct2}ddf.{ct3}xjj");
    
    /**/
    assertEquals("evaluate",ELUtil.evaluate(aux1,".{}"),"pedro");
    assertEquals("evaluate",ELUtil.evaluate(node,".{filho1}"),"joao");
    assertEquals("evaluate",ELUtil.evaluate(node,".{filho1.neto1}"),"joaozim");
    assertEquals("evaluate",ELUtil.evaluate(node,
        ".{( .{filho1.neto1} == \"joaozim\") ? .{filho1.neto1} : \"nao\"}"),"joaozim");
    assertEquals("evaluate",ELUtil.evaluate(node,
        ".{( .{filho1.neto1} == .{filho1.neto2} ) ? .{filho1.neto1} : .{filho1.neto2} }"),"joaozinho");
    assertEquals("evaluate",ELUtil.evaluate(node,
        ".{( .{filho1.neto1} != \"\" and .{filho1.neto3.neto3}==\"\" ) ? \"teste ok\" : .{filho1.neto2} }"),"teste ok");    
}


try{
    var StyleUtil = _js.dhtml.StyleUtil;
    var DHTMLUtil = _js.dhtml.DHTMLUtil;
    var SelectUtil = _js.dhtml.SelectUtil;
    var MaskObjects = _js.mask.MaskObjects;
    var DateMask = _js.mask.DateMask;
    var DecimalMask = _js.mask.DecimalMask;
    var FixMask = _js.mask.FixMask;
    var DoubleFixMask = _js.mask.DoubleFixMask;
    var RegexCharMask = _js.mask.RegexCharMask;
}catch(e){}