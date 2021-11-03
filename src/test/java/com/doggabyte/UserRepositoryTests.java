package com.doggabyte;

import static org.assertj.core.api.Assertions.assertThat;

import com.doggabyte.model.Role;
import com.doggabyte.repository.UserRepository;
import com.doggabyte.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void testCreateUser() {
        User user = new User();
        user.setEmail("test2@gmail.com");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode("666666"));
        user.setName("test2");
        user.setAccess("admin");
        user.setAddress("beijing");
        user.setCountry("China");
        user.setPhone("010-88888888");
        user.setTitle("manager");
        user.setAvatar("https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png");
        User savedUser = repo.save(user);
        User exsitUser = entityManager.find(User.class, savedUser.getUserid());

        assertThat(exsitUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void testFindUserByEmail() {
        String email = "leo@gmail.com";

        User user = repo.findByEmail(email);

        assertThat(user).isNotNull();
    }

    @Test
    public void testCreateRole() {
        Role role = new Role();
    }


}
