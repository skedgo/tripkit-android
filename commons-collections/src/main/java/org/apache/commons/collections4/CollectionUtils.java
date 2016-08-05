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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Provides utility methods and decorators for {@link Collection} instances.
 * <p/>
 * Various utility methods might put the input objects into a Set/Map/Bag. In case
 * the input objects override {@link Object#equals(Object)}, it is mandatory that
 * the general contract of the {@link Object#hashCode()} method is maintained.
 * <p/>
 * NOTE: From 4.0, method parameters will take {@link Iterable} objects when possible.
 *
 * @version $Id$
 * @since 1.0
 */
public class CollectionUtils {
    /**
     * <code>CollectionUtils</code> should not normally be instantiated.
     */
    private CollectionUtils() {}

    /**
     * Returns <code>true</code> iff all elements of {@code coll2} are also contained
     * in {@code coll1}. The cardinality of values in {@code coll2} is not taken into account,
     * which is the same behavior as {@link Collection#containsAll(Collection)}.
     * <p/>
     * In other words, this method returns <code>true</code> iff the
     * {@link #intersection} of <i>coll1</i> and <i>coll2</i> has the same cardinality as
     * the set of unique values from {@code coll2}. In case {@code coll2} is empty, {@code true}
     * will be returned.
     * <p/>
     * This method is intended as a replacement for {@link Collection#containsAll(Collection)}
     * with a guaranteed runtime complexity of {@code O(n + m)}. Depending on the type of
     * {@link Collection} provided, this method will be much faster than calling
     * {@link Collection#containsAll(Collection)} instead, though this will come at the
     * cost of an additional space complexity O(n).
     *
     * @param coll1 the first collection, must not be null
     * @param coll2 the second collection, must not be null
     * @return <code>true</code> iff the intersection of the collections has the same cardinality
     * as the set of unique elements from the second collection
     * @since 4.0
     */
    public static boolean containsAll(final Collection<?> coll1, final Collection<?> coll2) {
        if (coll2.isEmpty()) {
            return true;
        } else {
            final Iterator<?> it = coll1.iterator();
            final Set<Object> elementsAlreadySeen = new HashSet<Object>();
            for (final Object nextElement : coll2) {
                if (elementsAlreadySeen.contains(nextElement)) {
                    continue;
                }

                boolean foundCurrentElement = false;
                while (it.hasNext()) {
                    final Object p = it.next();
                    elementsAlreadySeen.add(p);
                    if (nextElement == null ? p == null : nextElement.equals(p)) {
                        foundCurrentElement = true;
                        break;
                    }
                }

                if (foundCurrentElement) {
                    continue;
                } else {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Finds the first element in the given collection which matches the given predicate.
     * <p/>
     * If the input collection or predicate is null, or no element of the collection
     * matches the predicate, null is returned.
     *
     * @param <T>        the type of object the {@link Iterable} contains
     * @param collection the collection to search, may be null
     * @param predicate  the predicate to use, may be null
     * @return the first element of the collection which matches the predicate or null if none could be found
     * @deprecated since 4.1, use {@link IterableUtils#find(Iterable, Predicate)} instead
     */
    @Deprecated
    public static <T> T find(final Iterable<T> collection, final Predicate<? super T> predicate) {
        if (collection != null && predicate != null) {
            for (T item : collection) {
                if (predicate.evaluate(item)) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * Executes the given closure on each element in the collection.
     * <p/>
     * If the input collection or closure is null, there is no change made.
     *
     * @param <T>        the type of object the {@link Iterable} contains
     * @param <C>        the closure type
     * @param collection the collection to get the input from, may be null
     * @param closure    the closure to perform, may be null
     * @return closure
     * @deprecated since 4.1, use {@link IterableUtils#forEach(Iterable, Closure)} instead
     */
    @Deprecated
    public static <T, C extends Closure<? super T>> C forAllDo(final Iterable<T> collection, final C closure) {
        if (collection != null && closure != null) {
            for (T item : collection) {
                closure.execute(item);
            }
        }
        return closure;
    }

    /**
     * Null-safe check if the specified collection is empty.
     * <p/>
     * Null returns true.
     *
     * @param coll the collection to check, may be null
     * @return true if empty or null
     * @since 3.2
     */
    public static boolean isEmpty(final Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    /**
     * Null-safe check if the specified collection is not empty.
     * <p/>
     * Null returns false.
     *
     * @param coll the collection to check, may be null
     * @return true if non-null and non-empty
     * @since 3.2
     */
    public static boolean isNotEmpty(final Collection<?> coll) {
        return !isEmpty(coll);
    }
}
