<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
                <%@ page isELIgnored="false" %>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
<!-- <link rel="stylesheet" href="path/to/font-awesome/css/font-awesome.min.css"> -->
<div class="row m-0">
    <div class="col-2 mt-4 p-0" style="width: 10%">
        <jsp:include page="/components/sideBarTrainee.jsp" />
    </div>
    <div class="col-10 m-0 p-0">
        <div class="border border-gray shadow p-3 bg-white rounded">
            <div class="p-2">
                <h3 class="mb-4 text-center">Trainee Dashboard</h3>
                <div id="chartSpinner" class="flexCenter justify-content-center">
                    <div class="spinner-border text-primary" role="status">
                        <span class="sr-only">Loading...</span>
                    </div>
                </div>
                <div id="chartDiv" class="gap-3">
                    <div class="row justify-content-center">
                        <div class="col-6 mb-4">
                            <div class="flexCenter border shadow p-1 bg-white rounded"
                                style="width: 260px;">
                                <canvas id="labDetail" width="230" height="200">
                                    <p class="text-center">Loading...</p>
                                </canvas>
                                <div class="d-flex gap-3">
                                    <div data-toggle="tooltip" class="cursor-pointer"
                                        data-placement="bottom" title="Completed">
                                        <i class="fa-regular fa-circle-check" style="color: rgb(54, 162, 235);"
                                            aria-hidden="true">: </i><span id="no_of_completed_lab"></span>
                                    </div>
                                    <div data-toggle="tooltip" class="cursor-pointer"
                                        data-placement="bottom" title="Inprogress">
                                        <i class="fa-regular fa-hourglass-half text-gray" aria-hidden="true"></i>:
                                        <span id="no_of_inprogress_lab"></span>
                                    </div>
                                    <div data-toggle="tooltip" class="cursor-pointer"
                                        data-placement="bottom" title="Unattempted">
                                        <i class="fa-regular fa-question-circle-o text-red" aria-hidden="true"></i>:
                                        <span id="no_of_unattempted_lab"></span>
                                    </div>
                                </div>
                                <div class="text-green bold mt-1">Lab Practice Detail</div>
                            </div>
                        </div>
                        <div class="col-6 mb-4">
                            <div class="flexCenter border shadow p-1 bg-white rounded"
                                style="width: 260px;">
                                <canvas id="labQuestionDetail" width="230" height="200">
                                    <p class="text-center">Loading...</p>
                                </canvas>
                                <div class="d-flex gap-3">
                                    <div data-toggle="tooltip" class="cursor-pointer"
                                        data-placement="bottom" title="Correct">
                                        <i class="fa fa-check text-green" aria-hidden="true">: </i><span
                                            id="no_of_correct_lab"></span>
                                    </div>
                                    <div data-toggle="tooltip" class="cursor-pointer"
                                        data-placement="bottom" title="Incorrect">
                                        <i class="fa fa-times text-red" aria-hidden="true"></i>: <span
                                            id="no_of_incorrect_lab"></span>
                                    </div>
                                    <div data-toggle="tooltip" class="cursor-pointer"
                                        data-placement="bottom" title="Unattempted">
                                        <i class="fa fa-ban text-orange" aria-hidden="true"></i>: <span
                                            id="no_of_unattempted_lab_question"></span>
                                    </div>
                                </div>
                                <div class="text-green bold mt-1">Lab Practice Questions</div>
                            </div>
                        </div>
                    </div>
                    <div class="row justify-content-center">
                        <div class="col-6 mb-4">
                            <div class="flexCenter border shadow p-1 bg-white rounded"
                                style="width: 260px;">
                                <canvas id="labTestDetail" width="230" height="200">
                                    <p class="text-center">Loading...</p>
                                </canvas>
                                <div class="d-flex gap-3">
                                    <div data-toggle="tooltip" class="cursor-pointer"
                                        data-placement="bottom" title="Completed">
                                        <i class="fa fa-check" style="color: rgb(54, 162, 235);"
                                            aria-hidden="true">: </i><span
                                            id="no_of_completed_lab_test"></span>
                                    </div>
                                    <!-- <div data-toggle="tooltip" class="cursor-pointer" data-placement="bottom" title="Inprogress">
                            <i class="fa fa-hourglass-start text-gray" aria-hidden="true"></i>: <span id="no_of_inprogress_lab_test"></span>
                        </div> -->
                                    <div data-toggle="tooltip" class="cursor-pointer"
                                        data-placement="bottom" title="Unattempted">
                                        <i class="fa-regular fa-question-circle-o text-red" aria-hidden="true"></i>:
                                        <span id="no_of_unattempted_lab_test"></span>
                                    </div>
                                </div>
                                <div class="text-green bold mt-1">Lab Test Detail</div>
                            </div>
                        </div>
                        <div class="col-6 mb-4">
                            <div class="flexCenter border shadow p-1 bg-white rounded"
                                style="width: 260px;">
                                <canvas id="labTestQuestionDetail" width="230" height="200">
                                    <p class="text-center">Loading...</p>
                                </canvas>
                                <div class="d-flex gap-3">
                                    <div data-toggle="tooltip" class="cursor-pointer"
                                        data-placement="bottom" title="Correct">
                                        <i class="fa fa-check text-green" aria-hidden="true">: </i><span
                                            id="no_of_correct_lab_test"></span>
                                    </div>
                                    <div data-toggle="tooltip" class="cursor-pointer"
                                        data-placement="bottom" title="Incorrect">
                                        <i class="fa fa-times text-red" aria-hidden="true"></i>: <span
                                            id="no_of_incorrect_lab_test"></span>
                                    </div>
                                    <div data-toggle="tooltip" class="cursor-pointer"
                                        data-placement="bottom" title="unattempted">
                                        <i class="fa fa-ban text-orange" aria-hidden="true"></i>: <span
                                            id="no_of_unattempted_lab_test_question"></span>
                                    </div>
                                </div>
                                <div class="text-green bold mt-1">Lab Test Questions</div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>
