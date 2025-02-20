package com.crossacid.taopicturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crossacid.taopicturebackend.model.dto.spaceuser.SpaceUserAddRequest;
import com.crossacid.taopicturebackend.model.dto.spaceuser.SpaceUserQueryRequest;
import com.crossacid.taopicturebackend.model.entity.SpaceUser;
import com.crossacid.taopicturebackend.model.vo.SpaceUserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author JiangTaoYu
* @description 针对表【space_user(空间用户关联)】的数据库操作Service
* @createDate 2025-02-19 19:44:25
*/
public interface SpaceUserService extends IService<SpaceUser> {

    /**
     * 创建空间成员
     * @param spaceUserAddRequest
     * @return
     */
    long addSpaceUser(SpaceUserAddRequest spaceUserAddRequest);

    /**
     * 检验空间成员
     * @param spaceUser
     * @param add
     */
    void validSpaceUser(SpaceUser spaceUser, boolean add);

    /**
     * 查询对象转封装类
     * @param spaceUserQueryRequest
     * @return
     */
    QueryWrapper<SpaceUser> getQueryWrapper(SpaceUserQueryRequest spaceUserQueryRequest);

    /**
     * 查询单个封装类
     * @param spaceUser
     * @param request
     * @return
     */
    SpaceUserVO getSpaceUserVO(SpaceUser spaceUser, HttpServletRequest request);

    /**
     * 查询封装类列表
     * @param spaceUserList
     * @return
     */
    List<SpaceUserVO> getSpaceUserVOList(List<SpaceUser> spaceUserList);
}
