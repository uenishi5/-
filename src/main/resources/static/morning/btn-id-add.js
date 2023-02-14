window.onload = $(function(){
	var moji = "testID"
    var tmp = document.getElementsByClassName("hoge") ;

    for(var i=0;i<=tmp.length-1;i++){
        //id追加
        tmp[i].setAttribute("id",moji+i);
    }
    
	});