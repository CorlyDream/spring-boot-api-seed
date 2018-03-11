package cc.corly.springboot.demo.service.impl;

import cc.corly.springboot.demo.entity.User;
import cc.corly.springboot.demo.mapper.UserMapper;
import cc.corly.springboot.demo.service.IUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Generator
 * @since 2018-03-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
