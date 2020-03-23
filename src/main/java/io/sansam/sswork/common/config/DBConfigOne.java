package io.sansam.sswork.common.config;

import com.zaxxer.hikari.HikariDataSource;
import io.sansam.sswork.dao.ScoreMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * <p>
 * DBConfigOne
 * </p>
 *
 * @author houcb
 * @since 2019-10-29 09:54
 */
@Configuration
@MapperScan(basePackages = "io.sansam.sswork.dao", sqlSessionFactoryRef = "sqlSessionFactory01")
public class DBConfigOne {

    @Bean("db01")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.db01")
    public DataSource dbOne() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean("sqlSessionFactory01")
    public SqlSessionFactory sqlSessionFactory01(@Qualifier("db01") DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean.getObject();
    }

    @Bean("transactionManager01")
    public PlatformTransactionManager transactionManager01(@Qualifier("db01") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
