<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
        <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
            <%@ page isELIgnored="false" %>
                <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
                <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
                <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
                <div class="row m-0 p-0">
                    <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
                        <jsp:include page="/components/sideBar.jsp" />
                    </div>
                    <div class="col-9 m-0 p-2" style="width: 80%">
                        <c:if test="${not empty successMessage}">
                            <div class="alert alert-success">${successMessage}</div>
                        </c:if>
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger">${errorMessage}</div>
                        </c:if>
                        <div class="row">
                            <div class="col-8">
                                <div class="m-8 shadow-sm p-3 mb-5 bg-white rounded">
                                    <h3 class="text-center mt-8 pt-5">Register Trainee</h3>
                                    <form:form action="registerTraineeViaForm?id=${batchId}" method="post"
                                        modelAttribute="trainee" class="needs-validation" id="registration-form">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="mb-3">
                                                    <form:label class="text-md text-dark-100 mb-1" path="employeeId">
                                                        Employee Id
                                                    </form:label>
                                                    <form:input path="employeeId" placeholder="Enter Employee Id"
                                                        class="form-control" />
                                                    <form:errors cssStyle="color:blue" path="employeeId" />
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="mb-3">
                                                    <form:label class="text-md text-dark-100 mb-1" path="name">Name
                                                    </form:label>
                                                    <form:input path="name" placeholder="Enter Employee Name"
                                                        class="form-control" />
                                                    <form:errors cssStyle="color:blue" path="name" />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="mb-3">
                                                    <form:label class="text-md text-dark-100 mb-1" path="emailId">Email
                                                    </form:label>
                                                    <form:input type="email" path="emailId"
                                                        placeholder="Enter Employee Email" class="form-control" />
                                                    <form:errors cssStyle="color:blue" path="emailId" />
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="mb-3">
                                                    <div class="d-flex align-items-center mb-2">
                                                        <label for="password"
                                                            class="text-md text-dark-100 mb-1">Password <span
                                                                class="text-red-600 ml-1">*</span></label>
                                                        <div class="password-toggle-icon px-2 z-3"
                                                            data-placement="right" data-bs-toggle="tooltip"
                                                            title="Password must contain at least 8 characters, including uppercase, lowercase, numbers, and special characters.">
                                                            <i class="fa-regular fa-question-circle"
                                                                style="color: rgb(172, 72, 72);"></i>
                                                        </div>
                                                    </div>
                                                    <div class="input-group mb-3">
                                                        <input name="password" id="password" type="password"
                                                            class="form-control" placeholder="Enter your password"
                                                            aria-label="Password" aria-describedby="password-addon">
                                                        <button class="btn btn-outline-secondary" type="button"
                                                            id="password-addon">
                                                            <i class="fa fa-eye-slash" aria-hidden="true"></i>
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <button id="registerBtn"
                                                    class="py-2 px-3 rounded border bg-blue text-white text-sm"
                                                    type="submit" disabled>
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
                                            <div class="col-md-6">
                                                <div class="text-danger" id="passwordError" style="display: none;">
                                                    Invalid Password.
                                                </div>
                                            </div>
                                        </div>
                                    </form:form>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="m-8 shadow-sm p-3 mb-5 bg-white rounded">
                                    <h4 class="text-center mt-8 pt-5">Upload Trainee Excel Sheet</h4>
                                    <form class="row g-3" method="post" action="registerTrainee?id=${batchId}"
                                        enctype="multipart/form-data">
                                        <div class="mb-3">
                                            <label for="formFileSm" class="form-label">Upload Trainee Excel
                                                Sheet</label>
                                            <input class="form-control form-control-sm" id="formFileSm" name="file"
                                                type="file" required>
                                        </div>
                                        <div class="mb-2">
                                            <button class="py-2 px-3 rounded border bg-blue text-white text-sm"
                                                type="submit">Submit</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <script>
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