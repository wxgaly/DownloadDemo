package nova.android.androidtester;

import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * nova.android.androidtester.
 *
 * @author Created by WXG on 2018/5/10 010 10:47.
 * @version V1.0
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class ActivityTest {

    @Mock
    private ArrayList mockList;

    public ActivityTest() {
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp() throws Exception {

        // Injecting the Instrumentation instance is required
        // for your test to run with AndroidJUnitRunner.
//        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
//        mActivity = getActivity();

        //mock creation
//        mockList = mock(ArrayList.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void typeOperandsAndPerformAddOperation() {
        // Call the CalculatorActivity add() method and pass in some operand values, then
        // check that the expected value is returned.

        //使用mock对象执行方法
//        mockList.add("one");
//        mockList.clear();
//
//        //检验方法是否调用
//        verify(mockList).add("one1");
//        verify(mockList).clear();

        // you can mock concrete classes, not only interfaces
//        LinkedList mockedList = mock(LinkedList.class);

// stubbing appears before the actual execution
//        when(mockedList.get(0)).thenReturn("first");

// the following prints "first"
//        System.out.println(mockedList.get(0));
//
//// the following prints "null" because get(999) was not stubbed
//        System.out.println(mockedList.get(999));
//        verify(mockedList).add("first");

        when(mockList.add(anyString())).thenAnswer((Answer<Boolean>) invocation -> {
            Object[] args = invocation.getArguments();
            Object mock = invocation.getMock();
            return false;
        });
        System.out.println(mockList.add("第1次返回false"));

        //lambda表达式
        when(mockList.add(anyString())).then(invocation -> true);
        System.out.println(mockList.add("第2次返回true"));

        when(mockList.add(anyString())).thenReturn(false);
        System.out.println(mockList.add("第3次返回false"));

    }

    @Test
    public void testRetrofit() {

    }

    @After
    public void tearDown() throws Exception {
//        super.tearDown();
    }

}
