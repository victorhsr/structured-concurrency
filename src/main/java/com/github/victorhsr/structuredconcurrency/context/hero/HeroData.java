package com.github.victorhsr.structuredconcurrency.context.hero;

import com.github.victorhsr.structuredconcurrency.context.orchestration.ProductDetailsContextComponent;

public record HeroData(String title, String image) implements ProductDetailsContextComponent {
}
