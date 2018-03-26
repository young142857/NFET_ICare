/**
 * Created by xuebei on 2017/8/1.
 */
var myChartFirst=echarts.init(document.getElementById("repairpie"));
var myChartSecond=echarts.init(document.getElementById("repairpie2"));
$.get('/mgr/fixStatistics').done(function (data) {		
    myChartFirst.setOption({
        title: {
            text: '故障类型比例',
            x: 'left'
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            x: 'left',
            y: '10%',
            data: data.nameStr
        },
        series: [
            {
                name: '故障类型比例',
                type: 'pie',
                radius: ['40%', '55%'],
                center: ['52%', '60%'],
                data: data.faultCounts
            }
        ]
    });
});
$.get('/mgr/fixStatistics').done(function (data) {
    myChartSecond.setOption({
        title: {
            text: '报修机型比例',
            x: 'left'
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            x: 'left',
            y: '10%',
            data: data.typeStr
        },
        series: [
            {
                name: '报修机型比例',
                type: 'pie',
                radius: ['40%', '55%'],
                center: ['52%', '60%'],
                data: data.deviceCounts
            }
        ]
    });
});

