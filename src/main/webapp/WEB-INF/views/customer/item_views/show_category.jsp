<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.ecommerce.general.helper.PathesHelper" %>

<c:import url='${PathesHelper.getPublicInclude("header")}' />

<div class="container">
    <h1 class="text-center">Show Category Items</h1>
    <div class="row">
        <c:forEach items="${categoryItems}" var="item">
            <div class="col-sm-6 col-md-3">
                <div class="thumbnail item-box">
                    <span class="price-tag">${item['price']}</span>
                    <img class="img-responsive" src="${PathesHelper.getPublicImg('img.png')}" alt="" />
                    <div class="caption">
                        <h3><a href="${initParam['customerPath']}show-item?itemid=${item['id']}">${item['name']}</a></h3>
                        <p>${item['description']}</p>
                        <div class="date">${item['addDate']}</div>
                        </div>
                    </div>
                </div>
        </c:forEach>
    </div>
</div>

<c:import url='${PathesHelper.getPublicInclude("footer")}' />