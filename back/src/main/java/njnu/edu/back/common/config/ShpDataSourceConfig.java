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
 * @Date: 2022/07/08/14:36
 * @Description:
 */
@Configuration
@MapperScan(basePackages = "njnu.edu.back.shpDao", sqlSessionTemplateRef  = "mainSqlSessionTemplate")
public class ShpDataSourceConfig {
    @Bean(name = "shpDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.shp")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "shpSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("shpDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:shpMapper/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "shpTransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("shpDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "shpSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("shpSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
