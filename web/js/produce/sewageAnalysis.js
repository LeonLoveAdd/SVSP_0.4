///////////污水分析日报//
var currentPage = 1;                          //当前页数
var isSearch = false;
var data1;

/**
 * 返回count值
 * */
function countValue() {
    var mySelect = document.getElementById("count");
    var index = mySelect.selectedIndex;
    return mySelect.options[index].text;
}

/**
 * 计算总页数
 * */
function totalPage() {
    var totalRecord = 0;
    if (!isSearch) {
        $.ajax({
            type: "POST",                       // 方法类型
            url: "totalSewageRecord",                  // url
            async: false,                      // 同步：意思是当有返回值以后才会进行后面的js程序
            dataType: "json",
            success: function (result) {
                if (result > 0) {
                    totalRecord = result;
                } else {
                    console.log("fail: " + result);
                    totalRecord = 0;
                }
            },
            error: function (result) {
                console.log("error: " + result);
                totalRecord = 0;
            }
        });
    } else {
        $.ajax({
            type: "POST",                       // 方法类型
            url: "searchSewageTotal",                  // url
            async: false,                      // 同步：意思是当有返回值以后才会进行后面的js程序
            data: JSON.stringify(data1),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (result) {
                // console.log(result);
                if (result > 0) {
                    totalRecord = result;
                    console.log("总记录数为:" + result);
                } else {
                    console.log("fail: " + result);
                    totalRecord = 0;
                }
            },
            error: function (result) {
                console.log("error: " + result);
                totalRecord = 0;
            }
        });
    }
    var count = countValue();                         // 可选
    var total = loadPages(totalRecord, count);
    return total;
}

/**
 * 设置克隆页码
 * */
function setPageClone(result) {
    $(".beforeClone").remove();
    setSewageList(result);
    var total = totalPage();
    $("#next").prev().hide();
    var st = "共" + total + "页";
    $("#totalPage").text(st);
    var myArray = new Array();
    for (var i = 0; i < total; i++) {
        var li = $("#next").prev();
        myArray[i] = i + 1;
        var clonedLi = li.clone();
        clonedLi.show();
        clonedLi.find('a:first-child').text(myArray[i]);
        clonedLi.find('a:first-child').click(function () {
            var num = $(this).text();
            switchPage(num);
            addAndRemoveClass(this);
        });
        clonedLi.addClass("beforeClone");
        clonedLi.removeAttr("id");
        clonedLi.insertAfter(li);
    }
    $("#previous").next().next().eq(0).addClass("active");       // 将首页页面标蓝
    $("#previous").next().next().eq(0).addClass("oldPageClass");
}

/**
 * 点击页数跳转页面
 * @param pageNumber 跳转页数
 * */
function switchPage(pageNumber) {
    console.log("当前页：" + pageNumber);
    if(pageNumber > totalPage()){
        pageNumber = totalPage();
    }
    if (pageNumber == 0) {                 //首页
        pageNumber = 1;
    }
    if (pageNumber == -2) {
        pageNumber = totalPage();        //尾页
    }
    if (pageNumber == null || pageNumber == undefined) {
        console.log("参数为空,返回首页!");
        pageNumber = 1;
    }
    $("#current").find("a").text("当前页：" + pageNumber);
    if (pageNumber == 1) {
        $("#previous").addClass("disabled");
        $("#firstPage").addClass("disabled");
        $("#next").removeClass("disabled");
        $("#endPage").removeClass("disabled");
    }
    if (pageNumber == totalPage()) {
        $("#next").addClass("disabled");
        $("#endPage").addClass("disabled");
        $("#previous").removeClass("disabled");
        $("#firstPage").removeClass("disabled");
    }
    if (pageNumber > 1) {
        $("#previous").removeClass("disabled");
        $("#firstPage").removeClass("disabled");
    }
    if (pageNumber < totalPage()) {
        $("#next").removeClass("disabled");
        $("#endPage").removeClass("disabled");
    }
    addPageClass(pageNumber);           // 设置页码标蓝
    var page = {};
    page.count = countValue();                        //可选
    page.pageNumber = pageNumber;
    currentPage = pageNumber;          //当前页面
    //addClass("active");
    page.start = (pageNumber - 1) * page.count;
    if (!isSearch) {
        $.ajax({
            type: "POST",                       // 方法类型
            url: "loadPageSewageList",         // url
            async: false,                      // 同步：意思是当有返回值以后才会进行后面的js程序
            data: JSON.stringify(page),
            dataType: "json",
            contentType: 'application/json;charset=utf-8',
            success: function (result) {
                if (result != undefined) {
                    setSewageList(result.data);
                } else {
                    console.log("fail: " + result);
                }
            },
            error: function (result) {
                console.log("error: " + result);
            }
        });
    } else {
        data['page'] = page;
        $.ajax({
            type: "POST",                       // 方法类型
            url: "searchSewage",         // url
            async: false,                      // 同步：意思是当有返回值以后才会进行后面的js程序
            data: JSON.stringify(data1),
            dataType: "json",
            contentType: 'application/json;charset=utf-8',
            success: function (result) {
                if (result != undefined) {
                    // console.log(result);
                    setSewageList(result.data);
                } else {
                    console.log("fail: " + result);
                }
            },
            error: function (result) {
                console.log("error: " + result);
            }
        });
    }
}

