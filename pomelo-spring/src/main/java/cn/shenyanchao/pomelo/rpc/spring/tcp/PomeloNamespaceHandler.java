package cn.shenyanchao.pomelo.rpc.spring.tcp;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author shenyanchao
 */
public class PomeloNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {

        registerBeanDefinitionParser("reference", new PomeloReferenceParser());
        registerBeanDefinitionParser("service", new PomeloServiceParser());
        registerBeanDefinitionParser("registry", new PomeloRegistryParser());
        registerBeanDefinitionParser("application", new PomeloApplicationParser());
    }

}
