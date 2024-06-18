package com.github.victorhsr.structuredconcurrency.context.details;

import com.github.victorhsr.structuredconcurrency.context.orchestration.PageContextComponent;

import java.util.Map;

public record Page(Map<String, String> characteristics) implements PageContextComponent {
}
