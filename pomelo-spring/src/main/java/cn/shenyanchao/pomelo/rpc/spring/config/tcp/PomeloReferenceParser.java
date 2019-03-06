package cn.shenyanchao.pomelo.rpc.spring.config.tcp;

import cn.shenyanchao.pomelo.rpc.support.PomeloRpcReference;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author shenyanchao
 */
public class PomeloReferenceParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        String interfaceName = element.getAttribute("interfaceName");
        String id = element.getAttribute("id");
        String group = element.getAttribute("group");
        int protocolType = Integer.parseInt(element.getAttribute("protocolType"));
        int serializerType = Integer.parseInt(element.getAttribute("serializerType"));
        int timeout = Integer.parseInt(element.getAttribute("timeout"));

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(PomeloRpcReference.class);
        beanDefinition.setLazyInit(false);

        beanDefinition.getPropertyValues().addPropertyValue("interfaceName", interfaceName);
        beanDefinition.getPropertyValues().addPropertyValue("group", group);
        beanDefinition.getPropertyValues().addPropertyValue("protocolType", protocolType);
        beanDefinition.getPropertyValues().addPropertyValue("serializerType", serializerType);
        beanDefinition.getPropertyValues().addPropertyValue("timeout", timeout);

        parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
        return beanDefinition;
    }

}
