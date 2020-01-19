package org.sky.tcc.icbc.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import io.seata.rm.datasource.DataSourceProxy;
import io.seata.spring.annotation.GlobalTransactionScanner;

@Configuration
public class DruidConfig {
	@Bean(value = "druidDataSource", initMethod = "init", destroyMethod = "close")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DruidDataSource druidDataSource() {
		return new DruidDataSource();

	}

	/**
	 * init datasource proxy
	 * 
	 * @Param: druidDataSource datasource bean instance
	 * @Return: DataSourceProxy datasource proxy
	 */
	@Primary
	@Bean("dataSource")
	public DataSourceProxy dataSourceProxy(DataSource dataSource) {
		return new DataSourceProxy(dataSource);
	}

	@Bean
	public DataSourceTransactionManager transactionManager(DataSourceProxy dataSourceProxy) {
		return new DataSourceTransactionManager(dataSourceProxy);
	}

	/**
	 * init jdbc template by using the dataSourceProxy
	 * 
	 * @Return: JdbcTemplate
	 */
	@Primary
	@Bean("jdbcTemplate")
	public JdbcTemplate dataSource(DataSourceProxy dataSourceProxy) {
		return new JdbcTemplate(dataSourceProxy);
	}

	/**
	 * init global transaction scanner
	 *
	 * @Return: GlobalTransactionScanner
	 */
	@Bean
	public GlobalTransactionScanner globalTransactionScanner() {
		return new GlobalTransactionScanner("tcc_bank_cmb", "demo-tx-grp");
	}

}
