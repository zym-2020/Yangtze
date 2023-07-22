package njnu.edu.back.common.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/22/9:59
 * @Description:
 */
@Configuration
@MapperScan(basePackages = "njnu.edu.back.dao.monitor", sqlSessionTemplateRef  = "monitorSqlSessionTemplate")
public class MonitorDataSourceConfig {
    @Bean(name = "monitorDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.monitor")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "monitorSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("monitorDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/monitor/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "monitorTransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("monitorDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "monitorSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("monitorSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
