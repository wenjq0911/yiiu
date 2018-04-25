package co.yiiu.web.front;

import co.yiiu.core.base.BaseController;
import co.yiiu.core.bean.Result;
import co.yiiu.core.util.IpUtil;
import co.yiiu.module.log.model.VisitLog;
import co.yiiu.module.log.model.VisitLogModel;
import co.yiiu.module.log.service.VisitLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/visitlog")
public class VisitLogController extends BaseController {
    @Autowired
    private VisitLogService visitLogService;

    @GetMapping("/add")
    @ResponseBody
    public Result Add(HttpServletRequest request) throws UnsupportedEncodingException {
        String ip = IpUtil.getIpAddr(request);
        //根据ip获取地区
        String city = IpUtil.getAddresses(ip,"utf-8");
        VisitLog log = new VisitLog();
        log.setAddress(city==null?"":city);
        log.setInTime(new Date());
        log.setIp(ip);
        log.setUser(getUser());
        visitLogService.save(log);
        return Result.success();
    }

    @GetMapping("/logs")
    @ResponseBody
    public  Result Logs(){
        List<VisitLogModel> all = visitLogService.findByGroupAddress();
        return Result.success(all);
    }

    @GetMapping("/times")
    @ResponseBody
    public Result LogsByTime(){
        List<VisitLogModel> all = visitLogService.findByGroupTime();
        return Result.success(all);
    }
}
