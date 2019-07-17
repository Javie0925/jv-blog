var datalist
var likes
var likeID
//点击回复创建回复块
function answer(id) {
	  if ($('#'+id).find('.answer_block'+id).html()) { $('.answer_block'+id).remove()}
	  else{
	  	var cname = '.answer_block'+id
		var fhN = "回复："
	  	var fhHtml = '<div class="answer_block'+id+'"> <textarea class="hf_input" placeholder="输入回复内容"  ></textarea> \
	  	<div class="Csunbmit"><label class="Cnick">你的昵称：</label><input class="Cusername" placeholder="你的昵称"></input>\
					<button class="submitBtn" onclick=submit_pl("'+cname+'")>确定回复</button></div></div>';
	   $('#'+id).append(fhHtml);
	   $(cname).find('.hf_input').css('width','99%')
	   $( cname).find('.hf_input').css('height','90px')
	   $( cname).find('.hf_input').css('resize','none')
	   $( cname).children('.hf_input').focus().val(fhN);
	  }
}

function like(id) {
	likes = parseInt($('#likeNum'+id).html())+1
	likeID = id
	ajaxPostReq('php/likeNum.php',{'id':id,'like':likes,'time':get_time()},{"1":'感谢您的支持！','0':"您已支持过了~"},voids)
}

function get_time( ) {
	  var myDate = new Date();
        //获取当前年
        var year=myDate.getFullYear();
        //获取当前月
        var month=myDate.getMonth()+1;
        //获取当前日
        var date=myDate.getDate();
        var h=myDate.getHours();       //获取当前小时数(0-23)
        var m=myDate.getMinutes();     //获取当前分钟数(0-59)
        if(m<10) m = '0' + m;
        var s=myDate.getSeconds();
        if(s<10) s = '0' + s;
        var now=year+'-'+month+"-"+date+" "+h+':'+m+":"+s;
        return now
}

function submit_pl(cname) {
	var pid
	var content
	var unickname
	var attitude
	var time = get_time()
	var data ={}
	if (cname==0) {
		pid=0
		content = $('#Ccont').val()
		unickname = $('#Cusername').val()
		unickname = unickname.replace(/^ +| +$/g,'')?unickname.replace(/^ +| +$/g,''):'游客'
		attitude = $("input[name='attitude']:checked").val();
		if ( !content.replace(/^ +| +$/g,'') ) {return}
		$('#Ccont').val('')
	}
	else{
		pid = cname.split('block')[1]
		content = $(cname).children('.hf_input').val()
		unickname =  $(cname).children('.Csunbmit').children('.Cusername').val()
		unickname =unickname.replace(/^ +| +$/g,'')?unickname.replace(/^ +| +$/g,''):'游客'
		attitude = 2;
		if (!content.replace('回复：','').replace(/^ +| +$/g,'')) {return}
		$(cname).children('.hf_input').val('')
		$(cname).remove()
	}
	
	data.pid = pid
	data.content = content
	data.name = unickname
	data.attitude = attitude
	data.time = time
	ajaxPostReq('php/add.php',data,{'0':'评论失败','1':'评论成功'},reflash)
}

function ajaxPostReq(handler,data,msg,cb) {
	$.ajax({
		type:"POST",
		cache:false,
		dataType:'text',
		url:handler,
		data:data,
		success:function (response) {
			alert(msg[response])
			cb(response)
		}
	})
}
//数据获取
function ajaxInitReq(handler,data,cb) {
   $.ajax({
		type:"POST",
		dataType:'text',
		url:handler,
		data:data,
		success:function (response) {
			cb(response)
		}
	})
}

function reflash(response) {
	start(0)
}

function voids(response){
	if (response==1) {
		$('#likeNum'+likeID).html(likes)
	}
}

function render(data) {
	datalist = JSON.parse(data)
	getPage(1)
}

function start(mode) {
	ajaxInitReq('php/handler.php',{'m':mode},render)
}