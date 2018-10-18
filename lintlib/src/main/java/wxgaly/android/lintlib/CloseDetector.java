package wxgaly.android.lintlib;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Position;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.SourceCodeScanner;
import com.intellij.psi.PsiMethod;

import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UExpression;
import org.jetbrains.uast.UTryExpression;
import org.jetbrains.uast.UastUtils;

import java.util.Collections;
import java.util.List;


/**
 * wxgaly.android.lintlib.
 *
 * @author Created by WXG on 2018/10/17 017 13:49.
 * @version V1.0
 */
public class CloseDetector extends Detector implements SourceCodeScanner {//implements Detector.JavaPsiScanner
    public static Issue ISSUE = Issue.create(
            "CloseMethod",
            "close method should used in finally block",
            "close method should used in finally block, prevent memory leaks",
            // 这个主要是用于对问题的分类，不同的问题就可以集中在一起显示。
            Category.CORRECTNESS,
            // 优先级
            6,
            // 定义查找问题的严重级别
            Severity.ERROR,
            // 提供处理该问题的Detector和该Detector所关心的资源范围。当系统生成了抽象语法树（Abstract syntax
            // tree，简称AST），或者遍历xml资源时，就会调用对应Issue的处理器Detector。
            new Implementation(CloseDetector.class,
                    Scope.JAVA_FILE_SCOPE)
    );
    // 限定关心的方法的调用类
    public static final String[] sSupportSuperType = new String[]{
            "java.io.InputStream", "java.io.OutputStream", "android.database.Cursor"
    };

    /**
     * 只关心名是close的方法
     *
     * @return
     */
    @Override
    public List<String> getApplicableMethodNames() {
        return Collections.singletonList("close");
    }


    @Override
    public void visitMethod(@NonNull JavaContext context,
                            @NonNull UCallExpression call,
                            @NonNull PsiMethod method) {
        boolean isSubClass = false;
        for (int i = 0; i < sSupportSuperType.length; i++) {
            if (!context.getEvaluator().isMemberInClass(method, sSupportSuperType[i])) {
                isSubClass = true;
                break;
            }
        }

        if (!isSubClass) {
            super.visitMethod(context, call, method);
        }


        UElement uElement = call.getUastParent();
        if (uElement != null) {
            UTryExpression parent = UastUtils.getParentOfType(uElement, UTryExpression.class, true);
            if (parent != null) {
                int firstLine = 0;
                Position start = context.getLocation(parent).getStart();
                if (start != null) {
                    firstLine = start.getLine();
                }
                System.out.println("================================  firstLine = " + firstLine);

                UExpression finallyClause = parent.getFinallyClause();

                if (finallyClause != null) {
                    int sLine = 0;
                    Position finallyStart = context.getLocation(finallyClause).getStart();
                    if (finallyStart != null) {
                        sLine = finallyStart.getLine();
                        System.out.println("================================  sLine = " + sLine);
                    }

//                    if (firstLine < sLine) {
//                        context.report(ISSUE, call, context.getLocation(call), "please use close method in finally");
//                    }

                }
            }
        }
        context.report(ISSUE, call, context.getLocation(call), "please use close method in finally");
    }

    /**
     * 该方法调用时，会传入代表close方法被调用的节点MethodInvocation,以及所在java文件的上下文JavaContext，
     * 还有AstVisitor。由于我们没有重写createJavaVisitor方法，所以不用管AstVisitor。
     * MethodInvocation封装了close被调用处的代码，而结合JavaContext对象，即可寻找对应的上下文，来帮助我们判断条件。
     *
     * @param context
     * @param visitor
     * @param node
     */
//    @Override
//    public void visitMethod(JavaContext context, AstVisitor visitor, MethodInvocation node) {
//        // 判断类型,看下所监测的资源是否是我们定义的相关资源
//        // 通过JavaContext的resolve的方法,传入node节点,由于所有的AST树上的节点都继承自NODE,所以可以通过node去找到class
//        JavaParser.ResolvedMethod method = (JavaParser.ResolvedMethod) context.resolve(node);
//        JavaParser.ResolvedClass clzz = method.getContainingClass();
//        System.out.println("-------------- visitMethod ------------");
//        boolean isSubClass = false;
//        for (int i = 0; i < sSupportSuperType.length; i++) {
//            if (clzz.isSubclassOf(sSupportSuperType[i], false)) {
//                isSubClass = true;
//                break;
//            }
//        }
//        if (!isSubClass) super.visitMethod(context, visitor, node);
//        /**
//         * 查找try和block的信息
//         * 在AST中，close 代码节点应该是try的一个子孙节点（try是语法上的block），所以从close代码节点向上追溯，
//         * 可以找到对应的try，而Node对象本来就有getParent方法，所以可以递归调用该方法来找到Try节点（这也是一个节点），
//         * 或者调用JavaContext的查找限定parent类型的方法:
//         */
//        Try fTryBlock = context.getParentOfType(node, Try.class);
//        int fLineNum = context.getLocation(fTryBlock).getStart().getLine();
//        System.out.println("    fLineNum=" + fLineNum);
//
//        /**
//         * 如果close在try模块中,接着就要对try进行向上查找,看try是否被包裹在try中,同时,是否处于finally中,try节点
//         * 有一个astFinally的方法,可以得到finally的节点,只要判断节点的位置,既可以实现判断close是否在finally中
//         */
//        Try sTryBlock = context.getParentOfType(fTryBlock, Try.class);
//        Block finaBlock = sTryBlock.astFinally();
//        int sLineNum = context.getLocation(finaBlock).getStart().getLine();
//        System.out.println("    sLineNum=" + sLineNum);
//        /**
//         * 若我们确定了close是在try 块中，且try块不在finally里，那么就需要触发Issue，这样在html报
//         * 告中就可以找到对应的信息了
//         * 一个莫名的bug,不能再这里写成
//         * if (fLineNum < sLineNum){
//         *     context.report(ISSUE, node, context.getLocation(node), "请在finally中调用close");
//         * }
//         * 否则再直接运行Analyze下Inspect选项后,在inspection窗口中没有办法看到Error from custom lint Check
//         * 的错误信息
//         */
//        if (fLineNum > sLineNum) {
//            return;
//        } else {
//            context.report(ISSUE, node, context.getLocation(node), "please use close method in finally");
//        }
//    }

}
