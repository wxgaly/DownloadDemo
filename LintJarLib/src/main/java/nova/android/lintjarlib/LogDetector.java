package nova.android.lintjarlib;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.SourceCodeScanner;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UCallExpression;

import java.util.Arrays;
import java.util.List;

/**
 * nova.android.lintlib.
 *
 * @author Created by WXG on 2018/10/17 017 18:38.
 * @version V1.0
 */
public class LogDetector extends Detector implements SourceCodeScanner {

    public static final Issue ISSUE = Issue.create(
            "LogUse",
            "����ʹ��Log/System.out.println",
            "ʹ��Logger������־��¼���ļ��з����������",
            Category.SECURITY, 6, Severity.ERROR,
            new Implementation(LogDetector.class, Scope.JAVA_FILE_SCOPE));


    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList("v", "d", "i", "w", "e", "wtf", "println");
    }


    @Override
    public void visitMethod(@NotNull JavaContext context,
                            @NotNull UCallExpression call,
                            @NotNull PsiMethod method) {
        if (context.getEvaluator().isMemberInClass(method, "android.util.Log")) {
            context.report(ISSUE, call, context.getLocation(call), "��ʹ��Logger������ʹ��log");
        }

        if(context.getEvaluator().isMemberInClass(method, "java.io.PrintStream")){
            context.report(ISSUE, call, context.getLocation(call), "��ʹ��Logger������ʹ��system.out.println");
        }
    }

}
