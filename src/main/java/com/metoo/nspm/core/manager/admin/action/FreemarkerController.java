package com.metoo.nspm.core.manager.admin.action;

import com.alibaba.fastjson.JSONObject;
import com.metoo.nspm.core.manager.admin.tools.DateTools;
import com.metoo.nspm.core.manager.zabbix.tools.InterfaceUtil;
import com.metoo.nspm.core.service.api.zabbix.ZabbixService;
import com.metoo.nspm.core.service.nspm.IDeviceTypeService;
import com.metoo.nspm.core.service.nspm.INetworkElementService;
import com.metoo.nspm.core.service.nspm.ITopologyService;
import com.metoo.nspm.core.service.zabbix.IItemTagService;
import com.metoo.nspm.core.service.zabbix.InterfaceService;
import com.metoo.nspm.core.utils.Global;
import com.metoo.nspm.core.utils.freemarker.FreemarkerUtil;
import com.metoo.nspm.core.utils.freemarker.PDFTemplateUtil;
import com.metoo.nspm.entity.nspm.DeviceType;
import com.metoo.nspm.entity.nspm.NetworkElement;
import com.metoo.nspm.entity.zabbix.Interface;
import com.metoo.nspm.entity.zabbix.ItemTag;
import com.metoo.nspm.vo.ItemTagBoardVO;
import com.metoo.nspm.vo.Result;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

/**
 * 使用Freemarker生成html和pdf
 *
 * HTML:
 *
 * PDF:
 *  https://blog.csdn.net/weixin_39806100/article/details/86616041
 */
@RequestMapping("/admin/freemarker")
@Controller
public class FreemarkerController {

    public static final Result RESULT = new Result();
    @Autowired
    private FreemarkerUtil freemarkerUtil;
    @Autowired
    private PDFTemplateUtil pdfTemplateUtil;
    @Autowired
    private InterfaceService interfaceService;
    @Autowired
    private IItemTagService itemTagService;
    @Autowired
    private IDeviceTypeService deviceTypeService;
    @Autowired
    private InterfaceUtil interfaceUtil;
    @Autowired
    private ZabbixService zabbixService;
    @Autowired
    private INetworkElementService networkElementService;
    @Autowired
    private ITopologyService topologyService;

    @RequestMapping("/html")
    public String html(Model model){
        Result result = new Result();
        result.setCode(100);
        model.addAttribute("result", result);
        return "index";
    }

    @RequestMapping("/convert")
    public String convert(){
        return "convert";
    }

    @RequestMapping("/ftl")
    public String ftl(Model model){
        Result result = new Result();
        result.setCode(100);
        model.addAttribute("result", result);
        return "test";
    }


//    @RequestMapping("/createHtml")
//    public void createHtml(Model model){
//        Result result = new Result();
//        result.setCode(100);
//        User user = new User();
//        user.setUsername("hkk");
////        result.setUser(user);
//        model.addAttribute("result", result);
//        this.freemarkerUtil.createHtml("test.ftl", "testq.html", result);
//    }

//    @RequestMapping("/showHtml/{ip}")
//    public ModelAndView showHtml(HttpServletResponse response, @PathVariable String ip) {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("convertHtml");
//        Map data = this.getData(ip);
//        modelAndView.addObject(data);
//        return modelAndView;
//    }

    @RequestMapping("/showHtml")
    public String showHtml(Model model) {
        this.getData(model);
//        model.addAttribute("ip", ip);
//        model.addAttribute("upTime", "aaa");
//        model.addAttribute("description", "asdfsafdsa");
        return "convertHtml";
    }


    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.getTimeInMillis() / 1000);
        System.out.println(calendar.getTimeInMillis());
        calendar.set(Calendar.HOUR_OF_DAY, new Date().getHours() - 1);
        System.out.println(calendar.getTimeInMillis() / 1000);
        System.out.println(calendar.getTimeInMillis());

