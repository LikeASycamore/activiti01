import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @description:
 * @author: chenzhuo
 * @create: 2021-04-08 19:39
 */
public class ActivitiDemo {


    /**
     * 用压缩包进行部署
     */
    @Test
    public void testDeployZip() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();

        // 通过资源包进行部署
        InputStream inputStream = this.getClass().getResourceAsStream("bpmn/evection.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println("流程id" + deployment.getId());
        System.out.println("流程部署的名称" + deployment.getName());
    }


    /**
     * 查询流程定义
     */
    @Test
    public void queryProcessEntity() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();

        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> definitions = query.processDefinitionKey("evection")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        for (ProcessDefinition definition : definitions) {
            System.out.println("流程定义id：" + definition.getId());
            System.out.println("流程定义名称：" + definition.getName());
            System.out.println("流程定义key：" + definition.getKey());
            System.out.println("流程定义版本：" + definition.getVersion());
            System.out.println("流程部署id：" + definition.getDeploymentId());
        }
    }

    /**
     * 删除流程部署信息
     * act_ge_bytearray
     * act_re_procdef
     * act_re_deployment
     * 如果流程没有走完，是没办法直接删除的，需要级联删除设置为true
     *
     */
    @Test
    public void deleteDeployMent() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();

        // 根据deployment表的id 级联删除
        repositoryService.deleteDeployment("2501", true);
    }


    /**
     * 下载资源文件
     * 方案1：使用activiti自带api下载资源文件
     * 方案3：
     */
    @Test
    public void getDeployment() throws IOException {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();

        // 流程定义信息
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionCategory("evection")
                .singleResult();
        String pngName = definition.getDiagramResourceName();
        String bpmnName = definition.getResourceName();

        // 部署id
        String deploymentId = definition.getDeploymentId();
        // 图片的流
        InputStream pngInput = repositoryService.getResourceAsStream(deploymentId, pngName);
        InputStream bpmnInput = repositoryService.getResourceAsStream(deploymentId, bpmnName);
        // 构造outputStream
        File pngFile = new File("G://activiti//evec.png");
        File bpmnFile = new File("G://activiti//evec.bpmn");
        FileOutputStream pngOutStream = new FileOutputStream(pngFile);
        FileOutputStream bpmnOutStream = new FileOutputStream(bpmnFile);

        // 将输入流中的数据进行输出
        IOUtils.copy(pngInput, pngOutStream);
        IOUtils.copy(bpmnInput, bpmnOutStream);

        pngOutStream.close();
        bpmnOutStream.close();

        pngInput.close();
        bpmnInput.close();
    }

    /**
     * 查询历史信息
     */
    @Test
    public void getHistoryMsg() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();

        // act_inst的实例对象
        HistoricActivityInstanceQuery instanceQuery = historyService.createHistoricActivityInstanceQuery();
        // 查询act_inst表
        instanceQuery.processInstanceId("10001");
        // 增加排序信息
        instanceQuery.orderByHistoricActivityInstanceStartTime().asc();
        List<HistoricActivityInstance> instances = instanceQuery.list();
        for (HistoricActivityInstance instance : instances) {
            System.out.println(instance.getActivityId());
            System.out.println(instance.getActivityName());
            System.out.println(instance.getProcessDefinitionId());
            System.out.println(instance.getProcessInstanceId());
        }
    }

    @Test
    public void test1() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = dateTimeFormatter.format(date);
        System.out.println(dateStr);
    }
}
