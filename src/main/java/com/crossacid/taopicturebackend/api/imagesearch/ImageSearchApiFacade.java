package com.crossacid.taopicturebackend.api.imagesearch;

import com.crossacid.taopicturebackend.api.imagesearch.model.ImageSearchResult;
import com.crossacid.taopicturebackend.api.imagesearch.baidu.GetImageFirstUrlApi;
import com.crossacid.taopicturebackend.api.imagesearch.baidu.GetImageListBaiduApi;
import com.crossacid.taopicturebackend.api.imagesearch.pexels.GetImageListPexelsApi;
import com.crossacid.taopicturebackend.api.imagesearch.baidu.GetImagePageUrlApi;
import com.crossacid.taopicturebackend.api.translate.TencentTranslate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@Component
public class ImageSearchApiFacade {

    @Resource
    private TencentTranslate tencentTranslate;

    /**
     * 搜索图片
     * @Deprecated 百度图片搜索图片功能的acs—token会变化，
     *             需要对app.js进行解析，略显复杂，此处废弃，改用pexels的api
     * @param imageUrl
     * @return
     */
    @Deprecated
    public static List<ImageSearchResult> searchImageByBaidu(String imageUrl) {
        String imagePageUrl = GetImagePageUrlApi.getImagePageUrl(imageUrl);
        System.out.println(imagePageUrl);
        String imageFirstUrl = GetImageFirstUrlApi.getImageFirstUrl(imagePageUrl);
        return GetImageListBaiduApi.getImageList(imageFirstUrl);
    }

    /**
     * 搜索图片
     *
     * @param query 图片关键字
     * @return 图片搜索结果列表
     */
    public List<ImageSearchResult> searchImage(String query) throws NoSuchAlgorithmException, IOException, InvalidKeyException {
        // 这里可以通过图像URL获取相关的搜索结果。因为我们使用Pexels，我们可以基于图像URL进行直接搜索。
        String en = tencentTranslate.Zh2Eng(query);
        System.out.println(en);
        return GetImageListPexelsApi.getImageList(en);
    }

    public static void main(String[] args) {
        // 测试以图搜图功能
        String imageUrl = "https://www.codefather.cn/logo.png";
        List<ImageSearchResult> resultList = searchImageByBaidu(imageUrl);
//        String query = "game";
//        List<ImageSearchResult> resultList = searchImage(query);
        System.out.println("结果列表" + resultList);
    }
}