//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println("一个小时前的时间：" + df.format(calendar.getTime()));
//        System.out.println("当前的时间：" + df.format(new Date()));
    }

    public void getData(Model model){
            List<DeviceType> nes = this.deviceTypeService.selectCountByJoin();
            List list1 = new ArrayList();
            DeviceType network = new DeviceType();
            network.setName("网元总数");
            if (nes.size() > 0) {
                int j = 0;
                for (DeviceType deviceType : nes) {
                    int n = 0;
                    if (deviceType.getNetworkElementList().size() > 0) {
                        for (NetworkElement ne : deviceType.getNetworkElementList()) {
                            if (ne.getIp() != null) {
                                Interface obj = this.interfaceService.selectObjByIp(ne.getIp());
                                if (obj != null) {
                                    if (obj.getAvailable().equals("1")) {
                                        n++;
                                        j++;
                                    }
                                }
                            }
                        }
                        deviceType.getNetworkElementList().clear();
                        deviceType.setOnlineCount(n);
                    }
                    list1.add(deviceType);
                }
                int count = nes.stream().mapToInt(e -> e.getCount()).sum();
                network.setCount(count);
                network.setOnlineCount(j);
            }
            list1.add(0, network);
            model.addAttribute("ne", JSONObject.toJSONString(list1));
            List<DeviceType> list2 = new ArrayList();
            DeviceType terminal = new DeviceType();
            terminal.setName("终端总数");
            List<DeviceType> terminals = this.deviceTypeService.selectTerminalCountByJoin();
            if (terminals.size() > 0) {
                for (DeviceType deviceType : terminals) {
                    int n = deviceType.getTerminalList().stream().mapToInt(e -> e.getOnline() == true ? 1 : 0).sum();
                    deviceType.setOnlineCount(n);
                    deviceType.getTerminalList().clear();
                    list2.add(deviceType);
                }
                int count = terminals.stream().mapToInt(e -> e.getCount()).sum();
                terminal.setCount(count);
                int onlineCount = list2.stream().mapToInt(e -> e.getOnlineCount()).sum();
                terminal.setOnlineCount(onlineCount);
            }
            list2.add(0, terminal);
            model.addAttribute("terminal", JSONObject.toJSONString(list2));

            model.addAttribute("currentTime", DateTools.getCurrentDate(new Date(), "yyyy-MM-dd HH:mm:ss"));

            // 网元总数
            List<NetworkElement> networkElements = this.networkElementService.selectObjAll();
            model.addAttribute("networkElements", networkElements);
            List list = new ArrayList();
            networkElements.stream().forEach(e->{
                boolean available = this.interfaceUtil.verifyHostIsAvailable(e.getIp());
                Map data = deviceInfo(e.getIp());
                data.put("available", available ? "" : "(SNMP不在线)");
                data.put("online", available ? 1 : 0);
                data.put("id", e.getId());
                data.put("ip", e.getIp());
                data.put("deviceName", e.getDeviceName());
                data.put("upTime", data.get("upTime"));
                data.put("description", data.get("description"));
                // 确定标签
                Interface anInterface = this.interfaceService.selectObjByIp(e.getIp());
                if (anInterface != null) {
                    Map params = new HashMap();
                    params.put("ip", e.getIp());
                    params.put("cpu", Global.BOARDCPU);
                    params.put("mem", Global.BOARDMEM);
                    params.put("temp", Global.BOARDTEMP);
                    List<ItemTag> itemTags = this.itemTagService.queryBoardByMap(params);

                    Calendar calendar = Calendar.getInstance();
                    Long time_till = calendar.getTimeInMillis() / 1000;
                    calendar.set(Calendar.HOUR_OF_DAY, new Date().getHours() - 1);
                    Long time_from = calendar.getTimeInMillis() / 1000;
                    if (itemTags.size() > 0) {
                        // 查找是否存在 boardcpu|不在查询CPu和内存使用率
                        List<ItemTagBoardVO> boards = this.itemTagService.selectBoard(e.getIp(), time_from, time_till);
                        if (boards.size() > 0) {
                            ItemTagBoardVO ele = boards.get(0);
                            data.put("cpu", JSONObject.toJSONString(ele.getCpu()));
                            data.put("mem", JSONObject.toJSONString(ele.getMem()));
                            data.put("temp", JSONObject.toJSONString(ele.getTemp()));
                        }
                    } else {
                        List names = Arrays.asList(
                                "cpuusage",
                                "memusage",
                                "temp");
                        ItemTagBoardVO board = this.zabbixService.getBoard(e.getIp(), names, 0, time_till, time_from);
                        if (board != null) {
                            data.put("cpu", JSONObject.toJSONString(board.getCpu()));
                            data.put("mem", JSONObject.toJSONString(board.getMem()));
                            data.put("temp", JSONObject.toJSONString(board.getTemp()));
                        }
                    }
                }
                // 端口信息
                List<Map<String, Object>> ports = this.topologyService.getDevicePortsByUuid(e.getUuid());
                data.put("ports", ports);
                list.add(data);
            });
        model.addAttribute("datas", list);

    }

