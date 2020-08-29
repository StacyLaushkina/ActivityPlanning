package com.laushkina.activityplanning

import com.laushkina.activityplanning.model.plan.Plan
import com.laushkina.activityplanning.model.plan.PlanService
import com.laushkina.activityplanning.ui.PlanPresenter
import com.laushkina.activityplanning.ui.PlanView
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Maybe
import io.reactivex.disposables.Disposable
import org.junit.Assert.assertEquals
import org.junit.Test

class PlanPresenterTest {
    private val defaultPlans = listOf(Plan(0, "Test1", 80, 7), Plan(1, "Test1", 80, 7))

    private val view = mock<PlanView>()
    private val service = mock<PlanService> {
        doReturn(Maybe.just(defaultPlans)).`when`(mock).getPlans()
        doReturn(7).`when`(mock).getHoursPerDay(defaultPlans)
    }

    private val presenter = PlanPresenter(view, service)

    @Test
    fun `show plans after loading data in onCreate`() {
        presenter.onCreate()

        verify(view).initPlans(defaultPlans, 7)
    }

    @Test
    fun `update all plans when change hours per day`() {
        val newPlans = listOf(Plan(0, "Test", 80, 1), Plan(0, "Test", 80, 1))

        doReturn(Maybe.just(newPlans)).`when`(service).updateHoursPerDay(defaultPlans, 1)

        presenter.onCreate()
        presenter.onHoursPerDayChanged(1)

        verify(view).updatePlans(newPlans, 1)
        verify(service).updateHoursPerDay(newPlans, 1)
    }

    @Test
    fun `do nothing when change hours per day if there are no plans`() {
        doReturn(Maybe.just(emptyList<Plan>())).`when`(service).getPlans()

        presenter.onCreate()
        presenter.onHoursPerDayChanged(1)

        verify(view, never()).updatePlans(any(), any())
        verify(service, never()).updateHoursPerDay(any(), any())
    }

    @Test
    fun `show create plan dialog when requested and there is place for new activities`() {
        val presenterSpy = spy(presenter)
        doReturn(80).`when`(presenterSpy).getRemainingPercent()
        presenterSpy.onAddRequested()

        verify(view).showAddPlanDialog(80)
    }

    @Test
    fun `do not show create plan dialog when requested if there is no place for new activities`() {
        val presenterSpy = spy(presenter)
        doReturn(0).`when`(presenterSpy).getRemainingPercent()
        presenterSpy.onAddRequested()

        verify(view, never()).showAddPlanDialog(any())
    }

    @Test
    fun `remove plan when requested and update view`() {
        doReturn(mock<Disposable>()).`when`(service).remove(any())
        presenter.onCreate()
        presenter.onRemoveRequested(0)

        verify(service).remove(defaultPlans[0])

        val plansCaptor = argumentCaptor<List<Plan>>()
        verify(view).updatePlans(plansCaptor.capture(), eq(7))
        assertEquals(1, plansCaptor.firstValue.size)
        assertEquals(defaultPlans[1], plansCaptor.firstValue[0])
    }

    @Test
    fun `init plans when create first one`() {
        doReturn(mock<Disposable>()).`when`(service).addOrUpdatePlan(any())

        presenter.onPlanConfirmed("some", 10)

        val newPlanCaptor = argumentCaptor<Plan>()
        verify(service).addOrUpdatePlan(newPlanCaptor.capture())
        assertEquals("some", newPlanCaptor.firstValue.name)
        assertEquals(10, newPlanCaptor.firstValue.percent)

        val plansCaptor = argumentCaptor<List<Plan>>()
        verify(view).initPlans(plansCaptor.capture(), eq(8))
        assertEquals(1, plansCaptor.firstValue.size)
        assertEquals("some", plansCaptor.firstValue[0].name)
        assertEquals(10, plansCaptor.firstValue[0].percent)
    }

    @Test
    fun `update plans when create thirs one`() {
        doReturn(mock<Disposable>()).`when`(service).addOrUpdatePlan(any())
        presenter.onCreate()

        presenter.onPlanConfirmed("some", 10)

        val newPlanCaptor = argumentCaptor<Plan>()
        verify(service).addOrUpdatePlan(newPlanCaptor.capture())
        assertEquals("some", newPlanCaptor.firstValue.name)
        assertEquals(10, newPlanCaptor.firstValue.percent)

        val plansCaptor = argumentCaptor<List<Plan>>()
        verify(view).updatePlans(plansCaptor.capture(), eq(7))
        assertEquals(3, plansCaptor.firstValue.size)
        assertEquals("some", plansCaptor.firstValue[2].name)
        assertEquals(10, plansCaptor.firstValue[2].percent)
        assertEquals(7, plansCaptor.firstValue[2].hoursPerDay)
    }
}