package org.geektimes.projects.user.domain;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import org.geektimes.projects.user.validator.bean.validation.PhoneValid;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import static javax.persistence.GenerationType.AUTO;

/**
 * 用户领域对象
 *
 * @since 1.0
 */
@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name = "User.findByNameAndPassword",
    query = "SELECT u FROM User u where u.name=:name AND u.password=:password"),
    @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u where u.name=:name")
})
public class User implements Serializable {

    @Id
    @Min(value = 1, message = "id>0")
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    @NotBlank(message = "密码长度须6-32 位")
    @Length(min = 6, max = 32, message = "密码长度须6-32 位")
    private String password;

    @Column
    private String email;

    @Column
    @PhoneValid(message = "电话号码: 采用中国大陆方式（11 位校验）")
    private String phoneNumber;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(phoneNumber, user.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, email, phoneNumber);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
