package com.github.victorhsr.structuredconcurrency.context.orchestration;

import com.github.victorhsr.structuredconcurrency.context.ProductDetailsContext;
import com.github.victorhsr.structuredconcurrency.context.details.ProductDetails;
import com.github.victorhsr.structuredconcurrency.context.hero.HeroData;
import com.github.victorhsr.structuredconcurrency.context.reviews.ReviewsData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ProductDetailsContextScopeTest {

    @Nested
    class HappyPathTests {

        @Test
        void buildContext(@Mock HeroData heroData, @Mock ReviewsData reviewsData, @Mock ProductDetails productDetails) {
            // given
            ProductDetailsContext expectedResult = new ProductDetailsContext(reviewsData, productDetails, heroData);

            // when
            ProductDetailsContext actual;

            try (var scope = new ProductDetailsContextScope()) {
                scope.fork(() -> heroData);
                scope.fork(() -> reviewsData);
                scope.fork(() -> productDetails);
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
        void shouldFailWhenThereIsNoHeroData(@Mock ReviewsData reviewsData, @Mock ProductDetails productDetails) {
            // when + then
            assertThrows(ProductDetailsException.class, () -> {
                try (var scope = new ProductDetailsContextScope()) {
                    scope.fork(() -> null);
                    scope.fork(() -> reviewsData);
                    scope.fork(() -> productDetails);
                    scope.join();

                    scope.getContext();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

        @Test
        void shouldFailWhenThereIsNoReviewsData(@Mock HeroData heroData, @Mock ProductDetails productDetails) {
            // when + then
            assertThrows(ProductDetailsException.class, () -> {
                try (var scope = new ProductDetailsContextScope()) {
                    scope.fork(() -> heroData);
                    scope.fork(() -> null);
                    scope.fork(() -> productDetails);
                    scope.join();

                    scope.getContext();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

        @Test
        void shouldFailWhenThereIsNoProductDetails(@Mock HeroData heroData, @Mock ReviewsData reviewsData) {
            // when + then
            assertThrows(ProductDetailsException.class, () -> {
                try (var scope = new ProductDetailsContextScope()) {
                    scope.fork(() -> heroData);
                    scope.fork(() -> reviewsData);
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