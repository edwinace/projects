<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>

<%-- TODO: Refactor: Has duplications: See "/category-products.jsp". --%>
<html>
	<head>
		<%-- TODO: Grammar: "Products OF shop" or "Products FROM shop"? --%>
		<title>Products of shop <s:property value="shop.name"/></title>
	</head>
	<body>
		<%-- TODO: Add pager. --%>
		<%-- TODO: Move these if/elses to title. --%>
		<s:if test="shop == null">
			<h1>Shop not found</h1>
			Such shop does not exist or was removed.
		</s:if>
		<s:elseif test="isEmpty(products)">
			<h1>No product exists</h1>
			<%-- TODO: Update. --%>
			<%--
			<s:link value="Add Product" view="/product.xhtml" rendered="%{identity.loggedIn}">
				<f:param name="shopId" value="%{shop.id}"/>
			</s:link>
			--%>
		</s:elseif>
		<s:else>
			<table class="ContentTable">
				<tr>
					<td class="LeftContentColumn">
						<div class="LeftPanel">
							<s:set var="shopUrl" value="%{urlBuilder.getViewShop(shop)}"/>
							<s:push value="shop">
								<h2 class="LeftPanelCaption">Shop <s:a href="%{#shopUrl}"><s:property value="shop.name"/></s:a></h2>
								<div class="LeftPanelBody">
									<div class="ShopLogoBox">
										<a href="<s:property value="#shopUrl"/>">
											<s:if test="%{logo != null}">
												<%-- TODO: Insert logo into beautiful frame, like product image. --%>
												<%-- TODO: Duplication: Extract to include file? E.g. "print-shop-logo-s200.jsp". --%>
												<img src="<%= request.getContextPath() %>/uploads/images/shop-logos/<s:property value="logo.id + '/s200/' + logo.fileName + '.jpg'"/>">
											</s:if>
											<s:else>
												<img src="<%= request.getContextPath() %>/images/no-shop-logo-s200.png">
											</s:else>
										</a>
									</div>
									<table class="ViewTable LeftPanelTable" style="width: 100%;">
										<tr>
											<th><s:text name="msgkey.label.country"/></th>
											<%-- TODO: Link to "Shops by country"? (mb) --%>
											<td><s:property value="country"/></td>
										</tr>
										<tr>
											<th><s:text name="msgkey.label.currency"/></th>
											<td><s:property value="currency"/></td>
										</tr>
										<tr>
											<th><s:text name="msgkey.label.productCount"/></th>
											<td><s:property value="productCount"/></td>
										</tr>
										<tr>
											<th><s:text name="msgkey.label.seller"/></th>
											<td>
												<%-- TODO: Duplication: Extract "linkToUserProfile" page decorator. --%>
												<s:url action="user" namespace="/" var="viewUserUrl">
													<s:param name="id" value="user.id"/>
												</s:url>
												<s:a href="%{#viewUserUrl}"><s:property value="user.fullName"/></s:a>
											</td>
										</tr>
										<%-- TODO: Bug: Does not work. --%>
										<s:if test="%{loggedIn && user.equals(currentUser)}">
											<s:url action="shop!edit" namespace="/member" var="editShopUrl">
												<s:param name="id" value="%{id}"/>
											</s:url>
											<tr>
												<th><s:text name="msgkey.label.actions"/></th>
												<td><s:a href="%{#editShopUrl}"><s:text name="msgkey.action.edit"/></s:a></td>
											</tr>
										</s:if>
									</table>
								</div>
							</s:push>
							
							<h2 class="LeftPanelCaption">Product categories</h2>
							<div class="LeftPanelBody">
								<table class="CategoryTable">
									<s:if test="isEmpty(categories)">
										<%-- TODO: i18n --%>
										<tr><td class="Odd"><s:text name="msgkey.text.notExist.category"/></td></tr>
									</s:if>
									<s:else>
										<s:iterator value="categories" status="row">
											<tr class="<s:property value="#row.odd ? 'Odd' : 'Even'"/>">
												<td>
													<%-- TODO: Link to "getProductsByShopAndCategories(String shopName, Set<String> categories)" NOT only by category. --%>
													<s:url action="shop!products" namespace="/" var="shopProductsUrl">
														<s:param name="shopId" value="%{shop.id}"/>
														<s:param name="categoryName" value="%{name}"/>
														<s:param name="page" value="%{pager.page}"/>
														<s:param name="results" value="%{pager.maxResults}"/>
													</s:url>
													<s:if test="categoryName != null && name.equalsIgnoreCase(categoryName)">
														<s:a href="%{#shopProductsUrl}"><strong><s:property value="name"/></strong></s:a>
													</s:if>
													<s:else>
														<s:a href="%{#shopProductsUrl}"><s:property value="name"/></s:a>
													</s:else>
												</td>
												<td class="ProductCount"><s:property value="productCount"/></td>
											</tr>
										</s:iterator>
									</s:else>
								</table>
							</div>
						</div>
					</td>
					
					<%-- TODO: Refactor: Product list duplication in "category-products.jsp". --%>
					<td class="CenterContentColumn">
						<f:topPager/>
						
						<s:set var="first" value="%{pager.firstResultNumber}"/>
						<s:set var="last" value="%{pager.lastResultNumber}"/>
						
						<h2 class="TableHeader">Products</h2>
						<div class="ProductList">
							<table class="ProductTable">
								<s:iterator value="%{products}" status="row">
								
									<s:if test="%{#row.first}">
										<s:set var="rowClass" value="'FirstProductRow'"/>
									</s:if>
									<s:elseif test="%{#row.odd}">
										<s:set var="rowClass" value="'OddProductRow'"/>
									</s:elseif>
									<s:elseif test="%{#row.even}">
										<s:set var="rowClass" value="'EvenProductRow'"/>
									</s:elseif>
									
									<%-- TODO: Low: Enhance: Print actions for logged in user and for product owner. --%>
									
									<s:url action="product!view" namespace="/" var="viewProductUrl">
										<s:param name="productId" value="%{id}"/>
									</s:url>
									
									<tr class="<s:property value="#rowClass"/>">
										<td class="LeftProductCell">
											<span class="RowNumber"><s:property value="#first + #row.index"/>.</span>
										</td>
										<td class="ThumbnailCell">
											<a class="ThumbnailLink" href="<s:property value="#viewProductUrl"/>">
												<s:if test="titleImage != null">
													<img src="<%= request.getContextPath() %>/uploads/images/products/<s:property value="titleImage.id + '/medium/' + titleImage.fileName + '.jpg'"/>">
												</s:if>
												<s:else>
													<img src="<%= request.getContextPath() %>/images/no-product-image-s100.png">
												</s:else>
											</a>
										</td>
										<td>
											<div class="ProductNameBlock">
												<s:a cssClass="ProductNameLink" href="%{#viewProductUrl}"><s:property value="name"/></s:a>
											</div>
											<jsp:include page="/WEB-INF/jsp/includes/summaryInListTable.jsp"/>
											
											<%-- TODO: Fix & Update to Seam. --%>
											<%--
											<ui:include src="layout/addToCart.xhtml"/>
											--%>
										</td>
										<td class="RightProductCell">
											<%-- 
												TODO: Rethink layout of product prices, maybe place them vertivally.
												      Use "courier" font for prices? (y)
												      Example:
												      
												       10.50
												         ...
												      120.95
											--%>
											<div class="Price">
												<s:if test="%{(minPrice != null) and (minPrice == maxPrice)}">
													<s:property value="minPrice"/>&nbsp;<s:property value="currency"/>
												</s:if>
												<s:elseif test="%{(minPrice != null) and (maxPrice != null)}">
													<%-- Use &#150; (short dash) or &#151; (long dash) as price separator. --%>
													<s:property value="minPrice"/>&nbsp;<s:property value="currency"/> &#150; 
													<s:property value="maxPrice"/>&nbsp;<s:property value="currency"/>
												</s:elseif>
												<s:else>
													<%-- TODO: What if product has no price? Print "-"? --%>
												</s:else>
											</div>
										</td>
									</tr>
								</s:iterator>
							</table>
						</div>
						
						<f:bottomPager/>
					</td>
				</tr>
			</table>
		</s:else>
	</body>
</html>