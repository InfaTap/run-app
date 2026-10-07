package com.example.run.domain

import com.example.core.domain.LocationDataCalculator
import com.example.core.domain.Timer
import com.example.core.domain.location.LocationTimestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class RunningTracker(
    private val locationObserver: LocationObserver,
    private val applicationScope: CoroutineScope
) {
    private val isTracking = MutableStateFlow(false)
    private val isObserving = MutableStateFlow(false)

    private val _runData = MutableStateFlow(RunData())
    val runData = _runData.asStateFlow()

    private val _elapsedTime = MutableStateFlow(Duration.ZERO)
    val elapsedTime = _elapsedTime.asStateFlow()





    @OptIn(ExperimentalCoroutinesApi::class)
    val currentLocation = isObserving.flatMapLatest { isObserving ->
        if (isObserving) {
            locationObserver.observeLocation(1000L)
        } else {
            flowOf()
        }

    }.stateIn(
        applicationScope,
        SharingStarted.Lazily,
        null
    )

    init {
        isTracking
            .flatMapLatest { isTracking ->
                if (isTracking){
                    Timer.timeAndEmit()
                }else flowOf()
            }.onEach {
                _elapsedTime.value += it
            }.launchIn(applicationScope)


        currentLocation
            .filterNotNull()
            .combineTransform(isTracking){location, isTracking ->
                if (isTracking){
                    emit(location)
                }
            }.zip(_elapsedTime) {location, elapsedTime ->
                LocationTimestamp(
                    location = location,
                    durationTimestamp = elapsedTime
                )
            }.onEach { location ->
                val currentLocation = runData.value.locations
                val lastLocationsList = if (currentLocation.isNotEmpty()){
                    currentLocation.last() + location
                } else {
                    listOf(location)
                }
                val newLocationList = currentLocation.replaceLast(lastLocationsList)
                val distanceInMeter = LocationDataCalculator.getDistanceInMeters(newLocationList)

                val distanceKm = distanceInMeter / 1000.0
                val currentDuration = location.durationTimestamp

                val avgPerSecondsKm = if (distanceKm == 0.0){
                    0
                } else {
                    (currentDuration.inWholeSeconds / distanceKm).roundToInt()
                }
                _runData.update {
                    RunData(
                        distanceMeters = distanceInMeter,
                        pace = avgPerSecondsKm.seconds,
                        locations = newLocationList
                    )
                }
            }.launchIn(applicationScope)

    }

    fun setIsTracking(isTracking: Boolean){
        this.isTracking.value = isTracking
    }

    fun startObserving() {
        isObserving.value = true
    }
    fun stopObserving(){
        isObserving.value = false
    }

    private fun <T> List<List<T>>.replaceLast(replacement: List<T>) : List<List<T>>{
        return if (this.isEmpty()){
            listOf(replacement)
        } else {
            this.dropLast(1) + listOf(replacement)
        }
    }
}

