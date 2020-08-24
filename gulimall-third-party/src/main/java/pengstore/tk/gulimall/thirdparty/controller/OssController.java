package pengstore.tk.gulimall.thirdparty.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pengstore.tk.common.utils.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class OssController {

    @Autowired
    OSS ossClient;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;

    @Value("${spring.cloud.alicloud.oss.bucket}")
    private String bucket;

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessId;

    @RequestMapping("/oss/policy")  // 要oss签名信息
    public R policy(){
        // Map<String, String>
        // String bucket = "test-peng"; // Enter your bucket name.
        // https://test-peng.oss-eu-central-1.aliyuncs.com/haha.jpg
        String host = "https://" + bucket + "." + endpoint; // Set host to a value in the format of bucketname.endpoint.

        // Set the URL of the server to which an upload callback request is sent. Replace the IP address and port number with your actual information.
        // String callbackUrl = "http://88.88.88.88:8888";  // 上传回调 暂时不需要
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dir = format+"/"; // Specify the prefix for the name of the object to be uploaded. 上传时指定的前缀，我们希望以日期为前缀

        Map<String, String> respMap = null;
        // Create an OSSClient instance. 此处不需要了，直接自动注入ossClient
        // OSS ossClient = new OSSClientBuilder().build(endpoint, accessId, accessKey);
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // You can upload an object up to 5 GB in size by using PostObject. To upload an object that is 5 GB in size, set CONTENT_LENGTH_RANGE to 5*1024*1024*1024.
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            // respMap.put("expire", formatISO8601Date(expiration));

    //        配置相应数据，并以跨域的方式响应出去，在这里不用了，我们统一在网关处理
    //        JSONObject jasonCallback = new JSONObject();
    //        jasonCallback.put("callbackUrl", callbackUrl);
    //        jasonCallback.put("callbackBody",
    //                "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
    //        jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
    //        String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
    //        respMap.put("callback", base64CallbackBody);
    //
    //        JSONObject ja1 = JSONObject.fromObject(respMap);
    //        // System.out.println(ja1.toString());
    //        response.setHeader("Access-Control-Allow-Origin", "*");
    //        response.setHeader("Access-Control-Allow-Methods", "GET, POST");
    //        response(request, response, ja1.toString());

        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        } finally {
            ossClient.shutdown();
        }

        return R.ok().put("data", respMap);
    }
}
