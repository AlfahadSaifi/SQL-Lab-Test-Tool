<%@ page isELIgnored="false" %>
<body>
    <nav class="navbar navbar-expand-lg bg-body-tertiary">
        <div class="container-fluid">
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarTogglerDemo02"
                aria-controls="navbarTogglerDemo02" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarTogglerDemo02" style="margin-left: 140px;">
                <ul class="nav justify-content-end">
                    <div class="user-name-text">
                        <bold>Hi ! <span> ${username}</span></bold>
                    </div>
                </ul>
            </div>
            <div class="d-flex " >
                <div class="text-success fw-bold" id="currentTime"></div>
            </div>
        </div>
    </nav>
