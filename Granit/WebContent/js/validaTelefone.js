function validaTelefone(campo){	
	var fone = campo.value;
	if(fone == '') return true	
	fone = fone.match(/(\([0-9][0-9]\)[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9])/);	
	if (fone == null){
		alert("Dado inv�lido");
		campo.value="";
		return false;
	}
	return true;

}