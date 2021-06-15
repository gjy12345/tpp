package cn.gjyniubi.cinema.common.config.durid;

import cn.gjyniubi.cinema.common.config.properties.DruidProperties;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @Author gujianyang
 * @Date 2021/2/9
 * @Class DruidConfig
 * 配置druid连接池
 */
@ConditionalOnProperty("spring.datasource.druid.master.url")
@Configuration
public class DruidConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }

    @Bean(name = "masterTransactionManager")
    @Primary
    public DataSourceTransactionManager masterTransactionManager(@Qualifier("masterDataSource") DataSource masterDataSource) {
        DataSourceTransactionManager manager=new DataSourceTransactionManager(masterDataSource);
        manager.setRollbackOnCommitFailure(true);
        return manager;
    }


}
