/**
 * Created by xuebei on 2017/8/1.
 */
var repairMyChart=echarts.init(document.getElementById("repairchart"));
var colors = ['#5793f3', '#d14a61', '#675bba'];
$.get('/mgr/fixStatistics').done(function (data) {
    repairMyChart.setOption( {
    color:colors,
    title: {
        text: '七日用户报修折线图',
        x:'1%'
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data:['新增报修','总报修']
    },
    toolbox: {
        show: true
//            feature: {
////                magicType: {show: true, type: ['stack', 'tiled']},
////                saveAsImage: {show: true}
//            }
    },
    xAxis: {
        type: 'category',
        boundaryGap: true,
        data: data.weekStr,
        axisTick: {
            alignWithLabel: true
        }
    },
    yAxis: {
        type: 'value'
    },
    series: [
        {
            lineStyle:{color:colors[0]},
            name: '新增报修',
            type: 'line',
            smooth: true,
            data: data.countArr,
            label: {
                normal: {
                    show: true,
                    position: 'top'
                }
            },
        },
        {
            lineStyle:{color:colors[1]},
            name: '总报修',
            type: 'line',
            smooth: true,
            data: data.sumArr,
            label: {
                normal: {
                    show: true,
                    position: 'top'
                }
            },
        }]
    });
});
// repairMyChart.setOption(option);
