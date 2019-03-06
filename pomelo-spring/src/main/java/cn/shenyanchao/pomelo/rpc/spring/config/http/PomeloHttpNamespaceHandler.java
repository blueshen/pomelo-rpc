package cn.shenyanchao.pomelo.rpc.spring.config.http;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author shenyanchao
 */
public class PomeloHttpNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {

        registerBeanDefinitionParser("service", new PomeloHttpServiceParser());
        registerBeanDefinitionParser("registry", new PomeloHttpRegisteryParser());
    }

}
