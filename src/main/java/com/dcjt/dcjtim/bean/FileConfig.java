package com.dcjt.dcjtim.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 滔哥
 * @since 2020/7/29
 */
@Data
@Component
@ConfigurationProperties(prefix = "file")
@PropertySource(value = {"classpath:applicationDynamic.properties"})
public class FileConfig {

    /**
     * 文件保存路径
     */
    private String savePath;

    /**
     * 允许的后缀名
     */
    List<String> fileSuffix;
}