<!-- The Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="exampleModalLongTitle"
    aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <!-- Modal header -->
            <div class="modal-header">
                <h5 class="modal-title" id="myModalLabel">Your Inprogress Test</h5>
                <!-- <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button> -->
            </div>
            <div class="modal-body" style="max-height: 70vh; overflow-y: auto;">
                <div id="inprogressLabTestContainer" class="d-flex gap-2 p-3"
                    style="flex-wrap: wrap; border: 1px solid #e5dbdb;  border-radius: 10px; ">
                    <div class="spinner-border text-primary" role="status">
                        <span class="sr-only">Loading...</span>
                    </div>
                </div>
            </div>
            <!-- <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div> -->
        </div>
    </div>
</div>


<script>
    $(document).ready(function () {
        $('[data-toggle="tooltip"]').tooltip();
        initializeCharts();
        getInprogress();
        // $('[data-toggle="tooltip"]').tooltip();
        $('#openModalBtn').click(function () {
            $('#myModal').modal('show');
        });
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
            url: "${pageContext.request.contextPath}/api/trainee/dashboardDetail",
            contentType: "application/json",
            success: function (data) {
                $("#chartDiv").addClass("d-flex").show();
                $("#chartSpinner").hide();
                dashboardData(data)
            },
            error: function (xhr, status, error) {
                console.error("Error:", error);
            },
        });

        function dashboardData(data) {
            let labDetail = data.labDetail;
            $("#no_of_completed_lab").append(labDetail.completedLab)
            $("#no_of_inprogress_lab").append(labDetail.resumeLab)
            $("#no_of_unattempted_lab").append(labDetail.unattemptedLab)
            const labData = {
                datasets: [{
                    label: 'Lab',
                    data: [labDetail.completedLab, labDetail.resumeLab, labDetail.unattemptedLab],
                    backgroundColor: [
                        'rgb(54, 162, 235)',  // Color for 'Completed'
                        '#A3A3A8', // Color for 'Resume'
                        'rgb(252, 66, 66)', // Color for 'Unattempted'
                    ],
                    hoverOffset: 4
                }],
                labels: [
                    'Completed',
                    'Inprogress',
                    'Unattempted',
                ],
            };

            const labDataOptions = {
                onClick: (event, elements) => {
                    if (elements.length > 0) {
                        const datasetIndex = elements[0].datasetIndex;
                        const index = elements[0]._index;
                        const label = labData.labels[index];
                        window.location.href = '${pageContext.request.contextPath}/trainee/labs?tab=' + label.toLowerCase();
                    }
                }
            };

            let labTestDetail = data.labTestDetail;
            $("#no_of_unattempted_lab_test").append(labTestDetail.unattemptedLabTest)
            $("#no_of_completed_lab_test").append(labTestDetail.completedLabTest)
            const labTestData = {
                datasets: [{
                    label: 'Lab',
                    data: [labTestDetail.completedLabTest, labTestDetail.unattemptedLabTest],
                    backgroundColor: [
                        'rgb(54, 162, 235)',
                        'rgb(252, 66, 66)',
                    ],
                    hoverOffset: 4
                }],
                labels: [
                    'Completed',
                    'Unattempted',
                ]
            };
            let labQuestion = data.labQuestion;
            $("#no_of_correct_lab").append(labQuestion.correctLabQuestion);
            $("#no_of_incorrect_lab").append(labQuestion.incorrectLabQuestion);
            $("#no_of_unattempted_lab_question").append(labQuestion.unattemptedLabQuestion);
            const labQuestionData = {
                datasets: [{
                    label: 'Lab Question',
                    data: [labQuestion.correctLabQuestion, labQuestion.incorrectLabQuestion, labQuestion.unattemptedLabQuestion],
                    backgroundColor: [
                        '#5ece53',
                        'rgb(252, 66, 66)',
                        'rgb(255, 205, 86)' 
                    ],
                    hoverOffset: 4
                }],
                labels: [
                    'Correct',
                    'Incorrect',
                    'Unattempted'
                ]
            };
            let labTestQuestion = data.labTestQuestion;
            $("#no_of_correct_lab_test").append(labTestQuestion.correctLabTestQuestion)
            $("#no_of_incorrect_lab_test").append(labTestQuestion.incorrectLabTestQuestion)
            $("#no_of_unattempted_lab_test_question").append(labTestQuestion.unattemptedLabTestQuestion)
            const labTestQuestionData = {
                datasets: [{
                    label: 'Lab Test Question',
                    data: [labTestQuestion.correctLabTestQuestion, labTestQuestion.incorrectLabTestQuestion, labTestQuestion.unattemptedLabTestQuestion],
                    backgroundColor: [
                        '#5ece53', // Color for 'Correct'
                        'rgb(255, 99, 132)', // Color for 'InCorrect'
                        'rgb(255, 205, 86)'  // Color for 'unattempted'
                    ],
                    hoverOffset: 4
                }],
                labels: [
                    'Correct',
                    'Incorrect',
                    'Unattempted'
                ]
            };
            const labTestDataOptions = {
                onClick: (event, elements) => {
                    if (elements.length > 0) {
                        const datasetIndex = elements[0].datasetIndex;
                        const index = elements[0]._index;
                        const label = labTestData.labels[index];
                        window.location.href = '${pageContext.request.contextPath}/trainee/labTests?tab=' + label.toLowerCase();
                    }
                }
            };

            const labConfig = {
                type: 'pie',
                data: labData,
                options: labDataOptions
            };
        

            const labTestConfig = {
                type: 'pie',
                data: labTestData,
                options: labTestDataOptions

            };
            const labQuestionConfig = {
                type: 'pie',
                data: labQuestionData,
               
            };
            const labTestQuestionConfig = {
                type: 'pie',
                data: labTestQuestionData,
                
            };
            const canvas = document.getElementById('labDetail');
            const ctx = canvas.getContext('2d');
            canvas.width = 450;
            canvas.height = 400;
            const myChart = new Chart(ctx, labConfig);

            const canvas2 = document.getElementById('labTestDetail');
            const ctx2 = canvas2.getContext('2d');
            canvas2.width = 450;
            canvas2.height = 400;
            const myChart2 = new Chart(ctx2, labTestConfig);

            const canvas3 = document.getElementById('labQuestionDetail');
            const ctx3 = canvas3.getContext('2d');
            canvas3.width = 450;
            canvas3.height = 400;
            const myChart3 = new Chart(ctx3, labQuestionConfig);
            const canvas4 = document.getElementById('labTestQuestionDetail');
            const ctx4 = canvas4.getContext('2d');
            canvas4.width = 450;
            canvas4.height = 400;
            const myChart4 = new Chart(ctx4, labTestQuestionConfig);
        }

        function getInprogress() {
            $.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/api/trainee/viewLabTestData?status=inprogress",
                contentType: "application/json",
                success: function (data) {
                    if (data.length != 0) {
                        $('#myModal').modal('show');
                        renderInprogressLabTestCards(data)
                    }
                },
                error: function (xhr, status, error) {
                    console.error("Error:", error);
                },
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
                    '<p class="card-text text-muted text-sm">' + labTest.noOfQuestions + ' Questions | ' + labTest.noOfQuestions + ' Marks </p>' +
                    '<div class="d-flex justify-content-end align-items-center">' +
                    '<a href=${pageContext.request.contextPath}/trainee/labTestStart?labTestId=' + labTest.labTestId + ' class="btn btn-success btn-sm">Resume LabTest</a>' +
                    '</div>' +
                    '</div>';
                unattemptedLabTestContainer.appendChild(card);
            });
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