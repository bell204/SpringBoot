package org.zerock.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.zerock.service.MessageService;


@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {

	@InjectMocks
	MessageService service;
	
	@Mock
	MessageSource mockMessageSource;
	
	@Test
	public void testGetMessageByCode() {
		doReturn("Hello!!").when(mockMessageSource)
		.getMessage("greeting", null, Locale.getDefault());
		
		// 테스트를 한다.
		String actualMessage = service.getMessageByCode("greeting");
		assertThat(actualMessage, is("Hello!!"));
		
	}
}
