package com.laushkina.activityplanning.model.track

import com.laushkina.activityplanning.model.plan.Plan
import com.laushkina.activityplanning.repository.PlanRepository
import com.laushkina.activityplanning.repository.TrackRepository
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class TrackService(
    private val trackRepository: TrackRepository,
    private val planRepository: PlanRepository
) {

    fun startTracking(): Maybe<List<Track>> {
        return planRepository.get()
            .flatMap { plans: List<Plan> -> createTracksForPlans(plans) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun finishTracking(date: Date): Maybe<List<Track>> {
        return trackRepository.get(date)
            .map { tracks: List<Track> -> markTracksAsFinished(tracks) }
            .map { tracks: List<Track> -> trackRepository.insertAll(tracks) }
            .flatMap { trackRepository.get(Date()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun markTracksAsFinished(tracks: List<Track>): List<Track> {
        tracks.map { track: Track ->
            track.startTime?.let {
                track.duration += System.currentTimeMillis() - track.startTime!!
            }
            track.isFinished = true
        }
        return tracks
    }

    fun getUnfinishedTracks(date: Date): Maybe<List<Track>> {
        return trackRepository.get(date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getTracks(date: Date): Maybe<List<Track>> {
        return trackRepository.get(date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun updateTrack(track: Track): Maybe<List<Track>> {
        return Maybe.just(track)
            .map { trackRepository.insert(track) }
            .flatMap { trackRepository.get(Date()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun createTracksForPlans(plans: List<Plan>): Maybe<List<Track>> {
        val tracks = mutableListOf<Track>()
        for (plan in plans) {
            tracks.add(
                Track(
                    Random().nextInt(),
                    plan,
                    startTime = null,
                    duration = 0,
                    isInProgress = false,
                    isFinished = false,
                    date = Date()
                )
            )
        }
        trackRepository.insertAll(tracks)
        return trackRepository.get(Date())
    }

    companion object {
        fun getTimeDiff(track: Track): Long {
            val startTime = track.startTime ?: return 0

            val currentProgress = if (track.isInProgress && !track.isFinished) {
                System.currentTimeMillis() - startTime
            } else 0
            return track.duration + currentProgress
        }

        fun getPlanningTimeMillis(plan: Plan): Long {
            val expectedMinutes = TimeUnit.HOURS.toMillis(plan.hoursPerDay.toLong())
            val percent = plan.percent * 0.01
            return (percent * expectedMinutes).toLong()
        }
    }
}