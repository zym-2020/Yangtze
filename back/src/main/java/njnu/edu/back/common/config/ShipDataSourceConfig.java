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
 * @Date: 2022/12/06/15:50
 * @Description:
 */
@Configuration
@MapperScan(basePackages = "njnu.edu.back.dao.ship", sqlSessionTemplateRef  = "shipSqlSessionTemplate")
public class ShipDataSourceConfig {
    @Bean(name = "shipDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.ship")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "shipSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("shipDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/ship/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "shipTransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("shipDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "shipSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("shipSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
