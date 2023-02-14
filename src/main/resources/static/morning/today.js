var today=new Date(); 

//年・月・日・曜日を取得
var month = today.getMonth()+1;
if(!count) var count = 1; // 今日か明日かチェック
else count++;
if(count == 1){
var week = today.getDay();
var day = today.getDate();
}else{
	var week = today.getDay()+1;
    var day = today.getDate()+1;
}
var week_ja= new Array("日","月","火","水","木","金","土");

//年・月・日・曜日を書き出す
document.write(month+"月"+day+"日 "+week_ja[week]+"曜日");
