package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;

import com.skedgo.android.common.model.TransportMode;

import org.apache.commons.collections4.CollectionUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.functions.Func2;

final class ModeCombinationStrategy implements
    Func2<Map<String, TransportMode>, List<String>, List<Set<String>>> {
  @Override public List<Set<String>> call(
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

    // Create an union set which combines all given modeIds.
    modeIdSets.add(new HashSet<>(modeIds));
    return modeIdSets;
  }
}