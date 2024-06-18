package com.github.victorhsr.structuredconcurrency.context.orchestration;

import com.github.victorhsr.structuredconcurrency.context.PageContext;
import com.github.victorhsr.structuredconcurrency.context.details.Page;
import com.github.victorhsr.structuredconcurrency.context.hero.HeroData;
import com.github.victorhsr.structuredconcurrency.context.recommendations.Recommendations;
import com.github.victorhsr.structuredconcurrency.context.reviews.ReviewsData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PageContextScopeTest {

    @Nested
    class HappyPathTests {

        @Test
        void buildContext(@Mock HeroData heroData, @Mock ReviewsData reviewsData, @Mock Page productDetails, @Mock Recommendations recommendations) {
            // given
            PageContext expectedResult = new PageContext(reviewsData, productDetails, heroData, recommendations);

            // when
            PageContext actual;

            try (var scope = new PageContextScope()) {
                scope.fork(() -> heroData);
                scope.fork(() -> reviewsData);
                scope.fork(() -> productDetails);
                scope.fork(() -> recommendations);
                scope.join();

                actual = scope.getContext();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            // then
            assertEquals(expectedResult, actual);
        }
    }

    @Nested
    class ExceptionFlows {

        @Test
        void shouldFailWhenThereIsNoHeroData(@Mock ReviewsData reviewsData, @Mock Page productDetails, @Mock Recommendations recommendations) {
            // when + then
            assertThrows(PageContextException.class, () -> {
                try (var scope = new PageContextScope()) {
                    scope.fork(() -> null);
                    scope.fork(() -> reviewsData);
                    scope.fork(() -> productDetails);
                    scope.fork(() -> recommendations);
                    scope.join();

                    scope.getContext();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

        @Test
        void shouldFailWhenThereIsNoReviewsData(@Mock HeroData heroData, @Mock Page productDetails, @Mock Recommendations recommendations) {
            // when + then
            assertThrows(PageContextException.class, () -> {
                try (var scope = new PageContextScope()) {
                    scope.fork(() -> heroData);
                    scope.fork(() -> null);
                    scope.fork(() -> productDetails);
                    scope.fork(() -> recommendations);
                    scope.join();

                    scope.getContext();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

        @Test
        void shouldFailWhenThereIsNoProductDetails(@Mock HeroData heroData, @Mock ReviewsData reviewsData, @Mock Recommendations recommendations) {
            // when + then
            assertThrows(PageContextException.class, () -> {
                try (var scope = new PageContextScope()) {
                    scope.fork(() -> heroData);
                    scope.fork(() -> reviewsData);
                    scope.fork(() -> null);
                    scope.fork(() -> recommendations);
                    scope.join();

                    scope.getContext();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

        @Test
        void shouldFailWhenThereIsNoRecommendations(@Mock HeroData heroData, @Mock Page productDetails, @Mock ReviewsData reviewsData) {
            // when + then
            assertThrows(PageContextException.class, () -> {
                try (var scope = new PageContextScope()) {
                    scope.fork(() -> heroData);
                    scope.fork(() -> reviewsData);
                    scope.fork(() -> productDetails);
                    scope.fork(() -> null);
                    scope.join();

                    scope.getContext();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }
}