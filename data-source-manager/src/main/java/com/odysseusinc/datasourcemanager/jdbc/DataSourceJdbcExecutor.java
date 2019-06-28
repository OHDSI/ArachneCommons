package com.odysseusinc.datasourcemanager.jdbc;

import com.odysseusinc.arachne.commons.types.DBMSType;
import com.odysseusinc.arachne.execution_engine_common.api.v1.dto.DataSourceUnsecuredDTO;
import com.odysseusinc.arachne.execution_engine_common.util.BigQueryUtils;
import com.odysseusinc.datasourcemanager.krblogin.KerberosService;
import com.odysseusinc.datasourcemanager.krblogin.RuntimeServiceMode;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Objects;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DataSourceJdbcExecutor {

	private final KerberosService kerberosService;

	public DataSourceJdbcExecutor(KerberosService kerberosService) {

		this.kerberosService = kerberosService;
	}

	public <T> T executeOnSource(DataSourceUnsecuredDTO dataSourceData, JdbcTemplateConsumer<T> consumer) throws IOException {

		if (Objects.isNull(consumer)) {
			throw new IllegalArgumentException("consumer is required");
		}

		File tempDir = Files.createTempDirectory("gis").toFile();

		// BigQuery
		if (Objects.equals(DBMSType.BIGQUERY, dataSourceData.getType()) && Objects.nonNull(dataSourceData.getKeyfile())) {
			File keyFile = Files.createTempFile(tempDir.toPath(), "", ".json").toFile();
			try(OutputStream out = new FileOutputStream(keyFile)) {
				IOUtils.write(dataSourceData.getKeyfile(), out);
			}
			String connStr = BigQueryUtils.replaceBigQueryKeyPath(dataSourceData.getConnectionString(), keyFile.getAbsolutePath());
			dataSourceData.setConnectionString(connStr);
		}

		// Kerberos
		if (dataSourceData.getUseKerberos()) {
			kerberosService.runKinit(dataSourceData, RuntimeServiceMode.SINGLE, tempDir);
		}

		DriverManagerDataSource dataSource = new DriverManagerDataSource(
						dataSourceData.getConnectionString(),
						dataSourceData.getUsername(),
						dataSourceData.getPassword()
		);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		try {
			return consumer.execute(jdbcTemplate);
		} finally {
			FileUtils.deleteQuietly(tempDir);
		}
	}
}
