package co.yiiu.web.admin;

import co.yiiu.config.SiteConfig;
import co.yiiu.core.base.BaseController;
import co.yiiu.core.bean.Result;
import co.yiiu.core.util.FreemarkerUtil;
import co.yiiu.module.node.service.NodeService;
import co.yiiu.module.security.service.RoleService;
import co.yiiu.module.user.model.User;
import co.yiiu.module.user.service.UserService;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;
import java.util.Map;

@Controller
@RequestMapping("/admin/check")
public class CheckAdminController extends BaseController{
    private Logger log = LoggerFactory.getLogger(UserAdminController.class);
    @Autowired
    private SiteConfig siteConfig;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private NodeService nodeService;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private Environment env;
    @Autowired
    FreemarkerUtil freemarkerUtil;

    /**
     * 未审核用户列表
     *
     * @param p
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String checklist(Integer p, Model model) {
        model.addAttribute("page", userService.pageUser(p == null ? 1 : p, siteConfig.getPageSize(),getUser().getNodes(),false));
        return "admin/user/check";
    }

    /**
     * 审核
     * @param id
     * @return
     */
    @PostMapping("/{id}/checkok")
    @ResponseBody
    public Result checkok(@PathVariable Integer id){
        //获取用户邮箱
        User user = userService.findById(id);
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setFrom(env.getProperty("spring.mail.username"));
            helper.setTo(user.getEmail());
            Map<String, Object> params = Maps.newHashMap();
            params.put("account", user.getUsername());
            helper.setSubject(freemarkerUtil.format(siteConfig.getCheck().getSubject(), params));
            helper.setText(freemarkerUtil.format(siteConfig.getCheck().getContentok(), params), true);
            javaMailSender.send(mimeMessage);
        }catch (Exception e){
            log.error(e.getMessage());
            return Result.error("邮件发送失败");
        }
        user.setChecked(true);
        userService.save(user);
        return Result.success();
    }

    @PostMapping("/{id}/checkno")
    @ResponseBody
    public Result checkno(@PathVariable Integer id){
        //获取用户邮箱
        User user = userService.findById(id);
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setFrom(env.getProperty("spring.mail.username"));
            helper.setTo(user.getEmail());
            Map<String, Object> params = Maps.newHashMap();
            params.put("account", user.getUsername());
            params.put("adminname", getUser().getRealName());
            params.put("phone", getUser().getPhone());
            helper.setSubject(freemarkerUtil.format(siteConfig.getCheck().getSubject(), params));
            helper.setText(freemarkerUtil.format(siteConfig.getCheck().getContentno(), params), true);
            javaMailSender.send(mimeMessage);
        }catch (Exception e){
            log.error(e.getMessage());
            return Result.error("邮件发送失败");
        }
        userService.deleteById(id);
        return Result.success();
    }
}
