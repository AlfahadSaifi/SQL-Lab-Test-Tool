<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
            <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ page isELIgnored="false" %>
            <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.js"></script>
            <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
            <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
            <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
            <div class="row m-0">
                <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
                    <jsp:include page="/components/sideBar.jsp" />
                </div>
                <div class="col-9 m-0 p-2" style="width: 80%;">
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger">${errorMessage}</div>
                        </c:if>
                    <div class="border border-gray shadow p-3 bg-white rounded">
                        <div class="p-2">
                            <h3 class="mb-4 text-center">Admin Dashboard</h3>
                            <div id="chartSpinner" class="flexCenter justify-content-center">
                                <div class="spinner-border text-primary" role="status">
                                    <span class="sr-only">Loading...</span>
                                </div>
                            </div>
                            <div id="chartDiv" class="container">
                                <div class="row">
                                    <div class="col-md-4 mb-4">
                                        <div class="flexCenter border shadow p-1 bg-white rounded"
                                            style="width: 260px;">
                                            <canvas id="batchDetail" width="230" height="200">
                                                <p class="text-center">Loading...</p>
                                            </canvas>
                                            <div class="d-flex gap-3">
                                                <div class="cursor-pointer" data-toggle="tooltip"
                                                    data-placement="bottom" title="Completed">
                                                    <i class="fa-regular fa-circle-check"
                                                        style="color: rgb(54, 162, 235);" aria-hidden="true"></i>:
                                                    <span id="active_batch"></span>
                                                </div>
                                                <div class="cursor-pointer" data-toggle="tooltip"
                                                    data-placement="bottom" title="Inprogress">
                                                    <i class="fa-regular fa-circle-xmark" style="color: #A3A3A8"
                                                        aria-hidden="true"></i>:
                                                    <span id="inactive_batch"></span>
                                                </div>
                                            </div>
                                            <div class="text-green bold mt-2">Batch Details</div>
                                        </div>
                                    </div>
                                    <div class="col-md-4 mb-4">
                                        <div class="flexCenter border shadow p-1 bg-white rounded"
                                            style="width: 260px;">
                                            <canvas id="labDetail" width="230" height="200">
                                                <p class="text-center">Loading...</p>
                                            </canvas>
                                            <div class="d-flex gap-3">
                                                <div class="cursor-pointer" data-toggle="tooltip"
                                                    data-placement="bottom" title="Assigned">
                                                    <i class="fa-solid fa-list-check" style="color: #5ece53;"
                                                        aria-hidden="true"></i>:
                                                    <span id="assigned_lab"></span>
                                                </div>
                                                <div class="cursor-pointer" data-toggle="tooltip"
                                                    data-placement="bottom" title="Unassigned">
                                                    <i class="fa-solid fa-link-slash" style="color: rgb(255, 99, 132)"
                                                        aria-hidden="true"></i>:
                                                    <span id="unassigned_lab"></span>
                                                </div>
                                            </div>
                                            <div class="text-green bold mt-2">Assignment Details</div>
                                        </div>
                                    </div>
                                    <div class="col-md-4 mb-4">
                                        <div class="flexCenter border shadow p-1 bg-white rounded"
                                            style="width: 260px;">
                                            <canvas id="labTestDetail" width="230" height="200">
                                                <p class="text-center">Loading...</p>
                                            </canvas>
                                            <div class="d-flex gap-3">
                                                <div class="cursor-pointer" data-toggle="tooltip"
                                                    data-placement="bottom" title="Assigned">
                                                    <i class="fa-solid fa-list-check" style="color: #5ece53;"
                                                        aria-hidden="true"></i>:
                                                    <span id="assigned_lab_test"></span>
                                                </div>
                                                <div class="cursor-pointer" data-toggle="tooltip"
                                                    data-placement="bottom" title="Unassigned">
                                                    <i class="fa-solid fa-link-slash" style="color: rgb(255, 99, 132)"
                                                        aria-hidden="true"></i>:
                                                    <span id="unassigned_lab_test"></span>
                                                </div>
                                            </div>
                                            <div class="text-green bold mt-2">Assessment Details</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <br/>
                            <br/>
                            <br/>
                        </div>
                    </div>
                </div>
            </div>
            <script>
                $(document).ready(function () {
                    $('[data-toggle="tooltip"]').tooltip();
                    initializeCharts();
                    function initializeCharts() {
                        $("#chartDiv").hide();
                        const canvasElements = document.querySelectorAll('canvas');
                        canvasElements.forEach((canvas) => {
                            const ctx = canvas.getContext('2d');
                            ctx.font = '16px Arial';
                            ctx.fillStyle = 'black';
                            ctx.textAlign = 'center';
                            ctx.fillText('Loading...', canvas.width / 2, canvas.height / 2);
                        });
                    }

                    $.ajax({
                        type: "GET",
                        url: "${pageContext.request.contextPath}/api/admin/adminDashboardDetail",
                        contentType: "application/json",
                        success: function (data) {
                            $("#chartDiv").show();
                            $("#chartSpinner").hide();
                            dashboardData(data)
                        },
                        error: function (xhr, status, error) {
                            console.error("Error:", error);
                        },
                    });

                    function dashboardData(data) {
                        let batch = data.adminBatchDetails;
                        let lab = data.adminLabDetails;
                        let labTest = data.adminLabTestDetails;
                        $("#active_batch").empty().append(batch.noOfActiveBatch);
                        $("#inactive_batch").empty().append(batch.noOfClosedBatch);
                        const batchData = {
                            datasets: [{
                                label: 'Lab',
                                data: [batch.noOfClosedBatch, batch.noOfActiveBatch],
                                backgroundColor: [
                                    '#A3A3A8',
                                    'rgb(47, 165, 243)',
                                ],
                                hoverOffset: 4
                            }],
                            labels: [
                                'closed',
                                'active'
                            ]
                        };

                        const batchOptions = {
                            onClick: (event, elements) => {
                                if (elements.length > 0) {
                                    const datasetIndex = elements[0].datasetIndex;
                                    const index = elements[0]._index;
                                    const label = batchData.labels[index];
                                    window.location.href = '${pageContext.request.contextPath}/admin/viewBatches?tab=' + label.toLowerCase();
                                }
                            }
                        };
                        $("#assigned_lab").empty().append(lab.noOfAssignedLab)
                        $("#unassigned_lab").empty().append(lab.noOfUnAssignedLab)
                        const labData = {
                            datasets: [{
                                label: 'Lab',
                                data: [lab.noOfAssignedLab, lab.noOfUnAssignedLab],
                                backgroundColor: [
                                    '#5ece53', // Color for 'Correct'
                                    'rgb(255, 99, 132)',
                                ],
                                hoverOffset: 4
                            }],
                            labels: [
                                'assigned',
                                'unassigned'
                            ]
                        };
                        const labOptions = {
                            onClick: (event, elements) => {
                                if (elements.length > 0) {
                                    const datasetIndex = elements[0].datasetIndex;
                                    const index = elements[0]._index;
                                    const label = labData.labels[index];
                                    window.location.href = '${pageContext.request.contextPath}/admin/viewAssignLab?tab=' + label.toLowerCase();
                                }
                            }
                        };
                        $("#assigned_lab_test").empty().append(labTest.noOfAssignedLabTest)
                        $("#unassigned_lab_test").empty().append(labTest.noOfUnAssignedLabTest)
                        const labTestData = {
                            datasets: [{
                                label: 'Lab Test',
                                data: [ labTest.noOfAssignedLabTest,labTest.noOfUnAssignedLabTest],
                                backgroundColor: [
                                    '#5ece53', // Color for 'Correct'
                                    'rgb(255, 99, 132)' // Color for 'Skip'
                                ],
                                hoverOffset: 4
                            }],
                            labels: [
                                'assigned',
                                'unassigned',
                            ]
                        };
                        const labTestOptions = {
                            onClick: (event, elements) => {
                                if (elements.length > 0) {
                                    const datasetIndex = elements[0].datasetIndex;
                                    const index = elements[0]._index;
                                    const label = labTestData.labels[index];
                                    window.location.href = '${pageContext.request.contextPath}/admin/viewAssignLabTest?tab=' + label.toLowerCase();
                                }
                            }
                        };

                        const batchConfig = {
                            type: 'pie',
                            data: batchData,
                            options: batchOptions
                        };


                        const labConfig = {
                            type: 'pie',
                            data: labData,
                            options: labOptions

                        };
                        const labTestConfig = {
                            type: 'pie',
                            data: labTestData,
                            options: labTestOptions
                        };
                        const canvas = document.getElementById('batchDetail');
                        const ctx = canvas.getContext('2d');
                        canvas.width = 450;
                        canvas.height = 400;
                        const myChart = new Chart(ctx, batchConfig);


                        const canvas2 = document.getElementById('labDetail');
                        const ctx2 = canvas2.getContext('2d');
                        canvas2.width = 450;
                        canvas2.height = 400;
                        const myChart2 = new Chart(ctx2, labConfig);

                        const canvas3 = document.getElementById('labTestDetail');
                        const ctx3 = canvas3.getContext('2d');
                        canvas3.width = 450;
                        canvas3.height = 400;
                        const myChart3 = new Chart(ctx3, labTestConfig);



                    }
                })
                Chart.plugins.register({
                    afterDraw: function (chart) {
                        if (chart.data.datasets[0].data.every(item => item === 0)) {
                            let ctx = chart.chart.ctx;
                            let width = chart.chart.width;
                            let height = chart.chart.height;
                            chart.clear();
                            ctx.save();
                            let fontSize = 20;
                            ctx.font = fontSize + 'px Arial';
                            ctx.textAlign = 'center';
                            ctx.textBaseline = 'middle';
                            ctx.fillText('No data to display', width / 2, height / 2);
                            ctx.restore();
                        }
                    }
                });

            </script>