package com.andlvovsky.periodicals.service;

import com.andlvovsky.periodicals.model.user.User;
import com.andlvovsky.periodicals.repository.UserRepository;
import com.andlvovsky.periodicals.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WithMockUser(username = "u")
public class UserServiceTests {

    private UserServiceImpl service;

    @Mock
    private UserRepository repository;

    private User user = new User(1L, "u", "p", null, null);

    @Before
    public void beforeEach() {
        service = new UserServiceImpl(repository);
        when(repository.findByName("u")).thenReturn(Optional.ofNullable(user));
    }

    @Test
    public void getsLoggedInUser() {
        User user = service.getLoggedUser();
        assertEquals(user, this.user);
    }

    @Test(expected = IllegalStateException.class)
    @WithAnonymousUser
    public void getLoggedInUserFails() {
        service.getLoggedUser();
    }

}
