package com.disk.file.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PathUtil {

    public static String getFilePath() {

        String path = "upload";
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
        path = File.separator + path + File.separator + formater.format(new Date());

        String staticPath = PathUtil.getStaticPath();

        File dir = new File(staticPath + path);
        if (!dir.exists()) {
            try {
                boolean isSuccessMakeDir = dir.mkdirs();
                if (!isSuccessMakeDir) {
                    log.error("目录创建失败:" + PathUtil.getStaticPath() + path);
                }
            } catch (Exception e) {
                log.error("目录创建失败" + PathUtil.getStaticPath() + path);
                return "";
            }
        }
        return path;
    }

    public static String getStaticPath() {
        String localStoragePath = PropertiesUtil.getProperty("file.local-storage-path");
        if (StringUtils.isNotEmpty(localStoragePath)) {
            return localStoragePath;
        }else {
            String projectRootAbsolutePath = getProjectRootPath();

            int index = projectRootAbsolutePath.indexOf("file:");
            if (index != -1) {
                projectRootAbsolutePath = projectRootAbsolutePath.substring(0, index);
            }

            return projectRootAbsolutePath + "static" + File.separator;
        }


    }

    /**
     * 路径解码
     * @param url
     * @return
     */
    public static String urlDecode(String url){
        String decodeUrl = null;
        try {
            decodeUrl = URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  decodeUrl;
    }

    /**
     * 获取项目所在的根目录路径 resources路径
     * @return
     */
    public static String getProjectRootPath() {
        String absolutePath = null;
        try {
            String url = ResourceUtils.getURL("classpath:").getPath();
            absolutePath = urlDecode(new File(url).getAbsolutePath()) + File.separator;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return absolutePath;
    }

}