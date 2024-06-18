package com.github.victorhsr.structuredconcurrency.service;

import com.github.victorhsr.structuredconcurrency.context.recommendations.Recommendations;
import com.github.victorhsr.structuredconcurrency.infrastructure.recommendations.RecommendationsClientA;
import com.github.victorhsr.structuredconcurrency.infrastructure.recommendations.RecommendationsClientB;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationsServiceTest {

    @Mock
    private RecommendationsClientA recommendationsClientA;
    @Mock
    private RecommendationsClientB recommendationsClientB;
    @InjectMocks
    private RecommendationsService recommendationsService;

    @Nested
    class HappyPathTests {

        @Test
        void shouldGetRecommendationsFromClientAWhenItsFaster(@Mock Recommendations recommendationsA, @Mock Recommendations recommendationsB) {
            // given
            final var productId = "123";
            when(recommendationsClientA.getRecommendations(productId)).thenReturn(recommendationsA);
            when(recommendationsClientB.getRecommendations(productId)).then(_inv -> {
                Thread.sleep(100);
                return recommendationsB;
            });

            // when
            Recommendations actual = recommendationsService.getRecommendations(productId);

            // then
            assertEquals(recommendationsA, actual);
            verify(recommendationsClientB).getRecommendations(eq(productId));
        }

        @Test
        void shouldGetRecommendationsFromClientBWhenItsFaster(@Mock Recommendations recommendationsA, @Mock Recommendations recommendationsB) {
            // given
            final var productId = "123";
            when(recommendationsClientA.getRecommendations(productId)).then(_inv -> {
                Thread.sleep(100);
                return recommendationsA;
            });
            when(recommendationsClientB.getRecommendations(productId)).thenReturn(recommendationsB);

            // when
            Recommendations actual = recommendationsService.getRecommendations(productId);

            // then
            assertEquals(recommendationsB, actual);
            verify(recommendationsClientA).getRecommendations(eq(productId));
        }

        @Test
        void shouldGetRecommendationsFromClientAWhenClientBFails(@Mock Recommendations recommendations) {
            // given
            final var productId = "123";
            when(recommendationsClientA.getRecommendations(productId)).thenReturn(recommendations);
            when(recommendationsClientB.getRecommendations(productId)).thenThrow(new RuntimeException());

            // when
            Recommendations actual = recommendationsService.getRecommendations(productId);

            // then
            assertEquals(recommendations, actual);
            verify(recommendationsClientB).getRecommendations(eq(productId));
        }

        @Test
        void shouldGetRecommendationsFromClientBWhenClientAFails(@Mock Recommendations recommendations) {
            // given
            final var productId = "123";
            when(recommendationsClientA.getRecommendations(productId)).thenThrow(new RuntimeException());
            when(recommendationsClientB.getRecommendations(productId)).thenReturn(recommendations);

            // when
            Recommendations actual = recommendationsService.getRecommendations(productId);

            // then
            assertEquals(recommendations, actual);
            verify(recommendationsClientA).getRecommendations(eq(productId));
        }
    }

    @Nested
    class ExceptionFlows {

        @Test
        void shouldReturnNullWhenBothServicesFail() {
            // given
            final var productId = "123";
            when(recommendationsClientA.getRecommendations(productId)).thenThrow(new RuntimeException());
            when(recommendationsClientB.getRecommendations(productId)).thenThrow(new RuntimeException());

            // when + then
            Recommendations actual = recommendationsService.getRecommendations(productId);

            // then
            assertNull(actual);
            verify(recommendationsClientA).getRecommendations(eq(productId));
            verify(recommendationsClientB).getRecommendations(eq(productId));
        }
    }
}