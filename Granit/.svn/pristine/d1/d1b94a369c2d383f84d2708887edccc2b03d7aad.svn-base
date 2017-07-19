 function validaEmail(campo) {	
	var email = campo.value;
	if(email == '') return true;
	email = email.match(/(\w+)@(.+)\.(\w+)$/);
	if (email == null){
		alert("Dado inválido");
		campo.value="";
		return false;
	}
	return true;
}