/*
 * Copyright 2014 sterlp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sterl.education;

import static org.hibernate.cfg.AvailableSettings.DIALECT;
import static org.hibernate.cfg.AvailableSettings.GENERATE_STATISTICS;
import static org.hibernate.cfg.AvailableSettings.HBM2DDL_DATABASE_ACTION;
import static org.hibernate.cfg.AvailableSettings.QUERY_STARTUP_CHECKING;
import static org.hibernate.cfg.AvailableSettings.SHOW_SQL;
import static org.hibernate.cfg.AvailableSettings.STATEMENT_BATCH_SIZE;
import static org.hibernate.cfg.AvailableSettings.USE_QUERY_CACHE;
import static org.hibernate.cfg.AvailableSettings.USE_REFLECTION_OPTIMIZER;
import static org.hibernate.cfg.AvailableSettings.USE_SECOND_LEVEL_CACHE;
import static org.hibernate.cfg.AvailableSettings.USE_STRUCTURED_CACHE;
import static org.hibernate.jpa.AvailableSettings.JDBC_DRIVER;
import static org.hibernate.jpa.AvailableSettings.JDBC_URL;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.stat.Statistics;

import lombok.Getter;

/**
 * Example how a entity manager can be started for unit tests, not using the persistence.xml.
 */
public class TestEntityManager {

    private final EntityManagerFactory managerFactory;
    @Getter
    private final EntityManager entityManager;
    @Getter
    private final Statistics statistics;
    
    public TestEntityManager(Class<?>... entites) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(JDBC_DRIVER, "org.h2.Driver");
        properties.put(JDBC_URL, "jdbc:h2:mem:test");
        properties.put(DIALECT, "org.hibernate.dialect.H2Dialect");
        properties.put(HBM2DDL_DATABASE_ACTION, "drop-and-create");
        properties.put(SHOW_SQL, true);
        properties.put(QUERY_STARTUP_CHECKING, true);
        properties.put(GENERATE_STATISTICS, true);
        properties.put(USE_REFLECTION_OPTIMIZER, true);
        properties.put(USE_SECOND_LEVEL_CACHE, false);
        properties.put(USE_QUERY_CACHE, false);
        properties.put(USE_STRUCTURED_CACHE, false);
        properties.put(STATEMENT_BATCH_SIZE, 50);
        
        managerFactory = new HibernatePersistenceProvider()
            .createContainerEntityManagerFactory(
                archiverPersistenceUnitInfo(entites), properties);

        entityManager = managerFactory.createEntityManager();
        statistics = getHibernateSession(entityManager).getSessionFactory().getStatistics();
        statistics.setStatisticsEnabled(true);
    }

    private static Session getHibernateSession(EntityManager entityManager) {
        return (Session) entityManager.getDelegate();
    }

    public void cancelTrx() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    public void beginTrx() {
        statistics.clear();
        entityManager.getTransaction().begin();
    }

    public void commit() {
        entityManager.getTransaction().commit();
    }

    private static PersistenceUnitInfo archiverPersistenceUnitInfo(Class<?>... entities) {
        
        
        return new PersistenceUnitInfo() {
            @Override
            public String getPersistenceUnitName() {
                return "TestPersistenceUnit";
            }

            @Override
            public String getPersistenceProviderClassName() {
                return "org.hibernate.jpa.HibernatePersistenceProvider";
            }

            @Override
            public PersistenceUnitTransactionType getTransactionType() {
                return PersistenceUnitTransactionType.RESOURCE_LOCAL;
            }

            @Override
            public DataSource getJtaDataSource() {
                return null;
            }

            @Override
            public DataSource getNonJtaDataSource() {
                return null;
            }

            @Override
            public List<String> getMappingFileNames() {
                return Collections.emptyList();
            }

            @Override
            public List<URL> getJarFileUrls() {
                try {
                    return Collections.list(this.getClass()
                        .getClassLoader()
                        .getResources(""));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }

            @Override
            public URL getPersistenceUnitRootUrl() {
                return null;
            }

            @Override
            public List<String> getManagedClassNames() {
                return Arrays.asList(entities).stream().map(e -> e.getName()).collect(Collectors.toList());
            }

            @Override
            public boolean excludeUnlistedClasses() {
                return false;
            }

            @Override
            public SharedCacheMode getSharedCacheMode() {
                return null;
            }

            @Override
            public ValidationMode getValidationMode() {
                return null;
            }

            @Override
            public Properties getProperties() {
                return new Properties();
            }

            @Override
            public String getPersistenceXMLSchemaVersion() {
                return null;
            }

            @Override
            public ClassLoader getClassLoader() {
                return null;
            }

            @Override
            public void addTransformer(ClassTransformer transformer) {

            }

            @Override
            public ClassLoader getNewTempClassLoader() {
                return null;
            }
        };
    }
}
