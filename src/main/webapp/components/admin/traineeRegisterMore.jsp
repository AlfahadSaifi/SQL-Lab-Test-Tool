<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
        <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
            <%@ page isELIgnored="false" %>
                <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
                <div class="row m-0 p-0">
                    <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
                        <jsp:include page="/components/sideBar.jsp" />
                    </div>
                    <div class="col-9 m-0 p-2 px-2" style="width: 80%">

                        <c:if test="${not empty successMessage}">
                            <div class="alert alert-success">${successMessage}</div>
                        </c:if>
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger">${errorMessage}</div>
                        </c:if>
                        <div class="border border-gray shadow p-3 bg-white rounded">
                            <div class="">
                                <ul class="nav nav-tabs" id="myTab" role="tablist">
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link active" id="register-tab" data-bs-toggle="tab"
                                            data-bs-target="#register" type="button" role="tab" aria-controls="register"
                                            aria-selected="true">Register</button>
                                    </li>
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link" id="general-tab" data-bs-toggle="tab"
                                            data-bs-target="#general" type="button" role="tab" aria-controls="general"
                                            aria-selected="true">General</button>
                                    </li>
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link" id="personal-tab" data-bs-toggle="tab"
                                            data-bs-target="#personal" type="button" role="tab" aria-controls="personal"
                                            aria-selected="false">Personal</button>
                                    </li>
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link" id="education-tab" data-bs-toggle="tab"
                                            data-bs-target="#education" type="button" role="tab"
                                            aria-controls="education" aria-selected="false">Education</button>
                                    </li>
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link" id="address-tab" data-bs-toggle="tab"
                                            data-bs-target="#address" type="button" role="tab" aria-controls="address"
                                            aria-selected="false">Address</button>
                                    </li>
                                </ul>
                                <form:form action="registerTraineeViaForm?batchId=${batchId}"
                                    method="post" modelAttribute="trainee" class="needs-validation"
                                    id="registration-form">
                                    <div class="tab-content" id="myTabContent" class="d-flex gap-2 p-3"
                                        style="flex-wrap: wrap; border: 1px solid #e5dbdb; border-top: 1px solid white; border-bottom-left-radius: 10px; border-bottom-right-radius: 10px;">
                                        <div class="tab-pane fade show active" id="register" role="tabpanel"
                                            aria-labelledby="register-tab">
                                            <div class="row p-3">
                                                <div class="col-md-8">
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="employeeId">Employee Id</form:label>
                                                                <form:input path="employeeId"
                                                                    placeholder="Enter Employee Id"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue" path="employeeId" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-6">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="name">Name</form:label>
                                                                <form:input path="name"
                                                                    placeholder="Enter Employee Name"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue" path="name" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="emailId">Email</form:label>
                                                                <form:input type="email" path="emailId"
                                                                    placeholder="Enter Employee Email"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue" path="emailId" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-6">
                                                            <div class="mb-3">
                                                                <div class="d-flex align-items-center mb-2">
                                                                    <label for="password"
                                                                        class="text-md text-dark-100 mb-1">Password
                                                                        <span class="text-red-600 ml-1">*</span></label>
                                                                    <div class="password-toggle-icon px-2 z-3"
                                                                        data-placement="right" data-bs-toggle="tooltip"
                                                                        title="Password must contain at least 8 characters, including uppercase, lowercase, numbers, and special characters.">
                                                                        <i class="fa-regular fa-question-circle"
                                                                            style="color: rgb(172, 72, 72);"></i>
                                                                    </div>
                                                                </div>
                                                                <div class="input-group mb-3">
                                                                    <input name="password" id="password" type="password"
                                                                        class="form-control"
                                                                        placeholder="Enter your password"
                                                                        aria-label="Password"
                                                                        aria-describedby="password-addon">
                                                                    <button class="btn btn-outline-secondary"
                                                                        type="button" id="password-addon">
                                                                        <i class="fa fa-eye-slash"
                                                                            aria-hidden="true"></i>
                                                                    </button>
                                                                </div>
                                                                <div class="col-md-9">
                                                                    <div class="text-danger" id="passwordError" style="display: none;">
                                                                        Invalid Password.
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>

                                                    </div>
                                                    <!-- <div class="row">
                                                            <div class="col-md-6">
                                                                <button id="registerBtn"
                                                                    class="py-2 px-3 rounded border bg-blue text-white text-sm"
                                                                    type="submit" disabled>Register</button>
                                                            </div>
                                                            <div class="col-md-6">
                                                                <div class="text-danger" id="passwordError"
                                                                    style="display: none;">
                                                                    Invalid Password.
                                                                </div>
                                                            </div>
                                                        </div> -->
                                                </div>
                                                <div class="col-md-4">
                                                    <div class="m-8 shadow p-3 bg-white rounded">
                                                        <h5 class="text-center mt-8 p-2">Upload Trainee Excel Sheet</h5>
                                                        <!-- <form class="row g-3" method="post"
                                                            action="registerTrainee?id=${batchId}"
                                                            enctype="multipart/form-data"> -->
                                                            <div class="mb-3">
                                                                <input class="form-control form-control-sm"
                                                                    id="file" name="file" type="file" >
                                                            </div>
                                                            
                                                            <div class="mb-2 d-flex justify-content-end ">
                                                                <button
                                                                    class="py-2 px-3 rounded border bg-blue text-white text-sm"
                                                                    type="button"
                                                                    onclick="uploadFile()"
                                                                    >Upload</button>
                                                            </div>
                                                            <div class="text-sm text-gray">
                                                                Auto generate password:
                                                                <br/>
                                                                first_name + Employee Id + #
                                                                <br/>
                                                                (replace 'a' with '@')  
                                                                <br/>
                                                                Example: Tr@inee17109#
                                                            </div>
                                                        <!-- </form> -->
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="tab-pane fade show" id="general" role="tabpanel"
                                            aria-labelledby="general-tab">
                                            <div class="col-12">
                                                <div class="p-3">
                                                    <div class="row">
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.userId">User Id
                                                                </form:label>
                                                                <form:input path="traineeDetail.userId"
                                                                    placeholder="Enter User Id" class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.userId" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-2">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.doj">
                                                                    Employee DOJ
                                                                </form:label>
                                                                <form:input path="traineeDetail.doj" placeholder = "dd-MM-yyyy" type="text"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.doj" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.designation">Designation
                                                                </form:label>
                                                                <form:input path="traineeDetail.designation"
                                                                    placeholder="Enter Designation"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.designation" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-2">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.grade">
                                                                    Grade</form:label>
                                                                <form:input path="traineeDetail.grade"
                                                                    placeholder="Enter Grade" class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.grade" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.ibu">
                                                                    Enter IBU
                                                                </form:label>
                                                                <form:input path="traineeDetail.ibu"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.ibu" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.function">Function</form:label>
                                                                <form:input path="traineeDetail.function"
                                                                    placeholder="Enter function" class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.function" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.tierCategorization">Tier
                                                                    Categorization
                                                                </form:label>
                                                                <form:input path="traineeDetail.tierCategorization"
                                                                    placeholder="Enter Tier Categorization"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.tierCategorization" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.probationPeriod">Probation
                                                                    Period
                                                                </form:label>
                                                                <form:input path="traineeDetail.probationPeriod"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.probationPeriod" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.io">IO
                                                                </form:label>
                                                                <form:input path="traineeDetail.io"
                                                                    placeholder="Enter IO" class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.io" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.confirmationDate">Confirmation
                                                                    Date
                                                                </form:label>
                                                                <form:input path="traineeDetail.confirmationDate"
                                                                    placeholder = "dd-MM-yyyy" type="text" class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.confirmationDate" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="tab-pane fade" id="personal" role="tabpanel"
                                            aria-labelledby="personal-tab">
                                            <div class="col-12">
                                                <div class="p-3">
                                                    <div class="row">
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.dob">
                                                                    Date Of Bith
                                                                </form:label>
                                                                <form:input path="traineeDetail.dob" placeholder = "dd-MM-yyyy" type="text"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.dob" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.personalEmailId">Personal
                                                                    Email-Id
                                                                </form:label>
                                                                <form:input path="traineeDetail.personalEmailId"
                                                                    placeholder="Enter Personal Email Id"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.personalEmailId" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.contactNumber">Contact Number
                                                                </form:label>
                                                                <form:input path="traineeDetail.contactNumber"
                                                                    placeholder="Enter Contact Number"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.contactNumber" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="tab-pane fade" id="education" role="tabpanel"
                                            aria-labelledby="education-tab">
                                            <div class="col-12">
                                                <div class="p-3">
                                                    <div class="row">
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.tenthPercent">10th Percent
                                                                </form:label>
                                                                <form:input path="traineeDetail.tenthPercent"
                                                                    placeholder="Enter 10th Percent"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.tenthPercent" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.twelfthPercent">12th Percent
                                                                </form:label>
                                                                <form:input path="traineeDetail.twelfthPercent"
                                                                    placeholder="Enter Twelfth Percent"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.twelfthPercent" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.graduation">Graduation
                                                                </form:label>
                                                                <form:input path="traineeDetail.graduation"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.graduation" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.graduationPercent">Graduation
                                                                    Percent
                                                                </form:label>
                                                                <form:input path="traineeDetail.graduationPercent"
                                                                    placeholder="Enter Graduation Percent"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.graduationPercent" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.branch">Branch</form:label>
                                                                <form:input path="traineeDetail.branch"
                                                                    placeholder="Enter Branch" class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.branch" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.graduationYOP">Graduation YOP
                                                                </form:label>
                                                                <form:input path="traineeDetail.graduationYOP"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.graduationYOP" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.collegeName">College Name
                                                                </form:label>
                                                                <form:input path="traineeDetail.collegeName"
                                                                    placeholder="Enter College Name"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.collegeName" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.university">University
                                                                </form:label>
                                                                <form:input path="traineeDetail.university"
                                                                    placeholder="Enter University Name"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.university" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.universityShortName">University
                                                                    ShortName
                                                                </form:label>
                                                                <form:input path="traineeDetail.universityShortName"
                                                                    placeholder="Enter University Short Name"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.universityShortName" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="tab-pane fade" id="address" role="tabpanel"
                                            aria-labelledby="address-tab">
                                            <div class="col-12">
                                                <div class="p-3">
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.address">Address</form:label>
                                                                <form:input path="traineeDetail.address"
                                                                    placeholder="Enter Address" class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.address" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-6">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.city">
                                                                    City</form:label>
                                                                <form:input path="traineeDetail.city"
                                                                    placeholder="Enter City" class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.city" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.state">
                                                                    State
                                                                </form:label>
                                                                <form:input path="traineeDetail.state"
                                                                    placeholder="Enter State" class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.state" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.country">Country</form:label>
                                                                <form:input path="traineeDetail.country"
                                                                    placeholder="Enter country" class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.country" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="traineeDetail.pinCode">Pincode</form:label>
                                                                <form:input path="traineeDetail.pinCode"
                                                                    placeholder="Enter Pincode" class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="traineeDetail.pinCode" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row my-2">
                                        <div class="col-md-6">
                                            <button 
                                                class="py-2 px-3 rounded border bg-blue text-white text-sm"
                                                type="submit">
                                                Register
                                            </button>
                                            <button class="py-2 px-3 rounded border bg-green text-white text-sm"
                                                type="reset">
                                                Reset
                                            </button>
                                            <a href="${pageContext.request.contextPath}/admin/viewTrainee?id=${batchId}"
                                                class="py-2 px-3 rounded border bg-red text-white text-sm">
                                                Cancel
                                            </a>
                                        </div>
                                      
                                    </div>
                                </form:form>
                            </div>
                           
                        </div>
                    </div>
                </div>
                </div>
                <script>
                      function uploadFile() {
                        var form = document.createElement("form");
                        form.action = "${pageContext.request.contextPath}/admin/registerTraineeViaExcel/?id=${batchId}";
                        form.method = "post";
                        form.enctype="multipart/form-data"
                        var inputFile = document.getElementById("file");
                        form.appendChild(inputFile);
                        document.body.appendChild(form);
                        form.submit();
                        document.body.removeChild(form);
                    }
                    // $(document).ready(function () {
                    //     $('.nav-link').on('shown.bs.tab', function (event) {
                    //         let activeTab = $(event.target).attr('aria-controls');

                    //         // Check if the active tab is 'register' and show/hide buttons accordingly
                    //         if (activeTab !== 'register') {
                    //             $('#buttonSection').css('display', 'flex');
                    //             $('#buttonCol1, #buttonCol2').css('display', 'block');
                    //         } else {
                    //             $('#buttonSection').css('display', 'none');
                    //         }
                    //     });

                    //     // Trigger the logic for the initially active tab
                    //     let activeTab = $('.nav-link.active');
                    //     if (activeTab.length > 0) {
                    //         activeTab.trigger('shown.bs.tab'); // Manually trigger the event for the active tab
                    //     }
                    // });


                    const passwordInput = document.querySelector('input[type="password"]');
                    const eyeIcon = document.querySelector('#password-addon');
                    eyeIcon.addEventListener('click', function () {
                        const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
                        passwordInput.setAttribute('type', type);
                        this.querySelector('i').classList.toggle('fa-eye-slash');
                        this.querySelector('i').classList.toggle('fa-eye');
                    });
                    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
                    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
                        return new bootstrap.Tooltip(tooltipTriggerEl);
                    });
                    function validatePassword(password) {
                        const hasLowercase = /[a-z]/.test(password);
                        const hasUppercase = /[A-Z]/.test(password);
                        const hasNumber = /\d/.test(password);
                        const hasSpecialChar = /[!@#$%^&*]/.test(password);
                        const hasMinimumLength = password.length >= 8;
                        const conditions = [
                            { condition: hasMinimumLength, message: 'at least 8 characters' },
                            { condition: hasLowercase, message: 'at least one lowercase letter' },
                            { condition: hasUppercase, message: 'at least one uppercase letter' },
                            { condition: hasNumber, message: 'at least one digit' },
                            { condition: hasSpecialChar, message: 'at least one special character: !@#$%^&*' }
                        ];
                        const unmetConditions = conditions.filter(cond => !cond.condition);
                        if (unmetConditions.length === 0) {
                            return 'Password is valid.';
                        } else {
                            const messages = unmetConditions.map(cond => cond.message);
                            return "Password must contain" + messages.join(', ');
                        }
                    }

                    var passwordField = document.getElementById('password');
                    passwordField.addEventListener('blur', function () {
                        var validationResult = validatePassword(passwordField.value);
                        const passwordErrorDiv = document.getElementById('passwordError');
                        if (validationResult !== 'Password is valid.') {
                            passwordErrorDiv.style.display = 'block';
                            passwordError.innerText = validationResult
                        } else {
                            passwordErrorDiv.style.display = 'none';
                            document.getElementById("registerBtn").disabled = false;
                        }
                    });
                </script>