package com.xuecheng.test.fastdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author hh
 * @create 2020/2/8
 * @description
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFastDFS {
    //上传测试
    @Test
    public void testUpload() {
        //加载fasddfs-client.properties配置文件
        try {
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            //定义TrackClient，用于请求TrackerServer
            TrackerClient trackerClient = new TrackerClient();
            //连接tracker
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取storage
            StorageServer storeStorageServer = trackerClient.getStoreStorage(trackerServer);
            //创建storageClient 通过tracker server 和 storage server 获得storageClient
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storeStorageServer);


            //向storage服务器上传文件
            //本地文件的路径
            String filePath = "D:/prac_project/fdfs_test_upload.jpg";
            //上传成功后拿到文件id
            String fileId = storageClient1.upload_file1(filePath, "png", null);
            System.out.println(fileId);//group1/M00/00/00/wKgAZ14_pb6ASZHeAAKBN9Um430128.png


        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

    }


    //下载测试
    public void testDownload() {

    }

}
