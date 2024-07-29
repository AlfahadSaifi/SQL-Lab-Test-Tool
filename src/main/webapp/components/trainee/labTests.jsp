<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page isELIgnored="false" %>
<div class="row m-0">
    <div class="col-2 mt-4 p-0" style="width: 10%">
        <jsp:include page="/components/sideBarTrainee.jsp" />
    </div>
    <div class="col-10 m-0 p-0">
        <div class="border border-gray shadow py-3 px-4 bg-white rounded">
            <div class="">
                <h3 class="my-2">Your Lab Tests</h3>
                <div class="flexwapRow g-5">
                <div class="container mt-1">
                    <ul class="nav nav-tabs" id="myTab" role="tablist">
                        <li class="nav-item" role="presentation">
                        <button class="nav-link active" id="unattempted-tab" data-bs-toggle="tab" data-bs-target="#unattempted" type="button" role="tab" aria-controls="unattempted" aria-selected="true" onclick="handleTabClick('unattempted')">Unattempted</button>
                        </li>
                        <li class="nav-item" role="presentation">
                        <button class="nav-link" id="inprogress-tab" data-bs-toggle="tab" data-bs-target="#inprogress" type="button" role="tab" aria-controls="inprogress" aria-selected="false" onclick="handleTabClick('inprogress')">Inprogress</button>
                        </li>
                        <li class="nav-item" role="presentation">
                        <button class="nav-link" id="completed-tab" data-bs-toggle="tab" data-bs-target="#completed" type="button" role="tab" aria-controls="completed" aria-selected="false" onclick="handleTabClick('completed')">Completed</button>
                        </li>
                    </ul>
                    <div class="tab-content" id="myTabContent">
                        <div class="tab-pane fade show active" id="unattempted" role="tabpanel" aria-labTestelledby="unattempted-tab">
                            <div id="unattemptedLabTestContainer" class="d-flex gap-2 p-3" style="flex-wrap: wrap; border: 1px solid #e5dbdb; border-top: 1px solid white; border-bottom-left-radius: 10px; border-bottom-right-radius: 10px;">
                                <div class="spinner-border text-primary" role="status">
                                    <span class="sr-only">Loading...</span>
                                  </div>
                           </div>
                        </div>
                        <div class="tab-pane fade" id="inprogress" role="tabpanel" aria-labTestelledby="inprogress-tab">
                        <div id="inprogressLabTestContainer" class="d-flex gap-2 p-3" style="flex-wrap: wrap; border: 1px solid #e5dbdb; border-top: 1px solid white; border-bottom-left-radius: 10px; border-bottom-right-radius: 10px;">
                            <div class="spinner-border text-primary" role="status">
                                <span class="sr-only">Loading...</span>
                              </div>
                            </div>
                        </div>
                        <div class="tab-pane fade" id="completed" role="tabpanel" aria-labTestelledby="completed-tab">
                        <div id="completedLabTestContainer" class="d-flex gap-2 p-3" style="flex-wrap: wrap; border: 1px solid #e5dbdb; border-top: 1px solid white; border-bottom-left-radius: 10px; border-bottom-right-radius: 10px;">
                            <div class="spinner-border text-primary" role="status">
                                <span class="sr-only">Loading...</span>
                              </div>
                            </div>
                        </div>
                    </div>
                    </div>            
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    let UnattemptedData=[];
    let InprogressData=[];
    let CompletedData=[];
    function handleTabClick(tabId) {
        document.querySelectorAll('.nav-link').forEach(tab => tab.classList.remove('active'));
        document.getElementById(tabId + '-tab').classList.add('active');
        document.querySelectorAll('.tab-pane').forEach(tabContent => {
            tabContent.classList.remove('show', 'active');
        });
        document.getElementById(tabId).classList.add('show', 'active');
        if (tabId == "unattempted"){
            getUnattempted();
        }else if(tabId == "inprogress"){
            getInprogress();
        } else if("completed"){
            getCompleted();
        }        
    }
    function getUnattempted(){
        $.ajax({
            type: "GET",
            url: "${pageContext.request.contextPath}/api/trainee/viewLabTestData?status=unattempted",
            contentType: "application/json",
            success: function (data) {
                renderUnattemptedLabTestCards(data)
            },
            error: function (xhr, status, error) {
                console.error("Error:", error);
            },
        });
    
    }
    function getInprogress(){
        $.ajax({
            type: "GET",
            url: "${pageContext.request.contextPath}/api/trainee/viewLabTestData?status=inprogress",
            contentType: "application/json",
            success: function (data) {
                renderInprogressLabTestCards(data)
            },
            error: function (xhr, status, error) {
                console.error("Error:", error);
            },
        });
    }
    function getCompleted(){
        $.ajax({
            type: "GET",
            url: "${pageContext.request.contextPath}/api/trainee/viewLabTestData?status=completed",
            contentType: "application/json",
            success: function (data) {
                renderCompletedLabTestCards(data)
            },
            error: function (xhr, status, error) {
                console.error("Error:", error);
            },
        });
    }
    function renderUnattemptedLabTestCards(labTestList) {
        const unattemptedLabTestContainer = document.getElementById("unattemptedLabTestContainer");
        unattemptedLabTestContainer.innerHTML = ''; 
        if (labTestList.length === 0) {
            unattemptedLabTestContainer.innerHTML = '<p>No Completed labTest available.</p>';
            return;
        }    
        labTestList.forEach(labTest => {
            const card = document.createElement('div');
            card.className = 'w-45 card shadow mb-4';
            card.innerHTML = '<div class="card-body">' +
                '<h5 class="card-title text-md">' + labTest.labTestName + '</h5>' +
                '<p class="card-text text-muted text-sm">' + labTest.noOfQuestions + ' Questions | ' + labTest.totalLabTestPoints + ' Points </p>' +
                '<div class="d-flex justify-content-end align-items-center">' +
                '<a href=${pageContext.request.contextPath}/trainee/instructionLabTest?labTestId=' + labTest.labTestId + ' class="btn btn-success btn-sm">Start LabTest</a>' +
                '</div>' +
                '</div>';
            unattemptedLabTestContainer.appendChild(card);
        });
    }
    function renderInprogressLabTestCards(labTestList) {
        const unattemptedLabTestContainer = document.getElementById("inprogressLabTestContainer");
        unattemptedLabTestContainer.innerHTML = ''; 
        if (labTestList.length === 0) {
            unattemptedLabTestContainer.innerHTML = '<p>No Completed labTest available.</p>';
            return;
        }    
        labTestList.forEach(labTest => {
            const card = document.createElement('div');
            card.className = 'w-45 card shadow mb-4';
            card.innerHTML = '<div class="card-body">' +
                '<h5 class="card-title text-md">' + labTest.labTestName + '</h5>' +
                '<p class="card-text text-muted text-sm">' + labTest.noOfQuestions + ' Questions | ' + labTest.totalLabTestPoints + ' Marks </p>' +
                '<div class="d-flex justify-content-end align-items-center">' +
                '<a href=${pageContext.request.contextPath}/trainee/labTestStart?labTestId=' + labTest.labTestId + ' class="btn btn-success btn-sm">Resume LabTest</a>' +
                '</div>' +
                '</div>';
            unattemptedLabTestContainer.appendChild(card);
        });
    }
    function renderCompletedLabTestCards(labTestList) {
    const unattemptedLabTestContainer = document.getElementById("completedLabTestContainer");
    unattemptedLabTestContainer.innerHTML = ''; 
    if (labTestList.length === 0) {
        unattemptedLabTestContainer.innerHTML = '<p>No Completed labTest available.</p>';
        return;
    }    
    labTestList.forEach(labTest => {
        const card = document.createElement('div');
        card.className = 'w-45 card shadow mb-4';
        card.innerHTML = '<div class="card-body">' +
            '<h5 class="card-title text-md">' + labTest.labTestName + '</h5>' +
            '<p class="card-text text-muted text-sm">' + labTest.noOfQuestions + ' Questions | ' + labTest.totalLabTestPoints + ' Marks </p>' +
            '<div class="d-flex justify-content-end align-items-center">' +
            '<a href=${pageContext.request.contextPath}/trainee/viewLabTestReport?labTestId=' + labTest.labTestId + ' class="btn btn-success btn-sm">View Report</a>' +
            '</div>' +
            '</div>';
        unattemptedLabTestContainer.appendChild(card);
    });
}

    const urlParams = new URLSearchParams(window.location.search);
    const activeTab = urlParams.get('tab');
    if (activeTab) {
        handleTabClick(activeTab);
    }else{
        handleTabClick("unattempted");
    }
</script>