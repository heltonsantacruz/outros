 function validaEmail(campo) {	
	var email = campo.value;
	if(email == '') return true;
	email = email.match(/(\w+)@(.+)\.(\w+)$/);
	if (email == null){
		alert("Dado inv�lido");
		campo.value="";
		return false;
	}
	return true;
}