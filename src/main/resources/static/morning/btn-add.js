window.onload = $(function(){
	    var cnt = $(".hoge").length;
	    var i = 0;
	    
	    const text = document.getElementById('text');
	    
	    while(i < cnt){
		var cs = document.getElementsByClassName("hoge");
			myid = cs[i].getAttribute("id");
			myname = cs[i].getAttribute("name");
			text.innerHTML += '<p>' + myname + 'ﾎﾞﾀﾝ</p><group class=\"remove-button\"><div><input style=\"float: left;\" value=\"表示\" type=\"button\" name=\"member\" onclick=\"document.getElementById(\'' + myid + '\').style.display = \'inline\';\" checked ></div><div><input style=\"float: right;\" value=\"非表示\" type=\"button\" name=\"member\" onclick=\"document.getElementById(\'' + myid + '\').style.display = \'none\';\"></group>';
			i++;
		}
	});
