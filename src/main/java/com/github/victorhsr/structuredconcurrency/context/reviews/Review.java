package com.github.victorhsr.structuredconcurrency.context.reviews;

import java.time.LocalDateTime;

public record Review(Double value, String comment, LocalDateTime date, String authorName) {
}
