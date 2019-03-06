package cn.shenyanchao.pomelo.rpc.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author shenyanchao
 */
public class HttpServiceProvider {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("application-http.xml");
    }
}
