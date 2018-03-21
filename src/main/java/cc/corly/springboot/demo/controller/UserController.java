package cc.corly.springboot.demo.controller;


import cc.corly.springboot.demo.entity.User;
import cc.corly.springboot.demo.service.IUserService;
import cc.corly.springboot.demo.util.Result;
import cc.corly.springboot.demo.util.ResultGenerator;
import cc.corly.springboot.demo.util.ServiceException;
import com.baomidou.mybatisplus.plugins.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Generator
 * @since 2018-03-10
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Resource
    private IUserService userService;

    @GetMapping("list")
    public Result getUserList(@RequestParam(defaultValue = "10") Integer size,
                              @RequestParam(defaultValue = "1") Integer page) {

        Page<User> pageObj = new Page<>();
        pageObj.setSize(size);
        pageObj.setCurrent(page);
        pageObj = userService.selectPage(pageObj);
        return ResultGenerator.genSuccessResult(pageObj);
    }

    @PostMapping("add")
    public Result addUser(@RequestBody User user) {
        log.info("test add user");
        userService.insert(user);
        return ResultGenerator.genSuccessResult(user.getTestId());
    }

    @GetMapping("except")
    public Result test() {
        throw new IllegalArgumentException("test");
    }
}

