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

import org.apache.commons.collections4.comparators.ComparableComparator;
import org.apache.commons.collections4.comparators.NullComparator;
import org.apache.commons.collections4.comparators.ReverseComparator;
import org.apache.commons.collections4.comparators.TransformingComparator;

import java.util.Comparator;

/**
 * Provides convenient static utility methods for <Code>Comparator</Code>
 * objects.
 * <p/>
 * Most of the functionality in this class can also be found in the
 * <code>comparators</code> package. This class merely provides a
 * convenient central place if you have use for more than one class
 * in the <code>comparators</code> subpackage.
 *
 * @version $Id$
 * @since 2.1
 */
public class ComparatorUtils {
    /**
     * Comparator for natural sort order.
     *
     * @see ComparableComparator#comparableComparator()
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // explicit type needed for Java 1.5 compilation
    public static final Comparator NATURAL_COMPARATOR = ComparableComparator.<Comparable>comparableComparator();

    /**
     * ComparatorUtils should not normally be instantiated.
     */
    private ComparatorUtils() {}

    /**
     * Gets a comparator that reverses the order of the given comparator.
     *
     * @param <E>        the object type to compare
     * @param comparator the comparator to reverse
     * @return a comparator that reverses the order of the input comparator
     * @see ReverseComparator
     */
    public static <E> Comparator<E> reversedComparator(final Comparator<E> comparator) {
        return new ReverseComparator<E>(comparator);
    }

    /**
     * Gets a Comparator that controls the comparison of <code>null</code> values.
     * <p/>
     * The returned comparator will consider a null value to be less than
     * any nonnull value, and equal to any other null value.  Two nonnull
     * values will be evaluated with the given comparator.
     *
     * @param <E>        the object type to compare
     * @param comparator the comparator that wants to allow nulls
     * @return a version of that comparator that allows nulls
     * @see NullComparator
     */
    @SuppressWarnings("unchecked")
    public static <E> Comparator<E> nullLowComparator(Comparator<E> comparator) {
        if (comparator == null) {
            comparator = NATURAL_COMPARATOR;
        }
        return new NullComparator<E>(comparator, false);
    }

    /**
     * Gets a Comparator that controls the comparison of <code>null</code> values.
     * <p/>
     * The returned comparator will consider a null value to be greater than
     * any nonnull value, and equal to any other null value.  Two nonnull
     * values will be evaluated with the given comparator.
     *
     * @param <E>        the object type to compare
     * @param comparator the comparator that wants to allow nulls
     * @return a version of that comparator that allows nulls
     * @see NullComparator
     */
    @SuppressWarnings("unchecked")
    public static <E> Comparator<E> nullHighComparator(Comparator<E> comparator) {
        if (comparator == null) {
            comparator = NATURAL_COMPARATOR;
        }
        return new NullComparator<E>(comparator, true);
    }

    /**
     * Gets a Comparator that passes transformed objects to the given comparator.
     * <p/>
     * Objects passed to the returned comparator will first be transformed
     * by the given transformer before they are compared by the given
     * comparator.
     *
     * @param <I>         the input object type of the transformed comparator
     * @param <O>         the object type of the decorated comparator
     * @param comparator  the sort order to use
     * @param transformer the transformer to use
     * @return a comparator that transforms its input objects before comparing them
     * @see TransformingComparator
     */
    @SuppressWarnings("unchecked")
    public static <I, O> Comparator<I> transformedComparator(Comparator<O> comparator,
                                                             final Transformer<? super I, ? extends O> transformer) {

        if (comparator == null) {
            comparator = NATURAL_COMPARATOR;
        }
        return new TransformingComparator<I, O>(transformer, comparator);
    }
}
