package org.devgateway.toolkit.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

/**
 * Checks that the methods of all classes annotated with @CacheableHibernateQueryResult
 * are explicitly annotated to cache or not the query result.
 *
 * Anytime a new method is defined this will catch a potential missing caching definition.
 *
 * @author Nadejda Mandrescu
 */
public class CachableQueryAnnotationCheck extends AbstractCheck {
    private static final String ERROR_MESSAGE =
            "@CacheableHibernateQueryResult must annotate its methods explicitly either with " +
            "@CacheHibernateQueryResult or @NoCacheHibernateQueryResult";

    @Override
    public int[] getDefaultTokens() {
        return new int[]{TokenTypes.CLASS_DEF, TokenTypes.INTERFACE_DEF};
    }

    @Override
    public void visitToken(DetailAST ast) {
        if (AnnotationUtility.containsAnnotation(ast, "CacheableHibernateQueryResult")) {
            DetailAST objBlock = ast.findFirstToken(TokenTypes.OBJBLOCK);
            DetailAST methodDef = objBlock.findFirstToken(TokenTypes.METHOD_DEF);
            while (methodDef != null) {
                if (methodDef.getType() == TokenTypes.METHOD_DEF) {
                    if (!(AnnotationUtility.containsAnnotation(methodDef, "CacheHibernateQueryResult")
                            || AnnotationUtility.containsAnnotation(methodDef, "NoCacheHibernateQueryResult"))) {
                        log(methodDef.getLineNo(), ERROR_MESSAGE);
                    }
                }
                methodDef = methodDef.getNextSibling();
            }
        }
    }
}
