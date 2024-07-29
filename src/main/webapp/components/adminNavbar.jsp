<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ page isELIgnored="false" %>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">

<style>
    .dropdown-toggle::after {
          content: none;
    }
</style>
        <body>
            <nav class="navbar navbar-expand-lg bg-body-tertiary">
                <div class="container-fluid">
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                        data-bs-target="#navbarTogglerDemo02" aria-controls="navbarTogglerDemo02" aria-expanded="false"
                        aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarTogglerDemo02" style="margin-left: 140px;">
                        <ul class="nav justify-content-end">
                            <li class="nav-item">
                                <a class="nav-link user-name-text" aria-current="page"
                                    href="${pageContext.request.contextPath}/admin/dashboard">Home</a>
                            </li>
                        </ul>
                    <ul class="nav justify-content-end">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle user-name-text" href="#" id="navbarDropdown"
                                role="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false"
                                style="text-decoration: none; position: relative; background-color: transparent;">
                                <i class="fas fa-user"></i> Hi ! <span>${username}</span>
                            </a>
                            <div class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                                <button class="dropdown-item" data-bs-toggle="modal"
                                    data-bs-target="#changePasswordModal">Change Password</button>
                            </div>
                        </li>
                    </ul>
                    </div>
                    <div class="d-flex ">
                        <ul class="nav justify-content-end">
                            <li class="">
                                <a class="px-5 text-red bold" aria-current="page"
                                    href="${pageContext.request.contextPath}/logout">Logout</a>
                            </li>
                        </ul>
                        <div class="text-success fw-bold">
                            Time:
                            <span class="text-success fw-bold" id="currentTime">
                            </span>
                        </div>
                    </div>
                </div>
            </nav>
  <div class="modal fade" id="changePasswordModal" tabindex="-1" aria-labelledby="changePasswordModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="changePasswordModalLabel">Change Password</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label for="newPassword" class="form-label">New Password</label>
                        <input type="password" class="form-control" id="newPassword" required>
                    </div>
                    <div class="mb-3">
                        <label for="confirmPassword" class="form-label">Confirm New Password</label>
                        <input type="password" class="form-control" id="confirmPassword" required>
                    </div>
                    <div id="passwordAlert" class="alert alert-danger d-none" role="alert">
                        Passwords do not match.
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" id="submitPasswordBtn">Save changes</button>
                </div>
            </div>
        </div>
    </div>


    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz"
            crossorigin="anonymous"></script>
    <script src="<c:url value='/resources/js/index.js' />"></script>
    <script>
        $(document).ready(function () {
            $("#confirmPassword").on('paste', function (event) {
                event.preventDefault();
            });

            $("#submitPasswordBtn").on("click", function () {
                submitPassword();
            });
        });

        function submitPassword() {
            var newPassword = $("#newPassword").val();
            var confirmPassword = $("#confirmPassword").val();

            if (newPassword === confirmPassword) {
                $.ajax({
                    type: "POST",
                    url: "${pageContext.request.contextPath}/api/admin/changePassword",
                    data: {password: newPassword},
                    success: function (response) {
                        alert("Password changed! You need to login again");
                        setTimeout(function () {
                            location.reload();
                        }, 500);
                        clearPasswordFields();
                    },
                    error: function (xhr, status, error) {
                        clearPasswordFields();
                        console.log(error);
                        $("#passwordAlert").removeClass("d-none").text("Please try again after sometime!");
                    }
                });
            } else {
                $("#passwordAlert").removeClass("d-none");
            }
        }

        function clearPasswordFields() {
            $("#newPassword").val("");
            $("#confirmPassword").val("");
            $("#passwordAlert").addClass("d-none");
        }
    </script>
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const form = document.querySelector('form');
            const submitButton = form.querySelector('button[type="submit"]');

            form.addEventListener('submit', function () {
                submitButton.disabled = true;
            });
        });

        $(document).ready(function () {
            function removeMessages() {
                if ($('#successMessage, #errorMessage').length > 0) {
                    $('#successMessage, #errorMessage').fadeOut(3000, function () {
                        $(this).remove();
                    });
                }
            }
            removeMessages();
        });
    </script>
</body>