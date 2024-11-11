package com.skedgo.tripkit

import com.skedgo.tripkit.common.model.TransportMode
import io.reactivex.functions.BiFunction
import org.apache.commons.collections4.CollectionUtils
import java.util.Arrays
import java.util.LinkedList
import java.util.regex.Pattern

// TODO convert to kotlin and add coroutine version.
class ModeCombinationStrategy :
    BiFunction<Map<String, TransportMode>, List<String>, List<Set<String>>> {
    override fun apply(
        modeMap: Map<String, TransportMode>,
        modeIds: List<String>
    ): List<MutableSet<String>> {
        val seenModeIds: MutableSet<String> = HashSet()
        val modeIdSets: MutableList<MutableSet<String>> = LinkedList()

        for (modeId in modeIds) {
            if (seenModeIds.contains(modeId)) {
                continue
            }

            val newSet: MutableSet<String> = HashSet()
            newSet.add(modeId)

            var foundMode: TransportMode?
            // For modes from modeIdentifiers, e.g. `pt_ltd_SCHOOLBUS_2029`, `pt_ltd_SCHOOLBUS_2031`, etc.
            // to remove numeric suffix to get base modeId for checking if exist on modeMap
            val baseModeId = Pattern.compile("_\\d+$").matcher(modeId).replaceAll("")

            // Check if the baseModeId is present in the modeMap
            foundMode = modeMap[baseModeId]
            if (foundMode != null) {
                var shouldMerge = false
                val implies = foundMode.implies?.toList()
                if (CollectionUtils.isNotEmpty(implies)) {
                    newSet.addAll(implies!!)

                    for (imply in implies) {
                        if (seenModeIds.contains(imply)) {
                            shouldMerge = true
                            break
                        }
                    }
                }

                if (shouldMerge) {
                    // For example, newSet is [B, C] and modeIdSets are [A, C] and [D].
                    // Then we have to find [A, C] to merge with [B, C].
                    // [A, C] then will become [A, B, C].
                    // If we don't do so, we may end up duplicate routes.
                    for (existingSet in modeIdSets) {
                        for (imply in implies!!) {
                            if (existingSet.contains(imply)) {
                                existingSet.addAll(newSet)
                                break
                            }
                        }
                    }
                } else {
                    modeIdSets.add(newSet)
                }
            } else {
                modeIdSets.add(newSet)
            }

            seenModeIds.addAll(newSet)
        }

        val multiModal = HashSet(modeIds)
        multiModal.remove(TransportMode.ID_WALK)
        if (multiModal.size > 1) {
            modeIdSets.add(multiModal)
        }

        //Will remove ps_drt and wa_whe hash set since result is just the same with ps_drt mode
        modeIdSets.remove(
            HashSet(
                Arrays.asList(TransportMode.ID_PS_DRT, TransportMode.ID_WHEEL_CHAIR)
            )
        )

        return modeIdSets
    }
}