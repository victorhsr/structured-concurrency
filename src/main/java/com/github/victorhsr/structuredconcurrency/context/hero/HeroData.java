package com.github.victorhsr.structuredconcurrency.context.hero;

import com.github.victorhsr.structuredconcurrency.context.orchestration.PageContextComponent;

public record HeroData(String title, String image) implements PageContextComponent {
}
