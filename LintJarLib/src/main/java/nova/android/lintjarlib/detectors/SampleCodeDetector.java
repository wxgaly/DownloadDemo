package nova.android.lintjarlib.detectors;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Detector.UastScanner;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import org.jetbrains.uast.UElement;
import org.jetbrains.uast.ULiteralExpression;
import org.jetbrains.uast.UastLiteralUtils;

import java.util.Collections;
import java.util.List;

import static nova.android.lintjarlib.NovaCategory.CODING_CONVENTION;

/**
 * nova.android.lintlib.
 *
 * @author Created by WXG on 2018/10/17 017 18:20.
 * @version V1.0
 */
public class SampleCodeDetector extends NovaBasicDetector implements UastScanner {
    /** Issue describing the problem and pointing to the detector implementation */
    public static final Issue ISSUE = Issue.create(
            // ID: used in @SuppressLint warnings etc
            "ShortUniqueId",

            // Title -- shown in the IDE's preference dialog, as category headers in the
            // Analysis results window, etc
            "Lint Mentions",

            // Full explanation of the issue; you can use some markdown markup such as
            // `monospace`, *italic*, and **bold**.
            "This check highlights string literals in code which mentions " +
                    "the word `lint`. Blah blah blah.\n" +
                    "\n" +
                    "Another paragraph here.\n",
            CODING_CONVENTION,
            6,
            Severity.WARNING,
            new Implementation(
                    SampleCodeDetector.class,
                    Scope.JAVA_FILE_SCOPE));

    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        return Collections.singletonList(ULiteralExpression.class);
    }

    @Override
    public UElementHandler createUastHandler(JavaContext context) {
        // Not: Visiting UAST nodes is a pretty general purpose mechanism;
        // Lint has specialized support to do common things like "visit every class
        // that extends a given super class or implements a given interface", and
        // "visit every call site that calls a method by a given name" etc.
        // Take a careful look at UastScanner and the various existing lint check
        // implementations before doing things the "hard way".
        // Also be aware of context.getJavaEvaluator() which provides a lot of
        // utility functionality.
        return new UElementHandler() {
            @Override
            public void visitLiteralExpression(ULiteralExpression expression) {
                String string = UastLiteralUtils.getValueIfStringLiteral(expression);

                if (string == null) {
                    return;
                }

                if (string.contains("lint") && string.matches(".*\\blint\\b.*")) {
                    context.report(ISSUE, expression, context.getLocation(expression),
                            "This code mentions `lint`: **Congratulations**");
                }
            }
        };
    }
}
