/**
 * Created by matt on 2018/7/18.
 */

/**
 * 获取日期yyyy-MM-dd
 * @param obj
 * @returns {*}
 */
function getDateStr(obj) {
    if (obj == null) return "";
    var year=(parseInt(obj.year)+1900).toString();
    var mouth=parseInt((obj.month)+1).toString();
    if(mouth.length!=2){
        mouth=0+mouth;
    }
    //  dataLeftCompleting(2, "0", mouth.toString()).toString();
    var  day=parseInt((obj.date)).toString();
    //ataLeftCompleting(2, "0", day.toString()).toString();
    if(day.length!=2){
        day=0+day;
    }
    var time1=year+"-"+mouth+"-"+day;
    return time1;
}
/**
 * 获取时间 yyyy-MM-dd HH:mm
 * @param obj
 * @returns {*}
 */
function getTimeStr(obj) {
    if (obj == null) return "";
    var year=(parseInt(obj.year)+1900).toString();
    var month=parseInt((obj.month)+1).toString();
    if(month.length!=2){
        month=0+month;
    }
    var hour = parseInt(obj.hours).toString();
    if (hour.length!=2){
        hour=0+hour;
    }
    var minutes = parseInt(obj.minutes).toString();
    if (minutes.length!=2){
        minutes=0+minutes;
    }
    var seconds = parseInt(obj.seconds).toString();
    if (seconds.length!=2){
        seconds=0+seconds;
    }
    //  dataLeftCompleting(2, "0", mouth.toString()).toString();
    var  day=parseInt((obj.date)).toString();
    //ataLeftCompleting(2, "0", day.toString()).toString();
    if(day.length!=2){
        day=0+day;
    }
    var time1 = year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + seconds;
    return time1;
}
/**
 * 通过字符串获取标准时间格式 yyyy-MM-dd'T'HH:mm:ss.SSSZ
 * @param time
 * @returns {string|*}
 */
function getStdTimeStr(time) {
    if (time == '') return "";
    var timeArr = time.split(' ');
    time = timeArr[0]+'T'+timeArr[1]+'.000Z';
    return time;
}

/**
 * 获取年月周 返回格式：2018年8月第1周
 */
