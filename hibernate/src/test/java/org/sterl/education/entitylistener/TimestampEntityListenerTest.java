/*
 * Copyright 2015.
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
package org.sterl.education.entitylistener;

import javax.persistence.EntityManager;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.sterl.education.TestEntityManager;

public class TimestampEntityListenerTest {
    
    private static TestEntityManager ENTITY_MANAGER;
    private static EntityManager em;

    @BeforeClass
    public static void startHibernate() {
        ENTITY_MANAGER = new TestEntityManager(PersonBE.class);
        em = ENTITY_MANAGER.getEntityManager();
    }
    

    @Test
    public void testCustomType() {
        PersonBE e = PersonBE.builder().name("Paul").build();
        ENTITY_MANAGER.beginTrx();
        em.persist(e);
        ENTITY_MANAGER.commit();
        
        assertNotNull(e.getId());
        assertNotNull(e.getTimestamp());
        assertNotNull(e.getTimestamp().getCreateDate());
        assertNotNull(e.getTimestamp().getUpdateDate());
        
        final long lastUpdatge = e.getTimestamp().getUpdateDate();
        e.setName("Ivan");
        
        ENTITY_MANAGER.beginTrx();
        e = em.merge(e);
        ENTITY_MANAGER.commit();
        assertTrue("Update date should be updated", lastUpdatge < e.getTimestamp().getUpdateDate());
    }
}
