/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portlet.dynamicdatamapping.lar;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.BasePortletDataHandler;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.PortletDataHandlerBoolean;
import com.liferay.portal.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.portal.kernel.lar.StagedModelType;
import com.liferay.portal.kernel.lar.xstream.XStreamAliasRegistryUtil;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSet;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.model.impl.DDMStructureImpl;
import com.liferay.portlet.dynamicdatamapping.model.impl.DDMTemplateImpl;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletPreferences;

/**
 * @author Marcellus Tavares
 * @author Juan Fernández
 * @author Steven Smith
 */

public class DDMPortletDataHandler extends BasePortletDataHandler {

	public static final String NAMESPACE = "dynamic_data_mapping";

	public static ActionableDynamicQuery getDDMStructureActionableDynamicQuery(
		final PortletDataContext portletDataContext,
		final List<DDMTemplate> ddmTemplates) {

		ExportActionableDynamicQuery exportActionableDynamicQuery =
			DDMStructureLocalServiceUtil.getExportActionableDynamicQuery(
				portletDataContext);

		final ActionableDynamicQuery.AddCriteriaMethod addCriteriaMethod =
			exportActionableDynamicQuery.getAddCriteriaMethod();

		exportActionableDynamicQuery.setAddCriteriaMethod(
			new ActionableDynamicQuery.AddCriteriaMethod() {

				@Override
				public void addCriteria(DynamicQuery dynamicQuery) {
					addCriteriaMethod.addCriteria(dynamicQuery);

					Property classNameIdProperty = PropertyFactoryUtil.forName(
						"classNameId");

					long classNameId = PortalUtil.getClassNameId(
						DDLRecordSet.class);

					dynamicQuery.add(classNameIdProperty.eq(classNameId));
				}

			});
		exportActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod() {

				@Override
				public void performAction(Object object)
					throws PortalException {

					DDMStructure ddmStructure = (DDMStructure)object;

					StagedModelDataHandlerUtil.exportStagedModel(
						portletDataContext, ddmStructure);

					try {
						ddmTemplates.addAll(ddmStructure.getTemplates());
					}
					catch (SystemException se) {
					}
				}

			});
		exportActionableDynamicQuery.setStagedModelType(
			new StagedModelType(
				DDMStructure.class.getName(), DDLRecordSet.class.getName()));

