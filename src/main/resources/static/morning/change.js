'use strict'	

function clickBtn(){
	const elm = document.getElementById("testID0");

	
	if(elm.style.display=="inline"){
		// hiddenで非表示
		elm.style.display ="none";
		this.value = '非表示';
	}else{
		// visibleで表示
		elm.style.display ="inline";
		this.value = '表示';
		
	}
	

}
