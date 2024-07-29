<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
    <%@ page isELIgnored="false" %>
        <div style="display:flex; margin-top:30px;">
            <div class="container-fluid m-8 w-50 shadow-sm p-3 mb-5 bg-white rounded">
                <h2 class="text-center mt-8 pt-5">Create Lab</h3>
            </div>

            <div>
                <div class="container-fluid shadow-sm p-3 mb-5 bg-white rounded">
                    <form class="row g-3" method="post" action="uploadFile" enctype="multipart/form-data">
                        <div class="mb-3">
                            <label for="formFileSm" class="form-label">Upload Excel Sheet</label>
                            <input class="form-control form-control-sm" id="formFileSm" name="file" type="file" required>
                        </div>
                        <button class="btn btn-primary" type="submit">Submit</button>
                    </form>
                </div>
            </div>
        </div>