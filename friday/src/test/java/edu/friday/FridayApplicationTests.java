package edu.friday;

import edu.friday.model.SysUser;
import edu.friday.repository.SysUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FridayApplicationTests {
    @Autowired
    SysUserRepository userRepository;
    @Test
    void contextLoads() {
    }
    @Test
    void testSaveUser(){
        SysUser user = new SysUser();
        user.setUserName("Alex22");
        user.setNickName("alex test");
        userRepository.save(user);
    }
    @Test
    void testUpdateUser(){
        userRepository.findById(143L).ifPresent(user -> {
            user.setUserName("Al");
            user.setNickName("alex test");
            userRepository.save(user);
        });
    }
    @Test
    void testFindUsers(){
        System.out.println(userRepository.findAll());
    }
    @Test
    void testDeleteUser(){
        userRepository.deleteById(138L);
    }
}