		return exportActionableDynamicQuery;
	}

	public static ActionableDynamicQuery getDDMStructureActionableDynamicQuery(
		final PortletDataContext portletDataContext,
		final List<DDMTemplate> ddmTemplates, final boolean export) {

		ExportActionableDynamicQuery exportActionableDynamicQuery =
			DDMStructureLocalServiceUtil.getExportActionableDynamicQuery(
				portletDataContext);

		final ActionableDynamicQuery.AddCriteriaMethod addCriteriaMethod =
			exportActionableDynamicQuery.getAddCriteriaMethod();

		exportActionableDynamicQuery.setAddCriteriaMethod(
			new ActionableDynamicQuery.AddCriteriaMethod() {

				@Override
				public void addCriteria(DynamicQuery dynamicQuery) {
					addCriteriaMethod.addCriteria(dynamicQuery);

					Property classNameIdProperty = PropertyFactoryUtil.forName(
						"classNameId");

					long classNameId = PortalUtil.getClassNameId(
						JournalArticle.class);

					dynamicQuery.add(classNameIdProperty.eq(classNameId));
				}

			});
		exportActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod() {

				@Override
				public void performAction(Object object)
					throws PortalException {

					DDMStructure ddmStructure = (DDMStructure)object;

					if (export) {
						StagedModelDataHandlerUtil.exportStagedModel(
							portletDataContext, ddmStructure);
					}

					try {
						List<DDMTemplate> ddmStructureDDMTemplates =
							DDMTemplateLocalServiceUtil.getTemplatesByClassPK(
								ddmStructure.getGroupId(),
								ddmStructure.getStructureId());

						ddmTemplates.addAll(ddmStructureDDMTemplates);
					}
					catch (SystemException se) {
					}
				}

			});
		exportActionableDynamicQuery.setStagedModelType(
			new StagedModelType(
				DDMStructure.class.getName(), JournalArticle.class.getName()));

		return exportActionableDynamicQuery;
	}

	public static ActionableDynamicQuery getDDMTemplateActionableDynamicQuery(
		final PortletDataContext portletDataContext,
		final List<DDMTemplate> ddmTemplates, final boolean export) {

		ExportActionableDynamicQuery exportActionableDynamicQuery =
				DDMTemplateLocalServiceUtil.getExportActionableDynamicQuery(
					portletDataContext);

		final ActionableDynamicQuery.AddCriteriaMethod addCriteriaMethod =
			exportActionableDynamicQuery.getAddCriteriaMethod();

		exportActionableDynamicQuery.setAddCriteriaMethod(
			new ActionableDynamicQuery.AddCriteriaMethod() {

				@Override
				public void addCriteria(DynamicQuery dynamicQuery) {
					addCriteriaMethod.addCriteria(dynamicQuery);

					Property classNameIdProperty = PropertyFactoryUtil.forName(
						"classNameId");

					long classNameId = PortalUtil.getClassNameId(
						DDMStructure.class);

					dynamicQuery.add(classNameIdProperty.eq(classNameId));
				}

			});
		exportActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod() {

				@Override
				public void performAction(Object object)
					throws PortalException {

					DDMTemplate ddmTemplate = (DDMTemplate)object;

					if (ddmTemplate.getClassPK() != 0) {
						DDMStructure ddmStructure =
							DDMStructureLocalServiceUtil.fetchDDMStructure(
								ddmTemplate.getClassPK());

						long classNameId = PortalUtil.getClassNameId(
							JournalArticle.class);

						if ((ddmStructure != null) &&
							(ddmStructure.getClassNameId() != classNameId)) {

							return;
						}
					}

					if (export) {
						StagedModelDataHandlerUtil.exportStagedModel(
							portletDataContext, ddmTemplate);
					}

					ddmTemplates.remove(ddmTemplate);
				}

			});
		exportActionableDynamicQuery.setStagedModelType(
			new StagedModelType(
				DDMTemplate.class.getName(), DDMStructure.class.getName()));

		return exportActionableDynamicQuery;
	}

	public DDMPortletDataHandler() {
		setDataLocalized(true);
		setDeletionSystemEventStagedModelTypes(
			new StagedModelType(DDMStructure.class),
			new StagedModelType(DDMTemplate.class));
		setExportControls(
			new PortletDataHandlerBoolean(
				NAMESPACE, "data-definitions", true, false, null,
				DDMStructure.class.getName()));
		setPublishToLiveByDefault(true);

		XStreamAliasRegistryUtil.register(
			DDMStructureImpl.class, "DDMStructure");
		XStreamAliasRegistryUtil.register(DDMTemplateImpl.class, "DDMTemplate");
	}

	@Override
	protected PortletPreferences doDeleteData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		if (portletDataContext.addPrimaryKey(
				DDMPortletDataHandler.class, "deleteData")) {

			return portletPreferences;
		}

		try {
			DDMStructureLocalServiceUtil.deleteStructures(
				portletDataContext.getScopeGroupId());

			DDMTemplateLocalServiceUtil.deleteTemplates(
				portletDataContext.getScopeGroupId());
		}
		catch (Exception e) {
		}

		return portletPreferences;
	}

	@Override
	protected String doExportData(
			final PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		Element rootElement = addExportDataRootElement(portletDataContext);

		if (portletDataContext.getBooleanParameter(
				NAMESPACE, "data-definitions")) {

			List<DDMTemplate> ddmTemplates = new ArrayList<>();

			ActionableDynamicQuery ddmStructureActionableDynamicQuery =
				getDDMStructureActionableDynamicQuery(
					portletDataContext, ddmTemplates);

			ddmStructureActionableDynamicQuery.performActions();

			for (DDMTemplate ddmTemplate : ddmTemplates) {
				StagedModelDataHandlerUtil.exportStagedModel(
					portletDataContext, ddmTemplate);
			}
		}

		if (portletDataContext.getBooleanParameter(NAMESPACE, "structures")) {
			List<DDMTemplate> ddmTemplates = new ArrayList<>();

			ActionableDynamicQuery ddmStructureActionableDynamicQuery =
				DDMPortletDataHandler.getDDMStructureActionableDynamicQuery(
					portletDataContext, ddmTemplates, true);

			ddmStructureActionableDynamicQuery.performActions();

			ActionableDynamicQuery ddmTemplateActionableDynamicQuery =
				DDMPortletDataHandler.getDDMTemplateActionableDynamicQuery(
					portletDataContext, ddmTemplates, true);

			ddmTemplateActionableDynamicQuery.performActions();

			for (DDMTemplate ddmTemplate : ddmTemplates) {
				StagedModelDataHandlerUtil.exportStagedModel(
					portletDataContext, ddmTemplate);
			}
		}

		return getExportDataRootElementString(rootElement);
	}

	@Override
	protected PortletPreferences doImportData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences, String data)
		throws Exception {

		if (portletDataContext.getBooleanParameter(
				NAMESPACE, "data-definitions")) {

			Element ddmStructuresElement =
				portletDataContext.getImportDataGroupElement(
					DDMStructure.class);

			List<Element> ddmStructureElements =
				ddmStructuresElement.elements();

			for (Element ddmStructureElement : ddmStructureElements) {
				StagedModelDataHandlerUtil.importStagedModel(
					portletDataContext, ddmStructureElement);
			}

			Element ddmTemplatesElement =
				portletDataContext.getImportDataGroupElement(DDMTemplate.class);

			List<Element> ddmTemplateElements = ddmTemplatesElement.elements();

			for (Element ddmTemplateElement : ddmTemplateElements) {
				StagedModelDataHandlerUtil.importStagedModel(
					portletDataContext, ddmTemplateElement);
			}
		}

		if (portletDataContext.getBooleanParameter(NAMESPACE, "structures")) {
			Element ddmStructuresElement =
				portletDataContext.getImportDataGroupElement(
					DDMStructure.class);

			List<Element> ddmStructureElements =
				ddmStructuresElement.elements();

			for (Element ddmStructureElement : ddmStructureElements) {
				StagedModelDataHandlerUtil.importStagedModel(
					portletDataContext, ddmStructureElement);
			}

			Element ddmTemplatesElement =
				portletDataContext.getImportDataGroupElement(DDMTemplate.class);

			List<Element> ddmTemplateElements = ddmTemplatesElement.elements();

			for (Element ddmTemplateElement : ddmTemplateElements) {
				StagedModelDataHandlerUtil.importStagedModel(
					portletDataContext, ddmTemplateElement);
			}
		}

		return portletPreferences;
	}

}