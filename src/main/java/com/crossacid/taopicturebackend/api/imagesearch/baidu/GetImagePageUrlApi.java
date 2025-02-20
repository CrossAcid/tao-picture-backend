package com.crossacid.taopicturebackend.api.imagesearch.baidu;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.crossacid.taopicturebackend.exception.BusinessException;
import com.crossacid.taopicturebackend.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Deprecated
public class GetImagePageUrlApi {

    /**
     * 获取图片页面地址
     *
     * @param imageUrl
     * @return
     */
    public static String getImagePageUrl(String imageUrl) {
        // 1. 准备请求参数
        Map<String, Object> formData = new HashMap<>();
        formData.put("image", imageUrl);
        formData.put("tn", "pc");
        formData.put("from", "pc");
        formData.put("image_source", "PC_UPLOAD_URL");
        // 获取当前时间戳
        long uptime = System.currentTimeMillis();
        // 请求地址
        String url = "https://graph.baidu.com/upload?uptime=" + uptime;
        // 二至三小时过期
        String acs_token = "1739766629358_1739780573221_RCmitpJIoppLLa6BOibcyipSZofiISxG7QHVU4sFPWlVodpRZvBZ9bqe3lCTKBH95O0/RLV+0M8FpQDOSAmIM9XBH7SxOcXGOh0xYz32roIvCB+iKMK+0JdXNdmYKRrxrHrMN7hmCctAC6lLklJ8YyPeJb/QDANHedYFpfkDwBeHcAxvIrNAqdwIvAySsRiSJrYbd9NsAL5AOLAfDAf5sUT/lz+xfNIZtDj+SoHtwz7YeNT3kC5YJ+eMAw44J0PPW5Hju5zI92akkFT04JVqSwmRHpTP1eV9zQrUYfIZX5lQgYgDJ8TYi692iLrkjZzw8eMhi4T4/lgROKx/n2LklG67ESl+pkaCD21AErSNJg2gsPIGPMs5UlG+XIK1rcpB2VDHTHLWCgDsXlBp7YcxCnqZOwACUBhyN6YYDaoEPdE=";
        try {
            // 2. 发送 POST 请求到百度接口
            HttpResponse response = HttpRequest.post(url)
                    .form(formData)
                    .header("acs-token", acs_token)
                    .timeout(5000)
                    .execute();
            // 判断响应状态
            if (HttpStatus.HTTP_OK != response.getStatus()) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "接口调用失败");
            }
            // 解析响应
            String responseBody = response.body();
            Map<String, Object> result = JSONUtil.toBean(responseBody, Map.class);

            // 3. 处理响应结果
            if (result == null || !Integer.valueOf(0).equals(result.get("status"))) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "接口调用失败");
            }
            Map<String, Object> data = (Map<String, Object>) result.get("data");
            String rawUrl = (String) data.get("url");
            // 对 URL 进行解码
            String searchResultUrl = URLUtil.decode(rawUrl, StandardCharsets.UTF_8);
            // 如果 URL 为空
            if (searchResultUrl == null) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "未返回有效结果");
            }
            return searchResultUrl;
        } catch (Exception e) {
            log.error("搜索失败", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "搜索失败");
        }
    }

    public static void main(String[] args) {
        // 测试以图搜图功能
        String imageUrl = "https://www.codefather.cn/logo.png";
        String result = getImagePageUrl(imageUrl);
        System.out.println("搜索成功，结果 URL：" + result);
    }
}

