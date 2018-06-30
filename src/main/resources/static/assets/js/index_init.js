$(document).ready(function(){
    getTotalStudent();
    getTotalPay();
    getMonthPay();
    getTotalGrade();
});
function getTotalStudent(){
    $.ajax( {
        "type": "get",
        "contentType": "application/json",
        "url": "../student/data/studentTotal",
        "dataType": "json",
        "success": function(data) {
            $("#student_total_num").html(data);
            getAnalysisStudent(data);
        }
    });
}
function getTotalPay(){
    $.ajax( {
        "type": "get",
        "contentType": "application/json",
        "url": "../student/data/totalIncome",
        "dataType": "json",
        "success": function(data) {
            $("#student_total_pay").html(data);
            getAnalysisCourse(data);
        }
    });
}
function getAnalysisCourse(total_income){
    $.ajax( {
        "type": "get",
        "contentType": "application/json",
        "url": "../grade/analysis/courseIncome",
        "dataType": "json",
        "success": function(data) {
            var array_data = data.info;
            var item_title = [];
            var item_value = [];
            var total = 0;
            for(var i=0 ; i<array_data.length ; i++){
                item_title.push(array_data[i].courseName);
                var json_obj = {};
                json_obj["value"] = array_data[i].totalPay;
                total += array_data[i].totalPay;
                json_obj["name"] = array_data[i].courseName;
                item_value.push(json_obj);
            }
            var other_pay = total_income - total;
            var other_obj = {};
            other_obj["value"] = other_pay;
            other_obj["name"] = "其它";
            item_value.push(other_obj);
            item_title.push("其它");
            initTablePie("course_analysis_bar","课程收入分析","",item_title,"总收入",item_value);
        }
    });
}

function getAnalysisStudent(total_student){
    $.ajax( {
        "type": "get",
        "contentType": "application/json",
        "url": "../student/analysis/student",
        "dataType": "json",
        "success": function(data) {
            var array_data = data.info;
            var item_title = [];
            var item_value = [];
            var total = 0;
            for(var i=0 ; i<array_data.length ; i++){
                item_title.push(array_data[i].courseName);
                var json_obj = {};
                json_obj["value"] = array_data[i].total;
                total += array_data[i].total;
                json_obj["name"] = array_data[i].courseName;
                item_value.push(json_obj);
            }
            var other_pay = total_student - total;
            var other_obj = {};
            other_obj["value"] = other_pay;
            other_obj["name"] = "其它";
            item_value.push(other_obj);
            item_title.push("其它");
            initTablePie("student_analysis_bar","学员报名分析","",item_title,"总学员",item_value);
        }
    });
}

function getTotalGrade(){
    $.ajax( {
        "type": "get",
        "contentType": "application/json",
        "url": "../grade/grade/total",
        "dataType": "json",
        "success": function(data) {
            $("#grade_total_num").html(data);
        }
    });
}


function getMonthPay(){
    $.ajax( {
        "type": "get",
        "contentType": "application/json",
        "url": "../student/data/monthTotalIncome",
        "dataType": "json",
        "success": function(data) {
            $("#student_month_pay").html(data);
        }
    });
}

function initTablePie(id,title,draw_date,item,item_title,item_value){
    var pie_obj = echarts.init(document.getElementById(id));
    var option = {
        title : {
            text: title,
            subtext: draw_date,
            x:'center'
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: item
        },
        series : [
            {
                name: item_title,//
                type: 'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:item_value,//,
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    pie_obj.setOption(option);
    return pie_obj;
}