/**
 * 输入页数跳转页面
 * */
function inputSwitchPage() {
    var pageNumber = $("#pageNumber").val();    // 获取输入框的值
    if(pageNumber > totalPage()){
        pageNumber = totalPage();
    }
    $("#current").find("a").text("当前页：" + pageNumber);
    if (pageNumber == null || pageNumber == "") {
        window.alert("跳转页数不能为空！")
    } else {
        if (pageNumber == 1) {
            $("#previous").addClass("disabled");
            $("#firstPage").addClass("disabled");
            $("#next").removeClass("disabled");
            $("#endPage").removeClass("disabled");
        }
        if (pageNumber == totalPage()) {
            $("#next").addClass("disabled");
            $("#endPage").addClass("disabled");

            $("#previous").removeClass("disabled");
            $("#firstPage").removeClass("disabled");
        }
        if (pageNumber > 1) {
            $("#previous").removeClass("disabled");
            $("#firstPage").removeClass("disabled");
        }
        if (pageNumber < totalPage()) {
            $("#next").removeClass("disabled");
            $("#endPage").removeClass("disabled");
        }
        currentPage = pageNumber;
        addPageClass(pageNumber);           // 设置页码标蓝
        var page = {};
        page.count = countValue();//可选
        page.pageNumber = pageNumber;
        page.start = (pageNumber - 1) * page.count;
        if (!isSearch) {
            $.ajax({
                type: "POST",                       // 方法类型
                url: "loadPageSewageList",         // url
                async: false,                      // 同步：意思是当有返回值以后才会进行后面的js程序
                data: JSON.stringify(page),
                dataType: "json",
                contentType: 'application/json;charset=utf-8',
                success: function (result) {
                    if (result != undefined) {
                        console.log(result);
                        setSewageList(result.data);
                    } else {
                        console.log("fail: " + result);
                    }
                },
                error: function (result) {
                    console.log("error: " + result);
                }
            });
        } else {
            data1['page'] = page;
            $.ajax({
                type: "POST",                       // 方法类型
                url: "searchSewage",         // url
                async: false,                      // 同步：意思是当有返回值以后才会进行后面的js程序
                data: JSON.stringify(data1),
                dataType: "json",
                contentType: 'application/json;charset=utf-8',
                success: function (result) {
                    if (result != undefined) {
                        // console.log(result);
                        setSewageList(result.data);
                    } else {
                        console.log("fail: " + result);
                    }
                },
                error: function (result) {
                    console.log("error: " + result);
                }
            });
        }
    }
}

/**
 * 分页 获取首页内容
 * */
function loadPageSewageList() {
    var pageNumber = 1;               // 显示首页
    $("#current").find("a").text("当前页：1");
    $("#previous").addClass("disabled");
    $("#firstPage").addClass("disabled");
    $("#next").removeClass("disabled");            // 移除上一次设置的按钮禁用
    $("#endPage").removeClass("disabled");
    if (totalPage() == 1) {
        $("#next").addClass("disabled");
        $("#endPage").addClass("disabled");
    }
    var page = {};
    page.count = countValue();                                 // 可选
    page.pageNumber = pageNumber;
    page.start = (pageNumber - 1) * page.count;
    $.ajax({
        type: "POST",                       // 方法类型
        url: "loadPageSewageList",          // url
        async: false,                       // 同步：意思是当有返回值以后才会进行后面的js程序
        data: JSON.stringify(page),
        dataType: "json",
        contentType: 'application/json;charset=utf-8',
        success: function (result) {
            if (result != undefined && result.status == "success") {
                console.log(result);
                setPageClone(result.data);
            } else {
                console.log(result.message);
            }
        },
        error: function (result) {
            console.log("error: " + result);
            console.log("失败");
        }
    });
    isSearch = false;
}

