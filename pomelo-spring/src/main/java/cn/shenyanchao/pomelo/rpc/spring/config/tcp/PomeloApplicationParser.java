package cn.shenyanchao.pomelo.rpc.spring.config.tcp;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import cn.shenyanchao.pomelo.rpc.support.PomeloRpcApplication;

/**
 * @author shenyanchao
 */
public class PomeloApplicationParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        String id = element.getAttribute("id");
        String address = element.getAttribute("address");
        //用于标识不同客户端,也可不配
        String clientId = element.getAttribute("clientId");
        String flag = element.getAttribute("flag");
        String timeout = element.getAttribute("timeout");

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(PomeloRpcApplication.class);
        beanDefinition.setLazyInit(false);
        beanDefinition.getPropertyValues().addPropertyValue("address", address);
        beanDefinition.getPropertyValues().addPropertyValue("clientId", clientId);
        beanDefinition.getPropertyValues().addPropertyValue("flag", Integer.parseInt(flag));
        beanDefinition.getPropertyValues().addPropertyValue("timeout", Integer.parseInt(timeout));

        parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
        return beanDefinition;
    }

}
