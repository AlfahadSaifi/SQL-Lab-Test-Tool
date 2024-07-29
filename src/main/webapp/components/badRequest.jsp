<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
  <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
    <%@ page isELIgnored="false" %>
      <div class="row m-0">
        <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
          <jsp:include page="/components/sideBar.jsp" />
        </div>
        <div class="col-9 m-0 p-2" style="width: 80%">
          <div class="container-fluid shadow rounded p-5 mb-5 bg-white ">
            <div style="display:flex;">
              <div class="container-fluid my-3">
                <div class="w-50 border border-gray shadow p-3 bg-white rounded mx-auto">
                  <div class="container-fluid p-3  rounded">
                    <h1 class="text-center text-danger py-3 my-3" style="font-size:50px;">
                      <b>Bad Gateway. <br>Please go Back.</b>
                    </h1>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      