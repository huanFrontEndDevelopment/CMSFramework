/**
 * 获取url参数(get方式)
 * @param name 参数名
 * @returns  参数值
 * @constructor
 */
function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}
$(document).ready(function(){
    var flag = GetQueryString("redirectFlag");
    if(flag == 1){
        alert("用户未登录或登录超时,请重新登录")
    }
    $("#submit_btn").click(function(){
        checkInput();
    });
});

$(document).keypress(function(e) {
    switch(e.which){
        case 13:checkInput();break;
    }
});

function checkInput(){
    var userName = $("#userName").val();
    var passWd = $("#passWd").val();
    if(userName==""||passWd==""){
        $("#error_message_text").html("用户名/密码不能为空");
        $('#myModal').modal('show');
    }else{
        ajax_request("./login/checkUser.json",{ "uname": userName,"upasswd":passWd},function(){
            location.href="./login/goIndex.do";
        });
    }
}

function ajax_request(url,para_data,callback){
    $.post(url,para_data,function(data_str){
        var data;
        if(data_str == null){
            callback();
        }
        if(typeof(data_str) == "object" && Object.prototype.toString.call(data_str).toLowerCase() == "[object object]" && !data_str.length){
            //data_str为json
            data = data_str;
            console.log("data_str="+JSON.stringify(data_str));
        }else{
            //data_str为string
            data = JSON.parse(data_str);
            console.log("data_str="+data_str);
        }

        if(data.hasOwnProperty("code")) {
            switch (data.code){
                case 1 : callback();break;
                default:{
                    $("#error_message_text").html(data.description+",错误代码:"+data.code);
                    $('#myModal').modal('show');
                }
            }
        }else{
            $("#error_message_text").html("数据接口出错");
            $('#myModal').modal('show');
        }

    });
}