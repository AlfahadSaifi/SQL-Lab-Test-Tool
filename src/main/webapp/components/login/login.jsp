<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
<div>
    <div class="row m-0 p-0 h-100">
        <div class="col-6 bg-signin">
            <div class="p-5">
                <div class="h3 pt-5 pb-2 bold" style="color: white; font-size: 35px; font-weight: 700;">Smart
                    Sql Lab</div>
                <div class="yellowLine">
                </div>
                <div class="text-md mb-1 bold text-white">Your gateway to seamless </div>
                <div class="text-md mb-1 bold text-white">testing and learning!</div>
            </div>
        </div>
        <div class="col-6 p-5">
            <div class="p-5">
                <div>
                    <div class="p-color text-3xl"><strong>Welcome </strong></div>
                    <div class="text-dark-0">Please enter your details and join us!</div>
                </div>
                <form action="login" method="post">
                    <div class="mb-3 mt-2">
                        <label for="name" class="text-md text-dark-100 mb-1 bold">Username <span
                                class="text-red-600 ml-1">*</span></label>
                        <input type="text" class="form-control" name="username" id="name"
                            placeholder="Enter your name" required>
                    </div>

                    <div class="mb-3">
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
                        <div class="" id="passwordError"
                            style="color: red; display: none;">
                            Invalid Password.
                        </div>
                    </div>
                    <div class="">
                        <c:set var="error" value="${param.error}" />
                        <c:if test="${error==true}">
                            <div class=" shadow-sm p-3 rounded alert alert-danger pt-6" role="alert">
                                Invalid Username or Password.
                            </div>
                        </c:if>
                    </div>
                    <div class="w-100">
                        <button type="submit" id="locginBtn"
                            class="py-2 rounded border w-100 bg-p text-white bold" >Login</button>
                    </div>
                </form>
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
        return "Password must contain"+ messages.join(', ');
    }
}



    var passwordField = document.getElementById('password');
    passwordField.addEventListener('blur', function () {
        // var validationResult = validatePassword(passwordField.value);
        const passwordErrorDiv = document.getElementById('passwordError');
        if (validationResult !== 'Password is valid.') {
            passwordErrorDiv.style.display = 'block';
            passwordError.innerText=validationResult
        } else {
            passwordErrorDiv.style.display = 'none';
            // document.getElementById("loginBtn").disabled = false;
        }
    });
</script>