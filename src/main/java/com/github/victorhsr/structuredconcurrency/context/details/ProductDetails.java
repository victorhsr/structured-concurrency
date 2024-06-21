package com.github.victorhsr.structuredconcurrency.context.details;

import com.github.victorhsr.structuredconcurrency.context.orchestration.PageContextComponent;

import java.util.Map;

public record ProductDetails(Map<String, String> characteristics) implements PageContextComponent {
}
