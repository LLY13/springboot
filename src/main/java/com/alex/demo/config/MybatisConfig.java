package com.alex.demo.config;

import com.alex.demo.constant.DatabaseEnum.FirstChar;
import com.alex.demo.constant.DatabaseEnum.Normal;
import com.alex.demo.constant.DatabaseEnum.Ordinal;
import com.alex.demo.pojo.po.JsonType;
import com.alex.demo.util.typeHandler.FirstCharHandler;
import com.alex.demo.util.typeHandler.JsonTypeHandler;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Set;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@MapperScan("com.alex.demo.dao")
public class MybatisConfig {

  @Autowired
  private HikariDataSource dataSource;

  @Bean
  public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
    SqlSessionFactoryBean session = new SqlSessionFactoryBean();
    session.setDataSource(dataSource);

    // session configuration
    org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
    config.setMapUnderscoreToCamelCase(true);
    config.setUseGeneratedKeys(true);

    // config type handler
    scanTypeHandler(config.getTypeHandlerRegistry());
    session.setConfiguration(config);

    // xml location configuration
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    session.setMapperLocations(resolver.getResources("classpath*:mapper/**/*.xml"));

    // get and return
    return session.getObject();
  }

  @Bean
  public PlatformTransactionManager transactionManager() {
    return new DataSourceTransactionManager(dataSource);
  }

  private void scanTypeHandler(TypeHandlerRegistry registry) {
    // custom enums
    Reflections enums = new Reflections("com.alex.demo.constant");
    register(registry, enums, FirstChar.class, FirstCharHandler.class);
    register(registry, enums, Ordinal.class, EnumOrdinalTypeHandler.class);
    register(registry, enums, Normal.class, EnumTypeHandler.class);
////     custom types
//    Reflections types = new Reflections("com.alex.demo.pojo.po");
//    register(registry, types, JsonType.class, JsonTypeHandler.class);
  }

  private <T, H extends BaseTypeHandler> void register(TypeHandlerRegistry registry,
      Reflections reflections, Class<T> clz, Class<H> handler) {
    Set<Class<? extends T>> classes = reflections.getSubTypesOf(clz);
    for (Class<?> c : classes) {
      registry.register(c, handler);
    }
  }
}