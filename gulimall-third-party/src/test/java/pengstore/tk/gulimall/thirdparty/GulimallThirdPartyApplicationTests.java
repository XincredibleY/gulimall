package pengstore.tk.gulimall.thirdparty;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class GulimallThirdPartyApplicationTests {

    @Autowired
    OSS ossClient;

    /**
     * 1、引入oss-starter https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-samples/aliyun-oss-spring-boot-sample
     * 2、配置key、endpoint相关信息即可 本例中在endPoint配置的
     * 3、使用OSSClient 进行相关操作
     * @throws FileNotFoundException
     */
    @Test
    public void testUpload() throws FileNotFoundException {
        // This example uses the endpoint of the China (Hangzhou) region. Specify the actual endpoint based on your requirements.
//        String endpoint = "oss-eu-central-1.aliyuncs.com";
        // Security risks may arise if you use the AccessKey pair of an Alibaba Cloud account to log on to OSS, because the account has permissions on all API operations. To ensure cloud security, we recommend that you follow best practices of Resource Access Management and use a RAM user account to call API operations or perform routine operations and maintenance. To create a RAM user account, log on to https://ram.console.aliyun.com.
//        String accessKeyId = "LTAI4G5aoMy572EEsuTb6Hte";
//        String accessKeySecret = "lf7sHUSoJ7iqkWV2AtioqPohy59UWC";

        // Create an OSSClient instance.
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // Upload a file stream.
        InputStream inputStream = new FileInputStream("D:\\BaiduNetdiskDownload\\谷粒商城分布式高级篇+高可用集群\\高级\\课件代码\\docs\\docs\\谷粒商城-微服务架构图.jpg");
        ossClient.putObject("test-peng", "haha.jpg", inputStream);

        // Shut down the OSSClient instance.
        ossClient.shutdown();

        System.out.println("上传完成");
    }
    @Test
    void contextLoads() {
    }

}
