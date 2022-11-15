package org.kexing.management.domin.service;

import com.yunmo.boot.web.upload.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/** @author lh */
@Service
public class FileStroageService {

  @Autowired FileStorageProperties fileStorageProperties;

  public String getLocation(String fileName) {
    return fileStorageProperties.getLocation()
        + File.separator
        + URLDecoder.decode(fileName, StandardCharsets.UTF_8);
  }
}
