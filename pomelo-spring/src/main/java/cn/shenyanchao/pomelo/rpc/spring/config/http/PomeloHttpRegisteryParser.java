package cn.shenyanchao.pomelo.rpc.spring.config.http;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import cn.shenyanchao.pomelo.rpc.support.PomeloRpcHttpRegistery;

/**
 * @author shenyanchao
 */
public class PomeloHttpRegisteryParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        String id = element.getAttribute("id");
        int port = Integer.parseInt(element.getAttribute("port"));
        int timeout = Integer.parseInt(element.getAttribute("timeout"));

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(PomeloRpcHttpRegistery.class);
        beanDefinition.getPropertyValues().addPropertyValue("port", port);
        beanDefinition.getPropertyValues().addPropertyValue("timeout", timeout);
        parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);

        return beanDefinition;
    }

}
