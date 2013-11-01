package com.liferay.portal.test.annotations;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.liferay.portal.test.lifecycle.JUnitLifecycleTest;
 
@RunWith(Suite.class)
@Suite.SuiteClasses({
	JUnitLifecycleTest.class,
	JUnitParametersTest.class
})

public class JUnitSuiteTest {
}
