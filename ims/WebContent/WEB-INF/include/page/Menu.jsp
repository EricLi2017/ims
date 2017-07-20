<%--Navigation Menu--%>
<div class="menu">
	<%--right part--%>
	<div class="right">
		<%=session.getAttribute("user")%><span class="seperate">|</span><a
			href="/ims/user/SignOut.jsp">Sign Out</a>
	</div>


	<%--left part--%>
	<div class="left">
		<ul>
			<li><a
				href="/ims/internal/product/InternalProductManagement.jsp"
				target="_blank">Internal Product Management</a>
				<ul>
					<li><a href="/ims/internal/product/AddProduct.jsp"
						target="_blank">Add Internal Product</a></li>
					<li><a href="/ims/internal/supply/SupplyManagement.jsp"
						target="_blank">Product Supply Management</a></li>
					<li><a href="/ims/internal/supply/AddSupply.jsp"
						target="_blank">Add Product Supply</a></li>
					<li><a
						href="/ims/internal/supply/SupplyTransactionManagement.jsp"
						target="_blank">Supply Transaction Management</a></li>
					<li><a href="/ims/internal/supply/AddSupplyTransaction.jsp"
						target="_blank">Add Supply Transaction</a></li>
				</ul></li>
			<li><a href="/ims/amazon/product/ProductManagement.jsp"
				target="_blank">Amazon Product Management</a>
				<ul>
					<li><a href="/ims/amazon/product/BatchAddProductFromAMZ.jsp"
						target="_blank">Upload SKU From Report</a></li>
					<li>
						<!-- 		sync --> <!-- 			amazon/service/UpdateFBAInventoryService.jsp -->
						<a href="/ims/amazon/service/UpdateFBAInventoryService.jsp"
						target="_blank">Update All FBA Inventory</a> <!-- 				async --> <!-- amazon/service/UpdateFBAInventoryMessagePage.jsp -->
					</li>
					<li><a href="/ims/amazon/order/UpdateAndCheckOrderPage.jsp"
						target="_blank">Update and Check Orders</a> <!-- 							<ul> --> <!-- 								<li>Add Orders and OrderItems to IMS from MWS</li> -->
						<!-- 								<li>Check Orders between MWS and IMS --> <!-- 									<ul> -->
						<!-- 										<li>Check Orders of MWS</li> --> <!-- 										<li>Check Orders of IMS</li> -->
						<!-- 									</ul> --> <!-- 								</li> --> <!-- 							</ul> -->
					<li><a href="/ims/amazon/order/ManageOrders.jsp"
						target="_blank">Manage Orders</a> <!-- 							<ul> --> <!-- 								<li>Query Order Count From MWS</li> -->
						<!-- 								<li>Add Orders To IMS From MWS</li> --> <!-- 								<li>Add Order Items To IMS From MWS</li> -->
						<!-- 								<li>View SKU Sum</li> --> <!-- 							</ul> --></li>
					<li>
						<!-- 				async --> <!-- 		amazon/service/CheckSKUService.jsp --> <a
						href="/ims/amazon/service/CheckSKUService.jsp" target="_blank">Check
							SKU Matching in Orders and Products</a>
					</li>
				</ul></li>
		</ul>
	</div>
	<%--middle part--%>
	<div class="middle">&nbsp;</div>
</div>