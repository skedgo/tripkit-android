package com.skedgo.tripkit;

import androidx.annotation.NonNull;

import com.skedgo.tripkit.common.model.TransportMode;

import io.reactivex.functions.BiFunction;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class ModeCombinationStrategy implements
        BiFunction<Map<String, TransportMode>, List<String>, List<Set<String>>> {
    @Override
    public List<Set<String>> apply(
            @NonNull Map<String, TransportMode> modeMap,
            @NonNull List<String> modeIds) {
        final Set<String> seenModeIds = new HashSet<>();
        final List<Set<String>> modeIdSets = new LinkedList<>();

        for (String modeId : modeIds) {
            if (seenModeIds.contains(modeId)) {
                continue;
            }

            final Set<String> newSet = new HashSet<>();
            newSet.add(modeId);

            final TransportMode foundMode = modeMap.get(modeId);
            if (foundMode != null) {
                boolean shouldMerge = false;
                final List<String> implies = foundMode.getImplies();
                if (CollectionUtils.isNotEmpty(implies)) {
                    newSet.addAll(implies);

                    for (String imply : implies) {
                        if (seenModeIds.contains(imply)) {
                            shouldMerge = true;
                            break;
                        }
                    }
                }

                if (shouldMerge) {
                    // For example, newSet is [B, C] and modeIdSets are [A, C] and [D].
                    // Then we have to find [A, C] to merge with [B, C].
                    // [A, C] then will become [A, B, C].
                    // If we don't do so, we may end up duplicate routes.
                    for (Set<String> existingSet : modeIdSets) {
                        for (String imply : implies) {
                            if (existingSet.contains(imply)) {
                                existingSet.addAll(newSet);
                                break;
                            }
                        }
                    }
                } else {
                    modeIdSets.add(newSet);
                }
            } else {
                modeIdSets.add(newSet);
            }

            seenModeIds.addAll(newSet);
        }

        // Create a union set which combines all given modeIds.
        // There is one exception: wa_wal is implied in multi-modal, so we don't need to specify it. We also don't want
        // to duplicate a query if there aren't enough modes to justify multi-modal, so do some final checking.
        if (modeIds.size() == 2 && modeIds.contains(TransportMode.ID_WHEEL_CHAIR) && modeIds.contains(TransportMode.ID_PS_DRT)) {

        } else {

        }

        HashSet<String> multiModal = new HashSet<>(modeIds);
        multiModal.remove(TransportMode.ID_WALK);
        if (multiModal.size() > 1) {
            modeIdSets.add(multiModal);
        }

        //Will remove ps_drt and wa_whe hash set since result is just the same with ps_drt mode
        modeIdSets.remove(
                new HashSet<>(
                        Arrays.asList(TransportMode.ID_PS_DRT, TransportMode.ID_WHEEL_CHAIR)
                )
        );

        return modeIdSets;
    }
}