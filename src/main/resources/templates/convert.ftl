<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"></meta>
  <meta http-equiv="X-UA-Compatible" content="IE=edge"></meta>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"></meta>
  <title>Document</title>
</head>
<style>
:root{
  --textC: #30758B;
}
body,h1,h2,h3,h4,p,ul{margin: 0;padding: 0;font-weight: normal;}
.p1cmLR{padding: 0 1cm;}
body{
  margin: 1cm;
  font-size: 10pt;
}
a{
  text-decoration: none;
  color: var(--textC);
}
a:hover {
  text-decoration: underline;
}
.flex{
  display: flex;
}
.tabRow{display: table-row;}
.tabCell{display: table-cell;}
h1{
  font-size: 20pt;
  padding: 1cm 0;
  font-weight: bold;
}
.title{
  font-size: 20pt;
  font-weight: bold;
}
h2{
  font-size: 16pt;
  color: var(--textC);
  padding-top: 1cm;
  padding-bottom: 6pt;
  font-weight: bold;
}
h3{
  color: var(--textC);
  font-size: 12pt;
  page-break-inside: avoid;
}
h4{
  color: var(--textC);
  font-size: 10pt;
  page-break-inside: avoid;
}
#directory h3{
  padding: 5pt 0;
}
#directory ul{
  padding: 2pt 14pt;
}
#directory li{
  color: var(--textC);
}
</style>
<body>
  <input type="hidden" attr="设备列表" value="" />
  <h1 class="title">巡检报告</h1>
  <h3>该报告包含设备的 安全策略、NAT策略、ACL策略、地址对象、服务对象、时间对象等配置信息的检查。<br /></h3>

  <h2>声明</h2>
  <h3>报告结果仅供参考使用<br/>报告生成时间： 2022-04-13 15:35:37</h3>
  <h2>巡检范围说明</h2>
  <h3>本巡检针对网元中SNMP在线的设备</h3>

  <h2>统计</h2>
  <!-- 统计图 -->
  <div class="flex">
    <div id="object-echart" style="width: 50%;height:300px;"></div>
    <div id="policy-echart" style="width: 50%;height:300px;"></div>
  </div>
  <h2>目录</h2>
  <div id="directory" class="p1cmLR">
    <h3><a href="#introduction">介绍</a></h3>
    <ul>
      <li><a href="#generalInformation">设备信息</a></li>
      <li><a href="#checkExplain">检查范围说明</a></li>
    </ul>
    <h3><a href="#executiveSummary">摘要</a></h3>
    <ul>
      <li><a href="#policySecurityRisks">策略检查问题</a></li>
      <li><a href="#targetSecurityRisks">对象检查问题</a></li>
      <li><a href="#riskPortSecurityRisks">高危端口问题</a></li>
      <li><a href="#looseSecurityRisks">宽松风险问题</a></li>
    </ul>
  </div>
  <h2 id="generalInformation" xmlns="">设备列表<a name="generalInformation"></a></h2>
    <div class="p1cmLR">
      <div class="tabRow" xmlns="">
        <div class="tabCell">设备名称：</div>
        <div class="tabCell">vrpcfg(12)</div>
      </div>
      <div class="tabRow" xmlns="">
          <div class="tabCell">设备IP：</div>
          <div class="tabCell">2022-04-13 15:35:37</div>
      </div>
      <div class="tabRow" xmlns="">
          <div class="tabCell">启动时间：</div>
          <div class="tabCell">vrpcfg(12)</div>
      </div>
      <div class="tabRow" xmlns="">
          <div class="tabCell">描述：</div>
          <div class="tabCell">22.22.22.22</div>
      </div>

      <div class="deviceCharts flex">
        <div id="deviceCpu-1" style="width: 50%;height:300px;"></div>
        <div id="deviceWd-1" style="width: 50%;height:300px;"></div>
      </div>
    </div>
    
