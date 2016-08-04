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

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertTrue;

/**
 * Tests ComparatorUtils.
 *
 * @version $Id$
 */
public class ComparatorUtilsTest {
    @Test
    public void nullLowComparator() {
        Comparator<Integer> comp = ComparatorUtils.nullLowComparator(null);
        assertTrue(comp.compare(null, 10) < 0);
        assertTrue(comp.compare(null, null) == 0);
        assertTrue(comp.compare(10, null) > 0);
    }

    @Test
    public void nullHighComparator() {
        Comparator<Integer> comp = ComparatorUtils.nullHighComparator(null);
        assertTrue(comp.compare(null, 10) > 0);
        assertTrue(comp.compare(null, null) == 0);
        assertTrue(comp.compare(10, null) < 0);
    }
}
