package cn.shenyanchao.pomelo.rpc.spring.tcp;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import cn.shenyanchao.pomelo.rpc.serialize.PomeloSerializer;
import cn.shenyanchao.pomelo.rpc.spring.tcp.support.PomeloRpcRegistry;

/**
 * @author shenyanchao
 */
public class PomeloRegistryParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        String id = element.getAttribute("id");
        String ip = element.getAttribute("ip");
        int port = Integer.parseInt(element.getAttribute("port"));
        int timeout = Integer.parseInt(element.getAttribute("timeout"));
        byte protocolType = Byte.parseByte(element.getAttribute("protocolType"));
        String serializer = element.getAttribute("serializer");
        int threadCount = Integer.parseInt(element.getAttribute("threadCount"));
        String group = element.getAttribute("group");

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(PomeloRpcRegistry.class);
        beanDefinition.getPropertyValues().addPropertyValue("ip", ip);
        beanDefinition.getPropertyValues().addPropertyValue("port", port);
        beanDefinition.getPropertyValues().addPropertyValue("timeout", timeout);
        beanDefinition.getPropertyValues().addPropertyValue("group", group);
        beanDefinition.getPropertyValues().addPropertyValue("protocolType", protocolType);
        beanDefinition.getPropertyValues().addPropertyValue("serializer", PomeloSerializer.valueOf(serializer));
        beanDefinition.getPropertyValues().addPropertyValue("threadCount", threadCount);

        parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);

        return beanDefinition;
    }

}
