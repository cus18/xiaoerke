package com.cxqm.xiaoerke.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.cxqm.xiaoerke.common.config.Global;

/**
 * oss 工具类
 * @author ft
 *
 */
public class OSSObjectTool {  
    private static  OSSClient ossClient = null;
    public static String BUCKET_DOCTOR_PIC = null;
    public static String BUCKET_ARTICLE_PIC = null;
    static {
    	String accesskey = Global.getConfig("aliyun.accesskey");
		String secret =  Global.getConfig("aliyun.secret");
		String host =  Global.getConfig("oss.host");
		BUCKET_DOCTOR_PIC = Global.getConfig("oss.bucket.doctor.pic");
		BUCKET_ARTICLE_PIC = Global.getConfig("oss.bucket.article.pic");
    	ossClient = new  OSSClient(host,accesskey, secret);
    }

    /**
     * 上传文件到oss
     * @param key
     * @param length
     * @param in
     * @param bucket
     * @return key
     * @throws OSSException
     * @throws ClientException
     * @throws FileNotFoundException
     */
    public static String uploadFileInputStream(String key, Long length ,InputStream in, String bucket) {
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(length);
        new HashMap<String,String>();
        ossClient.putObject(bucket, key , in, objectMeta);
        return key;
    }
    
    /**
     * 获取医生头像的 base url, 注 base url + key = full url
     * @return
     */
    public static String getDoctorPicBaseUrl(){
    	 return "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/";
    }
    
    public static Map<String,String> getObject(String bucketName, String key)
            throws OSSException, ClientException, FileNotFoundException {
    	//OSSObject ossObject = ossClient.getObject(bucketName, key);
        return null;
    }
    
    public static void main(String[] args) throws Exception {
		String file = "D:\\5.png";
		File f = new File(file);
		String bucket =  Global.getConfig("oss.bucket.doctor.pic");
		uploadFileInputStream("123", f.length(), new FileInputStream(f), bucket);
    }
    
}
