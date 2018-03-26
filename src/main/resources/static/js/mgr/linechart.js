/**
 * Created by xuebei on 2017/8/1.
 */
var myChart=echarts.init(document.getElementById("linechart"));
var colors = ['#5793f3', '#d14a61'];
$.get('/mgr/userList').done(function (data) {
    myChart.setOption({
        color:colors,
        title: {
            text: '七日用户总计折线图',
             x:'1%'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
             y: '3%',
            data:['新增用户','总注册用户']
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
                    name: '新增用户',
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
                    name: '总注册用户',
                    type: 'line',
                    smooth: true,
                    data: data.sumArr,
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                }
            ]
         });
    });
// myChart.setOption(option);
