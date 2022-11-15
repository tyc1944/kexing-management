package org.kexing.management.infrastruction;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.kexing.management.infrastruction.repository.mybatis.sql_server.ProductionOrderQueryMapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import static org.kexing.management.infrastruction.PersistenceMysqlConf.paginationInterceptor;

@Configuration
@MapperScan(
    basePackageClasses = ProductionOrderQueryMapper.class,
    sqlSessionTemplateRef = "sqlServerSessionFactoryTemplate")
public class PersistenceSqlServerConf {

  @Bean(name = "sqlServerDataSource")
  @ConfigurationProperties(prefix = "spring.second-datasource")
  public DataSource sqlServerDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "sqlServerSessionFactory")
  public SqlSessionFactory sqlServerSessionFactory(
      @Qualifier("sqlServerDataSource") DataSource dataSource) throws Exception {
    // MyBatis-Plus使用MybatisSqlSessionFactoryBean  MyBatis直接使用SqlSessionFactoryBean
    MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
    // 给MyBatis-Plus注入数据源
    bean.setDataSource(dataSource);
    bean.setMapperLocations(
        new PathMatchingResourcePatternResolver()
            .getResources("classpath:mapper/sql_server/*.xml"));
    bean.setPlugins(new Interceptor[] {paginationInterceptor()});
    return bean.getObject();
  }

  @Bean
  public PlatformTransactionManager sqlServerTransactionManager() {
    return new JpaTransactionManager();
  }

  @Bean(name = "sqlServerSessionFactoryTemplate")
  public SqlSessionTemplate sqlServerSessionFactory(
      @Qualifier("sqlServerSessionFactory") SqlSessionFactory sqlSessionFactory) {
    return new SqlSessionTemplate(sqlSessionFactory);
  }
}
