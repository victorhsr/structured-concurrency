package com.github.victorhsr.structuredconcurrency.context.recommendations;

import com.github.victorhsr.structuredconcurrency.context.orchestration.PageContextComponent;

import java.util.List;

public record Recommendations(List<RecommendationProduct> recommendations) implements PageContextComponent {


}
