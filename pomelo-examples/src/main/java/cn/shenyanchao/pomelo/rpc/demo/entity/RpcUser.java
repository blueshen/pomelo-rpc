package cn.shenyanchao.pomelo.rpc.demo.entity;

import java.io.Serializable;

/**
 * @author shenyanchao
 */
public class RpcUser implements Serializable {

    private static final long serialVersionUID = -467668234191746603L;

    private String name;

    private String age;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the age
     */
    public String getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RpcUser{");
        sb.append("name='").append(name).append('\'');
        sb.append(", age='").append(age).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