//    public void getData(Model model, String ip){
//        Map data = deviceInfo(ip);
//        model.addAttribute("ip", ip);
//        model.addAttribute("deviceName", data.get("deviceName"));
//        model.addAttribute("upTime", data.get("upTime"));
//        model.addAttribute("description", data.get("description"));
//        boolean available = this.interfaceUtil.verifyHostIsAvailable(ip);
//        if(available) {
//            List<DeviceType> nes = this.deviceTypeService.selectCountByJoin();
//            List list1 = new ArrayList();
//            DeviceType network = new DeviceType();
//            network.setName("网元总数");
//            if (nes.size() > 0) {
//                int j = 0;
//                for (DeviceType deviceType : nes) {
//                    int n = 0;
//                    if (deviceType.getNetworkElementList().size() > 0) {
//                        for (NetworkElement ne : deviceType.getNetworkElementList()) {
//                            if (ne.getIp() != null) {
//                                Interface obj = this.interfaceService.selectObjByIp(ne.getIp());
//                                if (obj != null) {
//                                    if (obj.getAvailable().equals("1")) {
//                                        n++;
//                                        j++;
//                                    }
//                                }
//                            }
//                        }
//                        deviceType.getNetworkElementList().clear();
//                        deviceType.setOnlineCount(n);
//                    }
//                    list1.add(deviceType);
//                }
//                int count = nes.stream().mapToInt(e -> e.getCount()).sum();
//                network.setCount(count);
//                network.setOnlineCount(j);
//            }
//            list1.add(0, network);
//            model.addAttribute("ne", JSONObject.toJSONString(list1));
//            List<DeviceType> list2 = new ArrayList();
//            DeviceType terminal = new DeviceType();
//            terminal.setName("终端总数");
//            List<DeviceType> terminals = this.deviceTypeService.selectTerminalCountByJoin();
//            if (terminals.size() > 0) {
//                for (DeviceType deviceType : terminals) {
//                    int n = deviceType.getTerminalList().stream().mapToInt(e -> e.getOnline() == true ? 1 : 0).sum();
//                    deviceType.setOnlineCount(n);
//                    deviceType.getTerminalList().clear();
//                    list2.add(deviceType);
//                }
//                int count = terminals.stream().mapToInt(e -> e.getCount()).sum();
//                terminal.setCount(count);
//                int onlineCount = list2.stream().mapToInt(e -> e.getOnlineCount()).sum();
//                terminal.setOnlineCount(onlineCount);
//            }
//            list2.add(0, terminal);
//            model.addAttribute("terminal", JSONObject.toJSONString(list2));
//
//            // 网元总数
//            List<NetworkElement> networkElements = this.networkElementService.selectObjAll();
//            model.addAttribute("networkElements", networkElements);
//
//            // 确定标签
//            Interface anInterface = this.interfaceService.selectObjByIp(ip);
//            if (anInterface != null) {
//                Map params = new HashMap();
//                params.put("ip", ip);
//                params.put("cpu", Global.BOARDCPU);
//                params.put("mem", Global.BOARDMEM);
//                params.put("temp", Global.BOARDTEMP);
//                List<ItemTag> itemTags = this.itemTagService.queryBoardByMap(params);
//
//                Calendar calendar = Calendar.getInstance();
//                Long time_till = calendar.getTimeInMillis() / 1000;
//                calendar.set(Calendar.HOUR_OF_DAY, new Date().getHours() - 1);
//                Long time_from = calendar.getTimeInMillis() / 1000;
//                if (itemTags.size() > 0) {
//                    // 查找是否存在 boardcpu|不在查询CPu和内存使用率
//                    List<ItemTagBoardVO> boards = this.itemTagService.selectBoard(ip, time_from, time_till);
//                    if (boards.size() > 0) {
//                        ItemTagBoardVO ele = boards.get(0);
//                        model.addAttribute("cpu", JSONObject.toJSONString(ele.getCpu()));
//                        model.addAttribute("mem", JSONObject.toJSONString(ele.getMem()));
//                        model.addAttribute("temp", JSONObject.toJSONString(ele.getTemp()));
//                    }
//                } else {
//                    List names = Arrays.asList(
//                            "cpuusage",
//                            "memusage",
//                            "temp");
//                    ItemTagBoardVO board = this.zabbixService.getBoard(ip, names, 0, time_till, time_from);
//                    if (board != null) {
//                        model.addAttribute("cpu", JSONObject.toJSONString(board.getCpu()));
//                        model.addAttribute("mem", JSONObject.toJSONString(board.getMem()));
//                        model.addAttribute("temp", JSONObject.toJSONString(board.getTemp()));
//                    }
//                }
//            }
//        }
//    }

    @RequestMapping("/createHtml")
    public void createHtml(HttpServletResponse response) throws Exception {
        Map data = this.getData();
        response.setContentType( "application/html; charset=UTF-8");
        response.setHeader("Content-Disposition","Attachment;filename= " + new String(("nmap_report_" + DateTools.getCurrentDate(new Date(), "yyyyMMddHHmm") + ".html").getBytes("UTF-8"),"UTF-8"));
        this.freemarkerUtil.createHtmlToBrowser("convertHtml.ftl", data, response.getWriter());
    }

    public Map getData(){
        Map obj = new HashMap();
        List<DeviceType> nes = this.deviceTypeService.selectCountByJoin();
        List list1 = new ArrayList();
        DeviceType network = new DeviceType();
        network.setName("网元总数");
        if (nes.size() > 0) {
            int j = 0;
            for (DeviceType deviceType : nes) {
                int n = 0;
                if (deviceType.getNetworkElementList().size() > 0) {
                    for (NetworkElement ne : deviceType.getNetworkElementList()) {
                        if (ne.getIp() != null) {
                            Interface anInterface = this.interfaceService.selectObjByIp(ne.getIp());
                            if (anInterface != null) {
                                if (anInterface.getAvailable().equals("1")) {
                                    n++;
                                    j++;
                                }
                            }
                        }
                    }
                    deviceType.getNetworkElementList().clear();
                    deviceType.setOnlineCount(n);
                }
                list1.add(deviceType);
            }
            int count = nes.stream().mapToInt(e -> e.getCount()).sum();
            network.setCount(count);
            network.setOnlineCount(j);
        }
        list1.add(0, network);
        obj.put("ne", JSONObject.toJSONString(list1));

        List<DeviceType> list2 = new ArrayList();
        DeviceType terminal = new DeviceType();
        terminal.setName("终端总数");
        List<DeviceType> terminals = this.deviceTypeService.selectTerminalCountByJoin();
        if (terminals.size() > 0) {
            for (DeviceType deviceType : terminals) {
                int n = deviceType.getTerminalList().stream().mapToInt(e -> e.getOnline() == true ? 1 : 0).sum();
                deviceType.setOnlineCount(n);
                deviceType.getTerminalList().clear();
                list2.add(deviceType);
            }
            int count = terminals.stream().mapToInt(e -> e.getCount()).sum();
            terminal.setCount(count);
            int onlineCount = list2.stream().mapToInt(e -> e.getOnlineCount()).sum();
            terminal.setOnlineCount(onlineCount);
        }
        list2.add(0, terminal);
        obj.put("terminal", JSONObject.toJSONString(list2));

        obj.put("currentTime", DateTools.getCurrentDate(new Date(), "yyyy-MM-dd HH:mm:ss"));

        // 网元总数
        List<NetworkElement> networkElements = this.networkElementService.selectObjAll();
        obj.put("networkElements", networkElements);
        List list = new ArrayList();
        networkElements.stream().forEach(e->{
            boolean available = this.interfaceUtil.verifyHostIsAvailable(e.getIp());
            Map data = deviceInfo(e.getIp());
            data.put("available", available ? "" : "(SNMP不在线)");

            data.put("online", available ? 1 : 0);
            data.put("id", e.getId());
            data.put("ip", e.getIp());
            data.put("deviceName", e.getDeviceName());
            data.put("upTime", data.get("upTime"));
            data.put("description", data.get("description"));
            // 确定标签
            Interface anInterface = this.interfaceService.selectObjByIp(e.getIp());
            if (anInterface != null) {
                Map params = new HashMap();
                params.put("ip", e.getIp());
                params.put("cpu", Global.BOARDCPU);
                params.put("mem", Global.BOARDMEM);
                params.put("temp", Global.BOARDTEMP);
                List<ItemTag> itemTags = this.itemTagService.queryBoardByMap(params);

                Calendar calendar = Calendar.getInstance();
                Long time_till = calendar.getTimeInMillis() / 1000;
                calendar.set(Calendar.HOUR_OF_DAY, new Date().getHours() - 1);
                Long time_from = calendar.getTimeInMillis() / 1000;
                if (itemTags.size() > 0) {
                    // 查找是否存在 boardcpu|不在查询CPu和内存使用率
                    List<ItemTagBoardVO> boards = this.itemTagService.selectBoard(e.getIp(), time_from, time_till);
                    if (boards.size() > 0) {
                        ItemTagBoardVO ele = boards.get(0);
                        data.put("cpu", JSONObject.toJSONString(ele.getCpu()));
                        data.put("mem", JSONObject.toJSONString(ele.getMem()));
                        data.put("temp", JSONObject.toJSONString(ele.getTemp()));
                    }
                } else {
                    List names = Arrays.asList(
                            "cpuusage",
                            "memusage",
                            "temp");
                    ItemTagBoardVO board = this.zabbixService.getBoard(e.getIp(), names, 0, time_till, time_from);
                    if (board != null) {
                        data.put("cpu", JSONObject.toJSONString(board.getCpu()));
                        data.put("mem", JSONObject.toJSONString(board.getMem()));
                        data.put("temp", JSONObject.toJSONString(board.getTemp()));
                    }
                }
            }
            list.add(data);
        });
        obj.put("datas", list);
        return obj;
    }

    public Map getData(String ip){
        Map data = deviceInfo(ip);
        data.put("ip", ip);
        boolean available = this.interfaceUtil.verifyHostIsAvailable(ip);
        if(available) {
            List<DeviceType> nes = this.deviceTypeService.selectCountByJoin();
            List list1 = new ArrayList();
            DeviceType network = new DeviceType();
            network.setName("网元总数");
            if (nes.size() > 0) {
                int j = 0;
                for (DeviceType deviceType : nes) {
                    int n = 0;
                    if (deviceType.getNetworkElementList().size() > 0) {
                        for (NetworkElement ne : deviceType.getNetworkElementList()) {
                            if (ne.getIp() != null) {
                                Interface obj = this.interfaceService.selectObjByIp(ne.getIp());
                                if (obj != null) {
                                    if (obj.getAvailable().equals("1")) {
                                        n++;
                                        j++;
                                    }
                                }
                            }
                        }
                        deviceType.getNetworkElementList().clear();
                        deviceType.setOnlineCount(n);
                    }
                    list1.add(deviceType);
                }
                int count = nes.stream().mapToInt(e -> e.getCount()).sum();
                network.setCount(count);
                network.setOnlineCount(j);
            }
            list1.add(0, network);
            data.put("ne", JSONObject.toJSONString(list1));
            List<DeviceType> list2 = new ArrayList();
            DeviceType terminal = new DeviceType();
            terminal.setName("终端总数");
            List<DeviceType> terminals = this.deviceTypeService.selectTerminalCountByJoin();
            if (terminals.size() > 0) {
                for (DeviceType deviceType : terminals) {
                    int n = deviceType.getTerminalList().stream().mapToInt(e -> e.getOnline() == true ? 1 : 0).sum();
                    deviceType.setOnlineCount(n);
                    deviceType.getTerminalList().clear();
                    list2.add(deviceType);
                }
                int count = terminals.stream().mapToInt(e -> e.getCount()).sum();
                terminal.setCount(count);
                int onlineCount = list2.stream().mapToInt(e -> e.getOnlineCount()).sum();
                terminal.setOnlineCount(onlineCount);
            }
            list2.add(0, terminal);
            data.put("terminal", JSONObject.toJSONString(list2));


            // 网元总数
            List<NetworkElement> networkElements = this.networkElementService.selectObjAll();
            data.put("nes", networkElements);

            // 确定标签
            Interface anInterface = this.interfaceService.selectObjByIp(ip);
            if (anInterface != null) {
                Map params = new HashMap();
                params.put("ip", ip);
                params.put("cpu", Global.BOARDCPU);
                params.put("mem", Global.BOARDMEM);
                params.put("temp", Global.BOARDTEMP);
                List<ItemTag> itemTags = this.itemTagService.queryBoardByMap(params);
                if (itemTags.size() > 0) {
                    // 查找是否存在 boardcpu|不在查询CPu和内存使用率
                    List<ItemTagBoardVO> boards = this.itemTagService.selectBoard(ip, 1683181310L, 1683184910L);
                    if (boards.size() > 0) {
                        ItemTagBoardVO ele = boards.get(0);
                        data.put("cpu", JSONObject.toJSONString(ele.getCpu()));
                        data.put("mem", JSONObject.toJSONString(ele.getMem()));
                        data.put("temp", JSONObject.toJSONString(ele.getTemp()));
                    }
                } else {
                    List names = Arrays.asList(
                            "cpuusage",
                            "memusage",
                            "temp");
                    ItemTagBoardVO board = this.zabbixService.getBoard(ip, names, 0, 1683181310L, 1683184910L);
                    if (board != null) {
                        data.put("cpu", JSONObject.toJSONString(board.getCpu()));
                        data.put("mem", JSONObject.toJSONString(board.getMem()));
                        data.put("temp", JSONObject.toJSONString(board.getTemp()));
                    }
                }
            }
        }
        return data;
    }

