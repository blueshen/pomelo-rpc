package cn.shenyanchao.pomelo.rpc.spring.config.tcp;

import cn.shenyanchao.pomelo.rpc.support.PomeloRpcService;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author shenyanchao
 */
public class PomeloServiceParser implements BeanDefinitionParser {

    public PomeloServiceParser() {

    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        String interfaceName = element.getAttribute("interfaceName");
        String ref = element.getAttribute("ref");
        String filterRef = element.getAttribute("filterRef");

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(PomeloRpcService.class);
        beanDefinition.setLazyInit(false);
        beanDefinition.getPropertyValues().addPropertyValue("interfaceName", interfaceName);
        beanDefinition.getPropertyValues().addPropertyValue("ref", ref);
        beanDefinition.getPropertyValues().addPropertyValue("filterRef", filterRef);

        parserContext.getRegistry().registerBeanDefinition(interfaceName, beanDefinition);
        return beanDefinition;
    }

}
