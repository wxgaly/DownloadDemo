package wxgaly.android.lintlib;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;

import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Collections;

/**
 * wxgaly.android.lintlib.
 *
 * @author Created by WXG on 2018/10/16 016 11:58.
 * @version V1.0
 */
public class WxgalyDetector extends ResourceXmlDetector {

    public static final Issue ISSUE = Issue.create(
            "MyId",
            "My brief summary of the issue",
            "My longer explanation of the issue",
            Category.CORRECTNESS, 6, Severity.WARNING,
            new Implementation(WxgalyDetector.class, Scope.RESOURCE_FILE_SCOPE));

    @Override
    public Collection<String> getApplicableElements() {
        return Collections.singletonList(
                "wxgaly.android.downloaddemo.view.CustomTextView");
    }

    @Override
    public void visitElement(XmlContext context, Element element) {
        if (!element.hasAttributeNS(
                "http://schemas.android.com/apk/res/wxgaly.android.downloaddemo.view",
                "exampleString")) {
            context.report(ISSUE, element, context.getLocation(element),
                    "Missing required attribute 'exampleString'");
        }
    }

}
