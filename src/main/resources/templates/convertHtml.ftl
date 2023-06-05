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
        padding: 7pt 10pt;
        color: var(--textC);
        line-height: 1;
        margin: 0 10pt;
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
    .devTable th{ padding: 10px;border-bottom: 1px solid #ccc;border-right: 1px solid #ccc;background-image: linear-gradient(to top, #ffffff,#2282d7);}
    .devTable td{ padding: 4px 10px;border-bottom: 1px solid #ccc;border-right: 1px solid #ccc;text-align: center;color: #2d2d2d;}

    .backButton {
        cursor: pointer;
        width: 34pt;height: 34pt;
        background: #196b68;
        border-radius: 50%;
        overflow: hidden;
        border: none;
        z-index: 9;
        position: fixed;
        bottom: 10vh; right: 1vw;
    }
    .backButton .icon {
        transform: scale(0.09);
        width: 8pt;
        height: 8pt;
        position: absolute;
        left: 7pt;
        top: 3pt;
        transition: all ease-in-out .5s;
    }
    .backButton .icon path {
        fill: #fff;
    }
    .backButton:hover .icon {
        animation: shake_341 2s infinite;
    }
    .backButton:focus .launch {}
    @keyframes shake_341 {
        10%, 90% {
            transform: scale(0.09) translate3d(-6px, 0, 0);
        }

        20%, 80% {
            transform: scale(0.09)  translate3d(7px, 0, 0);
        }

        30%, 50%, 70% {
            transform: scale(0.09)  translate3d(-9px, 0, 0);
        }

        40%, 60% {
            transform: scale(0.09)  translate3d(9px, 0, 0);
        }
    }
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
             <#if ele.available != ''>
                <a href='#${ele.id!}'><span style="color:red">${ele.deviceName}</span></a>
             <#else>
                <a href='#${ele.id!}'><span>${ele.deviceName}</span></a>
             </#if>
             <#--<a href='#${ele.id!}'>${ele.deviceName}<span style="color:red">${ele.available!}</span></a>-->
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
              <#if data.online == 1>

               <h3 style="margin-top: 20px;">板卡信息</h3>
                  <table class="devTable">
                      <thead>
                      <!-- 表格标题 -->
                      <tr class="">
                          <th>槽位</th>
                          <th>板卡型号</th>
                          <th>CPU利用率</th>
                          <th>内存利用率</th>
                          <th>温度</th>
                      </tr>
                      </thead>
                      <tbody>
                      <!-- 表格内容  循环tr  内容与标题对应-->
                      <tr>
                          <td>2016-05-02</td>
                          <td>2016-05-02</td>
                          <td>2016-05-02</td>
                          <td>2016-05-02</td>
                      </tr>
                      <tr>
                          <td>2016-05-02</td>
                          <td>2016-05-02</td>
                          <td>2016-05-02</td>
                          <td>2016-05-02</td>
                      </tr>
                      <tr>
                          <td>2016-05-02</td>
                          <td>2016-05-02</td>
                          <td>2016-05-02</td>
                          <td>2016-05-02</td>
                      </tr>
                      </tbody>
                  </table>

                  <h3 style="margin-top: 20px;">端口信息</h3>
                  <table class="devTable">
                      <thead>
                      <!-- 表格标题 -->
                      <tr class="">
                          <th>端口名称</th>
                          <th>ip地址</th>
                          <th>状态</th>
                          <th>vlan</th>
                          <th>描述</th>
                          <th>接口类型</th>
                          <th>Speed</th>
                          <th>接收速率</th>
                          <th>发送速率</th>
                          <th>Inbound discarded</th>
                          <th>Inbound errors</th>
                          <th>Outbound discarded</th>
                          <th>Outbound errors</th>
                          <th>光功率</th>
                      </tr>
                      </thead>
                      <tbody>
                          <#if data.ports!?size gt 0>
                              <#list data.ports as port>
                                  <!-- 表格内容  循环tr  内容与标题对应-->
                                  <tr>
                                      <td>${port.name!}</td>
                                      <td>${port.ip!}</td>
                                      <td>${port.status!}</td>
                                      <td>${port.vlan!}</td>
                                      <td>${port.description!}</td>
                                      <td>${port.iftype!}</td>
                                      <td>${port.ifspeed!}</td>
                                      <td>${port.ifreceived!}</td>
                                      <td>${port.ifsent!}</td>
                                      <td>${port.indiscards!}</td>
                                      <td>${port.inerrors!}</td>
                                      <td>${port.outdiscards!}</td>
                                      <td>${port.outerrors!}</td>
                                      <td></td>
                                  </tr>
                              </#list>
                          <#else>
                           <tr>
                               <td>&nbsp;</td>
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
              </#if>
          </div>
    </#list>
</div>
<button class="backButton">
    <span class="icon">
      <svg height="303.09363" width="187.41829" version="1.1">
        <path id="path4" stroke-linecap="round" style="stroke:none;stroke-width:2.81;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:10;stroke-dasharray:none" d="m 105.04479,7.9451197 c -0.54247,-0.518571 -1.09286,-1.029222 -1.64524,-1.54186 -0.61595,-0.572255 -1.22596,-1.146482 -1.85384,-1.710782 -1.25775,-1.134567 -2.537358,-2.259184 -3.844788,-3.36394 -2.09427,-1.77237697 -5.16413,-1.77038997 -7.26039,0 -1.30742,1.104756 -2.58107,2.227394 -3.83882,3.35798 -0.63782,0.574234 -1.25577,1.156416 -1.87769,1.734625 -0.53847,0.502703 -1.0829,1.003419 -1.61143,1.512084 -0.88818,0.848436 -1.75648,1.70482 -2.61684,2.5651783 -0.14306,0.143062 -0.28811,0.288111 -0.42919,0.43316 -17.060123,17.22703 -28.588524,37.172235 -35.801225,61.735159 -0.125179,0.435146 -0.260293,0.860358 -0.387459,1.293517 -0.04371,0.154984 -0.09339,0.300033 -0.137101,0.455017 -0.0099,0.03377 -0.0079,0.07153 -0.01788,0.105302 -5.766187,20.286964 -8.524101,43.53849 -8.524101,70.45796 0,3.05993 0.05365,6.10596 0.13114,9.14403 l -24.167515,28.14543 c -0.707361,0.82261 -1.160391,1.83199 -1.3074264,2.90495 l -9.80369097,72.24621 c -0.311955,2.29892 0.818631,4.55414 2.84931497,5.67479 2.028696,1.12263 4.538239,0.88221 6.320552,-0.60206 L 51.189952,227.47351 h 24.37615 c -3.33215,5.3072 -5.31316,12.12847 -5.31514,19.66505 0.002,4.51638 0.72127,8.8599 2.15983,12.95305 1.93929,5.40854 7.59023,18.67752 13.573,32.72739 l 2.91687,6.85505 c 0.88221,2.07439 2.91687,3.42156 5.17208,3.41957 1.52003,0.002 2.94072,-0.61199 3.97593,-1.6472 0.49873,-0.49873 0.90804,-1.09879 1.19615,-1.77635 l 3.020198,-7.10143 c 5.93309,-13.93661 11.53635,-27.10227 13.48358,-32.51875 1.42267,-4.06137 2.14394,-8.40687 2.14394,-12.91729 0,-7.53459 -1.983,-14.35387 -5.31515,-19.66107 h 23.65289 l 41.9668,35.01637 c 1.78032,1.48626 4.29185,1.72469 6.32055,0.60205 0.46892,-0.2583 0.88817,-0.5782 1.25378,-0.94381 1.22,-1.22 1.83596,-2.96058 1.59553,-4.73097 l -9.80569,-72.24027 c -0.14704,-1.07297 -0.60007,-2.08235 -1.30743,-2.90495 l -23.45419,-27.31289 c 0.0914,-3.31427 0.14107,-6.63847 0.14306,-9.97857 0,-26.93934 -2.75792,-50.202781 -8.53006,-70.499678 -0.006,-0.02183 -0.006,-0.0457 -0.012,-0.06753 -0.0278,-0.09936 -0.0596,-0.190728 -0.0874,-0.290098 -0.26625,-0.929902 -0.5484,-1.843908 -0.83055,-2.761888 -7.23257,-23.950938 -18.64573,-43.486827 -35.37602,-60.391968 -0.16279,-0.16704 -0.32573,-0.329972 -0.49064,-0.49489 -0.84022,-0.8487053 -1.69701,-1.6927643 -2.57114,-2.5274333 z m 12.504,114.1176503 c 0,6.2669 -2.44,12.16423 -6.87492,16.59915 -4.43293,4.43293 -10.32827,6.87491 -16.599138,6.87491 -12.94511,-0.002 -23.47407,-10.53094 -23.47605,-23.47605 v 0 c 0.004,-12.94709 10.53889,-23.47406 23.47406,-23.47406 6.268888,-0.002 12.166218,2.43801 16.601138,6.87293 4.43491,4.43492 6.8769,10.33025 6.87491,16.60312 z"></path>
      </svg>
    </span>
    <span class="text"></span>
</button>
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
        function smoothScrollToTop() {
            let Y_TopValve = (window.pageYOffset || document.body.scrollTop || document.documentElement.scrollTop);
            if (Y_TopValve > 1) {
                window.requestAnimationFrame(smoothScrollToTop);
                scrollTo(0, Math.floor(Y_TopValve * 0.85));
            } else {
                scrollTo(0, 0);
            }
        }
        const topBtn = document.querySelector('.backButton')
        topBtn.addEventListener('click', smoothScrollToTop)
        window.onscroll = () => {
            let Y_TopValve = (window.pageYOffset || document.body.scrollTop || document.documentElement.scrollTop);
            topBtn.style.display = Y_TopValve > 500 ? 'block' : 'none'
        }
    }()
</script>
</html>