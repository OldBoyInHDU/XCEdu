package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author hh
 * @create 2020/2/11
 * @description
 **/
public interface FileSystemRepository extends MongoRepository<FileSystem, String> {

}
