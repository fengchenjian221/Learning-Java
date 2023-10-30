package boot.config;


import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Slf4j
public class DynamicHDFSClient {

    // hdfs client
    private FileSystem fs;

    Configuration conf;

    public DynamicHDFSClient(String hadoopConfig) {
        // hadoopConfig路径
        /* 设置hdfs连接参数 */
        conf = new Configuration();
        if (StringUtils.isBlank(hadoopConfig)) {
            log.info("add resources hadoop default config.");
            //本地测试加载
            conf.addResource("core-site.xml");
            conf.addResource("hdfs-site.xml");
        } else {
            log.info("add {} hadoop config.", hadoopConfig);
            conf.addResource(new Path(hadoopConfig, "core-site.xml"));
            conf.addResource(new Path(hadoopConfig, "hdfs-site.xml"));
        }
        // 本地测试记得把这个加上！（如果不加此配置，会导致映射不到，出现上传下载卡住超时异常）
        // conf.set("dfs.client.use.datanode.hostname", "true");
        // 缓存 fs, 避免多 datanode 异常：Caused by: java.io.IOException: Filesystem closed
        conf.setBoolean("fs.hdfs.impl.disable.cache", true);
        /* get client */
        try {
            log.info("get hdfs client");
            fs = FileSystem.get(conf);
            log.info("get hdfs client success");
        } catch (Exception e) {
            log.info("get hdfs client error: {}", Throwables.getStackTraceAsString(e));
        }
    }

    /* 手动关闭客户端 */
    public void close() {
        try {
            fs.close();
        } catch (IOException e) {
            log.error("hdfs close error: {}", Throwables.getStackTraceAsString(e));
        }

    }

    /**
     * 本地文件上传hdfs
     *
     * @param localFileStr
     * @param dstFileStr
     */
    public void copyFromLocalFile(String localFileStr, String dstFileStr) {
        copyFromLocalFile(true, localFileStr, dstFileStr);
    }

