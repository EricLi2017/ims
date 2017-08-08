<%--Navigation Menu--%>
<div id="menu" class="menu">
	<%--right part--%>
	<div class="right">
		<ul>
			<li><span><%=session.getAttribute("user")%>&nbsp;&nbsp;|</span></li>
			<li><a href="/ims/schedule/TimingTasks.jsp">Settings</a>
				<ul>
					<li style="border-bottom: 1px dotted #ddd;"><a
						href="/ims/user/SignOut.jsp">Sign Out</a></li>
					<li><a href="/ims/schedule/TimingTasks.jsp">Timing Tasks</a></li>
				</ul></li>
		</ul>
	</div>


	<%--left part--%>
	<div class="left">
		<ul>
			<li><a href="/ims/amazon/product/ProductManagement.jsp">Amazon
					Product</a>
				<ul>
					<li><a href="/ims/amazon/product/ProductManagement.jsp">Amazon
							Product Management</a></li>
					<li><a href="/ims/amazon/product/BatchAddProductFromAMZ.jsp">Upload
							SKU From Report</a></li>
					<li>
						<!-- 		sync --> <!-- 			amazon/service/UpdateFBAInventoryService.jsp -->
						<a href="/ims/amazon/service/UpdateFBAInventoryService.jsp">Update
							All FBA Inventory</a> <!-- 				async --> <!-- amazon/service/UpdateFBAInventoryMessagePage.jsp -->
					</li>
					<li><a href="/ims/amazon/order/UpdateAndCheckOrderPage.jsp">Update
							and Check Orders</a> <!-- 							<ul> --> <!-- 								<li>Add Orders and OrderItems to IMS from MWS</li> -->
						<!-- 								<li>Check Orders between MWS and IMS --> <!-- 									<ul> -->
						<!-- 										<li>Check Orders of MWS</li> --> <!-- 										<li>Check Orders of IMS</li> -->
						<!-- 									</ul> --> <!-- 								</li> --> <!-- 							</ul> -->
					<li><a href="/ims/amazon/order/ManageOrders.jsp">Manage
							Orders</a> <!-- 							<ul> --> <!-- 								<li>Query Order Count From MWS</li> -->
						<!-- 								<li>Add Orders To IMS From MWS</li> --> <!-- 								<li>Add Order Items To IMS From MWS</li> -->
						<!-- 								<li>View SKU Sum</li> --> <!-- 							</ul> --></li>
					<li>
						<!-- 				async --> <!-- 		amazon/service/CheckSKUService.jsp --> <a
						href="/ims/amazon/service/CheckSKUService.jsp">Check SKU
							Matching in Orders and Products</a>
					</li>
				</ul></li>
			<li><a
				href="/ims/internal/product/InternalProductManagement.jsp">Internal
					Product</a>
				<ul>
					<li><a
						href="/ims/internal/product/InternalProductManagement.jsp">Internal
							Product Management</a></li>
					<li><a href="/ims/internal/product/AddProduct.jsp">Add
							Internal Product</a></li>
					<li><a href="/ims/internal/supply/SupplyManagement.jsp">Product
							Supply Management</a></li>
					<li><a href="/ims/internal/supply/AddSupply.jsp">Add
							Product Supply</a></li>
					<li><a
						href="/ims/internal/supply/SupplyTransactionManagement.jsp">Supply
							Transaction Management</a></li>
					<li><a href="/ims/internal/supply/AddSupplyTransaction.jsp">Add
							Supply Transaction</a></li>
				</ul></li>
		</ul>
	</div>
	<%--middle part--%>
	<div class="middle">&nbsp;</div>
</div>