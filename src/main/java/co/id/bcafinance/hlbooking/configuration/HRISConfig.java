package co.id.bcafinance.hlbooking.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:hris.properties")
public class HRISConfig {

    @Value(("${DB_URL_HRIS}"))
    private String dbUrlHris;

    @Value(("${DB_NAME_HRIS}"))
    private String dbNameHris;

    @Value(("${DB_UNAME_HRIS}"))
    private String dbUnameHris;

    @Value(("${DB_PWD_HRIS}"))
    private String dbPwdHris;


    @Bean(name = "sqlServerDataSourceHRIS")
    public DataSource sqlServerDataSourceHRIS() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSourceBuilder.url(dbUrlHris);
        dataSourceBuilder.username(dbUnameHris);
        dataSourceBuilder.password(dbPwdHris);
        return dataSourceBuilder.build();
    }

    @Bean(name = "sqlServerJdbcHRIS")
    public JdbcTemplate sqlServerJdbcTemplate(@Qualifier("sqlServerDataSourceHRIS") DataSource sqlServerDataSourceHRIS) {
        return new JdbcTemplate(sqlServerDataSourceHRIS);
    }
}



