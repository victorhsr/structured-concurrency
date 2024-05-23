package com.github.victorhsr.structuredconcurrency.infrastructure;

import com.github.victorhsr.structuredconcurrency.context.reviews.Review;
import com.github.victorhsr.structuredconcurrency.context.reviews.ReviewsData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ReviewsClient {

    private static final Map<String, ReviewsData> DATA_MAP;

    static {
        DATA_MAP = Map.of(
                "1", new ReviewsData(5d, buildReviews1()),
                "2", new ReviewsData(4d, buildReviews2()),
                "3", new ReviewsData(3d, buildReviews3())
        );
    }

    private static List<Review> buildReviews3() {
        return List.of(
                new Review(3d, "The delivery took too long...", LocalDateTime.now(), "Victor"),
                new Review(3d, "Seller doesn't reply to my messages", LocalDateTime.now(), "Anna")
        );
    }

    private static List<Review> buildReviews2() {
        return List.of(
                new Review(4d, "Good Price", LocalDateTime.now(), "Victor"),
                new Review(4d, "Working as expected", LocalDateTime.now(), "Anna")
        );
    }

    private static List<Review> buildReviews1() {
        return List.of(
                new Review(5d, "Nice", LocalDateTime.now(), "Victor"),
                new Review(5d, "Cool", LocalDateTime.now(), "Anna")
        );
    }

    public ReviewsData getReviews(String productId) throws InterruptedException {
        System.out.println("ReviewsClient getting results...");
        Thread.sleep(180);
        if (!DATA_MAP.containsKey(productId)) {
            throw new IllegalArgumentException(STR."productId not found \{productId}");
        }
        return DATA_MAP.get(productId);
    }

}
