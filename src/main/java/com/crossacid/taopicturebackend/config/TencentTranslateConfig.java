package com.crossacid.taopicturebackend.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class TencentTranslateConfig {

    @Value("${cos.client.secretId}")
    private String secretId;

    @Value("${cos.client.secretKey}")
    private String secretKey;

}
