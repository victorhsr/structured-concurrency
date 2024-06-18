package com.github.victorhsr.structuredconcurrency.service;

import com.github.victorhsr.structuredconcurrency.context.PageContext;
import com.github.victorhsr.structuredconcurrency.context.orchestration.PageContextException;
import com.github.victorhsr.structuredconcurrency.context.orchestration.PageContextScope;
import com.github.victorhsr.structuredconcurrency.infrastructure.HeroClient;
import com.github.victorhsr.structuredconcurrency.infrastructure.ProductDetailsClient;
import com.github.victorhsr.structuredconcurrency.infrastructure.ReviewsClient;
import com.github.victorhsr.structuredconcurrency.service.PageContextService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PageContextServiceTest {

    @Mock
    private HeroClient heroClient;
    @Mock
    private ProductDetailsClient productDetailsClient;
    @Mock
    private ReviewsClient reviewsClient;

    @InjectMocks
    private PageContextService pageContextService;

    @Nested
    class HappyPathTests {

        @Test
        void shouldProduceTheContext(@Mock PageContext expectedPageContext) {
            try (var mockedConstruction = mockConstruction(PageContextScope.class, (mock, context) -> {
                // given
                when(mock.getContext()).thenReturn(expectedPageContext);
            })) {
                // when
                PageContext actual = pageContextService.getPageContext("123");

                // then
                assertEquals(expectedPageContext, actual);
                verify(mockedConstruction.constructed().get(0), times(4)).fork(any(Callable.class));
            }
        }
    }

    @Nested
    class ExceptionFlows {

        @Test
        void shouldReturnNullWhenThereIsAnInterruptedException() {
            try (var mockedConstruction = mockConstruction(PageContextScope.class, (mock, context) -> {
                // given
                when(mock.join()).thenThrow(new InterruptedException());
            })) {
                // when + then
                PageContext actual = pageContextService.getPageContext("123");

                // then
                assertNull(actual);
            }
        }

        @Test
        void shouldReturnNullWhenThereIsAPageContextException() {
            try (var mockedConstruction = mockConstruction(PageContextScope.class, (mock, context) -> {
                // given
                when(mock.getContext()).thenThrow(new PageContextException(""));
            })) {
                // when + then
                PageContext actual = pageContextService.getPageContext("123");

                // then
                assertNull(actual);
            }
        }
    }

}