/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.forms.util;


import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.cache.Cache;
import javax.cache.CacheManager;
import java.util.Collection;

/**
 * @author idobre
 * @since 3/3/16
 * <p>
 * Class the removes the cache created in
 * org.devgateway.ccrs.web.wicket.page.reports.AbstractReportPage#ResourceStreamPanel#getCacheKey
 * function
 */
@Component
public class MarkupCacheService {

    @Autowired
    private CacheManager cm;

    /**
     * start-key used to identify the reports markup
     */
    private static final String START_NAME_REPORT_KEY = "REPORTMARKUP";

    /**
     * Flush markup cache for reports page
     */
    public final void flushMarkupCache() {
        final MarkupCache markupCacheClass = (MarkupCache) MarkupCache.get();
        final MarkupCache.ICache<String, Markup> markupCache = markupCacheClass.getMarkupCache();
        final Collection<String> keys = markupCache.getKeys();
        for (String key : keys) {
            // The key for reports markup cache contains the class name so it
            // should end in "ReportPage"
            if (key.startsWith(START_NAME_REPORT_KEY)) {
                markupCacheClass.removeMarkup(key);
            }
        }
    }

    /**
     * Add the content of a report (PDF, Excel, RTF) to cache
     *
     * @param outputType
     * @param reportName
     * @param parameters
     * @param buffer
     */
    public void addPentahoReportToCache(final String outputType, final String reportName, final String parameters,
                                        final byte[] buffer) {

        // get the reports cache "reportsCache", declared in ehcache.xml
        final Cache<String, byte[]> cache = cm.getCache("reportsCache", String.class, byte[].class);

        cache.put(createCacheKey(outputType, reportName, parameters), buffer);
    }

    /**
     * Fetch the content of a report from cache
     *
     * @param outputType
     * @param reportName
     * @param parameters
     * @return
     */
    public byte[] getPentahoReportFromCache(final String outputType, final String reportName, final String parameters) {

        // get the reports cache "reportsCache", declared in ehcache.xml
        final Cache<String, byte[]> cache = cm.getCache("reportsCache", String.class, byte[].class);

        final String key = createCacheKey(outputType, reportName, parameters);

        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        return null;
    }

    /**
     * Remove from cache all reports content
     */
    public void clearPentahoReportsCache() {

        // get the reports cache "reportsCache", declared in ehcache.xml
        final Cache<Object, Object> cache = cm.getCache("reportsCache");

        if (cache != null) {
            cache.removeAll();
        }
    }

    /**
     * Remove from cache all APIs/Services content.
     */
    public void clearAllCaches() {

        // get the reports cache "reportsApiCache", declared in ehcache.xml
        final Cache<Object, Object> cache = cm.getCache("reportsApiCache");
        if (cache != null) {
            cache.removeAll();
        }

        // get the reports cache "excelExportCache", declared in ehcache.xml
        final Cache<Object, Object> excelExportCache = cm.getCache("excelExportCache");
        if (excelExportCache != null) {
            excelExportCache.removeAll();
        }
    }

    private String createCacheKey(final String outputType, final String reportName, final String parameters) {
        return reportName + "-" + parameters + "-" + outputType;
    }
}
