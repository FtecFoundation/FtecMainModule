package com.ftec.services;

import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.services.interfaces.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    private UserDAO userDAO;
    private UserService userService;

    @Before
    public void init() {
        userService = mock(UserService.class);
        userDAO = mock(UserDAO.class);
    }

    private static User mockedUser() {
        User mockedUser = new User();
        mockedUser.setUsername("testUsername");
        mockedUser.setPassword("1234");
        mockedUser.setEmail("test@mail.com");
        return mockedUser;
    }

    @Test
    public void getById() throws Exception {
        User user = new User();
        user.setUsername("testUsername");
        when(userService.getById(anyLong())).thenReturn(user);

        User byId = userService.getById(5);
        assertEquals("testUsername", byId.getUsername());

        verify(userService).getById(5);
    }
}
