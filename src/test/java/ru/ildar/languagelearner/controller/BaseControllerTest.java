package ru.ildar.languagelearner.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.ildar.languagelearner.service.AppUserService;

/** Base class for all controller test classes of this application */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
public abstract class BaseControllerTest
{
    protected MockMvc mockMvc;

    @Autowired
    protected AppUserService appUserServiceMock;
    @Autowired
    protected UserDetailsService userDetailsServiceMock;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Before
    public void setUp()
    {
        Mockito.reset(appUserServiceMock, userDetailsServiceMock);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
}
