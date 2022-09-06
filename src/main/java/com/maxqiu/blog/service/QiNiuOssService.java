package com.maxqiu.blog.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maxqiu.blog.properties.QiNiuOssProperties;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

/**
 * 七牛云 服务
 *
 * @author Max_Qiu
 */
@Service
public class QiNiuOssService {
    private static final Configuration CONFIGURATION = new Configuration(Region.huadong());

    @Resource
    private QiNiuOssProperties ossProperties;

    /**
     * 上传文件
     *
     * @param file
     *            文件
     * @param fileName
     *            文件名称（含前缀）
     */
    public Boolean uploadFile(File file, String fileName) {
        if (!ossProperties.getEnable()) {
            return null;
        }
        if (file.isDirectory()) {
            System.out.println("不能上传文件夹");
            return false;
        }
        UploadManager uploadManager = new UploadManager(CONFIGURATION);
        Auth auth = Auth.create(ossProperties.getAccessKey(), ossProperties.getSecretKey());
        String upToken = auth.uploadToken(ossProperties.getBucket());
        try {
            uploadManager.put(file, fileName, upToken);
        } catch (QiniuException ex) {
            return false;
        }
        return true;
    }

    /**
     * 删除文件
     *
     * @param fileName
     *            文件名称（含前缀）
     */
    public Boolean deleteFile(String fileName) {
        if (!ossProperties.getEnable()) {
            return null;
        }
        Auth auth = Auth.create(ossProperties.getAccessKey(), ossProperties.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, CONFIGURATION);
        try {
            bucketManager.delete(ossProperties.getBucket(), fileName);
        } catch (QiniuException ex) {
            return false;
        }
        return true;
    }

    /**
     * 上传文件夹
     *
     * @param basePath
     *            路径前缀
     * @param directory
     *            文件夹
     */
    public Map<String, Integer> uploadDirectory(String basePath, File directory) {
        File[] files = directory.listFiles();
        Map<String, Integer> result = new HashMap<>();
        int success = 0;
        int error = 0;
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (uploadFile(file, basePath + "/" + file.getName())) {
                        success++;
                    } else {
                        error++;
                    }
                } else if (file.isDirectory()) {
                    Map<String, Integer> childResult = uploadDirectory(basePath + "/" + file.getName(), file);
                    success = success + childResult.get("success");
                    error = error + childResult.get("error");
                }
            }
        }
        result.put("success", success);
        result.put("error", error);
        return result;
    }
}
