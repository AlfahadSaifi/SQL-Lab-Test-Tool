<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page isELIgnored="false" %>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
<div class="px-5">
<c:if test="${not empty successMessage}">
<div class="alert alert-success">${successMessage}</div>
</c:if>
<c:if test="${not empty errorMessage}">
<div class="alert alert-danger">${errorMessage}</div>
</c:if>
</div>
<div style="display: flex; margin-top: 30px;">
<div class="container-fluid m-8 w-50 shadow-sm p-5 mb-5 bg-white rounded">
<h2 class="text-center">Register Admin</h2>
<form:form action="" method="post" modelAttribute="admin" class="my-4 needs-validation" id="registration-form">
    <div class="row mb-3">
        <div class="col-md-6">
            <form:label class="text-md text-dark-100 mb-1 bold" path="employeeId">Employee Id</form:label>
            <form:input path="employeeId" class="form-control" placeholder="Enter Employee Id" />
            <form:errors cssStyle="color:blue" path="employeeId" />
        </div>
        <div class="col-md-6">
            <form:label class="text-md text-dark-100 mb-1 bold" path="name">Name</form:label>
            <form:input path="name" class="form-control" placeholder="Enter Employee Name" />
            <form:errors cssStyle="color:blue" path="name" />
        </div>
    </div>
    <div class="row mb-3">
        <div class="col-md-6">
            <form:label class="text-md text-dark-100 mb-1 bold" path="emailId">Email</form:label>
            <form:input type="email" path="emailId" class="form-control" placeholder="Enter Employee Email" />
            <form:errors cssStyle="color:blue" path="emailId" />
        </div>
        <div class="col-md-6">
            <div class="d-flex ">
                <label for="password" class="text-md text-dark-100 mb-1 bold">Password <span
                        class="text-red-600 ml-1">*</span></label>
                <div class="password-toggle-icon px-2 z-3" data-placement="right"
                    data-bs-toggle="tooltip"
                    title="Password must contain at least 8 characters, including uppercase, lowercase, numbers, and special characters.">
                    <i class="fa-regular fa-question-circle" style="color: rgb(172, 72, 72);"></i>
                </div>
            </div>
            <div class="input-group mb-3">
                <input name="password" id="password" type="password" class="form-control" placeholder="Enter your password"
                    aria-label="Password" aria-describedby="password-addon">
                <button class="btn btn-outline-secondary" type="button" id="password-addon">
                    <i class="fa fa-eye-slash" aria-hidden="true"></i>
                </button>
            </div>
        </div>
        <div class="col-md-6">
            <button id="registerBtn" class="py-2 px-3 rounded border bg-blue text-white text-sm" disabled type="submit">
                Register
            </button>
            <button class="py-2 px-3 rounded border bg-green text-white text-sm" type="reset">
                Reset
            </button>
            <a href="${pageContext.request.contextPath}/admin/" class="py-2 px-3 rounded border bg-red text-white text-sm">
                Cancel
            </a>
        </div>
        <div class="col-md-6">
            <div class="" id="passwordError"
            style="color: red; display: none;">
            Invalid Password.
        </div>
        </div>
    </div>
    
</form:form>
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
return "Password must contain"+ messages.join(', ');
}
}



var passwordField = document.getElementById('password');
passwordField.addEventListener('blur', function () {
var validationResult = validatePassword(passwordField.value);
const passwordErrorDiv = document.getElementById('passwordError');
if (validationResult !== 'Password is valid.') {
    passwordErrorDiv.style.display = 'block';
    passwordError.innerText=validationResult
} else {
    passwordErrorDiv.style.display = 'none';
    document.getElementById("registerBtn").disabled = false;
}
});
</script>