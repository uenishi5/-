window.onload = function(){	
	for(let i = 0; i > 10; i++){
	var www = document.getElementByClassName('line')[i];
	if(www.innerHTML == "警報級"){
		www.style.color = 'red';
		www.style.backgroundColor = 'red';
	}else if(www.innerHTML == "注意報級"){
		www.style.color = 'yellow';
		www.style.backgroundColor = 'yellow';
	}else if(www.innerHTML == "発表なし"){
		www.style.color = 'gray';
		www.style.backgroundColor = 'gray';
	}else {
		www.style.color = 'black';
		www.style.backgroundColor = 'black';
	}
	}
}