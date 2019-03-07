package cn.shenyanchao.pomelo.rpc.spring.config.http;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import cn.shenyanchao.pomelo.rpc.support.PomeloRpcHttpService;

/**
 * @author shenyanchao
 */
public class PomeloHttpServiceParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        String projectName = element.getAttribute("projectName");
        String ref = element.getAttribute("ref");
        String interceptorRef = element.getAttribute("interceptorRef");
        String httpType = element.getAttribute("httpType");
        String returnType = element.getAttribute("returnType");

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(PomeloRpcHttpService.class);
        beanDefinition.setLazyInit(false);
        beanDefinition.getPropertyValues().addPropertyValue("projectName", projectName);
        beanDefinition.getPropertyValues().addPropertyValue("ref", ref);
        beanDefinition.getPropertyValues().addPropertyValue("interceptorRef", interceptorRef);
        beanDefinition.getPropertyValues().addPropertyValue("httpType", httpType);
        beanDefinition.getPropertyValues().addPropertyValue("returnType", returnType);

        parserContext.getRegistry().registerBeanDefinition(projectName, beanDefinition);
        return beanDefinition;
    }

}
