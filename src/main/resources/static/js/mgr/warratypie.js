/**
 * Created by xuebei on 2017/8/1.
 */
var warratypieMyChart=echarts.init(document.getElementById("warratypie"));
$.get('/mgr/warrantyStatistics').done(function (data) {
warratypieMyChart.setOption({
    title: {
        text: '延保&上门套餐比例',
        x: 'left'
    },
    tooltip: {
        trigger: 'item',
        formatter: "{a} <br/>{b}: {c} ({d}%)"
    },
    legend: {
        orient: 'vertical',
        x: 'left',
        y: '8%',
        data:data.nameStr
    },
    series: [
        {
            name: '延保&上门套餐比例',
            type: 'pie',
            radius: ['40%', '55%'],
            center: ['52%', '65%'],
            data: data.sumCount
        }
    ]
    });
});

// warratypieMyChart.setOption(option);
