package demo.sicau.datamanagementplatform.config.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过调用context.setSessionCreationEnable(false)表示不创建回话；如果之后调用
 * Subject.getSession()将抛出DisabledSessionException异常
 */
public class StatelessDefaultSubjectFactory extends DefaultWebSubjectFactory {

    private static final Logger logger = LoggerFactory.getLogger(StatelessDefaultSubjectFactory.class);

    @Override
    public Subject createSubject(SubjectContext context){
        //不创建session
        context.setSessionCreationEnabled(false);
        return super.createSubject(context);
    }
}