/**
 * 计算分页总页数
 * @param totalRecord
 * @param count
 * @returns {number}
 */
function loadPages(totalRecord, count) {
    if (totalRecord == 0) {
        console.log("总记录数为0，请检查！");
        return 0;
    }
    else if (totalRecord % count == 0)
        return totalRecord / count;
    else
        return parseInt(totalRecord / count) + 1;
}

function setSewageList(result) {
    // 获取id为cloneTr的tr元素
    var tr = $("#clone");
    tr.siblings().remove();
    var serialNumber = 0;    // 序号
    $.each(result, function (index, item) {
        serialNumber++;
        // 克隆tr，每次遍历都可以产生新的tr
        var clonedTr = tr.clone();
        clonedTr.show();
        // 循环遍历cloneTr的每一个td元素，并赋值
        clonedTr.children("td").each(function (inner_index) {
            var obj = eval(item);
            // 根据索引为部分td赋值
            switch (inner_index) {
                case (1):
                    // 序号
                    $(this).html(serialNumber);
                    break;
                case (2):
                    // 污水接收日期
                    $(this).html(getDateStr(obj.receiveDate));
                    break;
                case (3):
                    // 污水名称
                    $(this).html(obj.name);
                    break;
                case (4):
                    // COD
                    $(this).html(obj.cod);
                    break;
                case (5):
                    // BOD5
                    $(this).html(obj.bod5);
                    break;
                case (6):
                    // 氧
                    $(this).html(obj.oxygen);
                    break;
                case (7):
                    // 氮
                    $(this).html(obj.nitrogen);
                    break;
                case (8):
                    // 碱液
                    $(this).html(obj.lye);
                    break;
                case (9):
                    // PH
                    $(this).html(obj.ph);
                    break;
                case (10):
                    // 备注
                    $(this).html(obj.remarks);
                    break;
            }
        });
        // 把克隆好的tr追加到原来的tr前面
        clonedTr.removeAttr("id");
        clonedTr.insertBefore(tr);
    });
    // 隐藏无数据的tr
    tr.hide();
}

/**
 * 导出excel
 * @param e
 */
function exportExcel() {
    var name = 't_pr_sewage';
    // 获取勾选项
    var idArry = [];
    $.each($("input[name='select']:checked"),function(index,item){
        idArry.push(item.parentElement.parentElement.nextElementSibling.innerHTML);        // 将选中项的编号存到集合中
    });
    var sqlWords = '';
    var sql = ' in (';
    if (idArry.length > 0) {
        for (var i = 0; i < idArry.length; i++) {          // 设置sql条件语句
            if (i < idArry.length - 1) sql += "'" + idArry[i] + "'" + ",";
            else if (i == idArry.length - 1) sql += "'" + idArry[i] + "'" + ");";
        }
        sqlWords = "select id as '编号', name as '污水名称',receiveDate as '污水接收日期',COD,BOD5,oxygen as '氧',nitrogen as '氮',lye as '碱液',PH,remarks as '备注' from t_pr_sewage where id" + sql;
    }else {          // 若无勾选项则导出全部
        sqlWords = "select id as '编号', name as '污水名称',receiveDate as '污水接收日期',COD,BOD5,oxygen as '氧',nitrogen as '氮',lye as '碱液',PH,remarks as '备注' from t_pr_sewage;";
    }
    console.log("sql:"+sqlWords);
    window.open('exportExcel?name=' + name + '&sqlWords=' + sqlWords);
}

/**
 * 导入模态框
 * */
function importExcelChoose() {
    $("#importExcelModal").modal('show');
}

/**
 * 下载模板
 * */
function downloadModal() {
    var filePath = 'Files/Templates/污水分析日报模板.xls';
    var r = confirm("是否下载模板?");
    if (r == true) {
        window.open('downloadFile?filePath=' + filePath);
    }
}

/**
 * 导入excel
 */
