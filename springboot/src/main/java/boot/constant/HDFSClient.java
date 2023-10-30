package boot.constant;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class HDFSClient {

    private FileSystem fileSystem;

    @Before
    public void init() throws URISyntaxException, IOException, InterruptedException {

        //连接的集群nn地址
        URI uri = new URI("hdfs://hadoop102:8020");
        //创建一个配置文件
        Configuration configuration = new Configuration();
        //创建用户
        String user = "atguigu";
        //获取到了客户端对象
        fileSystem = FileSystem.get(uri,configuration,user);
    }

    @After
    public void close() throws IOException {
        //关闭连接
        fileSystem.close();
    }

    //创建目录
    @Test
    public void mkdirtest() throws IOException {
        fileSystem.mkdirs(new Path("/xiyou/huaguoshan"));
    }

    //上传
    @Test
    public void put() throws IOException {
        fileSystem.copyFromLocalFile(false,false,new Path("D:\\Java知识体系——孙悟空.txt"),new Path("/xiyou/huaguoshan"));
    }
    //参数优先级排序：（1）客户端代码中设置的值 >（2）ClassPath 下的用户自定义配置文件 >（3）然后是服务器的自定义配置（xxx-site.xml）>（4）服务器的默认配置（xxx-default.xml）

    //下载
    @Test
    public void get() throws IOException {
        fileSystem.copyToLocalFile(false,new Path("/xiyou/huaguoshan"),new Path("C:\\Users\\FengCJ\\Desktop\\工作资料\\Java知识体系——孙悟空.txt"),true);
    }

    //删除
    @Test
    public void delete() throws IOException {
        fileSystem.delete(new Path("/xiyou/huaguoshan"),true);
    }

    //移动和更名
    @Test
    public void remove() throws IOException {
        //文件名称的修改
        fileSystem.rename(new Path("/xiyou/huaguoshan/sunwukong.txt"), new
                Path("/xiyou/huaguoshan/meihouwang.txt"));
        //文件的移动和更名
        fileSystem.rename(new Path("/input/ss.txt"),new Path("/cls.txt"));
        //目录更名
        fileSystem.rename(new Path("/input"),new Path("/output"));
    }

    //获取文件详细信息
    @Test
    public void FileDetail() throws IOException {
        // 2 获取文件详情
        RemoteIterator<LocatedFileStatus> listFiles = fileSystem.listFiles(new Path("/"),
                true);
        while (listFiles.hasNext()) {
            LocatedFileStatus fileStatus = listFiles.next();
            System.out.println("========" + fileStatus.getPath() + "=========");
            System.out.println(fileStatus.getPermission());
            System.out.println(fileStatus.getOwner());
            System.out.println(fileStatus.getGroup());
            System.out.println(fileStatus.getLen());
            System.out.println(fileStatus.getModificationTime());
            System.out.println(fileStatus.getReplication());
            System.out.println(fileStatus.getBlockSize());
            System.out.println(fileStatus.getPath().getName());
        // 获取块信息
            BlockLocation[] blockLocations = fileStatus.getBlockLocations();
            System.out.println(Arrays.toString(blockLocations));
        }
    }

    //判断是文件夹还是文件
    @Test
    public void testFile() throws IOException {
        FileStatus[] listStatus = fileSystem.listStatus(new Path("/"));
        for (FileStatus fileStatus : listStatus) {
            // 如果是文件
            if (fileStatus.isFile()) {
                System.out.println("文件:"+fileStatus.getPath().getName());
            }else {
                System.out.println("路径:"+fileStatus.getPath().getName());
            }
        }
    }
}
