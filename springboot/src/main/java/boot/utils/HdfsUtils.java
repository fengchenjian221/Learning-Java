package boot.utils;


import boot.config.HdfsConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HdfsUtils {

    public static final Logger logger = LoggerFactory.getLogger(HdfsUtils.class);


    @Autowired
    private HdfsConfig hdfsConfig;

    /**
     * 获取HDFS配置信息 配置文件优先级
     * Configuration  > resource下的hdfs-site.xml > 服务器上的 hdfs-default.xml
     *
     * @return
     */
    private Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.set("dfs.support.append", "true");
        configuration.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");
        configuration.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        return configuration;
    }


    /**
     * 获取HDFS文件系统对象
     *
     * @return
     * @throws Exception
     */
    public FileSystem getFileSystem() throws Exception {
        // 客户端去操作hdfs时是有一个用户身份的，默认情况下hdfs客户端api会从jvm中获取一个参数作为自己的用户身份
        // DHADOOP_USER_NAME=hadoop
        // 也可以在构造客户端fs对象时，通过参数传递进去
        FileSystem fileSystem = FileSystem.get(new URI(hdfsConfig.getNameNodeUrl()), getConfiguration(), hdfsConfig.getHdfsUserName());
        return fileSystem;
    }

    /**
     * 在HDFS创建文件夹
     *
     * @param path
     * @return
     * @throws Exception
     */
    public boolean mkdir(String path) throws Exception {
        FileSystem fs = null;
        boolean isOk = false;
        if (StringUtil.isEmpty(path)) {
            return false;
        }
        try {
            if (existFile(path)) {
                logger.error("hdfs file is exists: {}", path);
                return true;
            }
            // 目标路径
            fs = getFileSystem();
            Path srcPath = new Path(path);
            isOk = fs.mkdirs(srcPath);
            logger.error("hdfs mkdir success: {}", path);
        } catch (Exception e) {
            logger.error("hdfs mkdir: {}", e);
        } finally {
            if (fs != null) {
                fs.close();
            }
        }
        return isOk;
    }

    /**
     * 判断HDFS文件是否存在
     *
     * @param path
     * @return
     * @throws Exception
     */
    public boolean existFile(String path) throws Exception {
        Boolean isExists = false;
        FileSystem fs = null;
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        try {
            fs = getFileSystem();
            Path srcPath = new Path(path);
            isExists = fs.exists(srcPath);
        } catch (Exception e) {
            logger.error("existFile {}", e);
        } finally {
            if (fs != null) {
                fs.close();
            }
        }
        return isExists;
    }

    /**
     * 读取HDFS目录信息
     *
     * @param path
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> readPathInfo(String path) throws Exception {
        try {
            if (StringUtils.isEmpty(path)) {
                return null;
            }
            if (!existFile(path)) {
                return null;
            }
            FileSystem fs = getFileSystem();
            // 目标路径
            Path newPath = new Path(path);
            FileStatus[] statusList = fs.listStatus(newPath);
            List<Map<String, Object>> list = new ArrayList<>();
            if (null != statusList && statusList.length > 0) {
                for (FileStatus fileStatus : statusList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("filePath", fileStatus.getPath());
                    map.put("fileStatus", fileStatus.toString());
                    list.add(map);
                }
                return list;
            }
        } catch (Exception e) {
            logger.error("hdfs readPathInfo {}", e);
        }
        return null;
    }

    /**
     * HDFS创建文件
     *
     * @param path 上传的路径
     * @param file
     * @throws Exception
     */
    public void createFile(String path, MultipartFile file) throws Exception {
        if (StringUtils.isEmpty(path) || null == file.getBytes()) {
            return;
        }
        FileSystem fs = null;
        FSDataOutputStream outputStream = null;
        try {
            fs = getFileSystem();
            String fileName = file.getOriginalFilename();
            // 上传时默认当前目录，后面自动拼接文件的目录
            Path newPath = new Path(path + "/" + fileName);
            // 打开一个输出流
            outputStream = fs.create(newPath);
            outputStream.write(file.getBytes());
            outputStream.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }

            if (fs != null) {
                fs.close();
            }
        }
    }


    /**
     * 直接往输出流输出文件
     *
     * @param path 活动方式 远程文件
     * @param os 输出流
     * @return
     * @throws Exception
     */
    public void writeOutputStreamFile(OutputStream os, String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return;
        }
