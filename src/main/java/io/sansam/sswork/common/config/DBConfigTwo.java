//package io.sansam.sswork.common.config;
//
//import com.zaxxer.hikari.HikariDataSource;
//import io.sansam.sswork.dao.StudentMapper;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//
///**
// * <p>
// * DBConfigOne
// * </p>
// *
// * @author houcb
// * @since 2019-10-29 09:54
// */
//@Configuration
//@MapperScan(basePackageClasses = StudentMapper.class, sqlSessionFactoryRef = "sqlSessionFactory02")
//public class DBConfigTwo {
//
//    @Bean("db02")
//    @ConfigurationProperties(prefix = "spring.datasource.hikari.db02")
//    public DataSource dbTwo() {
//        return DataSourceBuilder.create().type(HikariDataSource.class).build();
//    }
//
//    @Bean("sqlSessionFactory02")
//    public SqlSessionFactory sqlSessionFactory02(@Qualifier("db02") DataSource dataSource) throws Exception {
//        final SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        return bean.getObject();
//    }
//
//    @Bean("transactionManager02")
//    public PlatformTransactionManager transactionManager02(@Qualifier("db02") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//}
