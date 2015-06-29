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

package com.liferay.asset.publisher.web.util;

import com.liferay.asset.publisher.web.constants.AssetPublisherPortletKeys;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Layout;
import com.liferay.portlet.asset.AssetRendererFactoryRegistryUtil;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetEntryServiceUtil;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;
import com.liferay.portlet.asset.service.persistence.AssetEntryQuery;
import com.liferay.portlet.asset.util.AssetUtil;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Zsolt Oláh
 */
public class AssetEntryQueryResult {

	public AssetEntryQueryResult(
		AssetEntryQuery assetEntryQuery, HttpServletRequest portletRequest,
		PortletPreferences portletPreferences, long[] classNameIds,
		boolean searchWithIndex, SearchContainer<AssetEntry> searchContainer) {

		_assetEntryQuery = assetEntryQuery;

		try {
			process(
				portletRequest, portletPreferences, classNameIds,
				searchWithIndex, searchContainer.getStart(),
				searchContainer.getEnd());
		}
		catch (Exception ex) {
			if (_log.isWarnEnabled()) {
				_log.warn("Error processing");
			}
		}
	}

	public AssetEntryQueryResult(
		PortletPreferences portletPreferences, long[] groupIds, Layout layout) {

		_assetEntryQuery = AssetPublisherUtil.getAssetEntryQuery(
			portletPreferences, groupIds, null, null);

		AssetPublisherUtil.addPreferenceValues(
			_assetEntryQuery, portletPreferences, layout, groupIds, 0,
			QueryUtil.ALL_POS);

		boolean anyAssetType = GetterUtil.getBoolean(
			portletPreferences.getValue("anyAssetType", null), true);

		long[] classNameIds = null;

		if (!anyAssetType) {
			long[] availableClassNameIds =
				AssetRendererFactoryRegistryUtil.getClassNameIds(
					layout.getCompanyId());

			classNameIds = AssetPublisherUtil.getClassNameIds(
				portletPreferences, availableClassNameIds);
		}

		try {
			process(
				null, portletPreferences, classNameIds, false,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		}
		catch (Exception ex) {
			if (_log.isWarnEnabled()) {
				_log.warn("Error processing");
			}
		}
	}

	public List<AssetEntry> getResults() {
		return _results;
	}

	public int getTotal() {
		return _total;
	}

	private static int getGroupTotal(
			AssetEntryQuery assetEntryQuery, HttpServletRequest request,
			String portletName, int start, int end, boolean searchWithIndex)
		throws Exception {

		int groupTotal = 0;

		if (Validator.isNotNull(request) && searchWithIndex &&
			(assetEntryQuery.getLinkedAssetEntryId() == 0) &&
			!portletName.equals(
				AssetPublisherPortletKeys.HIGHEST_RATED_ASSETS) &&
			!portletName.equals(AssetPublisherPortletKeys.MOST_VIEWED_ASSETS)) {

			BaseModelSearchResult<AssetEntry> baseModelSearchResult =
				AssetUtil.searchAssetEntries(
					request, assetEntryQuery, start, end);

			groupTotal = baseModelSearchResult.getLength();
		}
		else {
			groupTotal = AssetEntryServiceUtil.getEntriesCount(assetEntryQuery);
		}

		return groupTotal;
	}

	private static int getLimit(int groupTotal, int limit) {
		if (groupTotal > 0) {
			if ((limit > 0) && (limit > groupTotal)) {
				limit -= groupTotal;
			}
			else {
				limit = 0;
			}
		}

		return limit;
	}

	private static List<AssetEntry> getResults(
			AssetEntryQuery assetEntryQuery, HttpServletRequest request,
			String portletName, int start, int end, boolean searchWithIndex)
		throws Exception {

		List<AssetEntry> results = new ArrayList<>();

		if (Validator.isNotNull(request) && searchWithIndex &&
			(assetEntryQuery.getLinkedAssetEntryId() == 0) &&
			!portletName.equals(
				AssetPublisherPortletKeys.HIGHEST_RATED_ASSETS) &&
			!portletName.equals(AssetPublisherPortletKeys.MOST_VIEWED_ASSETS)) {

			BaseModelSearchResult<AssetEntry> baseModelSearchResult =
				AssetUtil.searchAssetEntries(
					request, assetEntryQuery, start, end);

			results = baseModelSearchResult.getBaseModels();
		}
		else {
			assetEntryQuery.setStart(start);
			assetEntryQuery.setEnd(end);

			results = AssetEntryServiceUtil.getEntries(assetEntryQuery);
		}

		return results;
	}

	private static List<AssetEntry> getResults(
		PortletPreferences portletPreferences, AssetEntryQuery assetEntryQuery,
		long[] classNameIds, int start, int end, HttpServletRequest request,
		String portletName, boolean searchWithIndex) throws Exception {

		List<AssetEntry> results = new ArrayList<>();

		long assetVocabularyId = GetterUtil.getLong(
			portletPreferences.getValue("assetVocabularyId", null));

		if (assetVocabularyId > 0) {
			AssetVocabulary assetVocabulary =
				AssetVocabularyLocalServiceUtil.getVocabulary(
					assetVocabularyId);

			List<AssetCategory> assetCategories =
				AssetCategoryLocalServiceUtil.getVocabularyRootCategories(
					assetVocabulary.getVocabularyId(), QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null);

			assetEntryQuery.setClassNameIds(classNameIds);

			for (AssetCategory assetCategory : assetCategories) {
				assetCategory = assetCategory.toEscapedModel();

				long[] oldAllCategoryIds = assetEntryQuery.getAllCategoryIds();

				long[] newAllAssetCategoryIds = ArrayUtil.append(
					oldAllCategoryIds, assetCategory.getCategoryId());

				assetEntryQuery.setAllCategoryIds(newAllAssetCategoryIds);

				results = getResults(
					assetEntryQuery, request, portletName, start, end,
					searchWithIndex);

				int groupTotal = getGroupTotal(
					assetEntryQuery, request, portletName, start, end,
					searchWithIndex);

				start = getLimit(groupTotal, start);
				end = getLimit(groupTotal, end);
				assetEntryQuery.setAllCategoryIds(oldAllCategoryIds);
				assetEntryQuery.setStart(start);
				assetEntryQuery.setEnd(end);
			}
		}

		return results;
	}

	private static int getTotal(
		int total, PortletPreferences portletPreferences,
		AssetEntryQuery assetEntryQuery, long[] classNameIds, int start,
		int end, HttpServletRequest request,
		String portletName, boolean searchWithIndex) throws Exception {

		long assetVocabularyId = GetterUtil.getLong(
			portletPreferences.getValue("assetVocabularyId", null));

		if (assetVocabularyId > 0) {
			AssetVocabulary assetVocabulary =
							AssetVocabularyLocalServiceUtil.getVocabulary(
								assetVocabularyId);

			List<AssetCategory> assetCategories =
					AssetCategoryLocalServiceUtil.getVocabularyRootCategories(
						assetVocabulary.getVocabularyId(), QueryUtil.ALL_POS,
						QueryUtil.ALL_POS, null);

			assetEntryQuery.setClassNameIds(classNameIds);

			for (AssetCategory assetCategory : assetCategories) {
				assetCategory = assetCategory.toEscapedModel();

				total += getGroupTotal(
					assetEntryQuery, request, portletName, start, end,
					searchWithIndex);
			}
		}

		return total;
	}

	private void process(
			HttpServletRequest httpServletRequest,
			PortletPreferences portletPreferences, long[] classNameIds,
			boolean searchWithIndex, int start, int end)
		throws Exception {

		String portletName = "";

		if (Validator.isNotNull(httpServletRequest)) {
			PortletConfig portletConfig =
				(PortletConfig)httpServletRequest.getAttribute(
					JavaConstants.JAVAX_PORTLET_CONFIG);

			portletName = portletConfig.getPortletName();
		}

		List<AssetEntry> results = new ArrayList<>();
		int total = 0;

		if (!portletName.equals(AssetPublisherPortletKeys.RELATED_ASSETS) ||
			(_assetEntryQuery.getLinkedAssetEntryId() > 0)) {

			long assetVocabularyId = GetterUtil.getLong(
				portletPreferences.getValue("assetVocabularyId", null));

			if (assetVocabularyId > 0) {
				total = getTotal(
					total, portletPreferences, _assetEntryQuery, classNameIds,
					start, end, httpServletRequest, portletName,
					searchWithIndex);

				results = getResults(
					portletPreferences, _assetEntryQuery, classNameIds, start,
					end, httpServletRequest, portletName, searchWithIndex);
			}
			else if (assetVocabularyId != -1) {
				_assetEntryQuery.setClassNameIds(classNameIds);

				total += getGroupTotal(
					_assetEntryQuery, httpServletRequest, portletName, start,
					end, searchWithIndex);

				results = getResults(
					_assetEntryQuery, httpServletRequest, portletName, start,
					end, searchWithIndex);
			}
			else {
				for (long classNameId : classNameIds) {
					_assetEntryQuery.setClassNameIds(new long[] {classNameId});

					total = getTotal(
						total, portletPreferences, _assetEntryQuery,
						classNameIds, start, end, httpServletRequest,
						portletName, searchWithIndex);

					int groupTotal = 0;

					results = getResults(
						_assetEntryQuery, httpServletRequest, portletName,
						start, end, searchWithIndex);

					httpServletRequest.setAttribute(
						"view.jsp-results", results);

					if (!portletName.equals(
							AssetPublisherPortletKeys.RECENT_CONTENT)) {

						groupTotal = getGroupTotal(
							_assetEntryQuery, httpServletRequest, portletName,
							start, end, searchWithIndex);

						start = getLimit(groupTotal, start);

						end = getLimit(groupTotal, end);

						_assetEntryQuery.setEnd(QueryUtil.ALL_POS);
						_assetEntryQuery.setStart(QueryUtil.ALL_POS);
					}
				}
			}
		}

		_results = results;
		_total = total;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AssetEntryQueryResult.class);

	private final AssetEntryQuery _assetEntryQuery;
	private List<AssetEntry> _results = new ArrayList<>();
	private int _total = 0;

}