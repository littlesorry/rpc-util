package com.littlesorry.rpc.util.retry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by takara on 2/29/16.
 */
public class RetryAspectTest {

    ApplicationContext ctx;

    AspectTestBean aspectTestBean;

    IntGenerator generator;

    @Before
    public void setup() {
        initMocks(this);

        ctx = new ClassPathXmlApplicationContext(
                new String[]{"applicationContext_test.xml"});
        generator = (IntGenerator) ctx.getBean("intGenerator");
        aspectTestBean = (AspectTestBean) ctx.getBean("aspectTestBean");
    }

    @Test
    public void testAroundRetry() throws Exception {
        doReturn(1).when(generator).next();

        assertEquals(1, aspectTestBean.test().intValue());

        verify(generator, times(1)).next();
        verifyNoMoreInteractions(generator);
    }

    @Test
    public void testAroundRetry_1retry() throws Exception {
        doAnswer(new Answer() {
            int i = 0;

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                if (i++ == 0) throw new RuntimeException("1 time fail");
                return 1;
            }
        }).when(generator).next();

        assertEquals(1, aspectTestBean.test().intValue());

        verify(generator, times(2)).next();
        verifyNoMoreInteractions(generator);
    }

    @Test
    public void testAroundRetry_2retry() throws Exception {
        doAnswer(new Answer() {
            int i = 0;

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                if (i++ < 2) throw new RuntimeException("2 time fail");
                return 1;
            }
        }).when(generator).next();

        assertEquals(1, aspectTestBean.test().intValue());

        verify(generator, times(3)).next();
        verifyNoMoreInteractions(generator);
    }

    @Test
    public void testAroundRetry_3retry_fail() throws Exception {
        doThrow(new RuntimeException("3 time fail")).when(generator).next();

        try {
            aspectTestBean.test();
            Assert.fail();
        } catch (RuntimeException e) {
            assertEquals("3 time fail", e.getMessage());
            verify(generator, times(3)).next();
            verifyNoMoreInteractions(generator);
        }
    }
}