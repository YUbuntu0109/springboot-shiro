/**
 * MIT License
 * Copyright (c) 2018 yadong.zhang
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.zyd.shiro.controller;

import com.zyd.shiro.framework.object.ResponseVO;
import com.zyd.shiro.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @project: springboot-shiro
 * @description: 登录模块的控制器
 * @date: 2019-08-15 7:25 PM
 * @version: 1.0
 * @website: https://yubuntu0109.github.io/
 */
@Slf4j
@Controller
@RequestMapping(value = "/passport")
public class PassportController {

    /**
     * @description: 跳转到用户登录页
     * @param: model
     * @date: 2019-08-15 7:26 PM
     * @return: org.springframework.web.servlet.ModelAndView
     */
    @GetMapping("/login")//the parameter is never used
    public ModelAndView login(Model model) {
        return ResultUtil.view("/login");
    }

    /**
     * @description: 验证用户登录信息
     * @param: username
     * @param: password
     * @param: rememberMe
     * @param: kaptcha
     * @date: 2019-08-15 7:26 PM
     * @return: com.zyd.shiro.framework.object.ResponseVO
     */
    @PostMapping("/signin")
    @ResponseBody//the parameter of 'kaptcha' is never used
    public ResponseVO submitLogin(String username, String password, boolean rememberMe, String kaptcha) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        try {
            // 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            // 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            // 所以这一步在调用login(token)方法时,它会走到xxRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            currentUser.login(token);
            return ResultUtil.success("登录成功！");
        } catch (Exception e) {
            log.error("登录失败，用户名[{}]", username, e);
            token.clear();
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
     * @description: 使用权限管理工具完成用户的退出, 跳出登录, 给出提示信息
     * @param: redirectAttributes
     * @date: 2019-08-15 7:30 PM
     * @return: org.springframework.web.servlet.ModelAndView
     */
    @GetMapping("/logout")
    public ModelAndView logout(RedirectAttributes redirectAttributes) {
        // 问题: http://www.oschina.net/question/99751_91561
        // 解答: 退出登录代码其实不用实现,只需要保留这个接口即可,因为注销功能是由Shiro默认实现的,既而以下用于注销的代码可省略哟
        // SecurityUtils.getSubject().logout();
        redirectAttributes.addFlashAttribute("message", "您已安全退出");
        return ResultUtil.redirect("index");
    }
}
