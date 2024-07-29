<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ page isELIgnored="false" %>
        <div id="grayScreen"
            style="display: none; position: fixed; z-index: 20; top: 0; left: 0; width: 100%; height: 100%; background-color: rgba(0, 0, 0, 0.5);">
            <div id="normalScreenMessage"
                style="position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); color: white;">
                <div class="flexCenter">
                    <div>
                        Shifted to Full Screen Mode
                    </div>

                    <button id="fullScreenButton" class="py-2 px-3 rounded border bg-blue text-white text-sm">Go to
                        Fullscreen</button>
                </div>
            </div>
        </div>
        <div class="row m-0">
            <div class="col-6">
                <h3 class="text-center ">SQL Lab Test Portal</h3>
            </div>
            <!-- <div class="col-2"> -->
                <!-- <div id="attemptsPerQuestion" class="text-md text-dark-100 m-1">Attempts : </div> -->
            <!-- </div> -->
            <div class="col-2 timerDiv">
                <div class="text-md text-dark-100 m-1">Time Left: <span id="timer">
                        <div class="spinner-border text-primary" role="status">
                            <span class="sr-only">Loading...</span>
                        </div>
                    </span>
                </div>
            </div>
            <div class="col-2">
                <a id="downloadLink"
                    href="${pageContext.request.contextPath}/trainee/downloadLabTest/${labTestDto.labTestId}"
                    class="py-2 px-3 rounded border bg-blue text-white text-sm float-end">Reference</a>
            </div>
        </div>
        <div class="row mx-0 my-4">
            <div class="col-9">
                <div class="container-fluid shadow-sm p-3 bg-white rounded border-gray ">
                    <div class="m-2">
                        <div>
                            <div class="text-md m-2 d-flex justify-content-between">
                                <div  id="question" style="width: 85%; line-height: 2rem;">Your Question is Loading...
                                    <div class="spinner-border text-primary" role="status">
                                        <span class="sr-only">Loading...</span>
                                    </div>
                                </div>
                                <div class="text-sm bold"  >
                                    <span id="questionPoints"></span>
                                </div>
                            </div>
                            <div class="m-2 mt-3">
                                <textarea class="form-control" id="answer" rows="5" name="query"
                                    placeholder="Write the Query" required autocomplete="off"></textarea>
                            </div>
                            <div class="mt-4 m-2">
                                <button type="submit" id="submitQuestion" class="py-2 px-3 rounded border bg-blue text-white text-sm">Submit</button>
                                <button type="button" id="clearBtn" class="py-2 px-3 rounded border bg-green text-white text-sm">Clear</button>
                            </div>
                        </div>
                        <div id="loading"></div>
                        <div class="m-2">
                            <h3>Result: <span id="outputMessage" class="text-md bold text-success"></span></h3>
                            <div id="resultTable" class="shadow-sm p-3 mb-5 rounded alert alert-warning pt-6"
                                role="alert" style="overflow: scroll; max-height: 300px;">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-3">
                <div class="d-flex timerDiv">
                    Correct <div class="quesDiv greenQues centerQues">1</div>
                    Wrong <div class="quesDiv redQues centerQues">1</div>
                </div>
                <div class="text-md text-dark-100 m-1 ">Questions</div>
                <div class="border-gray quesBox" id="quesBox">
                    <div class="spinner-border text-primary" role="status">
                        <span class="sr-only">Loading...</span>
                    </div>
                </div>
                <div class="my-3">
                    <button type="submit" id="submitYourTest" class="py-2 px-3 rounded border bg-red text-white text-sm">Submit you test</button>
                </div>
            </div>
        </div>
        <div class="modal" id="loadingModal" style="display: none;">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-body text-center">
                        Loading...
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="erDiagramModal" tabindex="-1" role="dialog" aria-labelledby="erDiagramModalLabel"
            aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="erDiagramModalLabel">ER Diagram</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <iframe id="erDiagramIframe" style="width: 100%; height: 600px;">
                        </iframe>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="notAvailableModal" tabindex="-1" role="dialog" aria-labelledby="notAvailableModalLabel"
        aria-hidden="true">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="notAvailableModalLabel">ER Diagram</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <h3 class="text-center">Not Available</h3>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
        <script>
            //   document.addEventListener("contextmenu", (event) => {
            //      event.preventDefault();
            //   });
            let redirect = true;
            let countTabSwitch = getCountTabSwitch();
            function getCountTabSwitch() {
                let countTabSwitch = localStorage.getItem('countTabSwitch');
                return countTabSwitch !== null ? parseInt(countTabSwitch) : 100;
            }

            window.addEventListener('beforeunload', function (e) {
                var currentUrl = window.location.href;
                var excludedSubstring = 'viewLabTestReport';
                if (redirect) {
                    e.preventDefault();
                    e.returnValue = '';
                    var confirmationMessage = 'Are you sure you want to leave this page? Your changes may not be saved.';
                    e.returnValue = confirmationMessage;
                    return confirmationMessage;
                }
            });
            function handleVisibilityChange() {
                if (document.hidden || document.webkitHidden || document.mozHidden || document.msHidden) {
                    countTabSwitch--;
                    alert("Tab Switch is not allow warning!!! " + (countTabSwitch) + " left")
                }
            }
            document.addEventListener('visibilitychange', handleVisibilityChange);
            // document.addEventListener('webkitvisibilitychange', handleVisibilityChange);
            // document.addEventListener('mozvisibilitychange', handleVisibilityChange);
            // document.addEventListener('msvisibilitychange', handleVisibilityChange);
            $(document).ready(function () {
                let isFullScreen = false;
                let leftTime = 0;
                const queryString = window.location.search;
                const urlParams = new URLSearchParams(queryString);
                const labTestId = urlParams.get('labTestId')
                let batchId = 0;
                let labTestName;
                let questions = [];
                let questionDetailStatusList = [];
                let question;
                $('#grayScreen').show();
                getQuestion();
                function handleFullScreenChange() {
                    isFullScreen = document.fullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement || document.msFullscreenElement;
                    if (!isFullScreen) {
                        $('#normalScreenMessage').show();
                        $('#grayScreen').show();
                    } else {
                        $('#normalScreenMessage').hide();
                        $('#grayScreen').hide();
                    }
                }
                document.addEventListener('fullscreenchange', handleFullScreenChange);
                document.addEventListener('mozfullscreenchange', handleFullScreenChange);
                document.addEventListener('webkitfullscreenchange', handleFullScreenChange);
                document.addEventListener('msfullscreenchange', handleFullScreenChange);

                // Request full screen
                $('#fullScreenButton').click(function () {
                    let elem = document.documentElement;
                    if (elem.requestFullscreen) {
                        elem.requestFullscreen();
                    } else if (elem.webkitRequestFullscreen) {
                        elem.webkitRequestFullscreen();
                    } else if (elem.msRequestFullscreen) {
                        elem.msRequestFullscreen();
                    }
                });

                $('#downloadLink').on('click', function (event) {
                    event.preventDefault();
                    $.ajax({
                        url: $(this).attr('href'),
                        method: 'GET',
                        xhrFields: {
                            responseType: 'blob'
                        },
                        success: function (data, status, xhr) {
                            var blob = new Blob([data], { type: 'application/pdf' });
                            var iframe = document.getElementById('erDiagramIframe');
                            iframe.src = window.URL.createObjectURL(blob);
                            $('#erDiagramModal').modal('show');
                        },
                        error: function (xhr, status, error) {
                            $('#notAvailableModal').modal('show');
                        }
                    });
                });

                function getQuestion() {
                    $.ajax({
                        type: "GET",
                        url: "${pageContext.request.contextPath}/api/trainee/getLabTestQuestions?labTestId=" + labTestId,
                        contentType: "application/json",
                        success: function (labDetail) {
                            questions = labDetail.labTestDto?.questions;
                            labTestName = labDetail.labTestDto.labTestName;
                            questionDetailStatusList = mergeQuestion(questions, labDetail.labTestInfo?.questionStatusList);
                            let quesIndex = getQuestionIndex(questionDetailStatusList);
                            question = questions[quesIndex];
                            leftTime = labDetail.timer.timerLeft;
                            leftTime = findLeftTime(labDetail.timer.timerLeft, labDetail.labTestDto.endDate)
                            batchId = labDetail.labTestInfo?.batch;
                            traineeId = labDetail.labTestInfo?.traineeId;
                            renderQuestionPanal(questionDetailStatusList);
                            $("#attemptsPerQuestion").append(labDetail.labTestDto.attemptsPerQuestion)
                            $("#question").empty();
                            if (questions != null) {
                                $("#question").append("Q" + (quesIndex + 1) + ". " + question.questionDescription);
                                $("#questionPoints").text(question.questionPoints + (question.questionPoints === 1 ? ' mark' : ' marks'));
                            } else {
                                $("#question").append("Question Not Found!");
                            }
                            var timeInSeconds = leftTime;
                            function updateTimer() {
                                $.ajax({
                                    type: "POST",
                                    url: "${pageContext.request.contextPath}/api/timer/updateTimer",
                                    data: {
                                        timeLeft: timeInSeconds,
                                        testId: labTestId,
                                        batchId: batchId,
                                        traineeId: traineeId
                                    },
                                    success: function (response) {
                                        // console.log('Timer updated successfully:', response);
                                    },
                                    error: function (xhr, status, error) {
                                        console.error("Error updating timer:", error);
                                    }
                                });
                            }

                            let malpracticeFound = false;
                            var timer = setInterval(function () {
                                var minutes = Math.floor(timeInSeconds / 60);
                                var seconds = timeInSeconds % 60;
                                if (timeInSeconds % 10 === 0) {
                                    updateTimer();
                                }
                                var formattedTime = ('0' + minutes).slice(-3) + ':' + ('0' + seconds).slice(-2);
                                $('#timer').text(formattedTime);
                                timeInSeconds--;
                                let endTimeBoolean = checkEndTime(labDetail.labTestDto.endDate)
                                if (timeInSeconds < 0 || endTimeBoolean) {
                                    clearInterval(timer);
                                    alert('Timer expired!');
                                    submitYourTest();
                                }
                                if (!malpracticeFound && countTabSwitch <= 0) {
                                    alert('Malpractice found!!!');
                                    setTimeout(submitYourTest, 2000);
                                    malpracticeFound = true;
                                }
                            }, 1000);
                        },
                        error: function (xhr, status, error) {
                            console.error("Error:", error);
                        },
                    });

                    function checkEndTime(endDateArray) {
                        let endDateCheck = new Date(endDateArray[0], endDateArray[1] - 1, endDateArray[2], endDateArray[3], endDateArray[4]);
                        let currentDate = new Date();
                        if (currentDate.getTime() > endDateCheck.getTime()) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                    function findLeftTime(timerLeft, endDateArray) {
                        timerLeft = timerLeft / 60;
                        let endDateFind = new Date(endDateArray[0], endDateArray[1] - 1, endDateArray[2], endDateArray[3], endDateArray[4]);
                        let currentDate = new Date();
                        let timeDifference = endDateFind.getTime() - currentDate.getTime();
                        let timeDifferenceInMinutes = Math.floor(timeDifference / (1000 * 60));
                        if (timerLeft > timeDifferenceInMinutes) {
                            return timeDifferenceInMinutes * 60;
                        }
                        return timerLeft * 60;
                    }
                }
                function getQuestionIndex(data) {
                    let index = 0;
                    for (let i = 0; i < data.length; i++) {
                        const element = data[i];
                        if (element.status == "UNATTEMPTED") {
                            return i;
                        }
                    }
                    return 0;
                }
                function renderQuestion(index) {
                    if (questions.length == 0) {
                        getQuestion();
                    } else {
                        $("#question").empty();
                        question = questions[index];
                        $("#question").append("Q" + (index + 1) + ". " + question.questionDescription);
                        $("#questionPoints").text(question.questionPoints + (question.questionPoints === 1 ? ' mark' : ' marks'));
                        $("#resultTable").empty();
                        $("#outputMessage").empty();
                    }
                }
                $(document).on('click', '.quesDiv', function () {
                    var index = $(this).index('.quesDiv');
                    renderQuestion(index - 2);
                    $('#answer').val('');
                });
                $(document).on('click', '#clearBtn', function () {
                    $('#answer').val('');
                });

                $("#submitQuestion").click(function () {
                    let answer = $("#answer").val();
                    if (answer.length == 0) {
                        alert("Enter your answer")
                        return;
                    }
                    let sendData = {
                        labTestId: labTestId,
                        questionId: question.questionId,
                        query: answer,
                    };
                    $("#loading").empty().append("Loading...");
                    $("#submitQuestion").prop("disabled", true);
                    $.ajax({
                        type: "POST",
                        url: "${pageContext.request.contextPath}/api/trainee/labTestSubmitQuestion?labTestId=" + labTestId,
                        contentType: "application/json",
                        data: JSON.stringify(sendData),
                        success: function (labDetail) {
                            $("#outputMessage").empty();
                            $("#outputMessage").append(labDetail.output)
                            $("#loading").empty();
                            $("#submitQuestion").prop("disabled", false);
                            updateQuestionDetailList(labDetail.labTestInfo.questionStatusList);
                            createTable(labDetail.outputList)
                        },
                        error: function (xhr, status, error) {
                            console.error("Error:", error);
                        },
                    });
                })
                function createTable(tableData) {
                    var table = document.createElement('table');
                    table.classList.add('table', 'table-striped')
                    var tableBody = document.createElement('tbody');
                    tableData.forEach(function (rowData, index) {
                        var row = document.createElement('tr');
                        if (index != 0) {
                            let snoCell = document.createElement("td");
                            snoCell.appendChild(document.createTextNode(index))
                            row.appendChild(snoCell);
                        } else {
                            let snoCell = document.createElement("td");
                            snoCell.appendChild(document.createTextNode("S.No."))
                            row.appendChild(snoCell);
                        }
                        row.classList.add('text-primary')
                        rowData.forEach(function (cellData) {
                            var cell = document.createElement('td');
                            cell.appendChild(document.createTextNode(cellData));
                            row.appendChild(cell);
                        });
                        tableBody.appendChild(row);
                    });
                    table.appendChild(tableBody);
                    document.body.appendChild(table);
                    let resultTable = document.getElementById("resultTable");
                    $("#resultTable").empty();
                    if (resultTable) {
                        resultTable.appendChild(table);
                    } else {
                        console.error("Table Not Created...");
                    }
                }

                $("#submitYourTest").click(function () {
                    var confirmed = confirm("Are you sure you want to submit the test?");
                    if (confirmed) {
                        submitYourTest();
                    }
                })
                function submitYourTest() {
                    redirect = false;
                    $("#loadingModal").modal('show');
                    localStorage.removeItem(countTabSwitch);
                    $.ajax({
                        type: "POST",
                        url: "${pageContext.request.contextPath}/api/trainee/submitYourLabTest?id=" + labTestId,
                        contentType: "application/json",
                        success: function (data) {
                            $("#loadingModal").modal('hide');
                            window.location.href = "${pageContext.request.contextPath}/trainee/viewLabTestReport?labTestId=" + labTestId;
                        },
                        error: function (xhr, status, error) {
                            console.error("Error:", error);
                            console.error("error:", xhr.responseText);
                            $("#loadingModal").modal('hide');
                        },
                    });
                }
                function timer() {
                    $.ajax({
                        type: "POST",
                        url: "${pageContext.request.contextPath}/api/timer/updateTimer?=" + labTestId,
                        contentType: "application/json",
                        data: JSON.stringify(sendData),
                        success: function (labDetail) {
                            $("#outputMessage").empty();
                            $("#outputMessage").append(labDetail.output)
                            $("#loading").empty();
                            $("#submitQuestion").prop("disabled", false);
                            createTable(labDetail.outputList)
                        },
                        error: function (xhr, status, error) {
                            console.error("Error:", error);
                        },
                    });
                }
                function renderQuestionPanal(questionList) {
                    const colors = {
                        'UNATTEMPTED': '',
                        'SKIP': 'blue',
                        'INCORRECT': 'red',
                        'CORRECT': 'rgb(34 197 94)'
                    };
                    $("#quesBox").empty();
                    const quesBox = $("#quesBox");
                    questionList.forEach((question, index) => {
                        const { questionId, questionDescription, status } = question;
                        const div = $("<div></div>").attr("id", `quesDiv_${questionId}`).addClass("quesDiv centerQues cursor-pointer");
                        if (colors.hasOwnProperty(status)) {
                            div.css("background-color", colors[status]);
                        } else {
                            div.css("background-color", "gray");
                        }
                        div.text(index + 1);
                        quesBox.append(div);
                    });
                }
                function mergeQuestion(question, questionStatusList) {
                    let mergedArray = questions.map(question => {
                        let statusItem = questionStatusList.find(status => status.questionId === question.questionId);
                        return { ...question, ...statusItem };
                    });
                    return mergedArray;
                }
                function updateQuestionDetailList(updatedQuestions) {
                    updatedQuestions.forEach((updatedQuestion, index) => {
                        const { questionId, status } = updatedQuestion;
                        questionDetailStatusList[index].status = status;
                    });
                    renderQuestionPanal(questionDetailStatusList);
                }
            })
        </script>
        <script src="https://cdn.datatables.net/1.13.4/js/jquery.dataTables.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz"
            crossorigin="anonymous"></script>