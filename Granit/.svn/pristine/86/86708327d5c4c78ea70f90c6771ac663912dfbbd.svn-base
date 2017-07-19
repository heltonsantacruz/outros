
function validaCep(campo){
	var cep = campo.value;	
	if(cep == '') return true	
	cep = cep.match(/([0-9][0-9].[0-9][0-9][0-9]-[0-9][0-9][0-9])/);	
	if (cep == null){
		alert("Dado inválido");
		campo.value="";
		return false;
	}
	return true;

}