function importExcel() {
    document.getElementById("idExcel").click();
    document.getElementById("idExcel").addEventListener("change", function () {
        var eFile = document.getElementById("idExcel").files[0];
        var formFile = new FormData();
        formFile.append("excelFile", eFile);
        formFile.append("excelFile", eFile);
        $.ajax({
            type: "POST",                       // 方法类型
            url: "importSewageExcel",              // url
            async: false,                      // 同步：意思是当有返回值以后才会进行后面的js程序
            dataType: "json",
            data: formFile,
            processData: false,
            contentType: false,
            success: function (result) {
                if (result != undefined) {
                    console.log(result);
                    if (result.status == "success") {
                        alert(result.message);
                        window.location.reload();         //刷新
                    } else {
                        alert(result.message);
                        window.location.reload();
                    }
                }
            },
            error: function (result) {
                console.log(result);
            }
        });
    });
}

/**
 * 回车查询
 */
function enterSearch(){
    if (event.keyCode === 13) {   // 如果按下键为回车键，即执行搜素
        searchSewage();      //
    }
}

/**
 * 延时自动查询
 */
$(document).ready(function () {//页面载入是就会进行加载里面的内容
    var last;
    $('#searchContent').keyup(function (event) { //给Input赋予onkeyup事件
        last = event.timeStamp;//利用event的timeStamp来标记时间，这样每次的keyup事件都会修改last的值，注意last必需为全局变量
        setTimeout(function () {
            if(last-event.timeStamp=== 0){
                searchSewage();
            }else if (event.keyCode === 13) {   // 如果按下键为回车键，即执行搜素
                searchSewage();      //
            }
        },600);
    });
});

/**
 * 污水分析日报查询功能
 */
function searchSewage() {
    isSearch = true;
    var page = {};
    var pageNumber = 1;                       // 显示首页
    page.pageNumber = pageNumber;
    page.count = countValue();
    page.start = (pageNumber - 1) * page.count;
    if ($("#senior").is(':visible')) {
        data1 = {
            startDate: $("#search-startDate").val(),
            endDate: $("#search-endDate").val(),
            name: $.trim($("#search-sewageName").val()),
            remarks: $.trim($("#search-remarks").val()),
            page: page
        };
    }else{
        data1 = {
            keywords: $.trim($("#searchContent").val()),
            page: page
        };
    }
    if (data1 == null) alert("请点击'查询设置'输入查询内容!");
    else {
        $.ajax({
            type: "POST",                            // 方法类型
            url: "searchSewage",                 // url
            async: false,                           // 同步：意思是当有返回值以后才会进行后面的js程序
            data: JSON.stringify(data1),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (result) {
                console.log(result);
                if (result.data != undefined || result.status == "success") {
                    setPageClone(result.data);
                } else {
                    console.log(result.message);
                }
            },
            error: function (result) {
                console.log(result);
                alert("服务器错误！");
            }
        });
    }
}


/**
 * 预约登记-显示预约框
 */
function appointModal() {
    // 显示框体
    setSelectList();
    $('#appointModal').modal('show');

}

/**
 * 添加预约登记单
 */
function addAppoint() {

//主表
   var  data={
        client:{clientId:$('#model-companyCode').selectpicker('val')},
        laboratorySignatory:$('#laboratorySignatory').val(),
        sendingPerson:$('#sendingPerson').val(),
        water:true
    };
    console.log(data)
   //添加主表
    $.ajax({
        type: "POST",                       // 方法类型
        url: "addSewaGeregistration",              // url
        async: false,                      // 同步：意思是当有返回值以后才会进行后面的js程序
        dataType: "json",
        data: JSON.stringify(data),
        processData: false,
        contentType: 'application/json;charset=utf-8',
        success: function (result) {
            if (result != undefined && result.status == "success"){
                $('.myclass').each(function () {
                    var   dataItem={
                        wastesCode:$(this).children('td').eq(1).find("button").attr('title'),
                        wastesName:$(this).children('td').eq(2).find("input").val(),
                        isPH:$(this).children('td').eq(3).find('label').eq(0).find("input").prop('checked'),
                        isCOD:$(this).children('td').eq(3).find('label').eq(1).find("input").prop('checked'),
                        isBOD5:$(this).children('td').eq(3).find('label').eq(2).find("input").prop('checked'),
                        isO2:$(this).children('td').eq(3).find('label').eq(3).find("input").prop('checked'),
                        isN2:$(this).children('td').eq(3).find('label').eq(4).find("input").prop('checked'),
                        isLye:$(this).children('td').eq(3).find('label').eq(5).find("input").prop('checked'),
                    };
                    console.log(dataItem)
                    $.ajax({
                        type: "POST",                       // 方法类型
                        url: "addSewaGeregistrationItem",              // url
                        async: false,                      // 同步：意思是当有返回值以后才会进行后面的js程序
                        dataType: "json",
                        data: JSON.stringify(dataItem),
                        processData: false,
                        contentType: 'application/json;charset=utf-8',
                    })


                })
            }
        },
        error: function (result) {
            console.log(result);
        }
    });





}





