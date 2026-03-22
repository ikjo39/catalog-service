package com.polarbookshop.catalog_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@Configuration
@EnableJdbcAuditing // 영속성 엔티티에 대한 Auditing 활성화
public class DataConfig {
}
