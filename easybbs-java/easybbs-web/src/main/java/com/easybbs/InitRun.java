package com.easybbs;

import com.easybbs.entity.config.AppConfig;
import com.easybbs.entity.constants.Constants;
import com.easybbs.service.SysSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

@Component
public class InitRun implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(InitRun.class);

    @Resource
    private AppConfig appConfig;

    @Resource
    private SysSettingService sysSettingService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        File projectFile = new File(appConfig.getProjectFolder());
        if (!projectFile.exists()) {
            logger.info("配置文件中项目路径" + appConfig.getProjectFolder() + "不存在");
            return;
        }
        //创建好头像目录，方便防止默认头像
        String avatarFolderName = Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_AVATAR_NAME;

        File avatarFolder = new File(appConfig.getProjectFolder() + avatarFolderName);
        if (!avatarFolder.exists()) {
            avatarFolder.mkdirs();
        }

        File avatarFile = new File(appConfig.getProjectFolder() + avatarFolderName + Constants.AVATAR_DEFUALT);
        if (!avatarFile.exists()) {
            logger.info("文件目录下默认图片未设置,项目中默认头像将无法显示，请放置图片" + avatarFile.getAbsolutePath());
        }

        //刷新系统设置缓存
        // 当数据被频繁访问，而从原始数据源（如数据库）检索这些数据的成本很高时，使用缓存可以显著提高应用程序的响应时间
        // 可以存在redis这种分布式缓存系统，内存缓存， 或者web也可以存一些静态资源
        sysSettingService.refreshCache();
        logger.info("服务启动成功，开始愉快的开发吧");
    }

}