/*        if (!existFile(path)) {
            // 文件不存在则抛出异常
            throw new Exception(path + " hdfs文件不存在");
        }*/
        FileSystem fs = null;
        FSDataInputStream inputStream = null;
        try {
            // 目标路径
            Path srcPath = new Path(path);
            fs = getFileSystem();
            inputStream = fs.open(srcPath);
            // 防止中文乱码
            // BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            fileDownload(os, new BufferedInputStream(inputStream));
        } catch (Exception e) {
            throw e;
        } finally {
            if(inputStream != null) {
                inputStream.close();
            }
            if(fs != null) {
                fs.close();
            }
        }
    }

    /**
     * 读取HDFS文件内容
     *
     * @param path
     * @return
     * @throws Exception
     */
    public String readFile(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!existFile(path)) {
            return null;
        }
        FileSystem fs = null;
        FSDataInputStream inputStream = null;
        try {
            // 目标路径
            Path srcPath = new Path(path);
            fs = getFileSystem();
            inputStream = fs.open(srcPath);
            // 防止中文乱码
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String lineTxt = "";
            StringBuffer sb = new StringBuffer();
            while ((lineTxt = reader.readLine()) != null) {
                sb.append(lineTxt);
            }
            return sb.toString();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (fs != null) {
                fs.close();
            }
        }
    }


    /**
     * 读取HDFS文件列表
     *
     * @param path
     * @return
     * @throws Exception
     */
    public List<Map<String, String>> listFile(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!existFile(path)) {
            return null;
        }

        FileSystem fs = null;
        try {
            fs = getFileSystem();
            // 目标路径
            Path srcPath = new Path(path);
            // 递归找到所有文件
            RemoteIterator<LocatedFileStatus> filesList = fs.listFiles(srcPath, true);
            List<Map<String, String>> returnList = new ArrayList<>();
            while (filesList.hasNext()) {
                LocatedFileStatus next = filesList.next();
                String fileName = next.getPath().getName();
                Path filePath = next.getPath();
                Map<String, String> map = new HashMap<>();
                map.put("fileName", fileName);
                map.put("filePath", filePath.toString());
                returnList.add(map);
            }
            return returnList;
        } catch (Exception e) {
            logger.error("hdfs listFile {}", e);
        } finally {
            if (fs != null) {
                fs.close();

            }
        }
        return null;
    }


    /**
     * HDFS重命名文件
     *
     * @param oldName
     * @param newName
     * @return
     * @throws Exception
     */
    public boolean renameFile(String oldName, String newName) throws Exception {
        if (StringUtils.isEmpty(oldName) || StringUtils.isEmpty(newName)) {
            return false;
        }
        FileSystem fs = null;
        Boolean isOk = false;
        try {
            fs = getFileSystem();
            // 原文件目标路径
            Path oldPath = new Path(oldName);
            // 重命名目标路径
            Path newPath = new Path(newName);
            isOk = fs.rename(oldPath, newPath);

            return isOk;
        } catch (Exception e) {
            logger.error("hdfs renameFile {}", e);
        } finally {
            if (fs != null) {
                fs.close();
            }
        }
        return isOk;
    }


    /**
     * 删除HDFS文件
     *
     * @param path
     * @return
     * @throws Exception
     */
    public boolean deleteFile(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return false;
        }

        FileSystem fs = null;
        Boolean isOk = false;
        try {
            if (!existFile(path)) {
                return false;
            }
            fs = getFileSystem();
            Path srcPath = new Path(path);
            isOk = fs.deleteOnExit(srcPath);
        } catch (Exception e) {
            logger.error("hdfs deleteFile {}", e);
        } finally {
            if (fs != null) {
                fs.close();
            }
        }
        return isOk;
    }

    /**
     * 上传HDFS文件
     *
     * @param path 上传路径(本服务器文件全路径)
     * @param uploadPath 目标路径(全节点路径)
     * @throws Exception
     */
    public void uploadFile(String path, String uploadPath) throws Exception {
        if (StringUtils.isEmpty(path) || StringUtils.isEmpty(uploadPath)) {
            return;
        }
        FileSystem fs = null;
        try {
            fs = getFileSystem();
            // 上传路径
            Path clientPath = new Path(path);
            // 目标路径
            Path serverPath = new Path(uploadPath);
            // 调用文件系统的文件复制方法，第一个参数是否删除原文件true为删除，默认为false
            fs.copyFromLocalFile(false, clientPath, serverPath);
        } catch (Exception e) {
            logger.error("hdfs uploadFile {}", e);
        } finally {
            if (fs != null) {
                fs.close();
            }
        }

    }


    /**
     * 下载HDFS文件
     *
     * @param path hdfs目标路径
     * @param downloadPath 客户端存放路径
     * @throws Exception
     */
    public void downloadFile(String path, String downloadPath) throws Exception {
        if (StringUtils.isEmpty(path) || StringUtils.isEmpty(downloadPath)) {
            return;
        }
        FileSystem fs = null;
        try {
            fs = getFileSystem();
            // hdfs目标路径
            Path clientPath = new Path(path);
            // 客户端存放路径
            Path serverPath = new Path(downloadPath);
            // 调用文件系统的文件复制方法，第一个参数是否删除原文件true为删除，默认为false
            fs.copyToLocalFile(false, clientPath, serverPath);
        } catch (Exception e) {
            logger.error("hdfs downloadFile {}", e);
        } finally {
            if (fs != null) {
                fs.close();
            }
        }
    }

    /**
     * HDFS文件复制
     * @param sourcePath
     * @param targetPath
     * @throws Exception
     */
    /*public void copyFile(String sourcePath, String targetPath) throws Exception {
        if (StringUtils.isEmpty(sourcePath) || StringUtils.isEmpty(targetPath)) {
            return;
        }
        FileSystem fs = getFileSystem();
        // 原始文件路径
        Path oldPath = new Path(sourcePath);
        // 目标路径
        Path newPath = new Path(targetPath);

        FSDataInputStream inputStream = null;
        FSDataOutputStream outputStream = null;
        try {
            inputStream = fs.open(oldPath);
            outputStream = fs.create(newPath);

            IOUtils.copyBytes(inputStream, outputStream, bufferSize, false);
        } finally {
            inputStream.close();
            outputStream.close();
            fs.close();
        }
    }

    *//**
     * 打开HDFS上的文件并返回byte数组
     * @param path
     * @return
     * @throws Exception
     *//*
    public byte[] openFileToBytes(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!existFile(path)) {
            return null;
        }
        FileSystem fs = getFileSystem();
        // 目标路径
        Path srcPath = new Path(path);
        try {
            FSDataInputStream inputStream = fs.open(srcPath);
            return IOUtils.readFullyToByteArray(inputStream);
        } finally {
            fs.close();
        }
    }

    *//**
     * 打开HDFS上的文件并返回java对象
     * @param path
     * @return
     * @throws Exception
     *//*
    public <T extends Object> T openFileToObject(String path, Class<T> clazz) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!existFile(path)) {
            return null;
        }
        String jsonStr = readFile(path);
        return JsonUtil.fromObject(jsonStr, clazz);
    }

    *//**
     * 获取某个文件在HDFS的集群位置
     * @param path
     * @return
     * @throws Exception
     *//*
    public BlockLocation[] getFileBlockLocations(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!existFile(path)) {
            return null;
        }
        FileSystem fs = getFileSystem();
        // 目标路径
        Path srcPath = new Path(path);
        FileStatus fileStatus = fs.getFileStatus(srcPath);
        return fs.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
    }
*/

    /**
     * @param os  response输出流
     * @param bis 输入流
     */
    private void fileDownload(OutputStream os, BufferedInputStream bis) throws Exception {
        if (bis == null) {
            return;
        }
        try {
            byte[] buff = new byte[1024];
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, i);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}


