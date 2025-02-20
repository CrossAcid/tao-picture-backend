package com.crossacid.taopicturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.crossacid.taopicturebackend.model.dto.user.UserQueryRequest;
import com.crossacid.taopicturebackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crossacid.taopicturebackend.model.vo.LoginUserVO;
import com.crossacid.taopicturebackend.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author JiangTaoYu
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-02-12 16:24:56
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 加密密码
     *
     * @param userPassword 输入的密码
     * @return 密码
     */
    String getEncryptPassword(String userPassword);

    /**
     * 获得脱敏后的登录用户信息
     * @param user 用户
     * @return 脱敏后的登录用户
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取当前登录用户
     *
     * @param request request
     * @return 当前登录用户
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request request
     * @return boolean
     */
    boolean userLogout(HttpServletRequest request);


    /**
     * 获得脱敏后的用户信息
     * @param user 用户
     * @return UserVO
     */
    UserVO getUserVO(User user);

    /**
     * 获得脱敏后的用户信息列表
     * @param userList userList
     * @return List
     */
    List<UserVO> getUserVOList(List<User> userList);

    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(User user);

}
