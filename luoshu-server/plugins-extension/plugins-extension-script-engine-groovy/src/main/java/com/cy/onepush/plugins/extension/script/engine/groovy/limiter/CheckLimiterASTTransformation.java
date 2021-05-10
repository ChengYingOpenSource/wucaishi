package com.cy.onepush.plugins.extension.script.engine.groovy.limiter;

import com.sun.management.ThreadMXBean;
import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.stmt.*;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;

import static org.codehaus.groovy.ast.tools.GeneralUtils.*;

@GroovyASTTransformation
public final class CheckLimiterASTTransformation
    extends ClassCodeVisitorSupport implements ASTTransformation {

    private static final ClassNode ANNOTATION_CLASS = ClassHelper.make(CheckLimiter.class);

    protected SourceUnit source;
    private Statement initCallStatement;
    private Statement checkCallStatement;
    private ClassNode currentClass;

    /**
     * Generates Groovy code to call the check function from the script
     *
     * @return a Groovy {@link Statement} that calls this.checkerField.check()
     */
    private static Statement generateCheckStatement() {
        // this.checker.check()
        return stmt(callX(
            propX(varX("this"), LimiterCheck.CHECKER_FIELD),
            "check"
        ));
    }

    /**
     * Generates Groovy code to initialize the checker field
     *
     * @return a Groovy {@link Statement} that will initialize the checker field on this
     */
    private static Statement generateInitStatement() {
        final BlockStatement statement = new BlockStatement();
        statement.addStatements(Arrays.asList(
            // this.checker = new LimiterCheck(ManagementFactory.getThreadMXBean())
            stmt(assignX(
                propX(varX("this"), LimiterCheck.CHECKER_FIELD),
                ctorX(
                    new ClassNode(LimiterCheck.class),
                    args(callX(new ClassNode(ManagementFactory.class), "getThreadMXBean"))
                )
            )),
            // this.checker.setLimiter(limiter)
            stmt(callX(
                propX(varX("this"), LimiterCheck.CHECKER_FIELD),
                "setLimiter",
                args(propX(varX("this"), "limiter"))
            )),
            // this.checker.setScriptBinding(this.getBinding())
            stmt(callX(
                propX(varX("this"), LimiterCheck.CHECKER_FIELD),
                "setScriptBinding",
                args(callX(varX("this"), "getBinding"))
            )),
            // this.checker.init()
            stmt(callX(
                propX(varX("this"), LimiterCheck.CHECKER_FIELD),
                "init"
            ))
        ));
        return statement;
    }

    public void visit(ASTNode[] nodes, SourceUnit source) {
        // Entry point of AST transformation
        ThreadMXBean threadMXBean = ((ThreadMXBean) ManagementFactory.getThreadMXBean());
        if (threadMXBean.isThreadAllocatedMemorySupported()) {
            threadMXBean.setThreadAllocatedMemoryEnabled(true);
        } else if (threadMXBean.isThreadCpuTimeSupported()) {
            threadMXBean.setThreadCpuTimeEnabled(true);
        } else {
            final String message = "Thread allocated memory or cpu time not supported by this JVM. CheckExpression.";
            throw new UnsupportedOperationException(message);
        }

        if (nodes.length != 2 || !(nodes[0] instanceof AnnotationNode) || !(nodes[1] instanceof AnnotatedNode)) {
            throw new GroovyBugError("Expecting [AnnotationNode, AnnotatedNode] but got: " + Arrays.asList(nodes));
        }

        this.source = source;
        AnnotationNode node = (AnnotationNode) nodes[0];

        if (!ANNOTATION_CLASS.equals(node.getClassNode())) {
            throw new GroovyBugError("Transformation called from wrong annotation: " + node.getClassNode().getName());
        }

        setupTransform(node);

        // Should be limited to the current SourceUnit or propagated to the whole CompilationUnit
        final ModuleNode tree = source.getAST();
        // Guard every class and method defined in this script
        if (tree != null) {
            final List<ClassNode> classes = tree.getClasses();
            for (ClassNode classNode : classes) {
                visitClass(classNode);
            }
        }
    }

    @Override
    public void visitClass(ClassNode type) {
        currentClass = type;
        super.visitClass(type);
    }

    @Override
    public void visitAnnotations(AnnotatedNode node) {
        // this transformation does not apply on annotation nodes
        // visiting could lead to stack overflows
    }

    @Override
    public void visitField(FieldNode node) {
        if (!node.isStatic() && !node.isSynthetic()) {
            super.visitField(node);
        }
    }

    @Override
    public void visitProperty(PropertyNode node) {
        if (!node.isStatic() && !node.isSynthetic()) {
            super.visitProperty(node);
        }
    }

    @Override
    public void visitClosureExpression(ClosureExpression closureExpr) {
        Statement code = closureExpr.getCode();
        closureExpr.setCode(wrapBlock(code, checkCallStatement));
        super.visitClosureExpression(closureExpr);
    }

    @Override
    public final void visitDoWhileLoop(DoWhileStatement doWhileStatement) {
        visitLoop(doWhileStatement);
        super.visitDoWhileLoop(doWhileStatement);
    }

    @Override
    public final void visitWhileLoop(WhileStatement whileStatement) {
        visitLoop(whileStatement);
        super.visitWhileLoop(whileStatement);
    }

    @Override
    public final void visitForLoop(ForStatement forStatement) {
        visitLoop(forStatement);
        super.visitForLoop(forStatement);
    }

    @Override
    public void visitMethod(MethodNode node) {
        if (node.getName().equals("run") && currentClass.isScript() && node.getParameters().length == 0) {
            // the run() method will call the checker initialization routine
            Statement code = node.getCode();
            node.setCode(wrapBlock(code, initCallStatement, checkCallStatement));
            super.visitMethod(node);
        } else {
            if (!node.isSynthetic() && !node.isStatic() && !node.isAbstract()) {
                Statement code = node.getCode();
                node.setCode(wrapBlock(code, checkCallStatement));
            }
            if (!node.isSynthetic() && !node.isStatic()) {
                super.visitMethod(node);
            }
        }
    }

    @Override
    protected SourceUnit getSourceUnit() {
        return source;
    }

    /**
     * Shortcut method which avoids duplicating code for every type of loop. Actually wraps the loopBlock of different
     * types of loop statements.
     */
    private void visitLoop(LoopingStatement loopStatement) {
        Statement statement = loopStatement.getLoopBlock();
        loopStatement.setLoopBlock(wrapBlock(statement, checkCallStatement));
    }

    /**
     * This method will create all the necessary metaprogramming infraestructure
     *
     * @param node the annotation node for this transformation
     */
    private void setupTransform(AnnotationNode node) {
//
//        // Read class and method for infringement handler
//        final ClassExpression infringementHandlerClass = (ClassExpression) node.getMember("handlerClass");
//        final String infringementHandlerName = (String) ((ConstantExpression) node.getMember("handlerMethod"))
//            .getValue();

        // Build statements that can be called from Groovy
        initCallStatement = generateInitStatement();

        checkCallStatement = generateCheckStatement();
    }

    private Statement wrapBlock(Statement wrapped, Statement... added) {
        BlockStatement stmt = new BlockStatement();
        for (Statement item : added) {
            stmt.addStatement(item);
        }
        stmt.addStatement(wrapped);
        return stmt;
    }
}