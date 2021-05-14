package org.devgateway.toolkit.forms.wicket.components.table;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Nadejda Mandrescu
 */
public class PagingNavigationFactory implements Serializable {
    private static final long serialVersionUID = -1256448309558080826L;

    private final Class<? extends PagingNavigation> pagingClass;

    private final Object outerInstance;

    public PagingNavigationFactory(final Class<? extends PagingNavigation> pagingClass) {
        this(pagingClass, null);
    }

    public PagingNavigationFactory(final Class<? extends PagingNavigation> pagingClass, final Object outerInstance) {
        this.pagingClass = pagingClass;
        this.outerInstance = outerInstance;
    }

    public PagingNavigation newNavigation(final String id, final IPageable pageable,
            final IPagingLabelProvider labelProvider) throws Exception {
        if (outerInstance != null) {
            return pagingClass
                    .getConstructor(outerInstance.getClass(), String.class, IPageable.class, IPagingLabelProvider.class)
                    .newInstance(outerInstance, id, pageable, labelProvider);
        }
        return pagingClass.getConstructor(String.class, IPageable.class, IPagingLabelProvider.class)
                .newInstance(id, pageable, labelProvider);
    }
}
