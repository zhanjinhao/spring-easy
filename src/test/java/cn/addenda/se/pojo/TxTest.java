package cn.addenda.se.pojo;

/**
 * @Author ISJINHAO
 * @Date 2022/2/26 15:06
 */
public class TxTest {

    private Long id;

    private String name;

    private String remark;

    public TxTest() {
    }

    public TxTest(String name, String remark) {
        this.name = name;
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "TxTest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
