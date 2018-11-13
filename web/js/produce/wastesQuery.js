/**
 * 重置搜索数据1
 */
function reset() {
    window.location.reload();

}

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
            url: "totalInventoryRecord",                  // url 计算数据库的总条数
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
     }
    else {
        totalRecord=array1.length;
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
    setWasteInventoryList(result.data);
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
            AddAndRemoveClass(this)
        });
        clonedLi.addClass("beforeClone");
        clonedLi.removeAttr("id");
        clonedLi.insertAfter(li);
    }
    $("#previous").next().next().eq(0).addClass("active");       // 将首页页面标蓝
    $("#previous").next().next().eq(0).addClass("oldPageClass");
}

/**
 * 设置选中页页码标蓝
 */
function AddAndRemoveClass(item) {
    $('.oldPageClass').removeClass("active");
    $('.oldPageClass').removeClass("oldPageClass");
    $(item).parent().addClass("active");
    $(item).parent().addClass("oldPageClass");
}

/**
 * 点击页数跳转页面
 * @param pageNumber 跳转页数
 * */
function switchPage(pageNumber) {
    console.log("当前页：" + pageNumber);
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
    var page = {};
    page.count = countValue();                        //可选
    page.pageNumber = pageNumber;
    currentPage = pageNumber;          //当前页面
    setPageCloneAfter(pageNumber);        // 重新设置页码
    addPageClass(pageNumber);           // 设置页码标蓝
    //addClass("active");
    page.start = (pageNumber - 1) * page.count;
    if (!isSearch) {
        $.ajax({
            type: "POST",                       // 方法类型
            url: "getWasteInventoryList",                  // url
            async: false,                      // 同步：意思是当有返回值以后才会进行后面的js程序
            data: JSON.stringify(page),
            dataType: "json",
            contentType: 'application/json;charset=utf-8',
            success:function (result) {
                if(result != undefined && result.status == "success"){
                    setWasteInventoryList(result.data);
                    //setWasteInventoryList(result.data);
                }
                else {
                    console.log(result.message);
                }
            },
            error:function (result) {
                alert("服务器异常！")
            }

        });
    }
    if (isSearch) {//查询用的
        for(var i=0;i<array1.length;i++){
            $(array1[i]).hide();
        }
        var i=parseInt((pageNumber-1)*countValue());
        var j=parseInt((pageNumber-1)*countValue())+parseInt(countValue()-1);
        for(var i=i;i<=j;i++){
            $('#tbody1').append(array1[i]);
            $(array1[i]).show();
        }
    }
}

/**
 * 输入页数跳转页面
 * */
