<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <%@ page isELIgnored="false" %>
        <div class="row m-0">
            <div class="col-9">
                <h3 class="text-center ">SQL Practice Portal</h3>
            </div>
            <div class="col-3">

                <a id="downloadLink" 
                    class="py-2 px-3 rounded border bg-blue text-white text-sm float-end  ">
                    Download Required Document</a>
                    <div class="spinner-border text-primary d-none" id="downloadLoading" role="status">
                        <span class="sr-only">Loading...</span>
                    </div>
            </div>
        </div>
        <div class="row mx-0 my-4">
            <div class="col-9">
                <div class="container-fluid shadow-sm p-3 bg-white rounded border-gray">
                    <div class="m-2">
                        <div>
                            <div class="text-md m-2 d-flex justify-content-between">
                                <div id="question" style="width: 85%; line-height: 2rem;">Your Question is Loading...
                                    <div class="spinner-border text-primary" role="status">
                                        <span class="sr-only">Loading...</span>
                                    </div>
                                </div>
                                <div class="text-sm bold">
                                    <span id="questionPoints"></span>
                                </div>
                            </div>
                            <div class="mb-3">
                                <textarea class="form-control" id="answer" rows="3" name="query"
                                    placeholder="Write the Query" required autocomplete="off"></textarea>
                            </div>
                            <div class="mb-3">
                                <button type="submit" id="submitQuestion" class="py-2 px-3 rounded border bg-blue text-white text-sm">Submit</button>
                                <button type="button" id="clearBtn" class="py-2 px-3 rounded border bg-green text-white text-sm">Clear</button>
                            </div>
                        </div>
                        <div id="loading"></div>
                        <div>
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
                    Correct Ans <div class="quesDiv greenQues centerQues">1</div>
                    Wrong Ans <div class="quesDiv redQues centerQues">1</div>
                </div>
                <div class="text-md text-dark-100 m-1 ">Questions</div>
                <div class="border-gray quesBox" id="quesBox">
                    <div class="spinner-border text-primary" role="status">
                        <span class="sr-only">Loading...</span>
                    </div>
                </div>
                <div class="my-3">
                    <button type="submit" id="submitYourTest" class="py-2 px-3 rounded border bg-red text-white text-sm">Submit you Practice</button>
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

        <script>
            $(document).ready(function () {
                $("#downloadLoading").css("display", "hidden");
                var labId = '${labId}'
                let labName;
                let batchId = 0;
                let questions = [];
                let questionDetailStatusList = [];
                let question;
                getQuestion();
                function getQuestion() {
                    $.ajax({
                        type: "GET",
                        url: "${pageContext.request.contextPath}/api/trainee/getLabQuestions?labId=" + labId,
                        contentType: "application/json",
                        success: function (labDetail) {
                            labName = labDetail.labDto.labName;
                            questions = labDetail.labDto?.questions;
                            question = questions[0];
                            batchId = labDetail.labInfo?.batch;
                            traineeId = labDetail.labInfo?.traineeId;
                            questionDetailStatusList = mergeQuestion(questions, labDetail.labInfo?.questionStatusList);
                            renderQuestionPanal(questionDetailStatusList);
                            $("#question").empty();
                            if (questions != null) {
                                $("#question").append("Q" + 1 + ". " + question.questionDescription);
                                $("#questionPoints").text(question.questionPoints + (question.questionPoints === 1 ? ' mark' : ' marks'));
                            } else {
                                $("#question").append("Question Not Found!");
                            }
                        },
                        error: function (xhr, status, error) {
                            console.error("Error:", error);
                        },
                    });
                }
                $('#downloadLink').on('click', function (event) {
                    event.preventDefault();
                    $("#downloadLoading").removeClass("d-none");
                    console.log("hii test")
                    $.ajax({
                        url: "${pageContext.request.contextPath}/trainee/download/${labId}",
                        method: 'GET',
                        xhrFields: {
                            responseType: 'blob'
                        },
                        success: function (data, status, xhr) {
                            const blob = new Blob([data], { type: xhr.getResponseHeader('Content-Type') });
                            const url = window.URL.createObjectURL(blob);
                            const a = document.createElement('a');
                            a.href = url;
                            a.download = labName + '.pdf';
                            document.body.appendChild(a);
                            a.click();
                            window.URL.revokeObjectURL(url);
                            document.body.removeChild(a);
                            $("#downloadLoading").addClass("d-none");
                        },
                        error: function (xhr, status, error) {
                            if (xhr.status === 404) {
                                alert('File not available.');
                            } else {
                                alert('An error occurred while downloading the document.');
                            }
                            $("#downloadLoading").addClass("d-none");
                        }
                    });
                });

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
                        labId: labId,
                        questionId: question.questionId,
                        query: answer,
                    };
                    $("#loading").empty().append("Loading...");
                    $("#submitQuestion").prop("disabled", true);
                    $.ajax({
                        type: "POST",
                        url: "${pageContext.request.contextPath}/api/trainee/submitLabQuestion?labId=" + labId,
                        contentType: "application/json",
                        data: JSON.stringify(sendData),
                        success: function (labDetail) {
                            $("#outputMessage").empty();
                            $("#outputMessage").append(labDetail.output)
                            $("#loading").empty();
                            $("#submitQuestion").prop("disabled", false);
                            updateQuestionDetailList(labDetail.labInfo.questionStatusList);
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
                        $("#loadingModal").modal('show');
                        submitYourTest();
                    }
                })
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
                function submitYourTest() {
                    $.ajax({
                        type: "POST",
                        url: "${pageContext.request.contextPath}/api/trainee/submitYourTest?id=" + labId,
                        contentType: "application/json",
                        success: function (data) {
                            $("#loadingModal").modal('hide');
                            window.location.href = "${pageContext.request.contextPath}/trainee/viewLabReport?labId=" + labId;
                        },
                        error: function (xhr, status, error) {
                            console.error("Error:", error);
                            console.error("error:", xhr.responseText);
                            $("#loadingModal").modal('hide');
                        },
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

                $('#downloadBtn').on('click', function () {
                    var blob = new Blob([downloadPdf], { type: 'application/pdf' });
                });
                function base64toBlob(base64Data, contentType = 'application/pdf') {
                    const byteCharacters = atob(base64Data);
                    const byteNumbers = new Array(byteCharacters.length);
                    for (let i = 0; i < byteCharacters.length; i++) {
                        byteNumbers[i] = byteCharacters.charCodeAt(i);
                    }
                    const byteArray = new Uint8Array(byteNumbers);
                    return new Blob([byteArray], { type: contentType });
                }

                function downloadPDF(base64Data, fileName) {
                    const blob = base64toBlob(base64Data);
                    const link = document.createElement('a');
                    link.href = window.URL.createObjectURL(blob);
                    link.setAttribute('download', fileName);
                    document.body.appendChild(link);
                    link.click();
                    link.parentNode.removeChild(link);
                }
            })
        </script>