/**
 * 为公司代码和危废代码下拉框填充数据
 */
function setSelectList() {
    $.ajax({
        type: "POST",                       // 方法类型
        url: "getClientAndWastesCodeSelectedList",                  // url
        async: false,                      // 同步：意思是当有返回值以后才会进行后面的js程序
        dataType: "json",
        success: function (result) {
            if (result != undefined && result.status === "success") {
                var data = eval(result);
                console.log("下拉数据为：");
                console.log(data);
                // 下拉框数据填充
                var companyCode = $("#model-companyCode");
                $.each(data.companyCodeList, function (index, item) {
                    var option = $('<option />');
                    option.val((item.clientId));
                    option.text(item.companyName);
                    companyCode.append(option);
                });

                var wastesCode = $("#wastesCode");
                $.each(data.wastesCodeList, function (index, item) {
                    var option = $('<option />');
                    option.val(item.code);
                    option.text(item.code);
                    wastesCode.append(option);
                });
                //刷新下拉数据
                $('.selectpicker').selectpicker('refresh');

             $('#addClone').siblings().not($('#plusBtn')).remove();

            } else {
                console.log("fail: " + result);
            }
        },
        error: function (result) {
            console.log("error: " + result);
        }
    });
}


var num = 1;

/**
 * 预约登记/修改-新增样品1
 */
function addNewLine(item) {
    // 获取id为plusBtn的tr元素
    //var tr = $("#plusBtn").prev();
    var tr = null;
    if (item != null)
        tr = $(item).parent().parent().prev();
    else tr = $("#addBtn3").prev();
    // 克隆tr，每次遍历都可以产生新的tr
    var clonedTr = tr.clone();
    clonedTr.children('td').eq(0).html(($('.myclass').length)+1);
    if (clonedTr.children('td').eq(0).html() != 1) {     // 将非第一行的所有行加上减行号
        var delBtn = "<a class='btn btn-default btn-xs' onclick='delLine(this);'><span class='glyphicon glyphicon-minus' aria-hidden='true'></span></a>&nbsp;";
        clonedTr.children('td').eq(0).prepend(delBtn);
    }

    // 克隆后清空新克隆出的行数据
    //clonedTr.children().find("input:first-child").prop('name').charAt(11);
    clonedTr.children().find("input").val("");
    clonedTr.children().find("input:checkbox").prop('checked', false);
    clonedTr.children().find("select").selectpicker('val', '');
    // clonedTr.children().find("input,select").each(function () {
    //     var name = $(this).prop('name');
    //     var newName = name.replace(/[0-9]\d*/, num - 1);
    //     $(this).prop('name', newName);
    //     var id = $(this).prop('id');
    //     var newId = id.replace(/[0-9]\d*/, num - 1);
    //     $(this).prop('id', newId);
    // });
   // clonedTr.addClass("newLine");

   
   
    clonedTr.insertAfter(tr);
    clonedTr.removeAttr("id");
    //清空数据为重新初始化selectpicker
    $('.selectpicker').data('selectpicker', null);
    $('.bootstrap-select').find("button:first").remove();
    $('.selectpicker').selectpicker();

}

/**
 * 删除行
 */
function delLine(item) {
    var tr = item.parentElement.parentElement;
    var length = $(tr.parentNode).children().length - 2;         // 行数
    var tBody = $(tr.parentNode);                                  // 删除前获取父节点
    tr.parentNode.removeChild(tr);
    console.log(tr);
    console.log("length:" + length);
    for (var i = 1; i < length; i++) {             // 更新序号
        tBody.children().eq(i).children().eq(0).get(0).innerHTML = i + 1;     // 更新序号
        // 重新加上减行按钮
        var delBtn = "<a class='btn btn-default btn-xs' onclick='delLine(this);'><span class='glyphicon glyphicon-minus' aria-hidden='true'></span></a>&nbsp;";
        tBody.children().eq(i).children("td:eq(0)").prepend(delBtn);
        tBody.children().eq(i).children().find("input,select").each(function () {
            var name = $(this).prop('name');
            var newName = name.replace(/[0-9]\d*/, i);
            $(this).prop('name', newName);
            var id = $(this).prop('id');
            var newId = id.replace(/[0-9]\d*/, i);
            $(this).prop('id', newId);
        });
    }
    num--;
}

