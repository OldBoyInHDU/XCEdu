package com.xuecheng.filesystem.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.filesystem.service.IFileSystemService;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author hh
 * @create 2020/2/11
 * @description
 **/
@Service
public class FileSystemServiceImpl implements IFileSystemService {

    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    int connect_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    int network_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.charset}")
    String charset;
    @Value("${xuecheng.fastdfs.tracker_servers}")
    String tracker_servers;

    @Autowired
    private FileSystemRepository fileSystemRepository;

    //上传文件
    @Override
    public UploadFileResult upload(MultipartFile multipartFile, String filetag, String businesskey, String metadata) {
        //第一步：将文件上传到fastDFS中，得到一个文件id
       if (multipartFile == null) {
           ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
       }
        String fileId = fdfsUpload(multipartFile);
        if (StringUtils.isEmpty(fileId)) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }
        //第二部：将文件id及其它文件信息存储到mongodb中
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setFilePath(fileId);
        fileSystem.setFiletag(filetag);
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        fileSystem.setFileType(multipartFile.getContentType());

        if (StringUtils.isNotEmpty(metadata)) {
            Map map = JSON.parseObject(metadata, Map.class);
            fileSystem.setMetadata(map);
        }
        fileSystemRepository.save(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS, fileSystem);
    }

    //将文件上传到fastDFS中，得到一个文件id
    /**
     *
     * @param multipartFile 文件
     * @return fileId
     */
    private String fdfsUpload(MultipartFile multipartFile) {
        //初始化fastDFS环境
        initFdfsConfig();

        //创建trackerClient
        TrackerClient trackerClient = new TrackerClient();
        try {
            //创建连接，得到trackerServer
            TrackerServer trackerServer = trackerClient.getConnection();
            //通过trackerClient.传入trackerServer，获得storageServer
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            //创建storageClient1来上传文件
            StorageClient1 storageClient = new StorageClient1();
            //上传文件
            //得到文件字节
            byte[] bytes = multipartFile.getBytes();
            //得到文件的原始名称
            String originalFilename = multipartFile.getOriginalFilename();
            //得到扩展名
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //上传文件，获得文件id
            String fileId = storageClient.upload_file1(bytes, ext, null);
            return fileId;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return null;

    }

    //初始化fastDFS环境
    private void initFdfsConfig() {

        try {
            //初始化tracker服务地址（多个tracker中间以半角逗号分隔）
            ClientGlobal.initByTrackers(tracker_servers);
            ClientGlobal.setG_charset(charset);
            ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
            ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
            ExceptionCast.cast(FileSystemCode.FS_INITFDFSERROR);
        }
    }


}
