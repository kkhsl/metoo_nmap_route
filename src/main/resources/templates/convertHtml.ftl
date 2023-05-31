<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>巡检报告</title>
</head>
<style>
    :root{
        --textC: #30758B;
    }
    body,h1,h2,h3,h4,h5,h6,p,ul{margin: 0;padding: 0;font-weight: normal;}
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
    h6{
        font-size: 14pt;
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
    .devList{flex-wrap: wrap;}
    .devList a{
        cursor: pointer;
        margin: 4pt 6pt;
        color: var(--textC);
    }
    .devItem{
        line-height: 18pt;
        padding: 10pt 0;
    }
    .devItem h6{
        padding: 5pt 0;
    }

    #loading{
        display: flex;
        position: fixed;
        top: 0; right: 0;
        left: 0;bottom: 0;
        background: #fff;z-index: 1;
    }
    .spinner {
        --size: 30px;
        --first-block-clr: #005bba;
        --second-block-clr: #fed500;
        --clr: #111;
        width: 10vw;
        height: 10vw;
        position: relative;
        margin: auto;margin-top: 20vh;
    }

    .spinner::after,.spinner::before {
        box-sizing: border-box;
        position: absolute;
        content: "";
        width: var(--size);
        height: var(--size);
        top: 50%;
        animation: up 2.4s cubic-bezier(0, 0, 0.24, 1.21) infinite;
        left: 50%;
        background: var(--first-block-clr);
    }

    .spinner::after {
        background: var(--second-block-clr);
        top: calc(50% - var(--size));
        left: calc(50% - var(--size));
        animation: down 2.4s cubic-bezier(0, 0, 0.24, 1.21) infinite;
    }

    @keyframes down {
        0%, 100% {
            transform: none;
        }

        25% {
            transform: translateX(100%);
        }

        50% {
            transform: translateX(100%) translateY(100%);
        }

        75% {
            transform: translateY(100%);
        }
    }

    @keyframes up {
        0%, 100% {
            transform: none;
        }

        25% {
            transform: translateX(-100%);
        }

        50% {
            transform: translateX(-100%) translateY(-100%);
        }

        75% {
            transform: translateY(-100%);
        }
    }

    .devTable{
        border-collapse: collapse;
        margin-top: 20px;
        width: 100%;
        border: 1px solid #ccc;
    }
    .devTable th{ padding: 10px;border-bottom: 1px solid #ccc;border-right: 1px solid #ccc;}
    .devTable td{ padding: 4px 10px;border-bottom: 1px solid #ccc;border-right: 1px solid #ccc;}
</style>
<body>

<div id="loading">
    <div class="spinner"><p style="color:#8f8f8f;">正在生成巡检报告...</p></div>
</div>

<input type="hidden" attr="设备列表" value="">
<h1 class="title">巡检报告</h1>
<h3>该报告包含设备的 安全策略、NAT策略、ACL策略、地址对象、服务对象、时间对象等配置信息的检查。<br /></h3>

<h2>声明</h2>
<h3>报告结果仅供参考使用<br />报告生成时间： ${currentTime!}</h3>
<h2>巡检范围说明</h2>
<h3>本巡检针对网元中SNMP在线的设备</h3>

<h2>统计</h2>
<!-- 统计图 -->
<div class="flex p1cmLR">
    <div id="dev-echart" data-value='${ne!}' style="width: 50%;height:300px;"></div>
    <div id="dt-echart" data-value='${terminal!}' style="width: 50%;height:300px;"></div>
</div>
<!-- <h2>目录</h2>
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
</div> -->



<h2 id="generalInformation" xmlns="">设备列表<a name="generalInformation"></a></h2>
<div class="p1cmLR">

    <div class="devList flex">
         <#list networkElements as ele>
             <a href='#${ele.id!}'>${ele.deviceName} </a>
         </#list>
    </div>

    <#list datas as data>
          <div class="devItem" id = '${data.id!}'>
              <h6>${data.deviceName!} <span style="color:red">${data.available!}</span></h6>
              <div class="tabRow" xmlns="">
                  <div class="tabCell">设备IP：</div>
                  <div class="tabCell">${data.ip!}</div>
              </div>
              <div class="tabRow" xmlns="">
                  <div class="tabCell">启动时间：</div>
                  <div class="tabCell">${data.upTime!}</div>
              </div>
              <div class="tabRow" xmlns="">
                  <div class="tabCell">描述：</div>
                  <div class="tabCell">${data.description!}</div>
              </div>
              <#if data.online == 1>
                   <div class="deviceCharts flex">
                       <div class="deviceCpu"
                            data-cpu='${data.cpu!}'
                            data-mem='${data.mem!}'
                            style="width: 50%;height:300px;"></div>
                       <div class="deviceWd"
                            data-value='${data.temp!}'
                            style="width: 50%;height:300px;"></div>
                   </div>
              </#if>
              <h3 style="margin-top: 20px;">端口信息</h3>
                  <table class="devTable">
                          <thead>
                              <!-- 表格标题 -->
                              <tr class="">
                                  <th>端口名称</th>
                                  <th>ip地址</th>
                                  <th>状态</th>
                                  <th>vlan</th>
                                  <th>接口类型</th>
                                  <th>描述</th>
                                  <th>Speed</th>
                                  <th>接收速率</th>
                                  <th>发送速率</th>
                                  <th>光功率</th>
                                  <th>Discarded</th>
                                  <th>Inbound</th>
                                  <th>Outbound</th>
                                  <th>Errors</th>
                              </tr>
                          </thead>
                      <tbody>
                          <#if data.ports?size gt 0>
                              <#list data.ports as port>
                                  <!-- 表格内容  循环tr  内容与标题对应-->
                                  <tr>
                                      <td>${port.name!}</td>
                                      <td>${port.ip!}</td>
                                      <td>${port.status!}</td>
                                      <td>${port.vlan!}</td>
                                      <td>${port.iftype!}</td>
                                      <td>${port.description!}</td>
                                      <td></td>
                                      <td></td>
                                      <td></td>
                                      <td></td>
                                      <td></td>
                                      <td></td>
                                      <td></td>
                                      <td></td>
                                  </tr>
                              </#list>
                          <#else>
                           <tr>
                              <td></td>
                               <td></td>
                               <td></td>
                               <td></td>
                               <td></td>
                               <td></td>
                               <td></td>
                               <td></td>
                               <td></td>
                               <td></td>
                               <td></td>
                               <td></td>
                               <td></td>
                               <td></td>
                           </tr>
                          </#if>
                      </tbody>
                  </table>
          </div>
    </#list>
</div>

</body>
<script src="https://cdn.jsdelivr.net/npm/echarts@5.4.2/dist/echarts.min.js"></script>
<script>
    window.onload = function(){
        document.getElementById('loading').style.display='none';
    }
    ~function(){
        let devCharts, dtCharts;
        const deviceCharts = []
        const getAttrArr = (el, key)=>{
            return el && eval(el.getAttribute(key) || [])
        }
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
        const devChartsEl = document.getElementById('dev-echart');
        if(devChartsEl){
            const devType = [], devCount = [], devOnline = [];
            getAttrArr(devChartsEl, 'data-value').forEach(dev=>{
                devType.push(dev.name)
            devCount.push(dev.count)
            devOnline.push(dev.onlineCount)
        })
            devCharts =  echarts.init(devChartsEl)
            devCharts.setOption({
                title: {
                    text: '网络设备统计',
                    left: 'center',
                    textStyle
                },
                color: ['rgb(42,129,173)', 'green'],
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow'
                    }
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
                    data: devType,
                    axisLabel: {
                        interval: 0,
                        align: 'center',
                        margin: 20,
                        rotate: devType.length > 10 ? 25 : 0
                    }
                },
                series: [
                    {
                        name: '总数',
                        data: devCount,
                        label: {
                            show: false,
                            position: 'top'
                        },
                        type: 'bar',
                        barMaxWidth: '30px'
                    },
                    {
                        name: '在线数',
                        data: devOnline,
                        label: {
                            show: false,
                            position: 'top'
                        },
                        type: 'bar',
                        barMaxWidth: '30px'
                    }
                ]
            })
        }


        const dtChartsEl = document.getElementById('dt-echart')
        if(dtChartsEl){
            const dtType = [], dtCount = [], dtOnline = [];
            getAttrArr(dtChartsEl, 'data-value').forEach(dev=>{
                dtType.push(dev.name)
            dtCount.push(dev.count)
            dtOnline.push(dev.onlineCount)
        })
            dtCharts = echarts.init(dtChartsEl)
            dtCharts.setOption({
                title: {
                    text: '终端设备统计',
                    left: 'center',
                    textStyle
                },
                color: ['rgb(42,129,173)', 'green'],
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow'
                    }
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
                    data: dtType,
                    axisLabel: {
                        interval: 0,
                        align: 'center',
                        margin: 20,
                        rotate: dtType.length > 10 ? 25 : 0
                    }
                },
                series: [
                    {
                        name: '总数',
                        data: dtCount,
                        label: {
                            show: false,
                            position: 'top'
                        },
                        type: 'bar',
                        barMaxWidth: '30px'
                    },
                    {
                        name: '在线数',
                        data: dtOnline,
                        label: {
                            show: false,
                            position: 'top'
                        },
                        type: 'bar',
                        barMaxWidth: '30px'
                    }
                ]
            })
        }


        const deviceArrEle = document.querySelectorAll('.devItem')
        deviceArrEle.forEach(dev=>{
            const cmEl = dev.querySelector('.deviceCpu')
            if(cmEl){
                const time1 = [], time2 = [];
                const cpu = []
                const mem = []
                getAttrArr(cmEl, 'data-cpu').forEach(d=>{
                    time1.push(d.clock)
                cpu.push(d.value)
            })
                getAttrArr(cmEl, 'data-mem').forEach(d=>{
                    time2.push(d.clock)
                mem.push(d.value)
            })
                const deviceCpu = echarts.init(cmEl)
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
                                    data: time1.length ? time1 : time2
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
                                    name: 'CPU',
                                    type: 'line',
                                    symbol: 'none',
                                    areaStyle: { opacity: 0.7 },
                                    data: cpu
                                },
                                {
                                    name: '内存',
                                    type: 'line',
                                    symbol: 'none',
                                    areaStyle: { opacity: 0.3 },
                                    data: mem
                                }
                            ]
                        }
                )

                deviceCharts.push(deviceCpu)
            }



            const wdEl = dev.querySelector('.deviceWd')
            if(wdEl){
                const time = [], wd = [];
                getAttrArr(wdEl, 'data-value').forEach(d=>{
                    time.push(d.clock)
                wd.push(d.value)
            })
                const deviceWd = echarts.init(wdEl)
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
                        data: time
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
                            data: wd
                        }
                    ]
                })

                deviceCharts.push(deviceWd)
            }


        })


        window.onresize = () => {
            devCharts.resize()
            dtCharts.resize()
            deviceCharts.forEach(charts=>{
                charts.resize()
        })
        }
    }()
</script>
</html>