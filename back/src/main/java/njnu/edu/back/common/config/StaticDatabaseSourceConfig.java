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
 * @Date: 2022/11/30/19:50
 * @Description:
 */
@Configuration
@MapperScan(basePackages = "njnu.edu.back.dao.staticdb", sqlSessionTemplateRef  = "staticdbSqlSessionTemplate")
public class StaticDatabaseSourceConfig {
    @Bean(name = "staticdbDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.staticdb")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "staticdbSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("staticdbDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/staticdb/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "staticdbTransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("staticdbDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "staticdbSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("staticdbSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
