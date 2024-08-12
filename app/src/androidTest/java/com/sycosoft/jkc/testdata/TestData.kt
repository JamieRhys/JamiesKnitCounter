package com.sycosoft.jkc.testdata

import com.sycosoft.jkc.database.entities.Counter
import com.sycosoft.jkc.database.entities.Project

class TestData {
    companion object {
        val validProject = Project(name = "Test Project", description = "Test Description")
        val validCounter = Counter(name = "Test Counter", owningPartId = 1L)
    }
}