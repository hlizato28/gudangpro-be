package co.id.bcafinance.hlbooking.configuration;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 13/06/2024 12:43
@Last Modified 13/06/2024 12:43
Version 1.0
*/

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:financore.properties")
public class FinancoreConfig {
    @Value(("${DB_URL_FINA}"))
    private String dbUrlFina;

    @Value(("${DB_NAME_FINA"))
    private String dbNameFina;

    @Value(("${DB_UNAME_FINA}"))
    private String dbUnameFina;

    @Value(("${DB_PW_FINA}"))
    private String dbPwFina;


    @Bean(name = "sqlServerDataSourceFinancore")
    public DataSource sqlServerDataSourceFinancore() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSourceBuilder.url(dbUrlFina);
        dataSourceBuilder.username(dbUnameFina);
        dataSourceBuilder.password(dbPwFina);
        return dataSourceBuilder.build();
    }

    @Bean(name = "sqlServerJdbcFinancore")
    public JdbcTemplate sqlServerJdbcTemplate(@Qualifier("sqlServerDataSourceFinancore") DataSource sqlServerDataSourceFinancore) {
        return new JdbcTemplate(sqlServerDataSourceFinancore);
    }
}

