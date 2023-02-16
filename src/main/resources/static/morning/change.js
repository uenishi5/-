'use strict'	

function clickBtn(myid){
	
	var j = myid.name;
	
	var testID = document.getElementById("testID"  + j);
	var buttonID = document.getElementById("buttonID" + j);
	var isHidden = testID.style.display == "none";
	
	if(isHidden){
		// visibleで表示
		testID.style.display ="inline";
		buttonID.value = '表示';

	}else{
		// hiddenで非表示
		testID.style.display ="none";
		buttonID.value = '非表示';
		
	}
	

}