    /**
     * 本地文件上传hdfs
     *
     * @param override
     * @param localFileStr
     * @param dstFileStr
     */
    public void copyFromLocalFile(Boolean override, String localFileStr, String dstFileStr) {
        try {
            fs.copyFromLocalFile(false, override, new Path(localFileStr), new Path(dstFileStr));
        } catch (Exception e) {
            log.error("hdfs copyFromLocalFile error: {}", Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 创建hdfs文件
     */
    public FSDataOutputStream createFile2(String filename) throws Exception {
        Path filePath = new Path(filename);
        return fs.create(filePath, true);
    }

    /**
     * 读取hdfs指定目录下文件列表
     *
     * @param path
     * @param recursion
     * @throws Exception
     */
    public List<FileStatus> listFile2(Path path, boolean recursion) throws Exception {
        List<FileStatus> files = new ArrayList<>();
        log.info(path.toString());
        FileStatus[] fileStatusList = fs.listStatus(path);
        for (FileStatus fileStatus : fileStatusList) {
            if (fileStatus.isDirectory()) {
                if (recursion) {
                    files.addAll(listFile2(fileStatus.getPath(), true));
                } else {
                    files.add(fileStatus);
                }
            } else {
                files.add(fileStatus);
            }
        }

        return files;
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public boolean checkFileExist(String fileName) throws Exception {
        Path path = new Path(fileName);
        return fs.exists(path);
    }

    /**
     * @param path
     * @param recursion 是否包括子目录文件
     * @return
     * @throws Exception
     */
    public List<String> listFile(Path path, boolean recursion) throws Exception {
        List<FileStatus> fileStatusList = listFile2(path, recursion);
        if (CollectionUtils.isEmpty(fileStatusList)) {
            return Lists.newArrayList();
        }
        List<String> files = Lists.newArrayList();
        for (FileStatus fileStatus : fileStatusList) {
            if (fileStatus.isDirectory()) {
                if (recursion) {
                    files.addAll(listFile(fileStatus.getPath(), recursion));
                } else {
                    files.add(fileStatus.getPath().toString());
                }
            } else {
                files.add(fileStatus.getPath().toString());
            }
        }
        return files;
    }

    /**
     * 获取path目录下 & 文件名包含containsKey & 最后生成的目录
     *
     * @param path
     * @param containsKey
     * @return
     * @throws Exception
     */
    public String getLastDir(Path path, String containsKey) throws Exception {
        String file = "";

        FileStatus[] fileStatusList = fs.listStatus(path);
        long modTime = 0;
        for (FileStatus fileStatus : fileStatusList) {
            String tmpFile = fileStatus.getPath().toString();
            if (fileStatus.getModificationTime() > modTime
                    && tmpFile.contains(containsKey)
                    && fileStatus.isDirectory()
                    && listFile(fileStatus.getPath(), false).size() > 0
            ) {
                file = tmpFile;
                modTime = fileStatus.getModificationTime();
            }
        }

        return file;
    }


    /**
     * 在HDFS创建文件夹
     *
     * @param path
     * @return
     * @throws Exception
     */
    public boolean mkdir(String path) {
        boolean isOk = false;
        if (StringUtils.isBlank(path)) {
            return false;
        }
        try {
            if (existFile(path)) {
                log.info("hdfs file is exists: {}", path);
                return true;
            }
            Path srcPath = new Path(path);
            isOk = fs.mkdirs(srcPath);
            log.info("hdfs mkdir success: {}", path);
        } catch (Exception e) {
            log.error("hdfs mkdir: {}, error: {}", path, Throwables.getStackTraceAsString(e));
        }
        return isOk;
    }

    /**
     * 判断HDFS文件是否存在
     *
     * @param path
     * @return true 存在  false 不存在
     * @throws Exception
     */
    public boolean existFile(String path) {
        boolean isExists = false;
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        try {
            Path srcPath = new Path(path);
            isExists = this.fs.exists(srcPath);
        } catch (Exception e) {
            log.error("existFile error: {}", Throwables.getStackTraceAsString(e));
        }
        return isExists;
    }

    /**
     * HDFS远程文件复制
     *
     * @param sourcePath 资源文件path
     * @param targetPath 目标文件path
     * @throws Exception
     */
    public void copyRemoteFile(String sourcePath, String targetPath) {
        try {
            if (StringUtils.isEmpty(sourcePath) || StringUtils.isEmpty(targetPath)) {
                return;
            }
            // 1. 资源文件不存在直接返回
            if (!existFile(sourcePath)) {
                log.info("copyRemoteFile sourcePath {} is not exist", sourcePath);
                return;
            }
            /* 2. copy remote*/
            // source
            Path oldPath = new Path(sourcePath);
            // target
            Path newPath = new Path(targetPath);
            FileUtil.copy(fs, oldPath, fs, newPath, false, conf);
            log.info("remote cope file success,sourcePath:{}, targetPath:{}", sourcePath, targetPath);
        } catch (Exception e) {
            log.error("hdfs copyRemoteFile error: {}", Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 删除HDFS文件 不知处删除目录
     *
     * @param path
     * @throws Exception
     * @returnhadoo
     */
    public boolean deleteFile(String path) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        boolean isOk = false;
        try {
            if (!existFile(path)) {
                return false;
            }
            Path srcPath = new Path(path);
            isOk = fs.deleteOnExit(srcPath);
        } catch (Exception e) {
            log.error("hdfs deleteFile {}", Throwables.getStackTraceAsString(e));
        }
        return isOk;
    }

    /**
     * 删除hdf目录
     *
     * @param path
     * @return
     */
    public void deleteDir(String path) {
        try {
            if (!existFile(path)) {
                return;
            }
            fs.delete(new Path(path), true);
        } catch (IOException e) {
            log.error("deleteDir error:{}", Throwables.getStackTraceAsString(e));
        }
    }

    public static void main(String[] args) throws Exception {
        DynamicHDFSClient hdfsClient = new DynamicHDFSClient("/config/conf");
        //String sourcePath = "/user/flink/user-dependencies/depends-test.jar";
        String sourcePath = "hdfs:///user/jaer/commons-cli-1.4.jar";
        String targetPath = "/user/flink/tmp/test/commons-cli-1.4.jar";
        System.out.println(sourcePath.substring(sourcePath.lastIndexOf("/") + 1));
        //hdfsClient.copyRemoteFile(sourcePath, targetPath);

    }


}

