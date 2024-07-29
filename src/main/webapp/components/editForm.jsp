<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page isELIgnored="false"%>
    <h2 class="text-center mt-8 pt-5">Customer Registration</h3>
        <div class="container-fluid m-8 w-50 shadow-sm p-3 mb-5 bg-white rounded">
            <form:form action="editCustomer" method="post" modelAttribute="customerDto" class="needs-validation"
                id="registration-form">
                <div class="row">
                <form:hidden path="recordStatus"/>
                    <div class="col-md-6">
                        <form:label class="form-label" path="customerCode">Customer Code:</form:label>
                        <form:input path="customerCode" readonly = "true"/>
                        <form:errors cssStyle="color:blue" path="customerCode" /><br>
                    </div>
                    <div class="col-md-6">
                        <form:label class="form-label" path="customerName">Customer Name</form:label>
                        <form:input path="customerName" />
                        <form:errors cssStyle="color:blue" path="customerName"/><br>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <form:label class="form-label" path="customerPinCode">Customer Pin Code</form:label>
                        <form:input type="number" path="customerPinCode" />
                        <form:errors cssStyle="color:blue" path="customerPinCode" /><br>
                    </div>
                    <div class="col-md-6">
                        <form:label class="form-label" path="emailAddress">Customer Email</form:label>
                        <form:input type="email" path="emailAddress" />
                        <form:errors cssStyle="color:blue" path="emailAddress" /><br>
                    </div>
                </div>

                <div class="row">
                    <div class=" col-md-6">
                        <form:label class="form-label" path="contactNumber">Contact Number</form:label>
                        <form:input path="contactNumber" />
                        <form:errors cssStyle="color:blue" path="contactNumber" /><br>
                    </div>
                    <div class="col-md-6">

                        <form:label class="form-label" path="primaryContactNumber">Primary Contact Person</form:label>
                        <form:input path="primaryContactNumber" />
                        <form:errors cssStyle="color:blue" path="primaryContactNumber" /><br>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <form:label class="form-label" path="customerAddress1">Customer Address1</form:label>
                        <form:textarea rows="3" cols="20" path="customerAddress1" />
                        <form:errors  path="customerAddress1" cssStyle="color:blue" /><br>
                    </div>
                    <div class=" col-md-6">
                        <form:label class="form-label" path="customerAddress2">Customer Address2</form:label>
                        <form:textarea rows="3" cols="20" path="customerAddress2" />
                        <form:errors cssStyle="color:blue" path="customerAddress2" /><br>
                    </div>
                </div>

                <div class="form-row">
                    <form:label class="form-label" path="flag">Status Flag</form:label>
                    <form:select class="form-select form-group col-md-2" path="flag">
                        <form:option value="A" label="Active" />
                        <form:option value="I" label="Inactive" />
                    </form:select>
                    <form:errors cssStyle="color:blue" path="flag" /><br>
                </div>
                <button class="btn btn-primary"  type="submit">Save</button>
                <button class="btn btn-primary"  type="reset"> Clear</button>
                <a href="${pageContext.request.contextPath}/maker/home" class="btn btn-primary">Cancel</a>
            </form:form>
        </div>