//    @RequestMapping("/createHtml/{ip}")
//    public void createHtml(HttpServletResponse response, @PathVariable String ip) throws Exception {
//        this.getData(ip);
//        response.setContentType( "application/html; charset=UTF-8");
//        response.setHeader("Content-Disposition","Attachment;filename= " + new String(("nmap_report_" + DateTools.getCurrentDate(new Date(), "yyyyMMddHHmm") + ".html").getBytes("UTF-8"),"UTF-8"));
//        this.freemarkerUtil.createHtmlToBrowser("convertHtml.ftl", data, response.getWriter());
////        Map data = deviceInfo(ip);
////        data.put("ip", ip);
////        boolean available = this.interfaceUtil.verifyHostIsAvailable(ip);
////        if(available){
////            List<DeviceType> nes = this.deviceTypeService.selectCountByJoin();
////            List list1 = new ArrayList();
////            DeviceType network = new DeviceType();
////            network.setName("网元总数");
////            if(nes.size() > 0){
////                int j = 0;
////                for (DeviceType deviceType : nes) {
////                    int n = 0;
////                    if(deviceType.getNetworkElementList().size() > 0){
////                        for (NetworkElement ne : deviceType.getNetworkElementList()) {
////                            if(ne.getIp() != null){
////                                Interface obj = this.interfaceService.selectObjByIp(ne.getIp());
////                                if(obj != null){
////                                    if(obj.getAvailable().equals("1")){
////                                        n ++;
////                                        j ++;
////                                    }
////                                }
////                            }
////                        }
////                        deviceType.getNetworkElementList().clear();
////                        deviceType.setOnlineCount(n);
////                    }
////                    list1.add(deviceType);
////                }
////                int count = nes.stream().mapToInt(e -> e.getCount()).sum();
////                network.setCount(count);
////                network.setOnlineCount(j);
////            }
////            list1.add(0, network);
////            data.put("ne", JSONObject.toJSONString(list1));
////            List<DeviceType> list2 = new ArrayList();
////            DeviceType terminal = new DeviceType();
////            terminal.setName("终端总数");
////            List<DeviceType> terminals = this.deviceTypeService.selectTerminalCountByJoin();
////            if(terminals.size() > 0){
////                for (DeviceType deviceType : terminals) {
////                    int n = deviceType.getTerminalList().stream().mapToInt(e -> e.getOnline() == true ? 1 : 0).sum();
////                    deviceType.setOnlineCount(n);
////                    deviceType.getTerminalList().clear();
////                    list2.add(deviceType);
////                }
////                int count = terminals.stream().mapToInt(e -> e.getCount()).sum();
////                terminal.setCount(count);
////                int onlineCount = list2.stream().mapToInt(e -> e.getOnlineCount()).sum();
////                terminal.setOnlineCount(onlineCount);
////            }
////            list2.add(0, terminal);
////            data.put("terminal", JSONObject.toJSONString(list2));
////
////            // 确定标签
////            Interface anInterface = this.interfaceService.selectObjByIp(ip);
////            if(anInterface != null){
////                Map params = new HashMap();
////                params.put("ip", ip);
////                params.put("cpu", Global.BOARDCPU);
////                params.put("mem", Global.BOARDMEM);
////                params.put("temp", Global.BOARDTEMP);
////                List<ItemTag> itemTags = this.itemTagService.queryBoardByMap(params);
////                if(itemTags.size() > 0){
////                    // 查找是否存在 boardcpu|不在查询CPu和内存使用率
////                    List<ItemTagBoardVO> boards = this.itemTagService.selectBoard(ip, 1683181310L,  1683184910L);
////                    if(boards.size() > 0){
////                        ItemTagBoardVO ele = boards.get(0);
////                        data.put("cpu", JSONObject.toJSONString(ele.getCpu()));
////                        data.put("mem", JSONObject.toJSONString(ele.getMem()));
////                        data.put("temp", JSONObject.toJSONString(ele.getTemp()));
////                    }
////                }else{
////                    List names = Arrays.asList(
////                            "cpuusage",
////                            "memusage",
////                            "temp");
////                    ItemTagBoardVO board = this.zabbixService.getBoard(ip, names, 0,1683181310L,1683184910L);
////                    if(board != null){
////                        data.put("cpu", JSONObject.toJSONString(board.getCpu()));
////                        data.put("mem", JSONObject.toJSONString(board.getMem()));
////                        data.put("temp", JSONObject.toJSONString(board.getTemp()));
////                    }
////                }
////            }
////        response.setContentType( "application/html; charset=UTF-8");
////        response.setHeader("Content-Disposition","Attachment;filename= " + new String(("nmap_report_" + DateTools.getCurrentDate(new Date(), "yyyyMMddHHmm") + ".html").getBytes("UTF-8"),"UTF-8"));
////        this.freemarkerUtil.createHtmlToBrowser("convertHtml.ftl", data, response.getWriter());
////        }
//
//    }

    @RequestMapping("/createPdf")
    public void createPdf() throws Exception {
        this.pdfTemplateUtil.createPDF1(null, "test.ftl");
    }

    public Map deviceInfo(String ip){
        Map map = new HashMap();
        if(Strings.isNotBlank(ip)){
            boolean available = this.interfaceUtil.verifyHostIsAvailable(ip);
            if(available){
                List names = Arrays.asList(
                        "systemname",
                        "systemdescription",
                        "uptime");
                map = this.zabbixService.getDeviceHtml(ip,
                        names);
            }else{
                Map params = new HashMap();
                params.clear();
                params.put("ip", ip);
                List<NetworkElement> networkElements = this.networkElementService.selectObjByMap(params);
                if(networkElements.size() > 0){
                    NetworkElement networkElement = networkElements.get(0);
                    map.put("deviceName", networkElement.getDeviceName());
                    map.put("description", networkElement.getDescription());
                }
            }
        }
        return map;
    }

    @RequestMapping("/export")
    public void exportPdf(HttpServletResponse response) throws Exception{
        ByteArrayOutputStream baos = null;
        OutputStream out = null;
        try {
            // 模板中的数据，实际运用从数据库中查询
//            User user = new User();
//            user.setUsername("hkk");
            Map data = new HashMap();
            data.put("username", "hkk");
            data.put("code", "200");

            baos = PDFTemplateUtil.createPDF1(data, "convert.ftl");;

            // 设置响应消息头，告诉浏览器当前响应是一个下载文件
            response.setContentType( "application/x-msdownload");

            // 告诉浏览器，当前响应数据要求用户干预保存到文件中，以及文件名是什么 如果文件名有中文，必须URL编码
            String fileName = URLEncoder.encode("test.pdf", "UTF-8");

            response.setHeader( "Content-Disposition", "attachment;filename=" + fileName);
            out = response.getOutputStream();
            baos.writeTo(out);
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("导出失败：" + e.getMessage());
        } finally{
            if(baos != null){
                baos.close();
            }
            if(out != null){
                out.close();
            }
        }
    }

}
