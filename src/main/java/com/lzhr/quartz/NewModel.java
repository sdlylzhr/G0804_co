package com.lzhr.quartz;

/**
 * Created by lizhongren1 on 2017/12/20.
 */
public class NewModel {

    private String name;

    private Integer id;

    // 22222

    public void read(){

        System.out.println("读书");

        System.out.println("head");
    }

    //23333333333
    @Override
    public String toString() {
        return "NewModel{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
