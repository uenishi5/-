
window.onload = function(){	
	var dd = new Date();
	//「時」を取得する
	var hh = dd.getHours();
	var precipitation = document.getElementById("precipitation"); 
	
	var msg1 = "<span th:text=\"${weather.chanceOfRain_T00_06}\"></span>";
	var msg2 = "<span th:text=\"${weather.chanceOfRain_T06_12}\"></span>";
	var msg3 = "<span th:text=\"${weather.chanceOfRain_T12_18}\"></span>";
	var msg4 = "<span th:text=\"${weather.chanceOfRain_T18_24}\"></span>";
	
	if(hh >= 0 AND hh < 6){
		precipitation.innerHTML(msg1);
	}else if(hh >= 6 AND hh < 12){
		precipitation.innerHTML(msg2);
	}else if(hh >= 12 AND hh < 18){
		precipitation.innerHTML(msg3);
	}else {
		precipitation.innerHTML(msg4);
	}
}
