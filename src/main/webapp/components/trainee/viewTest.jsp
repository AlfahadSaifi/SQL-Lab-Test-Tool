<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<div class="row m-0">
    <div class="col-3 m-0 p-0" style="width: 20%">
        <jsp:include page="/components/sideBarTrainee.jsp" />
    </div>
    <div class="col-9 m-0 p-0">
        <div class="border border-gray shadow py-3 px-4 bg-white rounded">
            <div class="">
                <h3 class="my-3 ">Your Completed Lab Tests</h3>
                <div class="flexwapRow g-5">
                    <c:choose>
                        <c:when test="${empty labTestList}">
                            <p>No lab tests available.</p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="labTestList" items="${labTestList}">
                                <div class="w-45 card shadow">
                                    <div class="card-body">
                                        <h5 class="card-title text-md">${labTestList.labTestName}
                                        </h5>
                                        <p class="card-text text-muted text-sm">
                                            ${labTestList.questions.size()}
                                            Questions | ${labTestList.questions.size()} Marks |
                                            ${labTestList.duration} Mins 
                                        </p>
                                        <p class="card-text text-muted text-sm">
                                            <fmt:formatDate value="${labTestList.startDate}"
                                                pattern="dd/MM/yyyy" /> Start Date |
                                            <fmt:formatDate value="${labTestList.endDate}"
                                                pattern="dd/MM/yyyy" /> End Date 
                                        </p>
                                        <div class="d-flex justify-content-end align-items-center">
                                            <!-- <button class="btn btn-outline-success btn-sm me-2">View
                                                Details</button> -->
                                            <!-- <button class="btn btn-outline-success btn-sm me-2">View Attempts</button> -->
                                            <a href="${pageContext.request.contextPath}/trainee/viewLabTestReport?labTestId=${labTestList.labTestId}"
                                                class="btn btn-light bg-sec text-white btn-sm">View Report</a>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>