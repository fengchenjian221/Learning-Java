package boot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * hdfs配置
 */
@Configuration
@Data
public class HdfsConfig {
    // hdfs nameNode连接URL
    @Value("${nameNode.url}")
    private String nameNodeUrl;

    // 操作用户
    @Value("${hdfs.userName}")
    private String hdfsUserName;

    // 操作存储节点路径
    @Value("${hdfs.dataNode}")
    private String pdfDataNode;

}