</body>
<script src="https://cdn.jsdelivr.net/npm/echarts@5.4.2/dist/echarts.min.js"></script>
<script>
  const {grid, textStyle} = {
    grid: {
      left: '2%',
      right: '2%',
      bottom: '4%',
      containLabel: true
    },
    textStyle:{
      fontSize: 14
    }
  }
  var objectChart = echarts.init(document.getElementById('object-echart'));
  var policyChart = echarts.init(document.getElementById('policy-echart'));
  var objectOptions = {
      title: {
        text: '网络设备统计',
        left: 'center',
        textStyle
      },
      color: ['rgb(42,129,173)', 'rgb(240,77,97)', 'rgb(165,165,165)'],
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      legend: {
        data: ['对象总数', '未引用对象', '空对象'],
        bottom: 0,
        left: 'center'
      },
      grid,
      yAxis: {
        type: 'value',
        name: '数量',
        axisLabel: {
          formatter: '{value}'
        }
      },
      xAxis: {
        type: 'category',
        data: ['网元总数','SNMP在线','路由器','交换机','防火墙']
      },
      series: [
        {
          name: '在线数',
          data: [50,60,70,10,5],
          label: {
            show: true,
            position: 'top'
          },
          type: 'bar'
        },
        {
          name: '离线',
          data: [5,9,20,2,1],
          label: {
            show: true,
            position: 'top'
          },
          type: 'bar'
        }
      ]

  }
  var dtOptions = {
      title: {
        text: '终端设备统计',
        left: 'center',
        textStyle
      },
      grid,
      yAxis: {
        type: 'value',
        name: '数量',
        axisLabel: {
            formatter: '{value}'
        }
      },
      xAxis: {
        type: 'category',
        data: ['终端总数','在线终端','电脑','摄像头','打印机','门襟']
      },
      series: [
        {
          type: 'bar',
          data: [999,320,50,60,10,4],
          barMaxWidth: 40,
          label: {
            show: true,
            position: 'top'
          },
          itemStyle: {
            normal: {
              color: function (params) {
                var colors = ['#2a81ad', '#4cc3a0', '#a5a5a5', '#cdbc4f', '#4472c4', '#bababb']
                return colors[params.dataIndex]
              }
            }
          }
        }

      ]
  }
  objectChart.setOption(objectOptions)
  policyChart.setOption(dtOptions)


  var deviceCpu = echarts.init(document.getElementById('deviceCpu-1'))
  const devData = {
    x: ['13:01','14:01','15:01','16:01','17:02','18:03','18:18','18:28','18:38'],
    y: [
      {
        name: 'CPU',
        type: 'line',
        symbol: 'none',
        areaStyle: { opacity: 0.7 },
        data: [50,62,74,68,45,45,61,24,20]
      },
      {
        name: '内存',
        type: 'line',
        symbol: 'none',
        areaStyle: { opacity: 0.3 },
        data: [50,60,70,60,55,40,60,30,20]
      }
    ],
    data: [90,80,60,50,20,40,50,60,55],
    date:  ['13:01','14:01','15:01','16:01','17:02','18:03','18:18','18:28','18:38']
  }
  deviceCpu.setOption(
    {
      title: {
        text: 'Cpu、内存使用率',
        left: 'center',
        textStyle
      },
      color: [
        '#d48265',
        '#91c7ae',
        '#749f83',
        '#ca8622',
        '#bda29a',
        '#6e7074',
        '#546570',
        '#c4ccd3'
      ],
      backgroundColor: '',
      tooltip: {
        trigger: 'axis',
        valueFormatter: function(v) {
          return v + '%'
        }
      },
      dataZoom: [
        {
          type: 'inside'
        }
      ],
      legend: {
        right: '2%',
        data: ['CPU','内存']
      },
      // toolbox: {
      //   feature: {
      //     saveAsImage: {}
      //   }
      // },
      grid,
      xAxis: [
        {
          type: 'category',
          boundaryGap: false,
          data: devData.x
        }
      ],
      yAxis: {
        type: 'value',
        min: 0,
        max: 100,
        boundaryGap: [0, '100%']
      },
      series: devData.y
    }
  )
 
  var deviceWd = echarts.init(document.getElementById('deviceWd-1'))
  deviceWd.setOption({
        backgroundColor: '',
        tooltip: {
          trigger: 'axis'
        },
        title: {
          text: '温度',
          left: 'center',
          textStyle
        },
        grid,
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['13:01','14:01','15:01','16:01','17:02','18:03','18:18','18:28','18:38']
        },
        dataZoom: [
          {
            type: 'inside'
          }
        ],
        yAxis: {
          type: 'value',
          min: 0,
          max: 100,
          boundaryGap: [0, '100%']
        },
        series: [
          {
            name: '温度',
            type: 'line',
            symbol: 'none',
            sampling: 'lttb',
            itemStyle: {
              color: 'rgb(255, 70, 131)'
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: 'rgb(255, 158, 68)'
                },
                {
                  offset: 1,
                  color: 'rgb(255, 70, 131)'
                }
              ])
            },
            data: [80,60,80,50,30,50,40,50]
          }
        ]
      })
 
  window.onresize = () => {
    objectChart.resize()
    policyChart.resize()
    deviceCpu.resize()
  }

</script>
</html>