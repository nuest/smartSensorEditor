/**
 * Copyright (C) 2014-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.smartsensoreditor.dao;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.w3c.dom.Document;

import de.conterra.smarteditor.cswclient.ext.header.PolicyMap;
import de.conterra.smarteditor.dao.CatalogServiceDAO;
import de.conterra.smarteditor.util.XPathUtil;
import de.conterra.smarteditor.xml.EditorContext;

/**
 * Access object to the catalog service using the globally defined editorContext
 * 
 * @author Jana
 */
public class EditorAwareCatalogServiceDAO extends CatalogServiceDAO {

	private EditorContext editorContext;

	public EditorContext getEditorContext() {
		return editorContext;
	}

	public void setEditorContext(EditorContext editorContext) {
		this.editorContext = editorContext;
	}

    /**
     * Return a list of existing draft names for the given user using the global editor context for evaluating XPaths
     *
     * @return Map consisting of <fileIdentifier, draftname> or null, if no draft is found
     */
	@Override
    public Map<String, String> getDraftNames()
            throws de.conterra.smarteditor.cswclient.exception.InvokerException, java.rmi.RemoteException {
        Map<String, String> lMap = null;
        List<Document> lDraftRecords = this.getRecords("*",
                getMaxNumberOfDrafts(),
                "/filter/anyType.xml",
                PolicyMap.getActionString("discoveryDraft"));
        if (lDraftRecords != null) {
            lMap = new TreeMap<String, String>();
            // filter fileId and Title
            XPathUtil lPathUtil = new XPathUtil();
            lPathUtil.setContext(editorContext);
            for (Document lDocument : lDraftRecords) {
                String lFileId = lPathUtil.evaluateAsString("//gmd:fileIdentifier[1]/*/text()", lDocument);
                String lTitle = lPathUtil.evaluateAsString("//gmd:title[1]/*/text()", lDocument);
                lMap.put(lFileId, lTitle);
            }
        }
        return lMap;
    }


    
}
