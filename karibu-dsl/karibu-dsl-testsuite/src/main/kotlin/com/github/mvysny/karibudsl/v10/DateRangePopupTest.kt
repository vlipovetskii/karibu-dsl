package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.kaributesting.v10.*
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dialog.Dialog
import java.time.LocalDate
import kotlin.test.*

@DynaTestDsl
fun DynaNodeGroup.dateRangePopupTest() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }
    lateinit var component: DateRangePopup
    beforeEach { component = DateRangePopup() }

    test("Initial value is null") {
        expect(null) { component.value }
    }

    test("dsl") {
        UI.getCurrent().apply {
            dateRangePopup()
        }
        _expectOne<DateRangePopup>()
    }

    test("setting the value preserves the value") {
        component.value = DateInterval(LocalDate.of(2010, 1, 1), LocalDate.of(2012, 1, 1))
        expect(DateInterval(LocalDate.of(2010, 1, 1), LocalDate.of(2012, 1, 1))) { component.value!! }
    }

    group("value change listener tests") {
        test("Setting to the same value does nothing") {
            component.addValueChangeListener {
                fail("should not be fired")
            }
            component.value = null
        }

        test("Setting the value programatically triggers value change listeners") {
            lateinit var newValue: DateInterval
            component.addValueChangeListener {
                expect(false) { it.isFromClient }
                expect(null) { it.oldValue }
                newValue = it.value
            }
            component.value = DateInterval(LocalDate.of(2010, 1, 1), LocalDate.of(2012, 1, 1))
            expect(DateInterval(LocalDate.of(2010, 1, 1), LocalDate.of(2012, 1, 1))) { newValue }
        }

        test("value change won't trigger unregistered change listeners") {
            component.addValueChangeListener {
                fail("should not be fired")
            } .remove()
            component.value = DateInterval(LocalDate.of(2010, 1, 1), LocalDate.of(2012, 1, 1))
        }
    }

    group("popup tests") {
        beforeEach {
            // open popup
            component.isDialogVisible = true
            _get<Dialog>() // expect the dialog to pop up
        }

        test("Clear does nothing when the value is already null") {
            component.addValueChangeListener {
                fail("No listener must be fired")
            }
            _get<Button> { text = "Clear" } ._click()
            expect(null) { component.value }
            _expectNone<Dialog>()  // the Clear button must close the dialog
        }

        test("setting the value while the dialog is opened propagates the values to date fields") {
            component.value = DateInterval(LocalDate.of(2010, 1, 1), LocalDate.of(2012, 1, 1))
            expect(LocalDate.of(2010, 1, 1)) { _get<DatePicker> { label = "From (inclusive):" } .value }
            expect(LocalDate.of(2012, 1, 1)) { _get<DatePicker> { label = "To (inclusive):" } .value }
        }

        test("Clear properly sets the value to null") {
            component.value = DateInterval.now()
            var wasCalled = false
            component.addValueChangeListener {
                expect(null) { it.value }
                wasCalled = true
            }
            _get<Button> { text = "Clear" } ._click()
            expect(true) { wasCalled }
            expect(null) { component.value }
            _expectNone<Dialog>()  // the Clear button must close the dialog
        }

        test("Set properly sets the value to null if nothing is filled in") {
            component.value = DateInterval.now()
            var wasCalled = false
            component.addValueChangeListener {
                expect(null) { it.value }
                wasCalled = true
            }
            _get<DatePicker> { label = "From (inclusive):" } .value = null
            _get<DatePicker> { label = "To (inclusive):" } .value = null
            _get<Button> { text = "Set" } ._click()
            expect(true) { wasCalled }
            expect(null) { component.value }
            _expectNone<Dialog>()  // the Set button must close the dialog
        }

        test("Set properly sets the value in") {
            var wasCalled = false
            component.addValueChangeListener {
                expect(DateInterval(LocalDate.of(2010, 11, 1), LocalDate.of(2012, 1, 1))) { it.value }
                wasCalled = true
            }
            _get<DatePicker> { label = "From (inclusive):" } .value = LocalDate.of(2010, 11, 1)
            _get<DatePicker> { label = "To (inclusive):" } .value = LocalDate.of(2012, 1, 1)
            _get<Button> { text = "Set" } ._click()
            expect(true) { wasCalled }
            expect(DateInterval(LocalDate.of(2010, 11, 1), LocalDate.of(2012, 1, 1))) { component.value }
            _expectNone<Dialog>()  // the Set button must close the dialog
        }
    }
}
