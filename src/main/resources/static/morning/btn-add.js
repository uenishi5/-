window.onload = $(function(){
	    var cnt = $(".hoge").length;
	    var i = 0;	
	    
	    const text = document.getElementById('text');
	    
	    while(i < cnt){
		var cs = document.getElementsByClassName("morning");
			myid = 'buttonID' + i;
			divid = 'testID' + i;
			myname = cs[i].getAttribute("name");
			text.innerHTML += '<p>' + myname + 'ﾎﾞﾀﾝ</p><input id=\"' + myid + '\" type=\"button\" value = "表示" name=\"' + i + '\" onclick=\"clickBtn(' + myid + ')\">';
			i++;
		}
	});
	