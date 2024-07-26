package com.skedgo.tripkit;

import com.skedgo.tripkit.common.model.TransportMode;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import io.reactivex.functions.BiFunction;

// TODO convert to kotlin and add coroutine version.
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

            TransportMode foundMode;
            // For modes from modeIdentifiers, e.g. `pt_ltd_SCHOOLBUS_2029`, `pt_ltd_SCHOOLBUS_2031`, etc.
            // to remove numeric suffix to get base modeId for checking if exist on modeMap
            String baseModeId = Pattern.compile("_\\d+$").matcher(modeId).replaceAll("");

            // Check if the baseModeId is present in the modeMap
            foundMode = modeMap.get(baseModeId);
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