function getWeekDate() {
    var getMonthWeek = function(a, b, c)
    {
        /*
        a = d = 当前日期
        b = 6 - w = 当前周的还有几天过完(不算今天)
        a + b 的和在除以7 就是当天是当前月份的第几周
        */
        var date = new Date(a, parseInt(b) - 1, c),
            w = date.getDay(),
            d = date.getDate();
        return Math.ceil((d + 6 - w) / 7);
    };
    var getYearWeek = function(a, b, c)
    {
        /*
        date1是当前日期
        date2是当年第一天
        d是当前日期是今年第多少天
        用d + 当前年的第一天的周差距的和在除以7就是本年第几周
        */
        var date1 = new Date(a, parseInt(b) - 1, c),
            date2 = new Date(a, 0, 1),
            d = Math.round((date1.valueOf() - date2.valueOf()) / 86400000);
        return Math.ceil((d + ((date2.getDay() + 1) - 1)) / 7);
    };
    var today = new Date(); //获取当前时间
    var year = today.getFullYear();
    var month = today.getMonth() + 1;
    var day = today.getDate();

    //获取时间
    // var obj = new Date();
    // var year = obj.getFullYear();
    // var month = obj.getMonth()+1;
    // var day = obj.getDate();
    // if(day % 7 > 0)  var a = 1; else a = 0;
    // var week = parseInt(day / 7) + a;
    return year + "年" + month + "月第" + week + "周";

}
function getDateOfWeek() {
    var e;
    var month = new Date().getMonth()+1;
    var year = new Date().getFullYear();
    var day = new Date().getDate();
    if(month == 1||month == 3||month == 5||month == 7||month == 8||month == 10||month == 12){
        e = 31;
    }else if (month == 2){
        if((year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)) e = 29;
        else e = 28;
    }else {
        e = 30;
    }
    var f1;       //f为当前日期的下周一日期，f+7为当前日期的下周日日期
    var f7;
    var monday;
    var sunday;
    var g = day; //当前日期
    var h = new Date().getDay();
    if(h == 0){
        f1 = g+1;
        f7 = g+7;
        if(e == 28&&f1 > 28){
            monday = f1 - 28;
            sunday = f7 - 21;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 28&&f1 < 28&&f7>28){
            monday = f1 - 28;
            sunday = f7 - 21;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 28&&f7 < 28){
            monday = f1 - 28;
            sunday = f7 - 21;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }else if(e == 29&&f1 > 29){
            monday = f1 - 29;
            sunday = f7 - 22;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 29&&f1 < 29&&f7>29){
            monday = f1 - 29;
            sunday = f7 - 22;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 29&&f7 < 29){
            monday = f1 - 29;
            sunday = f7 - 22;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }else if(e == 30&&f1 > 30){
            monday = f1 - 30;
            sunday = f7 - 23;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 30&&f1 < 30&&f7>30){
            monday = f1 - 30;
            sunday = f7 - 23;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 30&&f7 < 30){
            monday = f1 - 30;
            sunday = f7 - 23;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }else if(e == 31&&f1 > 31){
            monday = f1 - 31;
            sunday = f7 - 24;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 31&&f1 < 31&&f7>31){
            monday = f1 - 31;
            sunday = f7 - 24;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 31&&f7 < 31){
            monday = f1 - 31;
            sunday = f7 - 24;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }
    }
    else if(h == 1){
        f1 = g+7;
        f7 = g+13;
        if(e == 28&&f1 > 28){
            monday = f1 - 28;
            sunday = f7 - 21;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 28&&f1 < 28&&f7>28){
            monday = f1 - 28;
            sunday = f7 - 21;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 28&&f7 < 28){
            monday = f1 - 28;
            sunday = f7 - 21;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }else if(e == 29&&f1 > 29){
            monday = f1 - 29;
            sunday = f7 - 22;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 29&&f1 < 29&&f7>29){
            monday = f1 - 29;
            sunday = f7 - 22;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 29&&f7 < 29){
            monday = f1 - 29;
            sunday = f7 - 22;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }else if(e == 30&&f1 > 30){
            monday = f1 - 30;
            sunday = f7 - 23;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 30&&f1 < 30&&f7>30){
            monday = f1 - 30;
            sunday = f7 - 23;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 30&&f7 < 30){
            monday = f1 - 30;
            sunday = f7 - 23;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }else if(e == 31&&f1 > 31){
            monday = f1 - 31;
            sunday = f7 - 24;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 31&&f1 < 31&&f7>31){
            monday = f1 - 31;
            sunday = f7 - 24;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 31&&f7 < 31){
            monday = f1 - 31;
            sunday = f7 - 24;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }
    }
    else if(h == 2){
        f1 = g+6;
        f7 = g+12;
        if(e == 28&&f1 > 28){
            monday = f1 - 28;
            sunday = f7 - 21;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 28&&f1 < 28&&f7>28){
            monday = f1 - 28;
            sunday = f7 - 21;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 28&&f7 < 28){
            monday = f1 - 28;
            sunday = f7 - 21;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }else if(e == 29&&f1 > 29){
            monday = f1 - 29;
            sunday = f7 - 22;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 29&&f1 < 29&&f7>29){
            monday = f1 - 29;
            sunday = f7 - 22;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 29&&f7 < 29){
            monday = f1 - 29;
            sunday = f7 - 22;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }else if(e == 30&&f1 > 30){
            monday = f1 - 30;
            sunday = f7 - 23;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 30&&f1 < 30&&f7>30){
            monday = f1 - 30;
            sunday = f7 - 23;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 30&&f7 < 30){
            monday = f1 - 30;
            sunday = f7 - 23;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }else if(e == 31&&f1 > 31){
            monday = f1 - 31;
            sunday = f7 - 24;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 31&&f1 < 31&&f7>31){
            monday = f1 - 31;
            sunday = f7 - 24;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 31&&f7 < 31){
            monday = f1 - 31;
            sunday = f7 - 24;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }
    }
    else if(h == 3){
        f1 = g+5;
        f7 = g+11;
        if(e == 28&&f1 > 28){
            monday = f1 - 28;
            sunday = f7 - 21;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 28&&f1 < 28&&f7>28){
            monday = f1 - 28;
            sunday = f7 - 21;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 28&&f7 < 28){
            monday = f1 - 28;
            sunday = f7 - 21;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }else if(e == 29&&f1 > 29){
            monday = f1 - 29;
            sunday = f7 - 22;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 29&&f1 < 29&&f7>29){
            monday = f1 - 29;
            sunday = f7 - 22;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 29&&f7 < 29){
            monday = f1 - 29;
            sunday = f7 - 22;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }else if(e == 30&&f1 > 30){
            monday = f1 - 30;
            sunday = f7 - 23;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 30&&f1 < 30&&f7>30){
            monday = f1 - 30;
            sunday = f7 - 23;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 30&&f7 < 30){
            monday = f1 - 30;
            sunday = f7 - 23;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }else if(e == 31&&f1 > 31){
            monday = f1 - 31;
            sunday = f7 - 24;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 31&&f1 < 31&&f7>31){
            monday = f1 - 31;
            sunday = f7 - 24;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 31&&f7 < 31){
            monday = f1 - 31;
            sunday = f7 - 24;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }
    }
    else if(h == 4){
        f1 = g+4;
        f7 = g+10;
        if(e == 28&&f1 > 28){
            monday = f1 - 28;
            sunday = f7 - 21;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 28&&f1 < 28&&f7>28){
            monday = f1 - 28;
            sunday = f7 - 21;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 28&&f7 < 28){
            monday = f1 - 28;
            sunday = f7 - 21;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }else if(e == 29&&f1 > 29){
            monday = f1 - 29;
            sunday = f7 - 22;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 29&&f1 < 29&&f7>29){
            monday = f1 - 29;
            sunday = f7 - 22;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 29&&f7 < 29){
            monday = f1 - 29;
            sunday = f7 - 22;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }else if(e == 30&&f1 > 30){
            monday = f1 - 30;
            sunday = f7 - 23;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 30&&f1 < 30&&f7>30){
            monday = f1 - 30;
            sunday = f7 - 23;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 30&&f7 < 30){
            monday = f1 - 30;
            sunday = f7 - 23;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }else if(e == 31&&f1 > 31){
            monday = f1 - 31;
            sunday = f7 - 24;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 31&&f1 < 31&&f7>31){
            monday = f1 - 31;
            sunday = f7 - 24;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 31&&f7 < 31){
            monday = f1 - 31;
            sunday = f7 - 24;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }
    }
    else if(h == 5){
        f1 = g+3;
        f7 = g+9;
        if(e == 28&&f1 > 28){
            monday = f1 - 28;
            sunday = f7 - 21;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 28&&f1 < 28&&f7>28){
            monday = f1 - 28;
            sunday = f7 - 21;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 28&&f7 < 28){
            monday = f1 - 28;
            sunday = f7 - 21;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }else if(e == 29&&f1 > 29){
            monday = f1 - 29;
            sunday = f7 - 22;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 29&&f1 < 29&&f7>29){
            monday = f1 - 29;
            sunday = f7 - 22;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 29&&f7 < 29){
            monday = f1 - 29;
            sunday = f7 - 22;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }else if(e == 30&&f1 > 30){
            monday = f1 - 30;
            sunday = f7 - 23;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 30&&f1 < 30&&f7>30){
            monday = f1 - 30;
            sunday = f7 - 23;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 30&&f7 < 30){
            monday = f1 - 30;
            sunday = f7 - 23;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }else if(e == 31&&f1 > 31){
            monday = f1 - 31;
            sunday = f7 - 24;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 31&&f1 < 31&&f7>31){
            monday = f1 - 31;
            sunday = f7 - 24;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 31&&f7 < 31){
            monday = f1 - 31;
            sunday = f7 - 24;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }
    }
    else if(h == 6){
        f1 = g+2;
        f7 = g+8;
        if(e == 28&&f1 > 28){
            monday = f1 - 28;
            sunday = f7 - 21;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 28&&f1 < 28&&f7>28){
            monday = f1 - 28;
            sunday = f7 - 21;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 28&&f7 < 28){
            monday = f1 - 28;
            sunday = f7 - 21;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }else if(e == 29&&f1 > 29){
            monday = f1 - 29;
            sunday = f7 - 22;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 29&&f1 < 29&&f7>29){
            monday = f1 - 29;
            sunday = f7 - 22;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 29&&f7 < 29){
            monday = f1 - 29;
            sunday = f7 - 22;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }else if(e == 30&&f1 > 30){
            monday = f1 - 30;
            sunday = f7 - 23;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 30&&f1 < 30&&f7>30){
            monday = f1 - 30;
            sunday = f7 - 23;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 30&&f7 < 30){
            monday = f1 - 30;
            sunday = f7 - 23;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }else if(e == 31&&f1 > 31){
            monday = f1 - 31;
            sunday = f7 - 24;
            return year+"年"+(month+1)+"月"+monday+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 31&&f1 < 31&&f7>31){
            monday = f1 - 31;
            sunday = f7 - 24;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month+1)+"月"+sunday+"日";
        }else if(e == 31&&f7 < 31){
            monday = f1 - 31;
            sunday = f7 - 24;
            return year+"年"+(month)+"月"+f1+"日"+"-"+year+"年"+(month)+"月"+f7+"日";
        }
    }
}
