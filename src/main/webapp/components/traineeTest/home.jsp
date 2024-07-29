<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<div class="mx-5 my-2">
    <div class="container-fluid">
        <div class="card shadow-sm bg-white rounded border-gray p-3">
            <div class="m-2">
                <h5 class="card-title text-md font-weight-bold">Labs</h5>
                <div class="row">
                    <c:forEach var="labDtoList" items="${labDtoList}">
                        <a href="${pageContext.request.contextPath}/trainee/labStart?labId=${labDtoList.labId}"
                            class="col-3 mb-2">
                            <div class="card border p-2">${labDtoList.labName}</div>
                        </a>
                    </c:forEach>
                </div>
            </div>
            <hr />
        </div>
    </div>
    <div class="container-fluid">
        <div class="card shadow-sm bg-white rounded border-gray p-3 mt-4">
            <div class="m-2">
                <h5 class="card-title text-md font-weight-bold">Labs Test</h5>
                <div class="row">
                    <c:forEach var="labTestDtoList" items="${labTestDtoList}">
                        <a href="${pageContext.request.contextPath}/trainee/labTestStart?labTestId=${labTestDtoList.labId}"
                            class="col-3 mb-2">
                            <div class="card border p-2">${labTestDtoList.labName}</div>
                        </a>
                    </c:forEach>
                </div>
            </div>
            <hr />
        </div>
    </div>
</div>
