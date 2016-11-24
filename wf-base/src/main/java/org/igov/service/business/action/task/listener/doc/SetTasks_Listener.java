package org.igov.service.business.action.task.listener.doc;

import java.io.InputStream;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.Attachment;
import static org.igov.service.business.action.task.core.AbstractModelTask.getStringFromFieldExpression;
import org.igov.service.business.dfs.DfsService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Kovilin
 */
@Component("SetTasks")
public class SetTasks_Listener implements TaskListener {
    
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(SetTasks_Listener.class);
     
    private Expression sTaskProcessDefinition;
            
    private Expression sID_Attachment;
            
    private Expression sContent;
            
    private Expression sAutorResolution;
    
    private Expression sTextResolution;
    
    private Expression sDateExecution;
     
    @Autowired
    private TaskService taskService;

    @Override
    public void notify(DelegateTask delegateTask) {
        try{
            String sTaskProcessDefinition_Value = 
                getStringFromFieldExpression(this.sTaskProcessDefinition, delegateTask.getExecution());
            String sID_Attachment_Value = 
                getStringFromFieldExpression(this.sID_Attachment, delegateTask.getExecution());
            String sContent_Value = 
                getStringFromFieldExpression(this.sContent, delegateTask.getExecution());
            String sAutorResolution_Value = 
                getStringFromFieldExpression(this.sAutorResolution, delegateTask.getExecution());
            String sTextResolution_Value = 
                getStringFromFieldExpression(this.sTextResolution, delegateTask.getExecution());
            String sDateExecution_Value = 
                getStringFromFieldExpression(this.sDateExecution, delegateTask.getExecution());
 
            LOG.info("SetTasks listener data: sTaskProcessDefinition_Value: " 
                + sTaskProcessDefinition_Value + " sID_Attachment_Value: " + sID_Attachment_Value + " sContent: " +
                sContent_Value + " sAutorResolution: " + sAutorResolution_Value + " sTextResolution: " 
                + sTextResolution_Value + " sDateExecution: " + sDateExecution_Value ); 
 
            InputStream json_Content = taskService.getAttachmentContent(sID_Attachment_Value);
            LOG.info((json_Content != null) ? "JSON_TASKLST:" + json_Content.toString():"JSON_TASKLST null pointer error");
            
            Attachment json_Attachment = taskService.getAttachment(sID_Attachment_Value);
            LOG.info((json_Attachment != null) ? "JSON_ATTACHMENT:" + json_Attachment.toString():"JSON_ATTACHMENT null pointer error");
            
            //LOG.info("json_Content sTaskProcessDefinition_Value: " + taskService.getAttachmentContent(sTaskProcessDefinition_Value));
            //LOG.info("json_Content sBodyDocument_Value: " + taskService.getAttachmentContent(sBodyDocument_Value));
            //LOG.info("json_Content sLoginAuthor_Value: " + taskService.getAttachmentContent(sLoginAuthor_Value));
        }
         catch (Exception ex){
             LOG.error("SetTasks listener throws an error: " + ex.toString());
        }
    }
}