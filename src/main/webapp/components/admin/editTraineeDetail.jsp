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
                                        <button class="nav-link active" id="general-tab" data-bs-toggle="tab"
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
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link " id="changePassword-tab" data-bs-toggle="tab"
                                            data-bs-target="#changePassword" type="button" role="tab"
                                            aria-controls="changePassword" aria-selected="true">Change Password</button>
                                    </li>
                                </ul>

                                <form:form action="editTraineeDetail?batchId=${batchId}" method="post" modelAttribute="traineeDetail"
                                    class="needs-validation" id="registration-form">
                                    <div class="tab-content" id="myTabContent" class="d-flex gap-2 p-3"
                                        style="flex-wrap: wrap; border: 1px solid #e5dbdb; border-top: 1px solid white; border-bottom-left-radius: 10px; border-bottom-right-radius: 10px;">

                                        <div class="tab-pane fade show active" id="general" role="tabpanel"
                                            aria-labelledby="general-tab">
                                            <div class="col-12">
                                                <div class="p-3">
                                                    <div class="row">
                                                        <form:input path="id" class="form-control d-none" />
                                                        <form:input id="employeeId" path="employeeId"
                                                            class="form-control d-none" />
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="userId">User Id</form:label>
                                                                <form:input path="userId" placeholder="Enter User Id"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue" path="userId" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-2">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="doj">
                                                                    Employee DOJ
                                                                </form:label>
                                                                    <form:input path="doj" type="text" placeholder="dd-MM-yyyy" class="form-control" />
                                                                    <form:errors cssStyle="color:blue" path="doj" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="designation">Designation
                                                                </form:label>
                                                                <form:input path="designation"
                                                                    placeholder="Enter Designation"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue" path="designation" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-2">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="grade">
                                                                    Grade</form:label>
                                                                <form:input path="grade" placeholder="Enter Grade"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue" path="grade" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="ibu">
                                                                    Enter IBU
                                                                </form:label>
                                                                <form:input path="ibu" class="form-control" />
                                                                <form:errors cssStyle="color:blue" path="ibu" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="function">Function</form:label>
                                                                <form:input path="function" placeholder="Enter function"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue" path="function" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="tierCategorization">Tier
                                                                    Categorization
                                                                </form:label>
                                                                <form:input path="tierCategorization"
                                                                    placeholder="Enter Tier Categorization"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="tierCategorization" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="probationPeriod">Probation
                                                                    Period
                                                                </form:label>
                                                                <form:input path="probationPeriod"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="probationPeriod" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="io">IO
                                                                </form:label>
                                                                <form:input path="io" placeholder="Enter IO"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue" path="io" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="confirmationDate">Confirmation Date
                                                                </form:label>
                                                                <form:input path="confirmationDate" type="text" placeholder="dd-MM-yyyy" class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="confirmationDate" />
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
                                                                    path="dob">
                                                                    Date Of Bith
                                                                </form:label>
                                                                <form:input path="dob" type="text" placeholder="dd-MM-yyyy" class="form-control" />
                                                                <form:errors cssStyle="color:blue" path="dob" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="personalEmailId">Personal
                                                                    Email-Id
                                                                </form:label>
                                                                <form:input path="personalEmailId"
                                                                    placeholder="Enter Personal Email Id"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="personalEmailId" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="contactNumber">Contact Number
                                                                </form:label>
                                                                <form:input path="contactNumber"
                                                                    placeholder="Enter Contact Number"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="contactNumber" />
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
                                                                    path="tenthPercent">10th Percent
                                                                </form:label>
                                                                <form:input path="tenthPercent"
                                                                    placeholder="Enter 10th Percent"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="tenthPercent" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="twelfthPercent">12th Percent
                                                                </form:label>
                                                                <form:input path="twelfthPercent"
                                                                    placeholder="Enter Twelfth Percent"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="twelfthPercent" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="graduation">Graduation
                                                                </form:label>
                                                                <form:input path="graduation" class="form-control" />
                                                                <form:errors cssStyle="color:blue" path="graduation" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="graduationPercent">Graduation
                                                                    Percent
                                                                </form:label>
                                                                <form:input path="graduationPercent"
                                                                    placeholder="Enter Graduation Percent"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="graduationPercent" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="branch">Branch</form:label>
                                                                <form:input path="branch" placeholder="Enter Branch"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue" path="branch" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="graduationYOP">Graduation YOP
                                                                </form:label>
                                                                <form:input path="graduationYOP" class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="graduationYOP" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="collegeName">College Name
                                                                </form:label>
                                                                <form:input path="collegeName"
                                                                    placeholder="Enter College Name"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue" path="collegeName" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="university">University
                                                                </form:label>
                                                                <form:input path="university"
                                                                    placeholder="Enter University Name"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue" path="university" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="universityShortName">University
                                                                    ShortName
                                                                </form:label>
                                                                <form:input path="universityShortName"
                                                                    placeholder="Enter University Short Name"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue"
                                                                    path="universityShortName" />
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
                                                                    path="address">Address</form:label>
                                                                <form:input path="address" placeholder="Enter Address"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue" path="address" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-6">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="city">
                                                                    City</form:label>
                                                                <form:input path="city" placeholder="Enter City"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue" path="city" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="state">
                                                                    State
                                                                </form:label>
                                                                <form:input path="state" placeholder="Enter State"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue" path="state" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="country">Country</form:label>
                                                                <form:input path="country" placeholder="Enter country"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue" path="country" />
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mb-3">
                                                                <form:label class="text-md text-dark-100 mb-1"
                                                                    path="pinCode">Pincode</form:label>
                                                                <form:input path="pinCode" placeholder="Enter Pincode"
                                                                    class="form-control" />
                                                                <form:errors cssStyle="color:blue" path="pinCode" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="tab-pane fade" id="changePassword" role="tabpanel"
                                            aria-labelledby="changePassword-tab">
                                            <div class="col-12">
                                                <div class="p-3">
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <div class="mb-3 ">
                                                                <label class="text-md text-dark-100 mb-1">Change Password</label>
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
                                                                <div class="col-md-6">
                                                                    <div class="text-danger" id="passwordError"
                                                                        style="display: none;">
                                                                        Invalid Password.
                                                                    </div>
                                                                </div>
                                                                <button
                                                                    class=" my-2 py-2 px-3 rounded border bg-blue text-white text-sm"
                                                                    type="button" id="changePasswordBtn" disabled
                                                                    onclick="changePassword()">
                                                                    Change Password
                                                                </button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row my-2">
                                        <div class="col-md-6">
                                            <button class="py-2 px-3 rounded border bg-blue text-white text-sm"
                                                type="submit">
                                                Save
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
                    function changePassword() {
                        var form = document.createElement("form");
                        form.action = "${pageContext.request.contextPath}/admin/changePassword";
                        form.method = "post";
                        var inputPass = document.getElementById("password");
                        var inputEmployeeId = document.getElementById("employeeId");
                        form.appendChild(inputPass);
                        form.appendChild(inputEmployeeId);
                        document.body.appendChild(form);
                        form.submit();
                        document.body.removeChild(form);
                    }
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
                            document.getElementById("changePasswordBtn").disabled = true;
                        } else {
                            passwordErrorDiv.style.display = 'none';
                            document.getElementById("changePasswordBtn").disabled = false;
                        }
                    });
                </script>