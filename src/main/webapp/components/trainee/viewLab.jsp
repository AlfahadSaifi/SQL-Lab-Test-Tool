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
        <div class="border border-gray shadow p-3 bg-white rounded">
            <div class="">
                <h3 class="my-3 ">Your Lab</h3>
                <div class="flexwapRow g-5">
                    <c:choose>
                        <c:when test="${empty labList}">
                            <p>No lab available.</p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="lab" items="${labList}">
                                <div class="w-45 card shadow">
                                    <div class="card-body">
                                        <h5 class="card-title text-md">${lab.labName}
                                        </h5>
                                        <p class="card-text text-muted text-sm">
                                            ${lab.questions.size()}
                                            Questions | ${lab.questions.size()} Marks |
                                            ${lab.duration} Mins |
                                            <fmt:formatDate value="${lab.startDate}"
                                                pattern="dd/MM/yyyy" /> Start Date |
                                        </p>
                                        <p class="card-text text-muted text-sm">
                                            <fmt:formatDate value="${lab.endDate}"
                                                pattern="dd/MM/yyyy" /> End Date |
                                            <fmt:formatDate value="${lab.createdDate}"
                                                pattern="dd/MM/yyyy" /> Created Date
                                        </p>
                                        <div class="d-flex justify-content-end align-items-center">
                                            <button class="btn btn-outline-success btn-sm me-2">View
                                                Details</button>
                                            <!-- <button class="btn btn-outline-success btn-sm me-2">View Attempts</button> -->
                                            <a href="${pageContext.request.contextPath}/trainee/instructionlab?labId=${lab.labId}"
                                                class="btn btn-success btn-sm">Start Lab Test</a>
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