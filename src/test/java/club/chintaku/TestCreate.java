package club.chintaku;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.junit.Test;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.HashSet;

/**
 * @description:
 * @author: chenzhuo
 * @create: 2021-04-04 21:11
 */
public class TestCreate {

    /**
     * 使用默认方式创建mysql表
     */

    @Test
    public void testCreateDbTable() {

        // 1.默认方式，创建activiti所需表
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        RepositoryService repositoryService = processEngine.getRepositoryService();
//        repositoryService.createDeployment();
//
//        // 2.从指定resource获取配置文件
//        ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConf");


    }
}
