//<script  language="JavaScript1.2"> 

var MascaraFactory = {    
    fixa: function (obj, mascara){    
        return new FixMask(obj, mascara);
    },
    
    fixaDupla :function (obj, mascara){
        var mask1 = mascara.substring(0,mascara.indexOf(";;"));
        var mask2 = mascara.substring(mascara.indexOf(";;")+2);
        return new DoubleFixMask(obj, mask1, mask2);
    },
    
    date: function (obj, mascara){
        return new DateMask(obj, mascara);
    },
    
    decimal: function(obj, mascara){
        return new DecimalMask(obj, mascara);
    },
    
    charRegex: function(obj, mascara){
        return new RegexCharMask(obj, mascara);
    }
}


var Mascara = {    
    
    adiciona: function (objeto, mascara){
        objeto = DHTMLUtil.getObject(objeto);
        if (objeto){
	        var id = mascara.substring(0,mascara.indexOf(":"));
    	    var params=mascara.substring(mascara.indexOf(":")+1);
	        if (objeto.length){
    	        var last;
        	    for(i=0; i<objeto.length;i++){
            	    last = MascaraFactory[id](objeto[i], params);        
	            }
    	        return last;
        	} else {
            	//Chama o metodo de criacao da mascara
	            return MascaraFactory[id](objeto, params);        
	        }
	    }
    },

    remove: function (objeto){
        objeto = DHTMLUtil.getObject(objeto);
        if (objeto.length){
            for(i=0; i<objeto.length;i++){
                MaskObjects.removeObserver(objeto[i]);
            }
        } else {
            MaskObjects.removeObserver(objeto);
        }
    }
    
}
