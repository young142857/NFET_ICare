/**
 * Created by xuebei on 2017/8/1.
 */
var warratyMyChart=echarts.init(document.getElementById("warratychart"));
var colors = ['#5793f3', '#d14a61', '#675bba'];
$.get('/mgr/warrantyStatistics').done(function (data) {
    warratyMyChart.setOption({
    color: colors,
    title: {
        text: '七日延保统计折线图',
        x: '1%'
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data: ['微信延保', '微信上门']
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
            lineStyle: {color: colors[0]},
            name: '微信延保',
            type: 'line',
            smooth: true,
            data: data.warrantyArr,
            label: {
                normal: {
                    show: true,
                    position: 'top'
                }
            }
        },
        {
            lineStyle: {color: colors[1]},
            name: '微信上门',
            type: 'line',
            smooth: true,
            data: data.visitArr,
            label: {
                normal: {
                    show: true,
                    position: 'top'
                }
            }
        }
    ]
   });
});
// warratyMyChart.setOption(option);
