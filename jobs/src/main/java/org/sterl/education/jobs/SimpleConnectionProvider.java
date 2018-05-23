package org.sterl.education.jobs;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.quartz.utils.ConnectionProvider;

import lombok.Data;
import lombok.NonNull;

@Data
public class SimpleConnectionProvider implements ConnectionProvider {
    @NonNull
    private final DataSource dataSource;
    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void initialize() {
    }
}
