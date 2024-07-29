<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<div class="mx-5 my-2">
    <div class="">
        <h3 class="text-center ">Test Report</h3>
    </div>
    <div class="container-fluid shadow-sm p-3 bg-white rounded border-gray ">
        <div class="m-2">
            <div>
                <div class="row">
                    <div class="col-4">
                        <div class="text-md text-dark-100 m-1 p-2 ">Total Question: <span class="text-black bold">10</span></div>
                        <div class="text-md text-dark-100 m-1 p-2">Correct Question: <span class="text-black bold">11</span></div>
                    </div>
                    <div class="col-4">
                        <div class="text-md text-dark-100 m-1 p-2">Incorrect Question: <span class="text-black bold">20</span></div>
                        <div class="text-md text-dark-100 m-1 p-2">Total Marks: <span class="text-black bold">09</span></div>
                    </div>
                    <div class="col-4">
                        <div class="text-md text-dark-100 m-1 p-2">Incorrect Question: <span class="text-black bold">20</span></div>
                        <div class="text-md text-dark-100 m-1 p-2">Total Marks: <span class="text-black bold">09</span></div>
                    </div>
                </div>
                <table id="myTable" class="table">
                    <thead>
                        <tr>
                            <th scope="col">Ques No</th>
                            <th scope="col">Result</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="e" items="${trainees}">
                            <tr>
                                <td>${e.employeeId}</td>
                                <td>${e.name}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>