window.lgyPl={
    config:{
        sever:extendRoot, //插件目录
        loading:'',
        hasmore:0,
        commentonload:0,
        scrollloadInit:0,
        paginationInit:0,
        user:'',
        commentIndex:1,
        commentQuery:'all',
        mycity:'',
        scrollload:0, //是否支持滚动加载
        scroll:0, //是否支持滚动加载
        scrollbottom:50,//底部触发距离
        pagination:0,
        pageline:5,//分页模式连续分页数
        pagefirst:false,//分页模式显示首页
        pagelast:false,//分页模式显示尾页
        pageskip:0,//分页模式显示跳页
        pagesize:20,
        showhot:1,
        hotpagesize:10,
        classid:0,
        id:0
    },
    init:function(options){
        var def=$.extend({},this.config,options);
        var that = this;
        lgyPl.config = def;
        that.xhr({
            api:'template',
            dataType:'html'
        },function(html){
            $('.pl-520am').html(html);
            that.getNewsComment();
            that.config.showhot?that.getNewsHotComment():null;
        });

        //this.checkUser();
    },
    mScroll:function(id){
        $("html,body").stop(true);
        $("html,body").animate({scrollTop: $("#"+id).offset().top}, 500);
    },
    checkUser:function(showInfo){
        this.xhr({
            api:'userinfo',
            method:'post'
        },function(json){
            if(json.err_msg=='success'){
                lgyPl.config.user = json.data;
                var havamsg = '';
                if(json.data.havemsg==1){
                    havamsg = '<i class="havemsg"></i>';
                }
                $('.showUserPic').html(havamsg+'<img src="'+json.data.userpic+'">');
            }else{
                showInfo?lgyPl.show(json.info):null;
            }
        });
    },
    xhr:function(query,callback,type){
        var api =query.api;
        var params = query.data || {};
        var method=query.method || 'get';
        var dataType=query.dataType || 'json';
        params.appkey = this.config['appkey'];
        var url = query.url || lgyPl.config.sever+api+'.php?ajax=1&_t='+Date.parse(new Date());
        $.ajax({
            url:url,
            data:params,
            dataType:dataType,
            method:method,
            type:method,
            success:function(data){
                typeof(callback === 'function') ? callback(data) : null;
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){
                if(textStatus=='error'){
                    lgyPl.show('连接失败,请检查您的网络！');
                }
            }
        });

    },
    show:function(text,time,callback){

        layer.msg(text,{time:1000},function(){
            if(callback){
                typeof(callback === 'function') ? callback() : null;
            }
        });
    },
    showLoading:function(){
        this.config.loading=layer.open({type: 2});
    },
    hideLoading:function(){
        layer.close(this.config.loading);
    },
    formToJson:function(id){
        $.fn.serializeObject = function()
        {
            var o = {};
            var a = this.serializeArray();
            $.each(a, function() {
                if (o[this.name]) {
                    if (!o[this.name].push) {
                        o[this.name] = [o[this.name]];
                    }
                    o[this.name].push(this.value || '');
                } else {
                    o[this.name] = this.value || '';
                }
            });
            return o;
        };
        return json = $(id).serializeObject();
    },
    trim:function(str){
        if(str){
            str = str.replace(/<[^>]+>/g,""); //去除html标签
            str = str.replace(/(^\s+)|(\s+$)/g,"");//去除空格
            str = str.replace(/[ ]/g,"");//去除回车
            return str = str.replace(/[\r\n]/g,"");//去除空格
        }
        return false;
    },
    show:function(info,time,callback){
        var that = $('.pl-showinfo');
        if($('.pl-showinfo-reply').length>0){
            that = $('.pl-showinfo-reply');
        }
        that.html(info).stop().fadeIn();
        setTimeout(function(){
            that.fadeOut();
            if(callback&&typeof(callback)=='function'){
                callback();
            }
        },time || 3000);
    },
    tm:function(id,data){
        var compiledTemplate = Handlebars.compile($('#'+id).html());

        var html = compiledTemplate(data);
        return html;
    },
    showPickFace:function(evt,reply){
        evt.stopPropagation?evt.stopPropagation():evt.cancelBubble=true;
        var obj = reply?'#pl-face-box-reply':'#pl-face-box';
        if($(obj).is(':hidden')){
            $('.pl-face-box').hide();
            $(obj).fadeIn();
            $('.pl-face-box-before').on('click',function(){
                $(obj).fadeOut();
            });
            $('.pl-img-box').hide();
        }else{
            $(obj).fadeOut();
        }
    },
    showPickImg:function(evt,reply){
        evt.stopPropagation?evt.stopPropagation():evt.cancelBubble=true;
        var obj = reply?'#pl-img-box-reply':'#pl-img-box';
        if($(obj).is(':hidden')){
            $('.pl-img-box').hide();
            $(obj).fadeIn();
            $('.pl-face-box').hide();
            $('.pl-img-box-before').on('click',function(){
                $(obj).fadeOut();
            });
        }else{
            $(obj).fadeOut();
        }
        $('.pl-img-file input').on('click',function(evt){
            evt.stopPropagation?evt.stopPropagation():evt.cancelBubble=true;
        });
        $(obj).find('button').off('click').on('click',function(evt){
            var src = $(obj).find('input').val();
            if(!src)return;
            var tag='[img]'+src+'[/img]';
            lgyPl.addplface(tag,reply);
            $(obj).find('input').val('');
        });
    },
    addplface:function(tag,reply) {
        var myField;
        var id =reply ? 'pl-520am-f-saytext-reply' : 'pl-520am-f-saytext';
        if (document.getElementById(id) && document.getElementById(id).type == 'textarea') {
            myField = document.getElementById(id);
        }
        else {
            return false;
        }
        if (document.selection) {
            myField.focus();
            sel = document.selection.createRange();
            sel.text = tag;
            myField.focus();
        }
        else if (myField.selectionStart || myField.selectionStart == '0') {
            var startPos = myField.selectionStart;
            var endPos = myField.selectionEnd;
            var cursorPos = endPos;
            myField.value = myField.value.substring(0, startPos)
                + tag
                + myField.value.substring(endPos, myField.value.length);
            cursorPos += tag.length;
            myField.focus();
            myField.selectionStart = cursorPos;
            myField.selectionEnd = cursorPos;
        }
        else {
            myField.value += tag;
            myField.focus();
        }
        $('.pl-face-box').hide();

    },
    updateKey:function(){
        var src = $('img[name="plKeyImg"]').attr('src');
        $('img[name="plKeyImg"]').attr('src',src+'&t='+Math.random());
    },
    showReply:function(plid,username){
        var obj = $('#pl-show-box-'+plid).find('.pl-show-replay');
        var output=lgyPl.tm('PlReplyTemplate',{plid:plid,username:username});
        if(obj.find('.pl-post').length>0){
            obj.empty();
        }else{
            $('.pl-show-replay').empty();
            obj.html(output);
            lgyPl.updateKey();
        }
    },
    doForPl:function(plid,dopl,that){
        var obj=$('#pl-'+dopl+'-'+plid);
        var num = Number(obj.text())+1;
        this.xhr({
            api: 'doaction',
            data:{enews:'DoForPl',plid:plid,dopl:dopl,id:this.config.id,classid:this.config.classid},
            method:'post'
        },function(json){

            if(json.err_msg=='success'){
                obj.text(num);
                $(that).append('<span class="pl-plusone" style="position:absolute;color:red;font-size:13px;z-index:9;right:0px;top:0">+1</span>');
                $('.pl-plusone').animate({top:'-20px','opacity':0},800,function(){
                    $('.pl-plusone').remove();

                });

            }else{
                $('#pl-err-info-'+plid).html(json.info).fadeIn();
                setTimeout(function(){
                    $('#pl-err-info-'+plid).fadeOut();
                },1000)
            }
        });
    },
    submitComment:function(obj,repid){
        var saytext=$('#pl-520am-f-saytext');
        var that = $('.pl-520am');
        var classid=that.data('classid');
        var id=that.data('id');
        var key=$('#pl-key');
        var username=$("#pl-username");
        if(repid){
            saytext = $('#pl-520am-f-saytext-reply');
            key=$('#pl-key-reply');
            username=$("#pl-username-reply");
        }else{
            $('.pl-show-replay').empty();
        }
        if(saytext.val()==''){
            lgyPl.show('未输入评论内容');
            return;
        }
        if(key.val()!=undefined&&key.val()==''){
            lgyPl.show('请输入验证码');
            return;
        }
        $(obj).attr('disabled',true).html('<img src="'+lgyPl.config.sever+'assets/loading.gif">');
        var post = {
            repid:repid,
            id:lgyPl.config.id,
            classid:lgyPl.config.classid,
            saytext:saytext.val(),
            key:key.val(),
            username:username.val(),
            enews:'AddPl'
        }
        lgyPl.xhr({
            api: 'doaction',
            data:post,
            method:'post'
        },function(json){
            $(obj).removeAttr('disabled',false).html('发 布');
            if(json.err_msg=='success'){
                lgyPl.show(json.info,'',function(){
                    $('.pl-show-replay').empty();
                });
                saytext.val('');
                key.val('');
                lgyPl.getNewsComment(1);
                lgyPl.updateKey();

            }else{
                lgyPl.show(json.info);
            }
        });
    }
    ,getNewsHotComment:function(){
        lgyPl.xhr({
            api: 'list',
            data:{id:this.config.id,classid:this.config.classid,pageSize:this.config.hotpagesize,query:'hot'},
            method:'post'
        },function(json){
            if(json.err_msg == 'success') {


                if(json.total>0){
                    var output=lgyPl.tm('NewsCommentTemplate',{data:json.data});
                    $('#pl-show-hot').html(output);
                    $('.pl-show-hot-list').fadeIn();
                }


            }else {

            }
        });

    },getNewsComment:function(reply,obj,type){
        $('.newscommentText').removeAttr('disabled');
        var id=$('body').attr('data-id');
        var classid=$('body').attr('data-classid');
        lgyPl.config.commentQuery = type?type:'all';
        var info = type=='hot'?'暂无热门评论':'暂无评论';
        $('.showNewsCommenetArea').removeAttr('id').attr('id','show-'+lgyPl.config.commentQuery);
        if(obj)$(obj).attr('disabled',true).html('载入中,请稍后...');

        if(reply&&!lgyPl.config.pagination){
            lgyPl.config.commentIndex = 1;
            $('.showNewsCommenetArea').html('<p class="null NewsComment_loading">loading...</p>');
            $('.showAllComment').removeAttr('disabled').hide().html('查看更多');
        }
        lgyPl.xhr({
            api: 'list',
            data:{id:this.config.id,classid:this.config.classid,pageSize:this.config.pagesize,pageIndex:this.config.commentIndex,query:this.config.commentQuery},
            method:'post'
        },function(json){
            if(obj)$(obj).removeAttr('disabled').html('查看更多');
            lgyPl.config.commentonload = 0 ;//解除锁定
            lgyPl.config.hasmore = json.hasmore; //是否有下一页
            if (json.err_msg == 'success') {
                var output=lgyPl.tm('NewsCommentTemplate',{data:json.data});

                /*--设置翻页--*/
                if(lgyPl.config.pagination){ //分页模式
                    $('#pl-show-'+lgyPl.config.commentQuery).html(output);
                    $('.pl-show-list-loading').remove();
                    if(!lgyPl.config.paginationInit){
                        includeFile(extendRoot+'assets/laypage/laypage.js',function(){
                            laypage.dir = extendRoot+'assets/laypage/laypage.css';
                            laypage({
                                cont: 'pl-pagination',
                                pages: json.pageTotal,
                                first: lgyPl.config.pagefirst,
                                last: lgyPl.config.pagelast,
                                groups:lgyPl.config.pageline,
                                skip:lgyPl.config.pageskip,
                                jump: function(obj, first){
                                    var curr = obj.curr;
                                    if(lgyPl.config.paginationInit){
                                        lgyPl.config.commentIndex = curr;
                                        $('.pl-show-list-loading').remove();
                                        $('#pl-show-'+lgyPl.config.commentQuery).append('<div class="pl-show-list-loading"></div>');
                                        lgyPl.getNewsComment(0);
                                        lgyPl.mScroll('pl-start');
                                    }
                                }
                            });
                            lgyPl.config.paginationInit=1;
                        });
                    }

                }else{ //追加模式
                    if(json.pageTotal>1){
                        lgyPl.setScrollLoad();
                    }
                    if(json.hasmore){
                        lgyPl.config.commentIndex++;
                    }else{
                        if(obj)$(obj).attr('disabled',true).html('已加载完毕')
                    }
                    $('.NewsComment_loading').remove();
                    if(reply){
                        $('#pl-show-'+lgyPl.config.commentQuery).empty();
                    }
                    if(json.total==0){
                        $('.showNewsCommenetArea').html('<p class="null"><i class="iconfont icon-chat"></i>'+info+'</p>');
                    }
                    $('#pl-show-'+lgyPl.config.commentQuery).append(output);
                }
                /*--end--*/
                $('#pl-totalnum').text(json.total);
                $('#pl-joinnum').text(json.onclick);
            }else {

            }
        });
    },
    setScrollLoad:function(){
        $('.showAllComment').show();
        if(lgyPl.config.scrollload&&!lgyPl.config.scrollloadInit){
            lgyPl.config.scrollloadInit=1; //已初始化滚动加载;
            window.onscroll = function(){
                var scrollTop = document.body.scrollTop;
                var bot = lgyPl.config.scrollbottom; //bot是底部距离的高度
                if ((bot + $(window).scrollTop()) >= ($(document).height() - $(window).height())) {
                    if(lgyPl.config.commentonload||!lgyPl.config.hasmore)return;
                    lgyPl.config.commentonload=1;
                    lgyPl.getNewsComment(0,'.showAllComment');
                }
            }
        }

    }

}
window.onclick=function(){
    $('.pl-face-box,.pl-img-box').hide();
}
