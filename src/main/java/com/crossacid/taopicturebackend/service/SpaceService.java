package com.crossacid.taopicturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crossacid.taopicturebackend.model.dto.space.SpaceAddRequest;
import com.crossacid.taopicturebackend.model.dto.space.SpaceQueryRequest;
import com.crossacid.taopicturebackend.model.entity.Space;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crossacid.taopicturebackend.model.entity.User;
import com.crossacid.taopicturebackend.model.vo.SpaceVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author JiangTaoYu
* @description 针对表【space(空间)】的数据库操作Service
* @createDate 2025-02-16 11:05:53
*/
public interface SpaceService extends IService<Space> {

    /**
     * 校验空间
     * @param space
     */
    void validSpace(Space space, boolean add);

    /**
     * 获取空间包装类
     * @param space
     * @param request
     * @return
     */
    SpaceVO getSpaceVO(Space space, HttpServletRequest request);

    /**
     * 获取空间包装类 分页
     * @param spacePage
     * @param request
     * @return
     */
    Page<SpaceVO> getSpaceVOPage(Page<Space> spacePage, HttpServletRequest request);

    /**
     * 获取分页查询对象
     * @param spaceQueryRequest
     * @return
     */
    QueryWrapper<Space> getQueryWrapper(SpaceQueryRequest spaceQueryRequest);


    /**
     * 根据空间级别填充空间对象
     * @param space
     */
    void fillSpaceBySpaceLevel(Space space);

    /**
     * 创建空间
     * @param spaceAddRequest
     * @param loginUser
     * @return
     */
    long addSpace(SpaceAddRequest spaceAddRequest, User loginUser);

    /**
     * 校验空间权限
     * @param loginUser
     * @param space
     */
    void checkSpaceAuth(User loginUser, Space space);
}
