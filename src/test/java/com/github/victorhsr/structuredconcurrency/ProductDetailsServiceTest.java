package com.github.victorhsr.structuredconcurrency;

import com.github.victorhsr.structuredconcurrency.context.ProductDetailsContext;
import com.github.victorhsr.structuredconcurrency.context.orchestration.ProductDetailsContextScope;
import com.github.victorhsr.structuredconcurrency.context.orchestration.ProductDetailsException;
import com.github.victorhsr.structuredconcurrency.infrastructure.HeroClient;
import com.github.victorhsr.structuredconcurrency.infrastructure.ProductDetailsClient;
import com.github.victorhsr.structuredconcurrency.infrastructure.ReviewsClient;
import lombok.SneakyThrows;
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
class ProductDetailsServiceTest {

    @Mock
    private HeroClient heroClient;
    @Mock
    private ProductDetailsClient productDetailsClient;
    @Mock
    private ReviewsClient reviewsClient;

    @InjectMocks
    private ProductDetailsService productDetailsService;

    @Nested
    class HappyPathTests {

        @Test
        @SneakyThrows
        void shouldProduceTheContext(@Mock ProductDetailsContext expectedProductDetailsContext) {
            try (var mockedConstruction = mockConstruction(ProductDetailsContextScope.class, (mock, context) -> {
                // given
                when(mock.getContext()).thenReturn(expectedProductDetailsContext);
            })) {
                // when
                ProductDetailsContext actual = productDetailsService.getProductDetails("123");

                // then
                assertEquals(expectedProductDetailsContext, actual);
                verify(mockedConstruction.constructed().get(0), times(3)).fork(any(Callable.class));
            }
        }
    }

    @Nested
    class ExceptionFlows {

        @Test
        @SneakyThrows
        void shouldReturnNullWhenThereIsAnInterruptedException() {
            try (var mockedConstruction = mockConstruction(ProductDetailsContextScope.class, (mock, context) -> {
                // given
                when(mock.join()).thenThrow(new InterruptedException());
            })) {
                // when + then
                ProductDetailsContext actual = productDetailsService.getProductDetails("123");

                // then
                assertNull(actual);
            }
        }

        @Test
        @SneakyThrows
        void shouldReturnNullWhenThereIsAProductDetailsException() {
            try (var mockedConstruction = mockConstruction(ProductDetailsContextScope.class, (mock, context) -> {
                // given
                when(mock.getContext()).thenThrow(new ProductDetailsException(""));
            })) {
                // when + then
                ProductDetailsContext actual = productDetailsService.getProductDetails("123");

                // then
                assertNull(actual);
            }
        }
    }

}