function inputSwitchPage() {
    var pageNumber = $("#pageNumber").val();    // 获取输入框的值
    $("#current").find("a").text("当前页：" + pageNumber);
    if (pageNumber == null || pageNumber == undefined) {
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
        setPageCloneAfter(pageNumber);        // 重新设置页码
        addPageClass(pageNumber);           // 设置页码标蓝
        var page = {};
        page.count = countValue();//可选
        page.pageNumber = pageNumber;
        page.start = (pageNumber - 1) * page.count;
        if (!isSearch) {
            $.ajax({
                type: "POST",                       // 方法类型
                url: "getWasteInventoryList",         // url
                async: false,                      // 同步：意思是当有返回值以后才会进行后面的js程序
                data: JSON.stringify(page),
                dataType: "json",
                contentType: 'application/json;charset=utf-8',
                success: function (result) {
                    if (result != undefined) {
                        console.log(result);
                        setWasteInventoryList(result.data);
                    } else {
                        console.log("fail: " + result);
                    }
                },
                error: function (result) {
                    console.log("error: " + result);
                }
            });
        }
        if (isSearch) {//查询用的
            for(var i=0;i<array1.length;i++){
                $(array1[i]).hide();
            }
            var i=parseInt((pageNumber-1)*countValue());
            var j=parseInt((pageNumber-1)*countValue())+parseInt(countValue()-1);
            for(var i=i;i<=j;i++){
                $('#tbody1').append(array1[i]);
                $(array1[i]).show();
            }
        }
    }
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

/**
 * 
 *加载危废数据
 */
function loadWasteInventoryList() {
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
    //查询危废仓库信息
    $.ajax({
        type: "POST",                       // 方法类型
        url: "getWasteInventoryList", // url
        data: JSON.stringify(page),
        async: false,                      // 同步：意思是当有返回值以后才会进行后面的js程序
        dataType: "json",
        contentType: 'application/json;charset=utf-8',
        success:function (result) {
            if(result != undefined && result.status == "success"){
                console.log(result);
                //设置危废查询列表
                setPageClone(result);
                setPageCloneAfter(pageNumber);        // 重新设置页码
                //setWasteInventoryList(result.data);
            }
            else {
                console.log(result.message);
            }
        },
        error:function (result) {
            alert("服务器异常！")
        }

    });
    isSearch = false;
    //加载进料方式列表
    $.ajax({
        type: "POST",                       // 方法类型
        url: "getHandelCategoryList",                  // url
        async: false,                      // 同步：意思是当有返回值以后才会进行后面的js程序
        dataType: "json",
        //contentType: "application/json; charset=utf-8",
        success:function (result) {
            if (result != undefined && result.status == "success"){
                var type=$('#search-type');
                type.children().remove();
                $.each(result.array1,function (index,item) {
                    var option=$('<option/>');
                    option.val(item.index);
                    option.text(item.name);
                    type.append(option);
                })
                type.get(0).selectedIndex=-1;
            }
            else {
                alert(result.message);

            }
        },
        error:function (result) {
            alert("服务器异常！")
        },

    });
}

//设置危废查询列表
function setWasteInventoryList(result) {
    var tr=$('#cloneTr');
    tr.siblings().remove();
    tr.attr('class','myclass')
    $.each(result,function (index,item) {
        // 克隆tr，每次遍历都可以产生新的tr
        if(item.boundType.name=='危废入库'&&parseFloat(item.actualCount.toFixed(2))>0){
            var clonedTr = tr.clone();
            clonedTr.show();
            clonedTr.children("td").each(function (inner_index) {
                var obj = eval(item);
                // 根据索引为部分td赋值
                switch (inner_index) {
                    // 入库单号
                    case (1):
                        $(this).html(obj.inboundOrderId);
                        break;
                    // 入库日期
                    case (2):
                        $(this).html(getDateStr(obj.inboundDate));
                        break;
                    //产废单位
                    case (3):
                        if(obj.produceCompany!=null){
                            $(this).html(obj.produceCompany.companyName);
                        }

                        break;
                    // 仓库名称
                    case (4):
                        if(obj.wareHouse!=null){
                            $(this).html(obj.wareHouse.wareHouseName);
                        }
                        break;
                    // 进料方式
                    case (5):
                        if(obj.handleCategory!=null){
                            $(this).html(obj.handleCategory.name);
                        }
                        break;
                    // 危废名称
                    case (6):
                             $(this).html(obj.wastesName);
                        break;
                    //危废类型
                    case (7):
                        $(this).html(obj.wastesCode);
                        break;
                    //数量
                    case (8):
                        $(this).html(obj.actualCount.toFixed(2));
                        break;
                        //出库单明细
                    case (9):
                        $(this).html(obj.inboundOrderItemId);
                        break;


                }
            });
            // 把克隆好的tr追加到原来的tr前面
            clonedTr.removeAttr("id");
            clonedTr.insertBefore(tr);
        }
    })
    tr.hide();
    tr.removeAttr('class');
}

array=[];
array1=[];
//危废库存查询功能
function searchWastesInventory() {
    isSearch=false;
    //如果需要按日期范围查询 寻找最早入库的日期
    var date;
    $.ajax({
        type: "POST",                       // 方法类型
        url: "getNewestInBoundDate",                  // url
        async: false,                      // 同步：意思是当有返回值以后才会进行后面的js程序
        //data:{'outboundOrderId':outboundOrderId},
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success:function (result) {
            if (result != undefined && result.status == "success"){
                date=getDateStr(result.dateList[0]);
                console.log(result);
            }
            else {
                alert(result.message);
            }
        },
        error:function (result) {
            alert("服务器异常！");
        }

    });
    //1分页模糊查询
    array.length=0;//清空数组
    array1.length=0;
    $('.myclass').each(function () {
        $(this).show();
    });
    for(var i=totalPage();i>0;i--){
        switchPage(parseInt(i));
        array.push($('.myclass'));
    }
    isSearch=true;
    var text=$.trim($('#searchContent').val());
    //1入库日期
    var  inboundOrderId =$.trim($('#search-inDate').val());
    var endDate=$.trim($('#search-endDate').val());
    //2产废单位
    var client=$.trim($('#search-client').val());
    //3进料方式
    var handelCategory=$.trim($('#search-type option:selected').text());
    var startDate=getDateByStr(inboundOrderId);
    var endDate=getDateByStr(endDate);

    var wareHouseName=$.trim($('#search-storageType').val());
    for(var j=0;j<array.length;j++){
        $.each(array[j],function () {
            if(startDate.toString()=='Invalid Date'){
                startDate=getDateByStr(date);
            }
            if(endDate.toString()=='Invalid Date'){
                endDate=new Date();
            }
            var start=$(this).children('td').eq(2).text();
            if(start.length==0){
                start=date;
            }
            //console.log(this);
            if(!($(this).children('td').eq(3).text().indexOf(client)!=-1&&$(this).children('td').text().indexOf(text)!=-1
                &&$(this).children('td').eq(5).text().indexOf(handelCategory)!=-1&&$(this).children('td').eq(4).text().indexOf(wareHouseName)!=-1
                &&(getDateByStr(start)<=endDate&&getDateByStr(start)>=startDate)
            )){
                $(this).hide();
            }
            if(($(this).children('td').eq(3).text().indexOf(client)!=-1&&$(this).children('td').text().indexOf(text)!=-1
                &&$(this).children('td').eq(5).text().indexOf(handelCategory)!=-1&&$(this).children('td').eq(4).text().indexOf(wareHouseName)!=-1
                &&(getDateByStr(start)<=endDate&&getDateByStr(start)>=startDate)
            )){
                array1.push($(this));
            }
        });
    }
    var total;

    if(array1.length%countValue()==0){
        total=array1.length/countValue()
    }

    if(array1.length%countValue()>0){
        total=Math.ceil(array1.length/countValue());
    }

    if(array1.length/countValue()<1){
        total=1;
    }

    $("#totalPage").text("共" + total + "页");

    var myArray = new Array();
    $('.beforeClone').remove();
    for ( i = 0; i < total; i++) {
        var li = $("#next").prev();
        myArray[i] = i+1;
        var clonedLi = li.clone();
        clonedLi.show();
        clonedLi.find('a:first-child').text(myArray[i]);
        clonedLi.find('a:first-child').click(function () {
            var num = $(this).text();
            switchPage(num);
            AddAndRemoveClass(this)
        });
        clonedLi.addClass("beforeClone");
        clonedLi.removeAttr("id");
        clonedLi.insertAfter(li);
    }
    $("#previous").next().next().eq(0).addClass("active");       // 将首页页面标蓝
    $("#previous").next().next().eq(0).addClass("oldPageClass");
    for(var i=0;i<array1.length;i++){
        array1[i].hide();
    }

    for(var i=0;i<countValue();i++){
        $(array1[i]).show();
        $('#tbody1').append((array1[i]));
    }






    // if(inboundOrderId.length<=0&&client.length<=0&&handelCategory.length<0){
    //     switchPage(1);
    //     $('.myclass').each(function () {
    //         $(this).show();
    //     })
    // }
}

/**
 * 回车查询
 */
function enterSearch() {
    if (event.keyCode === 13) {   // 如果按下键为回车键，即执行搜素
        searchWastesInventory();      //
    }
}
//危废库存粗查询

$(document).ready(function () {//页面载入是就会进行加载里面的内容
    var last;
    $('#searchContent').keyup(function (event) { //给Input赋予onkeyup事件
        last = event.timeStamp;//利用event的timeStamp来标记时间，这样每次的keyup事件都会修改last的值，注意last必需为全局变量
        setTimeout(function () {
            if(last-event.timeStamp==0){
                searchWastesInventory1();
            }
            else if (event.keyCode === 13) {   // 如果按下键为回车键，即执行搜素
                searchWastesInventory1();      //
            }
        },600);
    });
});

//粗查询
function searchWastesInventory1() {
    isSearch=false;
    //loadWasteInventoryList();
    //1分页模糊查询
    array.length=0;//清空数组
    array1.length=0;
    for(var i=totalPage();i>0;i--){
        switchPage(parseInt(i));
        array.push($('.myclass'));
    }
    isSearch=true;

    var text=$.trim($('#searchContent').val());

    for(var j=0;j<array.length;j++){
        $.each(array[j],function () {
            //console.log(this);
            if(($(this).children('td').text().indexOf(text)==-1)){
                $(this).hide();
            }
            if($(this).children('td').text().indexOf(text)!=-1){
                array1.push($(this));
            }
        });
    }


    var total;

    if(array1.length%countValue()==0){
        total=array1.length/countValue()
    }

    if(array1.length%countValue()>0){
        total=Math.ceil(array1.length/countValue());
    }

    if(array1.length/countValue()<1){
        total=1;
    }

    $("#totalPage").text("共" + total + "页");

    var myArray = new Array();

    $('.beforeClone').remove();

    for ( i = 0; i < total; i++) {
        var li = $("#next").prev();
        myArray[i] = i+1;
        var clonedLi = li.clone();
        clonedLi.show();
        clonedLi.find('a:first-child').text(myArray[i]);
        clonedLi.find('a:first-child').click(function () {
            var num = $(this).text();
            switchPage(num);
            AddAndRemoveClass(this)
        });
        clonedLi.addClass("beforeClone");
        clonedLi.removeAttr("id");
        clonedLi.insertAfter(li);
    }
    $("#previous").next().next().eq(0).addClass("active");       // 将首页页面标蓝
    $("#previous").next().next().eq(0).addClass("oldPageClass");
    for(var i=0;i<array1.length;i++){
        $(array1[i]).hide();
    }

    //首页展示
    for(var i=0;i<countValue();i++){
        $(array1[i]).show();
        $('#tbody1').append((array1[i]));
    }

    if(text.length<=0){
        loadWasteInventoryList();
    }

}

//危废库存查看
function view(item) {
var inboundOrderItemId=$(item).parent().prev().html();
//根据编号查找信息
    $.ajax({
        type: "POST",                       // 方法类型
        url: "getByInboundOrderItemId",                  // url 计算数据库的总条数
        data:{'inboundOrderItemId':inboundOrderItemId},
        async: false,                      // 同步：意思是当有返回值以后才会进行后面的js程序
        dataType: "json",
        success: function (result) {
            if (result != undefined && result.status == "success"){
                console.log(result);
                //危废库存查看，点击查看按钮
                setByInboundOrderItemId(result);
                $("#appointModal2").modal('show');
            }
            else {
                alert(result.message);
            }
        },
        error: function (result) {
      alert("服务器异常！")
        }
    });

}
//双击查看
function viewWastesIn(item) {



}

//危废库存查看，点击查看按钮
function setByInboundOrderItemId(result) {
    var tr=$('#cloneTr3');
    tr.siblings().remove();
    $.each(result.wasteInventoryList,function (index,item) {
        var clonedTr=tr.clone();
        clonedTr.show();
        clonedTr.children('td').each(function (inner_index) {
            var obj = eval(item);
            switch (inner_index) {
                case (0):
                    $(this).html(getDateStr(obj.inboundDate));
                    break;
                case (1):
                    if(obj.produceCompany!=null){
                        $(this).html(obj.produceCompany.companyName);
                    }
                    break;
                case (2):
                        $(this).html(obj.wastesName);
                    break;
                case (3):
                    $(this).html(obj.actualCount.toFixed(2));
                    break;
                case (4):
                    $(this).html(obj.wastesCode);
                    break;
                case (5):
                    if(obj.handleCategory!=null){
                        $(this).html(obj.handleCategory.name);
                    }
                    break;
                case (6):
                    if(obj.processWay!=null){
                        $(this).html(obj.processWay.name);
                    }
                    break;
            }
        })
        clonedTr.removeAttr("id");
        clonedTr.insertBefore(tr);

    })
     tr.hide();
}

/**
 *
 * 导出
 * @returns {string}
 */
function exportExcel() {

    var name = 't_pl_wasteinventory';
    var idArry = [];//存放主键
    var items = $("input[name='select']:checked");//判断复选框是否选中
    if (items.length <= 0) { //如果不勾选
        var sqlWords = "select t_pl_wasteinventory.inboundOrderId 入库单号,t_pl_wasteinventory.wastesCategory 危废类别,t_pl_wasteinventory.totalPrice 总价,t_pl_wasteinventory.departmentId 部门,t_pl_wasteinventory.createtor 创建人,t_pl_wasteinventory.creatorDate 创建日期,t_pl_wasteinventory.actualCount 实际数量, t_pl_wasteinventory.wastesCode 危废编码,t_pl_wasteinventory.processWay 处置方式,t_pl_wasteinventory.handleCategory 进料方式,t_pl_wasteinventory.formType 物质形态, t_pl_wasteinventory.packageType 包装方式, t_pl_wasteinventory.wastesName 危废名称          from t_pl_wasteinventory left join t_pr_laboratorytest on t_pl_wasteinventory.laboratoryTestId=t_pr_laboratorytest.laboratorytestnumber;";
        window.open('exportExcel?name=' + name + '&sqlWords=' + sqlWords);
    }

    if (items.length > 0) {
        $.each(items, function (index, item) {
            if ($(this).parent().parent().parent().children('td').eq(13).html().length > 0) {
                idArry.push($(this).parent().parent().parent().children('td').eq(13).html());        // 将选中项的编号存到集合中
            }
        });
        console.log(idArry)
        var sql = ' in (';
        if (idArry.length > 0) {
            for (var i = 0; i < idArry.length; i++) {          // 设置sql条件语句
                if (i < idArry.length - 1) sql += idArry[i] + ",";
                else if (i == idArry.length - 1) sql += idArry[i] + ");"
            }
            var sqlWords = "select * from t_pl_wasteinventory left join t_pr_laboratorytest on t_pl_wasteinventory.laboratoryTestId=t_pr_laboratorytest.laboratorytestnumber where inboundOrderItemId"+sql;

        }
        window.open('exportExcel?name=' + name + '&sqlWords=' + sqlWords);
    }

}