package cc.corly.springboot.demo.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Generator
 * @since 2018-03-11
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "test_id", type = IdType.AUTO)
    private Long testId;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 名称
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 测试下划线字段命名类型
     */
    private Integer testType;
    /**
     * 日期
     */
    private Date testDate;
    /**
     * 测试
     */
    private Long role;
    /**
     * 手机号码
     */
    private String phone;


    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getTestType() {
        return testType;
    }

    public void setTestType(Integer testType) {
        this.testType = testType;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public Long getRole() {
        return role;
    }

    public void setRole(Long role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
        ", testId=" + testId +
        ", tenantId=" + tenantId +
        ", name=" + name +
        ", age=" + age +
        ", testType=" + testType +
        ", testDate=" + testDate +
        ", role=" + role +
        ", phone=" + phone +
        "}";
    }
}
