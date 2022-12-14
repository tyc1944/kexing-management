package org.kexing.management.infrastruction;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.kexing.management.infrastruction.repository.jpa.mysql.UserAccountJpaRepository;
import org.kexing.management.infrastruction.repository.mybatis.mysql.UserAccountQueryMapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
    basePackageClasses = UserAccountJpaRepository.class,
    entityManagerFactoryRef = "mysqlEntityManager",
    transactionManagerRef = "mysqlTransactionManager")
@MapperScan(
    basePackageClasses = UserAccountQueryMapper.class,
    sqlSessionTemplateRef = "mysqlSessionTemplate")
public class PersistenceMysqlConf {

  @Autowired JpaProperties jpaProperties;

  @Bean(name = "mysqlDataSource")
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource mysqlDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "mysqlEntityManager")
  @Primary
  public LocalContainerEntityManagerFactoryBean mysqlEntityManager() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(mysqlDataSource());
    em.setPackagesToScan(new String[] {"org.kexing.management.domin.model.mysql","com.yunmo.attendance.api.entity","com.yunmo.haikang.device.api.entity"});
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);
    em.setJpaPropertyMap(jpaProperties.getProperties());

    return em;
  }

  @Bean(name = "mysqlSessionFactory")
  @Primary
  public SqlSessionFactory mysqlSessionFactory(@Qualifier("mysqlDataSource") DataSource dataSource)
      throws Exception {
    // MyBatis-Plus??????MybatisSqlSessionFactoryBean  MyBatis????????????SqlSessionFactoryBean
    MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
    // ???MyBatis-Plus???????????????
    bean.setDataSource(dataSource);
    bean.setMapperLocations(
        new PathMatchingResourcePatternResolver().getResources("classpath:mapper/mysql/*.xml"));
    bean.setPlugins(new Interceptor[] {paginationInterceptor()});
    return bean.getObject();
  }

  public static PaginationInterceptor paginationInterceptor() {
    PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
    // ???????????????????????????????????????????????? true??????????????????false ????????????  ??????false
    // paginationInterceptor.setOverflow(false);
    // ??????????????????????????????????????? 500 ??????-1 ????????????
    // paginationInterceptor.setLimit(500);
    // ?????? count ??? join ??????,??????????????? left join
    paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
    return paginationInterceptor;
  }


  @Bean
  @Primary
  public PlatformTransactionManager mysqlTransactionManager(
      @Qualifier("mysqlEntityManager") LocalContainerEntityManagerFactoryBean managerFactoryBean) {

    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(managerFactoryBean.getObject());
    return transactionManager;
  }

  @Primary
  @Bean(name = "mysqlSessionTemplate")
  public SqlSessionTemplate mysqlSessionTemplate(
      @Qualifier("mysqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
    return new SqlSessionTemplate(sqlSessionFactory);
  }
}
