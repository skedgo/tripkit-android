/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections4;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for CollectionUtils.
 *
 * @version $Id$
 */
public class CollectionUtilsTest {
    private List<Integer> collectionA = null;

    @Before
    public void setUp() {
        collectionA = new ArrayList<>();
        collectionA.add(1);
        collectionA.add(2);
        collectionA.add(2);
        collectionA.add(3);
        collectionA.add(3);
        collectionA.add(3);
        collectionA.add(4);
        collectionA.add(4);
        collectionA.add(4);
        collectionA.add(4);
    }

    @Test
    public void testIsEmptyWithEmptyCollection() {
        final Collection<Object> coll = new ArrayList<>();
        assertEquals(true, CollectionUtils.isEmpty(coll));
    }

    @Test
    public void testIsEmptyWithNonEmptyCollection() {
        final Collection<String> coll = new ArrayList<>();
        coll.add("item");
        assertEquals(false, CollectionUtils.isEmpty(coll));
    }

    @Test
    public void testIsEmptyWithNull() {
        final Collection<?> coll = null;
        assertEquals(true, CollectionUtils.isEmpty(coll));
    }

    @Test
    public void testIsNotEmptyWithEmptyCollection() {
        final Collection<Object> coll = new ArrayList<>();
        assertEquals(false, CollectionUtils.isNotEmpty(coll));
    }

    @Test
    public void testIsNotEmptyWithNonEmptyCollection() {
        final Collection<String> coll = new ArrayList<>();
        coll.add("item");
        assertEquals(true, CollectionUtils.isNotEmpty(coll));
    }

    @Test
    public void testIsNotEmptyWithNull() {
        final Collection<?> coll = null;
        assertEquals(false, CollectionUtils.isNotEmpty(coll));
    }

    @Test
    public void isEmpty() {
        assertFalse(CollectionUtils.isNotEmpty(null));
        assertTrue(CollectionUtils.isNotEmpty(collectionA));
    }
}
